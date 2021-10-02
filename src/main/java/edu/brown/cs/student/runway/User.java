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

  public int getWeight() {
    return Integer.parseInt(weight.substring(0, weight.length() - 3));
  }

  public int getUserId() {
    return userId;
  }

  public String getBustSize() {
    return bustSize;
  }

  public int getHeight() {
    String[] nums = height.replace("\"", "").split("'");
    return Integer.parseInt(nums[0].strip()) * 12 + Integer.parseInt(nums[1].strip());
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
