package edu.brown.cs.student.recommender;

/*
 * In order to merge both data sources together, we'll parse API response data first and send the
 *  list of Responses here? Then this class will create a hashmap between ids and responses. When
 *  data from the database is loaded, it references the relevant item in the hashmap using the id
 *  and updates it with the correct values
 */


import edu.brown.cs.student.bloomfilter.BloomFilterRecommender;
import edu.brown.cs.student.main.KDTree;

import java.util.*;

public class RecommenderImpl implements Recommender<RecommenderResponse> {

  private HashMap<String, RecommenderResponse> dataMap;
  private KDTree<RecommenderResponse> tree;
  private BloomFilterRecommender<RecommenderResponse> bfr;


    public RecommenderImpl(RecommenderResponse[] responses) {
      dataMap = new HashMap<>();
      for (RecommenderResponse r: responses) {
        dataMap.put(r.getId(), r);
      }

      //Make KDTree and BloomFilter with dataMap data
      bfr = new BloomFilterRecommender<>(dataMap, 0.01);
      tree = new KDTree<>(responses, 6);

      System.out.println("Loaded Recommender with " + responses.length + " students.");
  }

  public RecommenderResponse getResponse(String id){
      return dataMap.get(id);
  }


  @Override
  public List<RecommenderResponse> getTopKRecommendations(RecommenderResponse target, int k) {

      /*
      Get full sorted KDTree list
      get full sorted BF list
      sort list of id's in ascending order by summed index in both lists, return top K
       */

    List<RecommenderResponse> treeList = tree.knn(dataMap.size(), target.getSkills());
    List<RecommenderResponse> bfrList = bfr.getTopKRecommendations(target, dataMap.size());

    List<RecommenderResponse> result = new ArrayList<>(bfrList);
    result.sort(Comparator.comparingInt(o -> (treeList.indexOf(o) + bfrList.indexOf(o))));

    //if k is the size of the whole data set, save this list as the person's compatibility list while we're here.
    if(k == dataMap.size()){
      target.setCompatibilityList(result);
      return result;
    }
    return result.subList(0, k);
  }

  public List<HashSet<RecommenderResponse>> generateGroups(int k){
      //Generate a compatibility list for all users without an existing one.
      for(RecommenderResponse r: dataMap.values()){
        if(r.hasCompatibilityList()){
          continue;
        }
        r.setCompatibilityList(getTopKRecommendations(r, dataMap.size()));
      }


      /*
      create sorted list of responses by average compatibility score — that is, their average
      index in all other individual compatibility lists.
       */
      List<RecommenderResponse> ACList = new ArrayList<>(dataMap.values());
      ACList.sort(Comparator.comparingInt(this::avgCompatibilityScore));

      /* More compatible people have lower average compatibility scores, because
      it's calculated by taking the average index of the person's response in
      other people's compatibility lists. The lower the average score, the more
      that person is compatible with everyone. Therefore, we sort this list in
      ascending order, which means the most compatible people with everyone else
      will fall at the top. This is why we start at the end of the list and move
      up.
       */
      List<HashSet<RecommenderResponse>> groups = new ArrayList<>();

      int total = 0;
      for(int i = dataMap.size()-1; i >= 0; i--){
          //find the top K recommendations, which should just be the first K people in their compatibility list.
          HashSet<RecommenderResponse> group = new HashSet<>();
          int j = 0;
          while(group.size() < k && j < dataMap.size() - 1) {
            System.out.println(i + " " + j);
              RecommenderResponse groupMate = ACList.get(i).getCompatibilityList().get(j);
              if(!groupMate.inGroup){
                  group.add(groupMate);
                  groupMate.inGroup = true;
                  total+=1;
              }
              j++;
          }
          /*
          If the final group has too few members due to
          the students not breaking evenly into k groups...
           */
          if(group.size() < k){
            System.out.println(dataMap.size() + "students do not divide evenly into " + k + " groups.");
            /*
            if it's k-1 just print a notification of the smaller group
             */
            if(group.size() == k-1){
              System.out.println("The final group consists of " + (k-1) + " students.");
            }
            /*
            else if group.size() <= groups.size(), put them all in other groups
            such that all groups are either k or k+1, then notify
             */
            else if(group.size() <= groups.size()){
              int count = 1;
              for(RecommenderResponse r : group){
                groups.get(groups.size()-count).add(r);
                count++;
              }
              System.out.println("Reallocations have been made. " + (count-1) + "out of " +
                  groups.size() + " groups now have " + (k+1) + " members.");
            }
            /*
            else if numExtras + numGroups >= k-1, take 1 random member
            off existing groups until extra group has size k-1. For example, if you have 4
            groups of 15 and 1 group of 13, you should make it into 3 groups of 15 and 2 groups
            of 14.
             */
            else if(group.size() + groups.size() >= k-1){
              int count = 0;
              while(group.size() < k-1){
                Set<RecommenderResponse> g = groups.get(count);
                RecommenderResponse student = g.iterator().next();
                g.remove(student);
                group.add(student);
                count++;
              }
              groups.add(group);
              System.out.println("Reallocations have been made. " + count + "out of " +
                  (groups.size()) + " groups now have " + (k-1) + " members.");
            }else{
              groups.add(group);
              System.out.println("The final group has only " + group.size() + " members.");
            }
          } else{
            groups.add(group);
          }
          if (total == dataMap.size()) {
            break;
          }
      }
      return groups;
  }

  private int avgCompatibilityScore(RecommenderResponse response){
      int sum = 0;
      for(RecommenderResponse r : dataMap.values()){
        if(r != response) {
          sum += r.getCompatibilityList().indexOf(response);
        }
      }
      try{
        return sum/dataMap.size();
      } catch(ArithmeticException e){
        System.out.println("Error: No recommendations available. Try loading more responses.");
        return 0;
      }
  }
}
