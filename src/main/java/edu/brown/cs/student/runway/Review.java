package edu.brown.cs.student.runway;

import com.google.gson.annotations.SerializedName;

/**
 * Class representing review data loaded from the API.
 */
public class Review {
  @SerializedName("review_text")
  private String reviewText;

  @SerializedName("review_summary")
  private String reviewSummary;

  @SerializedName("review_date")
  private String reviewDate;
  private int id;

  /**
   * @return the reviewText attribute
   */
  public String getReviewText() {
    return reviewText;
  }

  /**
   * @return the reviewSummary attribute
   */
  public String getReviewSummary() {
    return reviewSummary;
  }

  /**
   * @return the reviewText attribute
   */
  public String getReviewDate() {
    return reviewDate;
  }

  /**
   * @return the id attribute
   */
  public int getId() {
    return id;
  }
}
