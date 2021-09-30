package edu.brown.cs.student.apiClient;

public class ApiClient {

  private final String apiAuth;
  public ApiClient() {
    this.apiAuth = ClientAuth.getApiAuth();
  }
}
