package edu.brown.cs.student.recommender;

import java.util.Map;

public class Positive {
  private int id;
  private String trait;

  /**
   * Class representing a row of the positive table in integration.sqlite3.
   * @param map of id and trait to value, from the table
   */
  public Positive(Map<String, String> map) {
    id = Integer.parseInt(map.get("id"));
    trait = map.get("trait");
  }

  public int getId() {
    return id;
  }

  public String getTrait() {
    return trait;
  }
}
