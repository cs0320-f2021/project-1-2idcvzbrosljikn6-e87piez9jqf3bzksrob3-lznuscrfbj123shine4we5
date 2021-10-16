package edu.brown.cs.student.recommender;

import com.google.gson.annotations.SerializedName;
import edu.brown.cs.student.orm.Database;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommenderResponse implements Item {

  private String id;
  private String name;
  private String meeting;
  private String grade;

  @SerializedName("years_of_experience")
  private String experience;

  private String horoscope;

  @SerializedName("meeting_times")
  private String meetingTimes;

  @SerializedName("preferred_language")
  private String preferredLanguage;

  @SerializedName("marginalized_groups")
  private String marginalizedGroups;

  @SerializedName("prefer_group")
  private String preferGroup;

  // below this is from database

  private ArrayList<String> interests = new ArrayList<>();
  private ArrayList<String> negativeTraits = new ArrayList<>();
  private ArrayList<String> positiveTraits = new ArrayList<>();

  private int commenting;
  private int testing;
  private int OOP;
  private int algorithms;
  private int teamwork;
  private int frontend;


  @Override
  public List<String> getVectorRepresentation() {
    return null;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getMeeting() {
    return meeting;
  }

  public String getGrade() {
    return grade;
  }

  public int getExperience() {
    return Integer.parseInt(experience);
  }

  public String getHoroscope() {
    return horoscope;
  }

  public String[] getMeetingTimes() {
    return meetingTimes.split("; ");
  }

  public String getPreferredLanguage() {
    return preferredLanguage;
  }

  public String[] getMarginalizedGroups() {
    return marginalizedGroups.split("; ");
  }

  public boolean getPreferGroup() {
    return preferGroup.equals("Yes");
  }

  public void fetchDatabaseData(Database db) {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("id", id);
    try {
      List<Interests> i = db.select(Interests.class, queryParams);
      List<Negative> n = db.select(Negative.class, queryParams);
      List<Positive> p = db.select(Positive.class, queryParams);
      List<Skills> s = db.select(Skills.class, queryParams);
      for (Interests interest: i) {
        interests.add(interest.getInterest());
      }
      for (Negative negative: n) {
        negativeTraits.add(negative.getTrait());
      }
      for (Positive positive: p) {
        positiveTraits.add(positive.getTrait());
      }

      Skills skills = s.get(0);
      commenting = skills.getCommenting();
      testing = skills.getTesting();
      OOP = skills.getOOP();
      algorithms = skills.getAlgorithms();
      teamwork = skills.getTeamwork();
      frontend = skills.getFrontend();

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
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

  public ArrayList<String> getInterests() {
    return new ArrayList<>(interests);
  }

  public ArrayList<String> getNegativeTraits() {
    return new ArrayList<> (negativeTraits);
  }

  public ArrayList<String> getPositiveTraits() {
    return new ArrayList<> (positiveTraits);
  }
}