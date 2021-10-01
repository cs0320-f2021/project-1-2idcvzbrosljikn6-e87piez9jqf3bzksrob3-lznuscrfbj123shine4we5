package edu.brown.cs.student.runway;

import java.time.LocalDate;

public class Review {
  private String review_text;
  private String review_summary;
  private String review_date;
  private int id;

  public String getReviewText() {
    return review_text;
  }

  public String getReviewSummary() {
    return review_summary;
  }

  public String getReviewDate() {
    return review_date;
  }

  public int getId() {
    return id;
  }

//  public Review(String reviewText, String reviewSummary, LocalDate reviewDate, int id) {
//    this.reviewText = reviewText;
//    this.reviewSummary = reviewSummary;
//    this.reviewDate = reviewDate;
//    this.id = id;
//  }
}
