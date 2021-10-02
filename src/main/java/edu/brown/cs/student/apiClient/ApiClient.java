package edu.brown.cs.student.apiClient;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import edu.brown.cs.student.main.DataStore;
import edu.brown.cs.student.runway.Rent;
import edu.brown.cs.student.runway.Review;
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
        .connectTimeout(Duration.ofMillis(1000))
        .build();
  }

  public void usersApiCall() {

    String reqUri = "https://runwayapi.herokuapp.com/users-two" + apiAuth;
    String userJson = this.makeRequest(HttpRequest.newBuilder(URI.create(reqUri))
        .header("x-api-key", apiAuth).GET().build());

//    String userJson = ApiSimulator.getSimulatedUsers();
    try {
      DataStore.setUsers(new Gson().fromJson(userJson, User[].class));
    } catch (JsonSyntaxException e) {
      System.out.println("ERROR: fill in here");
    }
  }

  public void reviewsApiCall() {
    String reviewJson = ApiSimulator.getSimulatedReviews();
    try {
      DataStore.setReviews(new Gson().fromJson(reviewJson, Review[].class));
    } catch (JsonSyntaxException e) {
      System.out.println("ERROR: fill in here");
    }
  }

  public void rentsApiCall() {
    String rentsJson = ApiSimulator.getSimulatedRents();
    try {
      DataStore.setRents(new Gson().fromJson(rentsJson, Rent[].class));
    } catch (JsonSyntaxException e) {
      System.out.println("ERROR: fill in here");
    }
  }

  private String makeRequest(HttpRequest req) {
    try {
      HttpResponse<String> apiResponse = client.send(req, HttpResponse.BodyHandlers.ofString());
      System.out.println("Status " + apiResponse.statusCode());
      return apiResponse.body();

    } catch (IOException ioe) {
      System.out.println("An I/O error occurred when sending or receiving data.");
      System.out.println(ioe.getMessage());

    } catch (InterruptedException ie) {
      System.out.println("The operation was interrupted.");
      System.out.println(ie.getMessage());

    } catch (IllegalArgumentException iae) {
      System.out.println(
          "The request argument was invalid. It must be built as specified by HttpRequest.Builder.");
      System.out.println(iae.getMessage());

    } catch (SecurityException se) {
      System.out.println("There was a security configuration error.");
      System.out.println(se.getMessage());
    }
    return "ERROR";
  }

}
