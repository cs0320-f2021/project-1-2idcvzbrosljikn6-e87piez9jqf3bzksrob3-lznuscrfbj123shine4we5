package edu.brown.cs.student.runway;

import com.google.gson.annotations.SerializedName;

/**
 * Class representing Runway data loaded from the API or from a file.
 * Runway contains all attributes the others do, so there should be no errors - gson handles it.
 */
public class Runway {

  public static final int FEET_MULTIPLIER = 12;
  private String fit;

  @SerializedName("user_id")
  private int userId;

  @SerializedName("bust_size")
  private String bustSize;

  @SerializedName("item_id")
  private int itemId;

  private String weight;
  private int rating;

  @SerializedName("rented_for")
  private String rentedFor;

  @SerializedName("review_text")
  private String reviewText;

  @SerializedName("body_type")
  private String bodyType;

  @SerializedName("review_summary")
  private String reviewSummary;

  private String category;
  private String height;
  private int age;

  @SerializedName("review_date")
  private String date;

  private int id;
  private String horoscope;

  /**
   * @return the fit attribute
   */
  public String getFit() {
    return fit;
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
   * @return the itemId attribute
   */
  public int getItemId() {
    return itemId;
  }

  /**
   * @return the weight attribute, converted to an integer
   */
  public int getWeight() {
    return Integer.parseInt(weight.substring(0, weight.length() - 3));
  }

  /**
   * @return the rating attribute
   */
  public int getRating() {
    return rating;
  }

  /**
   * @return the rentedFor attribute
   */
  public String getRentedFor() {
    return rentedFor;
  }

  /**
   * @return the reviewText attribute
   */
  public String getReviewText() {
    return reviewText;
  }

  /**
   * @return the bodyType attribute
   */
  public String getBodyType() {
    return bodyType;
  }

  /**
   * @return the reviewSummary attribute
   */
  public String getReviewSummary() {
    return reviewSummary;
  }

  /**
   * @return the category attribute
   */
  public String getCategory() {
    return category;
  }

  /**
   * @return the height attribute, converted to inches
   */
  public int getHeight() {
    String[] nums = height.replace("\"", "").split("'");
    return Integer.parseInt(nums[0].strip()) * FEET_MULTIPLIER + Integer.parseInt(nums[1].strip());
  }

  /**
   * @return the age attribute
   */
  public int getAge() {
    return age;
  }

  /**
   * @return the date attribute
   */
  public String getDate() {
    return date;
  }

  /**
   * @return the id attribute
   */
  public int getId() {
    return id;
  }

  /**
   * @return the horoscope attribute
   */
  public String getHoroscope() {
    return horoscope;
  }
}
