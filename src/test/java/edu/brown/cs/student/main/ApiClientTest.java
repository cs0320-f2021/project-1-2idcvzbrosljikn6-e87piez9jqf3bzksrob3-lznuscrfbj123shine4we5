package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;

import edu.brown.cs.student.apiClient.ApiClient;
import org.junit.Test;

public class ApiClientTest {

  public static final int RESULT_LENGTH_1 = 15;
  public static final int RESULT_LENGTH_2 = 30;

  @Test
  public void testUsersCall() {
    ApiClient ac = new ApiClient();
    ac.usersApiCall(false);
    assertEquals(DataStore.getUsers().length, RESULT_LENGTH_1, 2);

    ac.usersApiCall(true);
    assertEquals(DataStore.getRunways().length, RESULT_LENGTH_1, 2);
  }

  @Test
  public void testRentsCall() {
    ApiClient ac = new ApiClient();
    ac.rentsApiCall();
    assertEquals(DataStore.getRents().length, RESULT_LENGTH_2, 2);
  }

  @Test
  public void testReviewsCall() {
    ApiClient ac = new ApiClient();
    ac.reviewsApiCall();
    assertEquals(DataStore.getReviews().length, RESULT_LENGTH_2, 2);
  }

  @Test
  public void testNormaliseJson() {
    String json = "{\"fit\": \"fit\", \"user_id\": \"420272\", \"bust_size\": "
        + "\"34d\", \"item_id\": \"2260466\", \"weight\": \"137lbs\", \"rating\": "
        + "\"10\", \"rented_for\": \"vacation\", \"review_text\": \"An adorable romper! "
        + "Belt and zipper were a little hard to navigate in a full day of wear/bathroom "
        + "use, but that's to be expected. Wish it had pockets, but other than that-- "
        + "absolutely perfect! I got a million compliments.\", \"body_type\": "
        + "\"hourglass\", \"review_summary\": \"So many compliments!\", \"category\": "
        + "\"romper\", \"height\": \"5' 8\\\"\", \"size\": \"14\", \"age\": \"28\", "
        + "\"review_date\": \"April 20, 2016\", \"id\": 1, \"horoscope\": \"Taurus\"},";

    String jsonCorrect = "[{\"fit\": \"fit\", \"user_id\": \"420272\", \"bust_size\": "
        + "\"34d\", \"item_id\": \"2260466\", \"weight\": \"137lbs\", \"rating\": "
        + "\"10\", \"rented_for\": \"vacation\", \"review_text\": \"An adorable romper! "
        + "Belt and zipper were a little hard to navigate in a full day of wear/bathroom "
        + "use, but that's to be expected. Wish it had pockets, but other than that-- "
        + "absolutely perfect! I got a million compliments.\", \"body_type\": "
        + "\"hourglass\", \"review_summary\": \"So many compliments!\", \"category\": "
        + "\"romper\", \"height\": \"5' 8\\\"\", \"size\": \"14\", \"age\": \"28\", "
        + "\"review_date\": \"April 20, 2016\", \"id\": 1, \"horoscope\": \"Taurus\"}]";

    assertEquals(ApiClient.normaliseJson(json), jsonCorrect);
    assertEquals(ApiClient.normaliseJson(jsonCorrect), jsonCorrect);
  }
}
