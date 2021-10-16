package edu.brown.cs.student.recommender;

/*
 * In order to merge both data sources together, we'll parse API response data first and send the
 *  list of Responses here? Then this class will create a hashmap between ids and responses. When
 *  data from the database is loaded, it references the relevant item in the hashmap using the id
 *  and updates it with the correct values
 */


import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class RecommenderImpl implements Recommender<RecommenderResponse> {

  private HashMap<String, RecommenderResponse> dataMap;


    public RecommenderImpl(RecommenderResponse[] responses){
      RecommenderResponse r = responses[0];
      System.out.println(r.getInterests().get(0));
      System.out.println(r.getAlgorithms());
      System.out.println(r.getMarginalizedGroups());
      System.out.println(r.getMeetingTimes());

      /*
      traverse apiData and add ID's as keys to dataMap


       */
  }


  @Override
  public List<RecommenderResponse> getTopKRecommendations(RecommenderResponse item, int k) {
    return null;
  }
}
