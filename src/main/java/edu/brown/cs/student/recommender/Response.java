package edu.brown.cs.student.recommender;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response implements Item {

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

  public String getMarginalizedGroups() {
    return marginalizedGroups;
  }

  public boolean getPreferGroup() {
    return preferGroup.equals("Yes");
  }
}