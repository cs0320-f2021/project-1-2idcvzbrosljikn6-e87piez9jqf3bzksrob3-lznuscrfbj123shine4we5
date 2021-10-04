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

  public String getReviewText() {
    return reviewText;
  }

  public String getReviewSummary() {
    return reviewSummary;
  }

  public String getReviewDate() {
    return reviewDate;
  }

  public int getId() {
    return id;
  }
}
