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

public class RecommenderImpl implements Recommender<Response> {

  private HashMap<String,Response> dataMap;


    public RecommenderImpl(Collection<Response> apiData, Collection<Response> sqlData){

      /*
      traverse apiData and add ID's as keys to dataMap


       */
  }


  @Override
  public List<Response> getTopKRecommendations(Response item, int k) {
    return null;
  }
}
