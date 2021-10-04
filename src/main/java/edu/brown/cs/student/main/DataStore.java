package edu.brown.cs.student.main;

import edu.brown.cs.student.runway.Rent;
import edu.brown.cs.student.runway.Review;
import edu.brown.cs.student.runway.Runway;
import edu.brown.cs.student.runway.User;

/**
 * Class consisting only of static variables and methods so the data is accessible across all
 * classes.
 */
public final class DataStore {
  private static User[] users;
  private static Rent[] rents;
  private static Review[] reviews;
  private static Runway[] runways;

  // private constructor to prevent initialisation
  private DataStore() {
  }

  /**
   * @return stored array of Users
   */
  public static User[] getUsers() {
    return users;
  }

  /**
   * @param users sets stored array of users
   */
  public static void setUsers(User[] users) {
    DataStore.users = users;
  }

  /**
   * @return stored array of Rents
   */
  public static Rent[] getRents() {
    return rents;
  }

  /**
   * @param rents sets stored array of users
   */
  public static void setRents(Rent[] rents) {
    DataStore.rents = rents;
  }

  /**
   * @return stored array of Reviews
   */
  public static Review[] getReviews() {
    return reviews;
  }

  /**
   * @param reviews sets stored array of users
   */
  public static void setReviews(Review[] reviews) {
    DataStore.reviews = reviews;
  }

  /**
   * @return stored array of Runways
   */
  public static Runway[] getRunways() {
    return runways;
  }

  /**
   * @param runways sets stored array of users
   */
  public static void setRunways(Runway[] runways) {
    DataStore.runways = runways;
  }
}


