package edu.brown.cs.student.main;

import edu.brown.cs.student.runway.Runway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class KDTree {
  private final Node root;
  private final ArrayList<Runway[]> sortedLists;
  private final String[] axes;
  private final ArrayList<Integer> visited;

  public KDTree(Runway[] dataArr, String[] dimensions) {
    visited = new ArrayList<Integer>();
    root = new Node();
    sortedLists = new ArrayList<>();

    //created versions of the data sorted by each axis
    for (String dimension : dimensions) {
      Arrays.sort(dataArr, (o1, o2) -> {
        try {
          return (int) (getNumVal((Runway) o1, dimension) - getNumVal((Runway) o2, dimension));
        } catch (Exception e) {
          e.printStackTrace();
          return 0;
        }
      });
      sortedLists.add(dataArr);
    }

    axes = dimensions;
    int median = 0;
    if (dataArr.length % 2 == 0) {
      median = dataArr.length / 2;
    } else {
      median = (dataArr.length - 1) / 2;
    }
    root.data = sortedLists.get(0)[median];
    visited.add(root.data.getUserId());
    setupTree(0, root);
  }

  public void setupTree(int axis, Node n) {
    /*
     * create left/right children
     * left.data = next smallest neighbor on dimension
     * right.data = next largest neighbor on dimension
     */

    List<Runway> tempList = Arrays.asList(sortedLists.get(axis));

    if (needsChild(sortedLists.get(axis), tempList.indexOf(n.data), true)) {
      Node r = new Node();
      r.data = tempList.get(tempList.indexOf(n.data) + 1);
      //if assigned user is visited, keep looking to the right until it is not
      while (visited.contains(r.data.getUserId())) {
        r.data = tempList.get(tempList.indexOf(r.data) + 1);
      }
      n.right = r;
      visited.add(n.right.data.getUserId());
      if (visited.size() < tempList.size()) {
        int x = 1;
        if (axis + x == axes.length) {
          x = -axis;
        }
        setupTree(axis + x, n.right);
      }
    }

    if (needsChild(sortedLists.get(axis), tempList.indexOf(n.data), false)) {
      Node l = new Node();
      l.data = tempList.get(tempList.indexOf(n.data) - 1);
      while (visited.contains(l.data.getUserId())) {
        l.data = tempList.get(tempList.indexOf(l.data) - 1);
      }
      n.left = l;
      visited.add(n.left.data.getUserId());


      if (visited.size() < tempList.size()) {
        int x = 1;
        if (axis + x == axes.length) {
          x = -axis;
        }
        setupTree(axis + x, n.left);
      }
    }

  }

  public boolean needsChild(Runway[] array, int i, boolean right) {
    int dir;
    if (right) {
      dir = 1;
    } else {
      dir = -1;
    }

    while (true) {
      //if index is at the min/max, return false. No child needed.
      if (i + dir == array.length || i + dir == -1) {
        return false;
      }
      /*
      If the next user in line is visited, increase i and loop again.
      Otherwise return true - suitable child exists.
       */
      if (visited.contains(array[i + dir].getUserId())) {
        i += dir;
      } else {
        return true;
      }
    }
  }

  /*
   * This method returns an inputted user's height,weight, or age as a double.
   */
  public int getNumVal(Runway user, String axis) throws Exception {
    switch (axis) {
      case "weight":
        return user.getWeight();
      case "height":
        return user.getHeight();
      case "age":
        return user.getAge();
      default:
        throw new Exception("Unexpected axis input.");
    }
  }

  public Runway[] knn(int k, int weight, int height, int age) {
    ArrayList<Runway> neighbors = new ArrayList<>();

    /*
    Array to keep track of the index and distance of the furthest
    neighbor from target. Note: furthest[0] is the index within
    neighbors, furthest[1] is the distance from target point.
     */
    int[] furthest = new int[2];

    return knnHelper(k, new int[] {weight, height, age}, root, neighbors, furthest, 0);
  }

  /*
  Recursive helper method for knn(). Performs the K-Nearest Neighbors algorithm.
  Takes in an int[] target which contains the weight/height/age from knn().
  Also requires an axis number and an array of values for the furthest neighbor,
  explained in the comments for knn().
   */
  private Runway[] knnHelper(int k, int[] target, Node curr,
                             List<Runway> neighbors, int[] furthest, int axis) {
    //Get the straight-line (Euclidean) distance from your target point to the current node.
    int eDist = euclideanDistance(target[0], target[1], target[2], curr.data);

    /*
    If the current node is closer to your target point than
    one of your k-nearest neighbors, or if your collection
    of neighbors is not full, update the list accordingly
     */
    if (eDist < furthest[1] || neighbors.size() < k) {
      neighbors.add(curr.data);
      if (neighbors.size() > k) {
        //if there are now more than k neighbors saved, remove the furthest neighbor.
        neighbors.remove(furthest[0]);
        furthest = findFurthest(target, neighbors);
      } else if (eDist > furthest[1]) {
        furthest[1] = eDist;
        furthest[0] = neighbors.indexOf(curr.data);
      }
    }

    //If there are no children on which to recur, return current list of neighbors as array.
    if (curr.right == null && curr.left == null) {
      return neighbors.toArray(new Runway[0]);
    }

    /*
    If the Euclidean distance between the target point and
    the furthest neighbor is greater than the relevant axis distance
    between the current node and target point, recur on
    both children. Also recur on both children if you have found fewer
    than k neighbors so far.
     */
    int axisDist = 0;
    switch (axis) {
      case 0:
        axisDist = target[axis] - curr.data.getWeight();
        break;
      case 1:
        axisDist = target[axis] - curr.data.getHeight();
        break;
      case 2:
        axisDist = target[axis] - curr.data.getAge();
        break;
      default:
        System.out.println("ERROR");
        break;
    }
    if (furthest[1] > Math.abs(axisDist) || neighbors.size() < k) {
      if (curr.left != null) {
        if (axis == axes.length - 1) {
          axis = 0;
        } else {
          axis += 1;
        }
        Runway[] result = knnHelper(k, target, curr.left, neighbors, furthest, axis);
        if (curr.right != null && result != null) {
          return knnHelper(k, target, curr.left, Arrays.asList(result), furthest, axis);
        }
      } else {
        if (axis == axes.length - 1) {
          axis = 0;
        } else {
          axis += 1;
        }
        return knnHelper(k, target, curr.right, neighbors, furthest, axis);
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
     */
    if (axisDist > 0 && curr.left != null) {
      if (axis == axes.length - 1) {
        axis = 0;
      } else {
        axis += 1;
      }
      return knnHelper(k, target, curr.left, neighbors, furthest, axis);
    } else if (axisDist <= 0 && curr.right != null) {
      if (axis == axes.length - 1) {
        axis = 0;
      } else {
        axis += 1;
      }
      return knnHelper(k, target, curr.right, neighbors, furthest, axis);
    }
    return null;
  }

  /*
  Helper method to find the furthest neighbor and return its index and distance as an array.
   */
  private int[] findFurthest(int[] target, List<Runway> neighbors) {
    int index = 0;
    int dist = 0;
    for (Runway n : neighbors) {
      int nDist = euclideanDistance(target[0], target[1], target[2], n);
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
  private int euclideanDistance(int x, int y, int z, Runway r) {
    return (int) Math.abs(Math.sqrt(Math.pow(x - r.getWeight(), 2)
        + Math.pow(y - r.getHeight(), 2) + Math.pow(z - r.getAge(), 2)));
  }

  private static class Node {
    private Runway data;
    private Node left;
    private Node right;
  }
}
