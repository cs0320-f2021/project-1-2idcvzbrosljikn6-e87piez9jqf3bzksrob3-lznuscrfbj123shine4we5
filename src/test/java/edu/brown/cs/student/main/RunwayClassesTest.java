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

  @Test
  public void testUser() {
    String json =
        ApiClient.normaliseJson(FileParser.readIntoString("data/project-1/justusersSMALL.json"));
    User[] data = new Gson().fromJson(json, User[].class);
    assertEquals(data.length, 15);
    for (User u: data) {
      assertNotNull(u.getBodyType());
      assertNotNull(u.getBustSize());
      assertNotNull(u.getHoroscope());
    }
  }

  @Test
  public void testRunway() {
    String json =
        ApiClient.normaliseJson(FileParser.readIntoString("data/project-1/runwaySMALL.json"));
    Runway[] data = new Gson().fromJson(json, Runway[].class);
    assertEquals(data.length, 30);
    for (Runway u: data) {
      assertNotNull(u.getBodyType());
      assertNotNull(u.getBustSize());
      assertNotNull(u.getHoroscope());
      assertTrue(u.getUserId() > 9999);
      assertTrue(u.getRating() > 0 && u.getRating() < 11);
      assertTrue(u.getWeight() != 0);
    }
  }

  @Test
  public void testRent() {
    String json =
        ApiClient.normaliseJson(FileParser.readIntoString("data/project-1/justrentSMALL.json"));
    Rent[] data = new Gson().fromJson(json, Rent[].class);
    assertEquals(data.length, 30);
    for (Rent u: data) {
      assertNotNull(u.getCategory());
      assertNotNull(u.getFit());
      assertNotNull(u.getRentedFor());
    }
  }

  @Test
  public void testReview() {
    String json =
        ApiClient.normaliseJson(FileParser.readIntoString("data/project-1/justreviewsSMALL.json"));
    Review[] data = new Gson().fromJson(json, Review[].class);
    assertEquals(data.length, 30);
    for (Review u: data) {
      assertNotNull(u.getReviewDate());
      assertNotNull(u.getReviewText());
      assertNotNull(u.getReviewSummary());
    }
  }

  @Test
  public void testRunwayUserOverlap() {
    String json =
        ApiClient.normaliseJson(FileParser.readIntoString("data/project-1/justusersSMALL.json"));
    Runway[] data = new Gson().fromJson(json, Runway[].class);
    assertEquals(data.length, 15);
    for (Runway u: data) {
      assertNotNull(u.getBustSize());
      assertNotNull(u.getHoroscope());
      assertNull(u.getCategory());
      assertNull(u.getDate());
      assertNull(u.getRentedFor());
    }
  }
}
