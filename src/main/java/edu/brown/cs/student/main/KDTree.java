package edu.brown.cs.student.main;

import edu.brown.cs.student.recommender.KDTreeItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class KDTree<T extends KDTreeItem> {
  private final ArrayList<Hashtable<Integer, KDNode>> sortedTables;
  private final LinkedList<KDNode> nodeQueue;
  private final KDNode root;
  private final int numAxes;
  private final ArrayList<KDNode> nodeList;

  public KDTree(T[] dataArr, int axisNum) {
    nodeQueue = new LinkedList<>();
    numAxes = axisNum;
    sortedTables = new ArrayList<>();

    //Create all nodes at the start and put them in a list.
    nodeList = buildNodes(dataArr);

    //created hashtables of the data sorted by each s
    //temp solution
    int[] skillArr = new int[numAxes];
    for (int i = 0; i < numAxes; i++) {
      skillArr[i] = i;
    }
    for (int skill : skillArr) {
      nodeList.sort((o1, o2) -> {
        try {
          return (int) (o1.data.getAxis(skill) - o2.data.getAxis(skill));
        } catch (Exception e) {
          e.printStackTrace();
          return 0;
        }
      });
      Hashtable<Integer, KDNode> table = new Hashtable<>();
      for (int i = 0; i < nodeList.size(); i++) {
        KDNode n = nodeList.get(i);
        table.put(i, n);
        n.setIndex(skill, i);
        n.setRange(skill, 0, nodeList.size() - 1);
      }
      sortedTables.add(table);
    }


    int median = 0;
    if (dataArr.length % 2 == 0) {
      median = dataArr.length / 2;
    } else {
      median = (dataArr.length - 1) / 2;
    }
    root = sortedTables.get(0).get(median);
    root.visited = true;
    nodeQueue.add(root);
    root.axis = 0;

    while (!nodeQueue.isEmpty()) {
      setupChildren(nodeQueue.remove());
    }
  }

  public ArrayList<KDNode> buildNodes(T[] data) {
    ArrayList<KDNode> nodes = new ArrayList<>();
    for (T r : data) {
      KDNode node = new KDNode();
      node.data = r;
      nodes.add(node);
    }
    return nodes;
  }

  public void setupChildren(KDNode curr) {
    curr.right = findChild(curr, true);
    curr.left = findChild(curr, false);
  }


  public KDNode getRoot() {
    return root;
  }


  public KDNode findChild(KDNode curr, boolean right) {
    Hashtable<Integer, KDNode> table = sortedTables.get(curr.getAxis());
    int dir = (right) ? 1 : -1;
    int i = curr.getIndex(curr.getAxis());

    /*
    Traverse the relevant-axis sortedTable in the appropriate direction until
    reaching EITHER the range limit of curr OR a suitable child.

    A suitable child is both unvisited and located within all of curr's axial ranges.
     */
    while (curr.inRange(curr.axis, i + dir) && !curr.isSuitable(table.get(i))) {
      i += dir;
    }
    if (curr.isSuitable(table.get(i))) {
      KDNode child = table.get(i);
      //mark node as visited
      child.visit();

      //set child's range in relevant axis to be half of original range
      child.setRange(curr.getAxis(),
          (right) ? curr.getIndex(curr.getAxis()) : curr.getMin(curr.getAxis()),
          (right) ? curr.getMax(curr.getAxis()) : curr.getIndex(curr.getAxis()));

      //for all other axes, child inherits the parent's range
      for (int axis = 0; axis < numAxes; axis++) {
        if (axis != curr.getAxis()) {
          child.setRange(axis, curr.getMin(axis), curr.getMax(axis));
        }
      }

      //child's relevant axis set to next in the cycle
      child.setAxis((curr.getAxis() < numAxes - 1) ? curr.getAxis() + 1 : 0);

      //add child to queue
      nodeQueue.add(child);

      return child;
    }

    return null;
  }

  public ArrayList<T> knn(int k, int[] target) {
    ArrayList<T> neighbors = new ArrayList<>();

    /*
    Array to keep track of the index and distance of the furthest
    neighbor from target. Note: furthest[0] is the index within
    neighbors, furthest[1] is the distance from target point.
     */

    return knnHelper(k, target, root, neighbors, 0, 0);
  }

  /*
  Recursive helper method for knn(). Performs the K-Nearest Neighbors algorithm.
  Takes in an int[] target which contains the weight/height/age from knn().
  Also requires an axis number and an array of values for the furthest neighbor,
  explained in the comments for knn().
   */
  private ArrayList<T> knnHelper(int k, int[] target, KDNode curr,
                                 ArrayList<T> neighbors, int furthest, int axis) {
    //Get the straight-line (Euclidean) distance from your target point to the current node.
    int eDist = euclideanDistance(target, curr.data);

    /*
    If the current node is closer to your target point than
    one of your k-nearest neighbors, or if your collection
    of neighbors is not full, update the list accordingly
     */
    if (eDist < furthest || neighbors.size() < k) {
      neighbors.add(curr.data);
      neighbors.sort(
          Comparator.comparingInt(o -> euclideanDistance(target, o)));
      if (neighbors.size() > k) {
        //if there are now more than k neighbors saved, remove the furthest neighbor.
        neighbors.remove(neighbors.size() - 1);
      }
      furthest = euclideanDistance(target,
          neighbors.get(neighbors.size() - 1));
    }

    //If there are no children on which to recur, return current list of neighbors as array.
    if (curr.right == null && curr.left == null) {
      neighbors.sort(
          Comparator.comparingInt(o -> euclideanDistance(target, o)));
      return neighbors;
    }

    /*
    If the Euclidean distance between the target point and
    the furthest neighbor is greater than the relevant axis distance
    between the current node and target point, recur on
    both children. Also recur on both children if you have found fewer
    than k neighbors so far.
     */
    int axisDist = 0;
    axisDist = target[axis] - curr.data.getAxis(axis);
    if (furthest > Math.abs(axisDist) || neighbors.size() < k) {
      if (curr.left != null) {
        ArrayList<T> result = knnHelper(k, target, curr.left, neighbors, furthest,
            (axis == numAxes - 1)
                ? 0 : axis + 1);
        if (curr.right != null && result != null) {
          return knnHelper(k, target, curr.right, result, furthest, (axis == numAxes - 1) ? 0
              : axis + 1);
        } else {
          return result;
        }
      } else {
        return knnHelper(k, target, curr.right, neighbors, furthest,
            (axis == numAxes - 1) ? 0 : axis + 1);
      }
    }

    /*
    If the previous if-statement is false, and you do not need
    to recur down both children, then:

    If the current node's coordinate on the relevant axis is
    less than the target's coordinate on the relevant axis,
    recur on the right child.

    Else if the current node's coordinate on the relevant axis
    is greater than the target's coordinate on the relevant axis,
    recur on the left child.

    The reason we don't have to account for 0 as an axis distance is you'd
    never enter this part of the if statement if all the dimensions are the
    same. To get here, the axis distance has to exceed the furthest Euclidean
    distance, which means it's impossible for the two to have the same
    dimensions.
     */
    if (axisDist < 0 && curr.left != null) {
      return knnHelper(k, target, curr.left, neighbors, furthest,
          (axis == numAxes - 1) ? 0 : axis + 1);
    } else if (axisDist > 0 && curr.right != null) {
      return knnHelper(k, target, curr.right, neighbors, furthest,
          (axis == numAxes - 1) ? 0 : axis + 1);
    }
    return neighbors;
  }

  /*
  Helper method to find the furthest neighbor and return its index and distance as an array.
   */
  private int[] findFurthest(int[] target, List<T> neighbors) {
    int index = 0;
    int dist = 0;
    for (T n : neighbors) {
      int nDist = euclideanDistance(target, n);
      dist = Math.max(dist, nDist);
      if (dist == nDist) {
        index = neighbors.indexOf(n);
      }
    }
    return new int[] {index, dist};
  }

  /*
  A helper function that gets the straight line distance between a target point and
  a given user (in terms of weight height and age).
   */
  private int euclideanDistance(int[] target, T r) {
    int sum = 0;
    for (int i = 0; i < numAxes; i++) {
      sum += Math.pow(target[i] - r.getAxis(i), 2);
    }
    return (int) Math.sqrt(sum);
  }
  //public version for testing
//  public int euclideanDistance(T r1, T r2) {
//    return (int) Math.abs(Math.sqrt(Math.pow(r1.getWeight() - r2.getWeight(), 2)
//        + Math.pow(r1.getHeight() - r2.getHeight(), 2) + Math.pow(r1.getAge() - r2.getAge(), 2)));
//  }

  public ArrayList<KDNode> getNodes() {
    return nodeList;
  }

  public class KDNode {
    private T data;
    private KDNode left;
    private KDNode right;
    // know your indices in each of the 3 sortedTables. indices match up to axes
    private ArrayList<ArrayList<Integer>> indicesAndRanges = new ArrayList<>();
    private boolean visited;
    private int axis;


    public void setAxis(int a) {
      axis = a;
    }

    public int getAxis() {
      return axis;
    }

    public KDNode getRight() {
      return right;
    }

    public KDNode getLeft() {
      return left;
    }

    public T getData() {
      return data;
    }

    public void setIndex(int currentAxis, int index) {
      try {
        indicesAndRanges.get(currentAxis).add(0, index);
      } catch (IndexOutOfBoundsException e) {
        while (indicesAndRanges.size() <= currentAxis) {
          indicesAndRanges.add(new ArrayList<>());
        }
        indicesAndRanges.get(currentAxis).add(0, index);
      }
    }

    public void setRange(int currentAxis, int min, int max) {
      try {
        indicesAndRanges.get(currentAxis).set(1, min);
      } catch (IndexOutOfBoundsException e) {
        indicesAndRanges.get(currentAxis).add(1, min);
      }
      try {
        indicesAndRanges.get(currentAxis).set(2, max);
      } catch (IndexOutOfBoundsException e) {
        indicesAndRanges.get(currentAxis).add(2, max);
      }
    }

    public int getIndex(int currentAxis) {
      return indicesAndRanges.get(currentAxis).get(0);
    }

    public int getMin(int currentAxis) {
      return indicesAndRanges.get(currentAxis).get(1);
    }

    public int getMax(int currentAxis) {
      return indicesAndRanges.get(currentAxis).get(2);
    }

    public boolean isVisited() {
      return visited;
    }

    public void visit() {
      visited = true;
    }

    public boolean inRange(int currentAxis, int index) {
      return (index >= getMin(currentAxis) && index <= getMax(currentAxis));
    }

    public boolean inRange(KDNode node) {
      for (int i = 0; i < numAxes; i++) {
        if (!inRange(i, node.getIndex(i))) {
          return false;
        }
      }
      return true;
    }

    public boolean isSuitable(KDNode n) {
      return !(n.isVisited() || !inRange(n));
    }
  }
}
