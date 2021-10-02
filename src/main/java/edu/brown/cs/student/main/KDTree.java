package edu.brown.cs.student.main;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;


public class KDTree {
  private Node root;
  private ArrayList<List<Hashtable<String,String>>> sortedLists;
  private String[] dims;
/*
TODO: Reformat code to use User/Runway classes from API component instead of Hashtable
 */
  public KDTree(List<Hashtable<String,String>> dataList, String[] dimensions) {
    root = new Node();
    sortedLists = new ArrayList<>();
    for(String dimension : dimensions){
      Collections.sort(dataList, (o1,o2) -> {
        return (int) (getNumVal(o1, dimension) - getNumVal(o2, dimension));
      });
      sortedLists.add(dataList);
    }

    root.data = null;
    dims = dimensions;
    setupTree(0, root);
  }

  public void setupTree(int index, Node n){
    /*
     * 1. find median value for particular dimension in current data list
     * 2. split list into sublists with higher or lower values than median
     * 3. recur on children with the next dimension, they should cycle.
     * 4. recur until data list size is 1
     */

    if(n.data.size() > 1) {
      String dimension = dims[index];

      // 1. find median value for particular dimension in current data list

      n.data.sort((o1, o2) -> {
        return (int) (getNumVal(o1, dimension) - getNumVal(o2, dimension));
      });

      int size = n.data.size();
      double median = getNumVal(n.data.get(size / 2), dimension);

      // 2.  split list into sublists with higher or lower values than median

      n.left = new Node(n);
      n.right = new Node(n);
      n.left.data = n.data.subList(0, size / 2);
      n.right.data = n.data.subList((size + 2) / 2, size - 1);

      // 3. recur on children with the next dimension, they should cycle.

      if (index == dims.length - 1) {
        index = 0;
      } else {
        index++;
      }

      setupTree(index, n.left);
      setupTree(index, n.right);
    }
  }

  /*
  * This method returns an inputted user's height,weight, or age as a double.
   */
  public double getNumVal(Hashtable<String,String> user, String dimension){
    if(dimension.equals("height")){
      //convert height string, written in feet and inches, into a double in inches.
      //ex: " 6' 7" " --> 79

      //first, remove inches marker (double quote character)
      String s = user.get(dimension).replace("\"", "");
      //split remaining string into feet value and inches value
      String[] nums = s.split("'");

      //Take resulting string array and transcribe it to an int array
      //Note: There may be a better way to do this in Java, I'm more familiar with doing it in Python.
      int[] height = new int[2];
      for(int i = 0; i < 2; i++){
        height[i] = Integer.parseInt(nums[i]);
      }

      //return height in inches
      return (height[0]*12)+height[1];

    } else{
      return Integer.parseInt(user.get(dimension).replaceAll("[a-zA-Z]",""));
    }
  }

  public List<Hashtable<String,String>> knn(int k, String userID){
    /*

     */

    return null;
  }

  public List<Hashtable<String,String>> knn(int k, int weight, int height, int age){
    return null;
  }

  public class Node {
    private Hashtable<String,String> data;
    private Node left;
    private Node right;
  }
}
