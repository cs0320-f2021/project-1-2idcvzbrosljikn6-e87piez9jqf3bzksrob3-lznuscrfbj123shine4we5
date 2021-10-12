package edu.brown.cs.student.recommender;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Response implements Item {

//  private ArrayList<String> vectorRepresentation;
  private String id;
  private static int idCounter = 1;
  private int ormComfort;
  private int apiComfort;
  private int kdTreeComfort;
  private int bloomFilterComfort;
  private String frequency;
  private String medium;
  private String[] times;
  private String workloadDistribution;
  private String sprintChoice;
  private String genderIdentity;
  private boolean genderPreference;
  private String racialIdentity;
  private boolean racialPreference;

  public Response(String line) {
//    this.vectorRepresentation = vectorRepresentation;
//    this.id = id;
    String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    parseHelper(tokens);


//    ormComfort, apiComfort, kdTreeComfort, bloomFilterComfort
//    frequency, medium, times
//    workloadDistribution, sprintChoice
//    genderIdentity, genderPreference, racialIdentity, racialPreference
  }
  @Override
  public List<String> getVectorRepresentation() {
    return null;
  }

  @Override
  public String getId() {
    return id;
  }

  private void parseHelper(String[] tokens) {
    try {
      ormComfort = Integer.parseInt(tokens[1]);
      apiComfort = Integer.parseInt(tokens[2]);
      kdTreeComfort = Integer.parseInt(tokens[3]);
      bloomFilterComfort = Integer.parseInt(tokens[4]);
    }
    catch (NumberFormatException e) {
      e.printStackTrace(); // replace this later
      ormComfort = 0;
      apiComfort = 0;
      kdTreeComfort = 0;
      bloomFilterComfort = 0;
      return;
    }

    frequency = tokens[5];
    medium = tokens[6];
    times = tokens[7].replace("\"", "").split(", ");
    workloadDistribution = tokens[8];
    sprintChoice = tokens[9];
    genderIdentity = tokens[10];
    genderPreference = tokens[11].equals("Yes");
    racialIdentity = tokens[12];
    racialPreference = tokens[13].equals("Yes");
  }

  public void setId() {
    this.id = String.valueOf(idCounter++);
  }

}