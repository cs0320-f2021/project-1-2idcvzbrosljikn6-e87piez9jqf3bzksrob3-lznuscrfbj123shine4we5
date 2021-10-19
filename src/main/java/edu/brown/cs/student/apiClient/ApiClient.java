package edu.brown.cs.student.apiClient;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import edu.brown.cs.student.main.DataStore;
import edu.brown.cs.student.main.FileParser;
import edu.brown.cs.student.runway.Rent;
import edu.brown.cs.student.runway.Review;
import edu.brown.cs.student.runway.Runway;
import edu.brown.cs.student.runway.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * ApiClient is a wrapper around an instance of HttpClient.
 * All HTTP requests are routed through an instance of ApiClient.
 */
public class ApiClient {

  public static final int ACCEPTABLE_STATUS_CODE = 200;
  public static final int UNACCEPTABLE_STATUS_CODE = 300;
  private final String apiAuth;
  private HttpClient client;

  public ApiClient() {
    this.apiAuth = ClientAuth.getApiAuth(); // gets authentication string stored in secret folder
    this.client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofMillis(500))
        .build();
  }

  public edu.brown.cs.student.recommender.RecommenderResponse[] localRecommenderUsers() {
    return new Gson().fromJson(FileParser.readIntoString("data/integration.json"),
        edu.brown.cs.student.recommender.RecommenderResponse[].class);
  }

  /**
   * Helper method that returns an array of Responses if successful, null otherwise.
   *
   * @return array of Response data with ORM data yet to be added to it
   */
  public edu.brown.cs.student.recommender.RecommenderResponse[] recommenderUsers() {
    String reqUri = "https://runwayapi.herokuapp.com/integration";
    HttpResponse<String> userData = null;
    try {
      userData = client.send(HttpRequest.newBuilder(URI.create(reqUri))
              .POST(HttpRequest.BodyPublishers.ofString(ClientAuth.getJsonUserAuth()))
              .header("x-api-key", ClientAuth.getApiKey()).build(),
          HttpResponse.BodyHandlers.ofString());

//      System.out.println(userData.body());

      return new Gson().fromJson(userData.body(),
          edu.brown.cs.student.recommender.RecommenderResponse[].class);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Method called when the user requests user data from the endpoints.
   * Stores retrieved data in the DataStore
   *
   * @param loadRunways whether to load user data as runway data, for the KD tree.
   *                    The KD tree uses Runways and Gson handles the missing attributes, so this
   *                    works.
   */
  public void usersApiCall(boolean loadRunways) {
    String reqUri = "https://runwayapi.herokuapp.com/users-one" + apiAuth;
    String userJson = this.makeRequest(HttpRequest.newBuilder(URI.create(reqUri))
        .GET().build());

    if (userJson.equals("ERROR")) { // if the first set of attempts to make the request failed
      String backupUri = "https://runwayapi.herokuapp.com/users-three" + apiAuth;
      userJson = this.makeRequest(HttpRequest.newBuilder(URI.create(backupUri))
          .GET().build());
    }

    if (userJson.equals("ERROR")) { // if we still fail, cut our losses
      System.out.println("Failed to retrieve user data.");
      return;
    }

    userJson = normaliseJson(userJson); // checks if json data is in correct format

    try {
      if (loadRunways) {
        Runway[] data = new Gson().fromJson(userJson, Runway[].class);
        System.out.println("Loaded " + data.length + " users from API endpoint");
        DataStore.setRunways(data);
      } else {
        DataStore.setUsers(new Gson().fromJson(userJson, User[].class));
      }
    } catch (JsonSyntaxException e) {
      System.out.println("ERROR: invalid JSON syntax received from users API call");
    }
  }

  /**
   * Method called when the user requests review data from the endpoints.
   * Stores the retrieved data in the DataStore
   */
  public void reviewsApiCall() {
    String reqUri = "https://runwayapi.herokuapp.com/reviews-three" + apiAuth;
    String reviewJson = this.makeRequest(HttpRequest.newBuilder(URI.create(reqUri))
        .GET().build());

    if (reviewJson.equals("ERROR")) {
      String backupUri = "https://runwayapi.herokuapp.com/reviews-four" + apiAuth;
      reviewJson = this.makeRequest(HttpRequest.newBuilder(URI.create(backupUri))
          .GET().build());
    }
    if (reviewJson.equals("ERROR")) {
      System.out.println("Failed to retrieve review data.");
      return;
    }

    reviewJson = normaliseJson(reviewJson);

    try {
      Review[] data = new Gson().fromJson(reviewJson, Review[].class);
      System.out.println("Loaded " + data.length + " reviews from API endpoint");
      DataStore.setReviews(data);
    } catch (JsonSyntaxException e) {
      System.out.println("ERROR: invalid JSON syntax received from reviews API call");
    }
  }

  /**
   * Method called when the user requests rent data from the endpoints.
   * Stores the retrieved data in the DataStore
   */
  public void rentsApiCall() {
    String reqUri = "https://runwayapi.herokuapp.com/rent-three" + apiAuth;
    String rentsJson = this.makeRequest(HttpRequest.newBuilder(URI.create(reqUri))
        .GET().build());

    if (rentsJson.equals("ERROR")) {
      String backupUri = "https://runwayapi.herokuapp.com/rent-two" + apiAuth;
      rentsJson = this.makeRequest(HttpRequest.newBuilder(URI.create(backupUri))
          .GET().build());
    }
    if (rentsJson.equals("ERROR")) {
      System.out.println("Failed to retrieve rent data.");
      return;
    }

    rentsJson = normaliseJson(rentsJson);

    try {
      Rent[] data = new Gson().fromJson(rentsJson, Rent[].class);
      System.out.println("Loaded " + data.length + " rents from API endpoint");
      DataStore.setRents(data);
    } catch (JsonSyntaxException e) {
      System.out.println("ERROR: invalid syntax received from rents API call");
    }
  }

  /**
   * Private helper method called when trying to fetch data from the API endpoints.
   * Uses a naive algorithm to fetch the results - tries to fetch the data from what we
   * determined to be the most reliable and best balanced endpoint, and if that fails, try a
   * backup endpoint.
   * The timeout chosen was determined through trial and error, providing a good balance between
   * speed and reliability.
   *
   * @param req the request to make
   * @return the body of the request is successful, ERROR otherwise
   */
  private String makeRequest(HttpRequest req) {
    HttpResponse<String> apiResponse = null;
    for (int i = 0; i < 5; i++) {
      try {
        apiResponse = client.send(req, HttpResponse.BodyHandlers.ofString());
      } catch (IOException ignored) { // ignore because timeouts may occur, just ignore them
      } catch (InterruptedException ie) {
        System.out.println("The operation was interrupted.");
        System.out.println(ie.getMessage());

      } catch (IllegalArgumentException iae) {
        System.out.println(
            "The request argument was invalid. It must be built as specified"
                + " by HttpRequest.Builder.");
        System.out.println(iae.getMessage());

      } catch (SecurityException se) {
        System.out.println("There was a security configuration error.");
        System.out.println(se.getMessage());
      }
      if (apiResponse != null
          && apiResponse.statusCode() >= ACCEPTABLE_STATUS_CODE && apiResponse.statusCode()
          < UNACCEPTABLE_STATUS_CODE) {
        break; // breaks out of the loop if we had a successful response
      }
    }

    if (apiResponse == null
        || !(apiResponse.statusCode() >= ACCEPTABLE_STATUS_CODE && apiResponse.statusCode()
        < UNACCEPTABLE_STATUS_CODE)) {
      return "ERROR"; // if no successful response
    }

    return apiResponse.body();
  }

  /**
   * Helper method that checks for json list validity.
   *
   * @param json a String to normalise
   * @return the normalised String
   */
  public static String normaliseJson(String json) {
    if (json.endsWith(",")) {
      json = json.substring(0, json.length() - 1);
    }
    if (!json.startsWith("[")) {
      json = "[" + json + "]";
    }
    return json;
  }
}
