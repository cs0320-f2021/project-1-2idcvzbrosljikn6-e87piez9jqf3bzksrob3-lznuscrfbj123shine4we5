package edu.brown.cs.student.recommender;

import java.util.Map;

public class Negative {
  private int id;
  private String trait;

  /**
   * Class representing a row of the negative table in integration.sqlite3.
   * @param map of id and trait to value, from the table
   */
  public Negative(Map<String, String> map) {
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
