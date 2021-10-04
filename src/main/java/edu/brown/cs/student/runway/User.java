package edu.brown.cs.student.runway;

import com.google.gson.annotations.SerializedName;


/**
 * Class representing User data loaded from the API.
 */
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

  /**
   * @return the weight attribute, converted to an integer
   */
  public int getWeight() {
    return Integer.parseInt(weight.substring(0, weight.length() - 3));
  }

  /**
   * @return the userId attribute
   */
  public int getUserId() {
    return userId;
  }

  /**
   * @return the bustSize attribute
   */
  public String getBustSize() {
    return bustSize;
  }

  /**
   * @return the height attribute, converted to inches
   */
  public int getHeight() {
    String[] nums = height.replace("\"", "").split("'");
    return Integer.parseInt(nums[0].strip()) * 12 + Integer.parseInt(nums[1].strip());
  }

  /**
   * @return the age attribute
   */
  public int getAge() {
    return age;
  }

  /**
   * @return the bodyType attribute
   */
  public String getBodyType() {
    return bodyType;
  }

  /**
   * @return the horoscope attribute
   */
  public String getHoroscope() {
    return horoscope;
  }
}
