package edu.brown.cs.student.runway;

import com.google.gson.annotations.SerializedName;

public class Rent {
  private String fit;
  @SerializedName("user_id")
  private int userId;
  @SerializedName("item_id")
  private int itemId;
  private int rating;

  @SerializedName("rented_for")
  private String rentedFor;
  private String category;
  private int size;
  private int id;

  public String getRentedFor() {
    return rentedFor;
  }

  public String getFit() {
    return fit;
  }

  public int getUserId() {
    return userId;
  }

  public int getItemId() {
    return itemId;
  }

  public int getRating() {
    return rating;
  }

  public String getCategory() {
    return category;
  }

  public int getSize() {
    return size;
  }

  public int getId() {
    return id;
  }
}
