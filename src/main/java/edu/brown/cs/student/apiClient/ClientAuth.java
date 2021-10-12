package edu.brown.cs.student.apiClient;

import edu.brown.cs.student.main.FileParser;

/**
 * This simple class is for reading the API Key from your secret file
 * (THAT SHOULD NOT BE PUSHED TO GIT).
 */
public final class ClientAuth {

  private ClientAuth() {
  }

  /**
   * Reads the API Key from the secret text file where we have stored it.
   * Refer to the handout for more on security practices.
   *
   * @return a String of the api key.
   */
  public static String getApiAuth() {
    FileParser parser = new FileParser("config/secret/loginAndKey.txt");
    return parser.readNewLine();
  }

  /**
   * Reads the JSON user auth code from the secret text file where we have stored it.
   * @return a String of the JSON of the user auth code in the format {"auth":"<cslogin>"}
   */
  public static String getJsonUserAuth() {
    FileParser parser = new FileParser("config/secret/jsonAuth.txt");
    return parser.readNewLine();
  }

  /**
   * Reads the API key from the secret text file where we have stored it.
   * @return a String of the API key
   */
  public static String getApiKey() {
    FileParser parser = new FileParser("config/secret/apiKey.txt");
    return parser.readNewLine();
  }
}
