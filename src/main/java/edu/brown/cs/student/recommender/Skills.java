package edu.brown.cs.student.recommender;

import java.util.Map;

public class Skills {
  private int id;
  private String name;
  private int commenting;
  private int testing;
  private int oop;
  private int algorithms;
  private int teamwork;
  private int frontend;

  /**
   * Class representing a row of the skills table in integration.sqlite3.
   *
   * @param map of all skills to values, from the table
   */
  public Skills(Map<String, String> map) {
    id = Integer.parseInt(map.get("id"));
    name = map.get("name");
    commenting = Integer.parseInt(map.get("commenting"));
    testing = Integer.parseInt(map.get("testing"));
    oop = Integer.parseInt(map.get("OOP"));
    algorithms = Integer.parseInt(map.get("algorithms"));
    teamwork = Integer.parseInt(map.get("teamwork"));
    frontend = Integer.parseInt(map.get("frontend"));
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getCommenting() {
    return commenting;
  }

  public int getTesting() {
    return testing;
  }

  public int getOop() {
    return oop;
  }

  public int getAlgorithms() {
    return algorithms;
  }

  public int getTeamwork() {
    return teamwork;
  }

  public int getFrontend() {
    return frontend;
  }
}
