package edu.brown.cs.student.runway;

import com.google.gson.annotations.SerializedName;

/**
 * Class representing Runway data loaded from the API or from a file.
 * Runway contains all attributes the others do, so there should be no errors - gson handles it.
 */
public class Runway {

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


  public String getFit() {
    return fit;
  }

  public int getUserId() {
    return userId;
  }

  public String getBustSize() {
    return bustSize;
  }

  public int getItemId() {
    return itemId;
  }

  public int getWeight() {
    return Integer.parseInt(weight.substring(0, weight.length() - 3));
  }

  public int getRating() {
    return rating;
  }

  public String getRentedFor() {
    return rentedFor;
  }

  public String getReviewText() {
    return reviewText;
  }

  public String getBodyType() {
    return bodyType;
  }

  public String getReviewSummary() {
    return reviewSummary;
  }

  public String getCategory() {
    return category;
  }

  public int getHeight() {
    String[] nums = height.replace("\"", "").split("'");
    return Integer.parseInt(nums[0].strip()) * 12 + Integer.parseInt(nums[1].strip());
  }

  public int getAge() {
    return age;
  }

  public String getDate() {
    return date;
  }

  public int getId() {
    return id;
  }

  public String getHoroscope() {
    return horoscope;
  }
}
