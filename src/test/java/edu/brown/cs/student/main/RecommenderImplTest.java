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

//public class RecommenderImplTest {
//
//
//
////    @Test
////    public void test_user_stories_1_to_3() {
////        String json =
////                ApiClient.normaliseJson(FileParser.readIntoString("data/justusersSMALL.json"));
////        User[] data = new Gson().fromJson(json, User[].class);
////        assertEquals(data.length, RESULT_LENGTH_1);
////        for (User u: data) {
////            assertNotNull(u.getBodyType());
////            assertNotNull(u.getBustSize());
////            assertNotNull(u.getHoroscope());
////            assertTrue(u.getWeight() != 0);
////            assertEquals(data.length, RESULT_LENGTH_2);
////        }
////    }