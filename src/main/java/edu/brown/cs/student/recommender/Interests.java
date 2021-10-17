package edu.brown.cs.student.recommender;

import java.util.Map;

public class Interests {
  private int id;
  private String interest;

  /**
   * Class representing a row of the interests table in integration.sqlite3
   * @param map of id and interest to value, from the table
   */
  public Interests(Map<String, String> map) {
    id = Integer.parseInt(map.get("id"));
    interest = map.get("interest");
  }

  public int getId() {
    return id;
  }

  public String getInterest() {
    return interest;
  }
}
