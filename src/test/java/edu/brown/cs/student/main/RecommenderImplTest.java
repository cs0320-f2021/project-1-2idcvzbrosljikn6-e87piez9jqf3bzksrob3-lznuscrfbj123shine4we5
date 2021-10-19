package edu.brown.cs.student.main;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import edu.brown.cs.student.apiClient.ApiClient;
import edu.brown.cs.student.orm.Database;
import edu.brown.cs.student.recommender.RecommenderImpl;
import edu.brown.cs.student.recommender.RecommenderResponse;
import edu.brown.cs.student.runway.Rent;
import edu.brown.cs.student.runway.Review;
import edu.brown.cs.student.runway.Runway;
import edu.brown.cs.student.runway.User;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

public class RecommenderImplTest {
    @Test
    public void test_user_story_4() throws SQLException, ClassNotFoundException {
        ApiClient client = new ApiClient();
        edu.brown.cs.student.recommender.RecommenderResponse[] responses = client.localRecommenderUsers();
        Database db = new Database("data/integration.sqlite3");
        for (edu.brown.cs.student.recommender.RecommenderResponse response : responses) {
            response.fetchDatabaseData(db);
        }
        RecommenderImpl recimpl = new RecommenderImpl(responses);
        List<HashSet<RecommenderResponse>> list = recimpl.generateGroups(60);
        assertEquals(list.get(0).size(), 61);
    }
}