package edu.brown.cs.student.runway;

public class Rent {
  private String fit;
  private int user_id; //long? string? or instance of User?
  // private final User linkedUser;
  private int item_id; // long? string?
  private int rating;
  private String rented_for;
  private String category;
  private int size;
  private int id; // or just instance of Review?
  // private final Review linkedReview;

//  public Rent(String fit, int userId, int itemId, int rating, String rentedFor,
//              String category, int size, int id) {
//    this.fit = fit;
//    this.userId = userId;
//    this.itemId = itemId;
//    this.rating = rating;
//    this.rentedFor = rentedFor;
//    this.category = category;
//    this.size = size;
//    this.id = id;
//    System.out.println(id);
//  }

  public String getRentedFor(){
    return this.rented_for;
  }

  public String getFit() {
    return fit;
  }

  public int getUser_id() {
    return user_id;
  }

  public int getItem_id() {
    return item_id;
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
