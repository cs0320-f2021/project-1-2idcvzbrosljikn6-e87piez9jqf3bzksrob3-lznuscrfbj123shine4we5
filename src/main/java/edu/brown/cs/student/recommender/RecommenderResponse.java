package edu.brown.cs.student.recommender;

import com.google.gson.annotations.SerializedName;
import edu.brown.cs.student.orm.Database;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommenderResponse implements Item, KDTreeItem {

  private String id;
  private String name;
  private String meeting;
  private String grade;
  private List<RecommenderResponse> compatibilityList;
  private boolean inGroup = false;

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
  private int oop;
  private int algorithms;
  private int teamwork;
  private int frontend;
  private final int[] skillArr =
      new int[] {commenting, testing, oop, algorithms, teamwork, frontend};


  @Override
  public List<String> getVectorRepresentation() {
    List<String> vector = new ArrayList<>(Arrays.asList(experience, horoscope, meetingTimes,
        preferredLanguage, marginalizedGroups, preferGroup));
    for (String elt : interests) {
      vector.add(elt);
    }
    for (String elt : negativeTraits) {
      vector.add(elt);
    }
    for (String elt : positiveTraits) {
      vector.add(elt);
    }
    return vector;
  }

  @Override
  public String getId() {
    return id;
  }

  public int[] getSkills() {
    return getSkillArr();
  }

  public void setCompatibilityList(List<RecommenderResponse> list) {
    compatibilityList = list;
  }

  public List<RecommenderResponse> getCompatibilityList() {
    return compatibilityList;
  }

  public boolean hasCompatibilityList() {
    return (compatibilityList != null);
  }

  public int getSkills(int s) {
    return getSkillArr()[s];
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

  /**
   * Helper method that fetches data from the database for this id and loads it into the instance.
   *
   * @param db the instance of the Database class used to fetch data from
   */
  public void fetchDatabaseData(Database db) {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("id", id);
    try {
      List<Interests> i = db.select(Interests.class, queryParams);
      List<Negative> n = db.select(Negative.class, queryParams);
      List<Positive> p = db.select(Positive.class, queryParams);
      List<Skills> s = db.select(Skills.class, queryParams);
      for (Interests interest : i) {
        interests.add(interest.getInterest());
      }
      for (Negative negative : n) {
        negativeTraits.add(negative.getTrait());
      }
      for (Positive positive : p) {
        positiveTraits.add(positive.getTrait());
      }

      Skills skills = s.get(0);
      commenting = skills.getCommenting();
      testing = skills.getTesting();
      oop = skills.getOop();
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

  public ArrayList<String> getInterests() {
    return new ArrayList<>(interests);
  }

  public ArrayList<String> getNegativeTraits() {
    return new ArrayList<>(negativeTraits);
  }

  public ArrayList<String> getPositiveTraits() {
    return new ArrayList<>(positiveTraits);
  }

  @Override
  public int getAxis(int i) {
    return getSkillArr()[i];
  }

  public boolean isInGroup() {
    return inGroup;
  }

  public void setInGroup(boolean inGroup) {
    this.inGroup = inGroup;
  }

  public int[] getSkillArr() {
    return skillArr;
  }
}
