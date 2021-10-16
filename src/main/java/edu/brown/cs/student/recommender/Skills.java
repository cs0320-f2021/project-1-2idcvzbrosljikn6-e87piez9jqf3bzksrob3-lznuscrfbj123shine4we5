package edu.brown.cs.student.recommender;

import java.util.Map;

public class Skills {
  private int id;
  private String name;
  private int commenting;
  private int testing;
  private int OOP;
  private int algorithms;
  private int teamwork;
  private int frontend;

  public Skills(Map<String, String> map) {
    id = Integer.parseInt(map.get("id"));
    name = map.get("name");
    commenting = Integer.parseInt(map.get("commenting"));
    testing = Integer.parseInt(map.get("testing"));
    OOP = Integer.parseInt(map.get("OOP"));
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

  public int getOOP() {
    return OOP;
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
