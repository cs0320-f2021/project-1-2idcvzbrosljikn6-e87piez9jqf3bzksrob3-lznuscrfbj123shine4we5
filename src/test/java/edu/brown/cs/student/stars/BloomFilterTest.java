package edu.brown.cs.student.stars;

import edu.brown.cs.student.bloomfilter.BloomFilter;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for BloomFilter.java
 */
public class BloomFilterTest {
  static Random r = new Random();

  @Test
  public void testConstructorCNK() {
    for (int i = 0; i < 10000; i++) {
      double c = r.nextInt(20) + 1;
      int n = r.nextInt(10000) + 1;
      int k = r.nextInt(20) + 1;
      BloomFilter<String> bf = new BloomFilter<>(c, n, k);
      assertEquals(bf.getK(), k);
      assertEquals(bf.getExpectedBitsPerElement(), c, 0);
      assertEquals(bf.getExpectedNumberOfElements(), n);
      assertEquals(bf.size(), c * n, 0);
    }
  }


  /**
   * Test of createHash method, of class BloomFilter.
   */
  @Test
  public void testCreateHash_String() {
    String val = UUID.randomUUID().toString();
    int result1 = BloomFilter.createHash(val);
    int result2 = BloomFilter.createHash(val);
    assertEquals(result2, result1);
    int result3 = BloomFilter.createHash(UUID.randomUUID().toString());
    assertNotSame(result3, result2);

    int result4 = BloomFilter.createHash(val.getBytes(StandardCharsets.UTF_8));
    assertEquals(result4, result1);
  }

  /**
   * Test of createHash method, of class BloomFilter.
   */
  @Test
  public void testCreateHash_byteArr() {
    String val = UUID.randomUUID().toString();
    byte[] data = val.getBytes(StandardCharsets.UTF_8);
    int result1 = BloomFilter.createHash(data);
    int result2 = BloomFilter.createHash(val);
    assertEquals(result1, result2);
  }

  /**
   * Test of createHash method, of class BloomFilter.
   */
  @Test
  public void testCreateHashes_byteArr() {
    String val = UUID.randomUUID().toString();
    byte[] data = val.getBytes(StandardCharsets.UTF_8);
    int[] result1 = BloomFilter.createHashes(data, 10);
    int[] result2 = BloomFilter.createHashes(data, 10);
    assertEquals(result1.length, 10);
    assertEquals(result2.length, 10);
    assertArrayEquals(result1, result2);
    int[] result3 = BloomFilter.createHashes(data, 5);
    assertEquals(result3.length, 5);
    for (int i = 0; i < result3.length; i++) {
      assertEquals(result3[i], result1[i]);
    }

  }

  /**
   * Test of equals method, of class BloomFilter.
   */
  @Test
  public void testEquals() {
    BloomFilter<String> instance1 = new BloomFilter<>(1000, 100);
    BloomFilter<String> instance2 = new BloomFilter<>(1000, 100);

    for (int i = 0; i < 100; i++) {
      String val = UUID.randomUUID().toString();
      instance1.add(val);
      instance2.add(val);
    }

    assert (instance1.equals(instance2));
    assert (instance2.equals(instance1));

    // make instance1 and instance2 different before clearing
    instance1.add("Another entry");

    instance1.clear();
    instance2.clear();

    assert (instance1.equals(instance2));
    assert (instance2.equals(instance1));

    for (int i = 0; i < 100; i++) {
      String val = UUID.randomUUID().toString();
      instance1.add(val);
      instance2.add(val);
    }

    assertEquals(instance1, instance2);
    assertEquals(instance2, instance1);
  }

  /**
   * Test of hashCode method, of class BloomFilter.
   */
  @Test
  public void testHashCode() {
    BloomFilter<String> instance1 = new BloomFilter<>(1000, 100);
    BloomFilter<String> instance2 = new BloomFilter<>(1000, 100);

    assertEquals(instance1.hashCode(), instance2.hashCode());

    for (int i = 0; i < 100; i++) {
      String val = UUID.randomUUID().toString();
      instance1.add(val);
      instance2.add(val);
    }

    assertEquals(instance1.hashCode(), instance2.hashCode());

    instance1.clear();
    instance2.clear();

    assertEquals(instance1.hashCode(), instance2.hashCode());

    instance1 = new BloomFilter<>(100, 10);
    instance2 = new BloomFilter<>(100, 9);
    assertNotEquals(instance1.hashCode(), instance2.hashCode());

    instance1 = new BloomFilter<>(100, 10);
    instance2 = new BloomFilter<>(99, 9);
    assertNotEquals(instance1.hashCode(), instance2.hashCode());

    instance1 = new BloomFilter<>(100, 10);
    instance2 = new BloomFilter<>(50, 10);
    assertNotEquals(instance1.hashCode(), instance2.hashCode());
  }

  /**
   * Test of expectedFalsePositiveProbability method, of class BloomFilter.
   */
  @Test
  public void testExpectedFalsePositiveProbability() {
    // These probabilities are taken from the bloom filter probability table at
    // http://pages.cs.wisc.edu/~cao/papers/summary-cache/node8.html
    BloomFilter<String> instance = new BloomFilter<>(1000, 100);

    // (m / n) = 10, k = 7
    double expResult = 0.00819;
    double result = instance.expectedFalsePositiveProbability();
    assertEquals(instance.getK(), 7);
    assertEquals(expResult, result, 0.000009);

    instance = new BloomFilter<>(100, 10);

    // (m / n) = 10, k = 7
    expResult = 0.00819;
    result = instance.expectedFalsePositiveProbability();
    assertEquals(instance.getK(), 7);
    assertEquals(expResult, result, 0.000009);

    instance = new BloomFilter<>(20, 10);

    // (m / n) = 2, k = 1
    expResult = 0.393;
    result = instance.expectedFalsePositiveProbability();
    assertEquals(1, instance.getK());
    assertEquals(expResult, result, 0.0005);

    instance = new BloomFilter<>(110, 10);

    // (m / n) = 11, k = 8
    expResult = 0.00509;
    result = instance.expectedFalsePositiveProbability();
    assertEquals(8, instance.getK());
    assertEquals(expResult, result, 0.00001);
  }

  /**
   * Test of clear method, of class BloomFilter.
   */
  @Test
  public void testClear() {
    BloomFilter<String> instance = new BloomFilter<>(1000, 100);
    for (int i = 0; i < instance.size(); i++) {
      instance.setBit(i, true);
    }
    instance.clear();
    for (int i = 0; i < instance.size(); i++) {
      assertSame(instance.getBit(i), false);
    }
  }

  /**
   * Test of add method, of class BloomFilter.
   */
  @Test
  public void testAdd() {
    BloomFilter<String> instance = new BloomFilter<>(1000, 100);

    for (int i = 0; i < 100; i++) {
      String val = UUID.randomUUID().toString();
      instance.add(val);
      assert (instance.mightContain(val));
    }
  }

  /**
   * Test of addAll method, of class BloomFilter.
   */
  @Test
  public void testAddAll() {
    List<String> v = new ArrayList<>();
    BloomFilter<String> instance = new BloomFilter<>(1000, 100);

    for (int i = 0; i < 100; i++) {
      v.add(UUID.randomUUID().toString());
    }

    instance.addAll(v);

    for (int i = 0; i < 100; i++) {
      assert (instance.mightContain(v.get(i)));
    }
  }

  /**
   * Test of contains method, of class BloomFilter.
   */
  @Test
  public void testMightContain() {
    BloomFilter<String> instance = new BloomFilter<>(10000, 10);

    for (int i = 0; i < 10; i++) {
      instance.add(Integer.toBinaryString(i));
      assert (instance.mightContain(Integer.toBinaryString(i)));
    }

    assertFalse(instance.mightContain(UUID.randomUUID().toString()));
  }

  /**
   * Test of containsAll method, of class BloomFilter.
   */
  @Test
  public void testMightContainAll() {
    List<String> v = new ArrayList<String>();
    BloomFilter<String> instance = new BloomFilter<>(1000, 100);

    for (int i = 0; i < 100; i++) {
      v.add(UUID.randomUUID().toString());
      instance.add(v.get(i));
    }

    assert (instance.mightContainAll(v));
  }

  /**
   * Test of getBit method, of class BloomFilter.
   */
  @Test
  public void testGetBit() {
    BloomFilter<String> instance = new BloomFilter<>(1000, 100);
    Random r = new Random();

    for (int i = 0; i < 100; i++) {
      boolean b = r.nextBoolean();
      instance.setBit(i, b);
      assertSame(instance.getBit(i), b);
    }
  }

  /**
   * Test of setBit method, of class BloomFilter.
   */
  @Test
  public void testSetBit() {
    BloomFilter<String> instance = new BloomFilter<>(1000, 100);
    Random r = new Random();

    for (int i = 0; i < 100; i++) {
      instance.setBit(i, true);
      assertSame(instance.getBit(i), true);
    }

    for (int i = 0; i < 100; i++) {
      instance.setBit(i, false);
      assertSame(instance.getBit(i), false);
    }
  }

  /**
   * Test of size method, of class BloomFilter.
   */
  @Test
  public void testSize() {
    for (int i = 100; i < 1000; i++) {
      BloomFilter<String> instance = new BloomFilter<>(i, 10);
      assertEquals(instance.size(), i);
    }
  }

  /**
   * Test error rate *
   */
  @Test
  public void testFalsePositiveRate1() {
    // Numbers are from // http://pages.cs.wisc.edu/~cao/papers/summary-cache/node8.html
    for (int j = 10; j < 21; j++) {
      List<byte[]> v = new ArrayList<byte[]>();
      BloomFilter<Object> instance = new BloomFilter<>(100 * j, 100);

      for (int i = 0; i < 100; i++) {
        byte[] bytes = new byte[100];
        r.nextBytes(bytes);
        v.add(bytes);
      }
      instance.addAll(v);

      long f = 0;
      double tests = 300000;
      for (int i = 0; i < tests; i++) {
        byte[] bytes = new byte[100];
        r.nextBytes(bytes);
        if (instance.mightContain(bytes)) {
          if (!v.contains(bytes)) {
            f++;
          }
        }
      }

      double ratio = f / tests;
      assertEquals(instance.expectedFalsePositiveProbability(), ratio, 0.01);
    }
  }

  /**
   * Test for correct k
   **/
  @Test
  public void testGetK() {
    // Numbers are from http://pages.cs.wisc.edu/~cao/papers/summary-cache/node8.html
    BloomFilter<String> instance;

    instance = new BloomFilter<>(2, 1);
    assertEquals(1, instance.getK());

    instance = new BloomFilter<>(3, 1);
    assertEquals(2, instance.getK());

    instance = new BloomFilter<>(4, 1);
    assertEquals(3, instance.getK());

    instance = new BloomFilter<>(5, 1);
    assertEquals(3, instance.getK());

    instance = new BloomFilter<>(6, 1);
    assertEquals(4, instance.getK());

    instance = new BloomFilter<>(7, 1);
    assertEquals(5, instance.getK());

    instance = new BloomFilter<>(8, 1);
    assertEquals(6, instance.getK());

    instance = new BloomFilter<>(9, 1);
    assertEquals(6, instance.getK());

    instance = new BloomFilter<>(10, 1);
    assertEquals(7, instance.getK());

    instance = new BloomFilter<>(11, 1);
    assertEquals(8, instance.getK());

    instance = new BloomFilter<>(12, 1);
    assertEquals(8, instance.getK());
  }

  /**
   * Test of contains method, of class BloomFilter.
   */
  @Test
  public void testContains_GenericType() {
    int items = 100;
    BloomFilter<String> instance = new BloomFilter<>(0.01, items);

    for (int i = 0; i < items; i++) {
      String s = UUID.randomUUID().toString();
      instance.add(s);
      assertTrue(instance.mightContain(s));
    }
  }

  /**
   * Test of contains method, of class BloomFilter.
   */
  @Test
  public void testContains_byteArr() {
    int items = 100;
    BloomFilter<String> instance = new BloomFilter<>(0.01, items);

    for (int i = 0; i < items; i++) {
      byte[] bytes = new byte[500];
      r.nextBytes(bytes);
      instance.add(bytes);
      assertTrue(instance.mightContain(bytes));
    }
  }

  /**
   * Test of count method, of class BloomFilter.
   */
  @Test
  public void testCount() {
    int expResult = 100;
    BloomFilter<String> instance = new BloomFilter<>(0.01, expResult);
    for (int i = 0; i < expResult; i++) {
      byte[] bytes = new byte[100];
      r.nextBytes(bytes);
      instance.add(bytes);
    }
    int result = instance.count();
    assertEquals(expResult, result);

    instance = new BloomFilter<>(0.01, expResult);
    for (int i = 0; i < expResult; i++) {
      instance.add(UUID.randomUUID().toString());
    }
    result = instance.count();
    assertEquals(expResult, result);
  }


}