package edu.brown.cs.student.main;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class KDTree{
  private Node root;
  private String[] dims;

  public KDTree(List<Hashtable<String,String>> dataList, String[] dimensions) {
    root = new Node();
    root.data = dataList;
    dims = dimensions;
    setupTree(0);
  }

  public void setupTree(int dimension){
    /*
     * 1. find median value for particular dimension in current data list
     * 2. split list into sublists with higher or lower values than median
     * 3. recur on children with the next dimension, they should cycle.
     * 4. recur until data list size is 1
     */

  }

  public static class Node{
    private List<Hashtable<String,String>> data;
    private Node parent;
    private Node left;
    private Node right;
  }
}
