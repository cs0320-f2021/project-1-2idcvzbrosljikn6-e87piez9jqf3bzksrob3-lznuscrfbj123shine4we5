package edu.brown.cs.student.apiClient;

import com.google.gson.Gson;

public class ApiClient {

  private final String apiAuth;
  public ApiClient() {
    this.apiAuth = ClientAuth.getApiAuth();
  }

  public String usersApiCall() {
    String userJson = ApiSimulator.getSimulatedUsers();

    System.out.println(userJson);

    return userJson;
  }

  public String reviewsApiCall() {
    String reviewJson = ApiSimulator.getSimulatedReviews();
    return reviewJson;
  }

  public String rentsApiCall() {
    String rentsJson = ApiSimulator.getSimulatedRents();
    return rentsJson;
  }

}
