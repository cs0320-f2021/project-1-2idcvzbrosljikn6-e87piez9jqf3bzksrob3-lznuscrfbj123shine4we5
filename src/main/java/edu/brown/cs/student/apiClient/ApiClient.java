package edu.brown.cs.student.apiClient;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import edu.brown.cs.student.main.DataStore;
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

public class ApiClient {

  private final String apiAuth;
  private HttpClient client;

  public ApiClient() {
    this.apiAuth = ClientAuth.getApiAuth();
    this.client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofMillis(500))
        .build();
  }

  public void usersApiCall(boolean loadRunways) {
    String reqUri = "https://runwayapi.herokuapp.com/users-one" + apiAuth;
    String userJson = this.makeRequest(HttpRequest.newBuilder(URI.create(reqUri))
        .GET().build());

    if (userJson.equals("ERROR")) {
      String backupUri = "https://runwayapi.herokuapp.com/users-three" + apiAuth;
      userJson = this.makeRequest(HttpRequest.newBuilder(URI.create(backupUri))
          .GET().build());
    }

    if (userJson.equals("ERROR")) {
      System.out.println("Failed to retrieve user data.");
      return;
    }
    try {
      if (loadRunways) {
        DataStore.setRunways(new Gson().fromJson(userJson, Runway[].class));
      } else {
        DataStore.setUsers(new Gson().fromJson(userJson, User[].class));
      }
    } catch (JsonSyntaxException e) {
      System.out.println("ERROR: invalid JSON syntax received from users API call");
    }
  }

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

    try {
      DataStore.setReviews(new Gson().fromJson(reviewJson, Review[].class));
    } catch (JsonSyntaxException e) {
      System.out.println("ERROR: invalid JSON syntax received from reviews API call");
    }
  }

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

    try {
      DataStore.setRents(new Gson().fromJson(rentsJson, Rent[].class));
    } catch (JsonSyntaxException e) {
      System.out.println("ERROR: invalid syntax received from rents API call");
    }
  }


  private String makeRequest(HttpRequest req) {
    HttpResponse<String> apiResponse = null;
    for (int i = 0; i < 5; i++) {
      System.out.println(i);
      try {
        apiResponse = client.send(req, HttpResponse.BodyHandlers.ofString());
      } catch (IOException ioe) {
        System.out.println("An I/O error occurred when sending or receiving data.");
        System.out.println(ioe.getMessage());

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
          && apiResponse.statusCode() >= 200 && apiResponse.statusCode() < 300) {
        break;
      }
    }

    if (apiResponse == null || !(apiResponse.statusCode() >= 200 && apiResponse.statusCode() < 300)) {
      return "ERROR";
    }

    System.out.println("Status " + apiResponse.statusCode());
    System.out.println(apiResponse.body());
    return apiResponse.body();
  }
}
