package edu.brown.cs.student.recommender;

import java.util.Map;

public class Negative {
  private int id;
  private String trait;

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
