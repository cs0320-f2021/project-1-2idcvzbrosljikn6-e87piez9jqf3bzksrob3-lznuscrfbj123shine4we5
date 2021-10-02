package edu.brown.cs.student.apiClient;

import com.google.gson.Gson;
import edu.brown.cs.student.runway.Rent;
import edu.brown.cs.student.runway.Review;
import edu.brown.cs.student.runway.User;

public class ApiClient {

  private final String apiAuth;
  public ApiClient() {
    this.apiAuth = ClientAuth.getApiAuth();
  }

  public User[] usersApiCall() {
    String userJson = ApiSimulator.getSimulatedUsers();
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

}
