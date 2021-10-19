package edu.brown.cs.student.main;

import static org.junit.Assert.*;

import edu.brown.cs.student.runway.Runway;
import org.junit.Test;

import edu.brown.cs.student.apiClient.ApiClient;

import java.util.Arrays;
import java.util.List;

public class KDTreeTest {

  @Test
  public void testTreeBuild() throws Exception {
    new ApiClient().usersApiCall(true);
    Runway[] data = DataStore.getRunways();
    String[] axes = new String[] {"weight", "height", "age"};

    KDTree<Runway> kd = new KDTree<>(data, axes.length);
    for (KDTree.KDNode node : kd.getNodes()) {
      assertTrue(node.getLeft() == null ||
          node.getLeft().getIndex(node.getAxis()) < node.getIndex(node.getAxis()));
      assertTrue(node.getRight() == null ||
          node.getRight().getIndex(node.getAxis()) > node.getIndex(node.getAxis()));
    }
    assertEquals(kd.getNodes().size(), data.length);
  }

//  @Test
//  public void testKNN() throws Exception {
//    new ApiClient().usersApiCall(true);
//    Runway[] data = DataStore.getRunways();
//    String[] axes = new String[] {"weight", "height", "age"};
//
//    KDTree<Runway> kd = new KDTree<>(data,axes.length);
//    //pick a random node
//    KDTree<Runway>.KDNode node = kd.getNodes().get((int)(Math.random()*kd.getNodes().size()));
//    //get the nearest s/2 neighbors to node, where s = total number of nodes
//    List<Runway> nn = Arrays.asList(kd.knn(kd.getNodes().size()/2,
//       kd.getNumVal(node.getData(), axes[0]),
//     kd.getNumVal(node.getData(), axes[1]),
//    kd.getNumVal(node.getData(), axes[2])));
//
//    /*
//    assert that all nodes not included in
//    nearest neighbors list are further away
//    from node than the furthest-nearest-neighbor,
//    AND
//    that all nodes included in nearest neighbors list
//    are closer to node than the furthest-nearest-neighbor
//    (barring the furthest neighbor in question)
//     */
//    System.out.println(
//        "Distances from target of the nearest " + kd.getNodes().size() / 2 + " neighbors:");
//    for (RecommenderResponse r : nn) {
//      System.out.print(kd.euclideanDistance(node.getData(), r) + " ");
//    }
//
//    for (KDTree.KDNode n : kd.getNodes()) {
//      if (nn.contains(n.getData())) {
//        assertTrue(kd.euclideanDistance(node.getData(), n.getData())
//            <= kd.euclideanDistance(node.getData(), nn.get(nn.size() - 1)));
//      } else {
//        assertTrue("Found a node "
//                + kd.euclideanDistance(node.getData(), n.getData()) +
//                " units away from target that should have been included above.",
//            kd.euclideanDistance(node.getData(), n.getData())
//                >= kd.euclideanDistance(node.getData(), nn.get(nn.size() - 1)));
//      }
//    }
//  }
}
//

