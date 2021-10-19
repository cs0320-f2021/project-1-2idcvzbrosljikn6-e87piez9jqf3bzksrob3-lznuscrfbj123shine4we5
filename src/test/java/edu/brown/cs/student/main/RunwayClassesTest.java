package edu.brown.cs.student.main;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import edu.brown.cs.student.apiClient.ApiClient;
import edu.brown.cs.student.runway.Rent;
import edu.brown.cs.student.runway.Review;
import edu.brown.cs.student.runway.Runway;
import edu.brown.cs.student.runway.User;
import org.junit.Test;

public class RunwayClassesTest {

  public static final int RESULT_LENGTH_1 = 15;
  public static final int RESULT_LENGTH_2 = 30;
  public static final int MIN_ID = 10000;
  public static final int MAX_RATING = 10;
  public static final int MAX_ID = 10000;

  @Test
  public void testUser() {
    String json =
        ApiClient.normaliseJson(FileParser.readIntoString("data/justusersSMALL.json"));
    User[] data = new Gson().fromJson(json, User[].class);
    assertEquals(data.length, RESULT_LENGTH_1);
    for (User u: data) {
      assertNotNull(u.getBodyType());
      assertNotNull(u.getBustSize());
      assertNotNull(u.getHoroscope());
    }
  }

  @Test
  public void testRunway() {
    String json =
        ApiClient.normaliseJson(FileParser.readIntoString("data/runwaySMALL.json"));
    Runway[] data = new Gson().fromJson(json, Runway[].class);
    assertEquals(data.length, RESULT_LENGTH_2);
    for (Runway u: data) {
      assertNotNull(u.getBodyType());
      assertNotNull(u.getBustSize());
      assertNotNull(u.getHoroscope());
      assertTrue(u.getUserId() >= MAX_ID);
      assertTrue(u.getRating() > 0 && u.getRating() <= MAX_RATING);
      assertTrue(u.getWeight() != 0);
    }
  }

  @Test
  public void testRent() {
    String json =
        ApiClient.normaliseJson(FileParser.readIntoString("data/justrentSMALL.json"));
    Rent[] data = new Gson().fromJson(json, Rent[].class);
    assertEquals(data.length, RESULT_LENGTH_2);
    for (Rent u: data) {
      assertNotNull(u.getCategory());
      assertNotNull(u.getFit());
      assertNotNull(u.getRentedFor());
    }
  }

  @Test
  public void testReview() {
    String json =
        ApiClient.normaliseJson(FileParser.readIntoString("data/justreviewsSMALL.json"));
    Review[] data = new Gson().fromJson(json, Review[].class);
    assertEquals(data.length, RESULT_LENGTH_2);
    for (Review u: data) {
      assertNotNull(u.getReviewDate());
      assertNotNull(u.getReviewText());
      assertNotNull(u.getReviewSummary());
    }
  }

  @Test
  public void testRunwayUserOverlap() {
    String json =
        ApiClient.normaliseJson(FileParser.readIntoString("data/justusersSMALL.json"));
    Runway[] data = new Gson().fromJson(json, Runway[].class);
    assertEquals(data.length, RESULT_LENGTH_1);
    for (Runway u: data) {
      assertNotNull(u.getBustSize());
      assertNotNull(u.getHoroscope());
      assertNull(u.getCategory());
      assertNull(u.getDate());
      assertNull(u.getRentedFor());
    }
  }
}
