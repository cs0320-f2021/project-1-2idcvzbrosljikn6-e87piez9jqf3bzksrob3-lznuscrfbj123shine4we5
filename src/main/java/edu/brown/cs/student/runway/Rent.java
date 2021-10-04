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

  /**
   * @return the rentedFor attribute
   */
  public String getRentedFor() {
    return rentedFor;
  }

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
   * @return the itemId attribute
   */
  public int getItemId() {
    return itemId;
  }

  /**
   * @return the rating attribute
   */
  public int getRating() {
    return rating;
  }

  /**
   * @return the category attribute
   */
  public String getCategory() {
    return category;
  }

  /**
   * @return the size attribute
   */
  public int getSize() {
    return size;
  }

  /**
   * @return the id attribute
   */
  public int getId() {
    return id;
  }
}
