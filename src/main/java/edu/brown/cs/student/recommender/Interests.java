package edu.brown.cs.student.recommender;

import java.util.Map;

public class Interests {
  private int id;
  private String interest;

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
