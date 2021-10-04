package edu.brown.cs.student.runway;

import com.google.gson.annotations.SerializedName;

/**
 * Class representing rent data loaded from the API.
 * Gson populates variable values automatically.
 */
public class Rent {
  private String fit;

  @SerializedName("user_id") // gson needs to be informed when an attribute's name in json and
  // Java don't match up
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
