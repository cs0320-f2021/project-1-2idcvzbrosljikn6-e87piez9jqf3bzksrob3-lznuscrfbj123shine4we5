package edu.brown.cs.student.main;

import edu.brown.cs.student.runway.Rent;
import edu.brown.cs.student.runway.Review;
import edu.brown.cs.student.runway.Runway;
import edu.brown.cs.student.runway.User;

public final class DataStore {
  private static User[] users;
  private static Rent[] rents;
  private static Review[] reviews;
  private static Runway[] runways;

  private DataStore() {

  }

  public static User[] getUsers() {
    return users;
  }

  public static void setUsers(User[] users) {
    DataStore.users = users;
  }

  public static Rent[] getRents() {
    return rents;
  }

  public static void setRents(Rent[] rents) {
    DataStore.rents = rents;
  }

  public static Review[] getReviews() {
    return reviews;
  }

  public static void setReviews(Review[] reviews) {
    DataStore.reviews = reviews;
  }

  public static Runway[] getRunways() {
    return runways;
  }

  public static void setRunways(Runway[] runways) {
    DataStore.runways = runways;
  }
}


