package edu.brown.cs.student.apiClient;

import com.google.gson.Gson;
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
        .connectTimeout(Duration.ofMillis(100))
        .build();
  }

  public User[] usersApiCall() {

    String reqUri = "https://runwayapi.herokuapp.com/users-two";
    String userJson = this.makeRequest(HttpRequest.newBuilder(URI.create(reqUri))
        .header("x-api-key", apiAuth).GET().build());

//    String userJson = ApiSimulator.getSimulatedUsers();
    return new Gson().fromJson(userJson, User[].class);
  }

  public Review[] reviewsApiCall() {
    String reviewJson = ApiSimulator.getSimulatedReviews();
    return new Gson().fromJson(reviewJson, Review[].class);
  }

  public Rent[] rentsApiCall() {
    String rentsJson = ApiSimulator.getSimulatedRents();
    return new Gson().fromJson(rentsJson, Rent[].class);
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
