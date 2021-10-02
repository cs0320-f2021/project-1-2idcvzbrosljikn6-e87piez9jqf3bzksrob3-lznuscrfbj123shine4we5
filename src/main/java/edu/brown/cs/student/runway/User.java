package edu.brown.cs.student.runway;

import com.google.gson.annotations.SerializedName;

public class User {

  @SerializedName("user_id")
  private int userId;
  private String weight;

  @SerializedName("bust_size")
  private String bustSize;
  private String height;
  private int age;

  @SerializedName("body_type")
  private String bodyType;
  private String horoscope;

  public String getWeight() {
    return weight;
  }

  public int getUserId() {
    return userId;
  }

  public String getBustSize() {
    return bustSize;
  }

  public String getHeight() {
    return height;
  }

  public int getAge() {
    return age;
  }

  public String getBodyType() {
    return bodyType;
  }

  public String getHoroscope() {
    return horoscope;
  }
}
