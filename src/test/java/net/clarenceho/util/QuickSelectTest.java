package net.clarenceho.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuickSelectTest {

  @Test
  public void testPartitionIntegerSingle() {
    Integer[] inputs = new Integer[] { 3 };
    QuickSelect<Integer> qs = new QuickSelect<>();
    int index = qs.partition(Arrays.asList(inputs), 0, 0, 0);
    Assert.assertEquals(0, index);
  }

  @Test
  public void testPartitionIntegerSmall() {
    Integer[] inputs = new Integer[] { 9, 7, 5, 3, 1};
    QuickSelect<Integer> qs = new QuickSelect<>();
    int index = qs.partition(Arrays.asList(inputs), 0, 4, 3);
    Assert.assertEquals(1, index);
  }

  @Test
  public void testPartitionDoubleSmall() {
    Double[] inputs = new Double[] { 13.37, 42.42, 1.1, 9.9, 2.2, 8.8, 6.6, 7.7 };
    QuickSelect<Double> qs = new QuickSelect<>();
    int index = qs.partition(Arrays.asList(inputs), 0, 7, 3);
    Assert.assertEquals(5, index);
  }

  @Test
  public void testSelectSmallString() {
    String[] inputs = new String[] { "AABC", "ABC", "XYZ", "ZAA", "AAABC" };
    List<String> inputList = Arrays.asList(inputs);
    QuickSelect<String> qs = new QuickSelect<>();
    String output = qs.select(inputList, 0, 4, 0);
    Assert.assertEquals("AAABC", output);

    output = qs.select(inputList, 0, 4, 1);
    Assert.assertEquals("AABC", output);

    output = qs.select(inputList, 0, 4, 4);
    Assert.assertEquals("ZAA", output);

    output = qs.select(inputList, 0, 4, 2);
    Assert.assertEquals("ABC", output);

    output = qs.select(inputList, 0, 4, 3);
    Assert.assertEquals("XYZ", output);
  }

  @Test
  public void testFindKSmallestWithNull() {
    QuickSelect<Integer> qs = new QuickSelect<>();
    Assert.assertNull(qs.findKSmallest(null, 42));
    Assert.assertNull(qs.findKSmallest(new ArrayList<>(), 42));
  }

  @Test
  public void testFindKSmallestSmall() {
    Integer[] inputs = new Integer[] { 456, 963, 741, 123, 753 };
    List<Integer> inputList = Arrays.asList(inputs);
    QuickSelect<Integer> qs = new QuickSelect<>();

    int output = qs.findKSmallest(inputList, 4);
    Assert.assertEquals(963, output);

    output = qs.findKSmallest(inputList, 0);
    Assert.assertEquals(123, output);

    output = qs.findKSmallest(inputList, 2);
    Assert.assertEquals(741, output);

    output = qs.findKSmallest(inputList, 1);
    Assert.assertEquals(456, output);

    output = qs.findKSmallest(inputList, 3);
    Assert.assertEquals(753, output);
  }

  @Test
  public void testFindKSmallestRandomLarge() {
    int size = 10_000;
    Random generator = new Random(42);
    List<Integer> inputList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      inputList.add(generator.nextInt(500));
    }

    List<Integer> comparisonList = new ArrayList<>(inputList);
    Collections.sort(comparisonList);

    QuickSelect<Integer> qs = new QuickSelect<>();
    for (int i = size - 1; i >= 0; i--) {
      int output = qs.findKSmallest(inputList, i);
      Assert.assertEquals((int)comparisonList.get(i), output);
    }
  }

  /**
   * Compare the performance of optimized sort vs Quickselect
   */
  @Ignore
  @Test
  public void comparePerformance() {
    int total_items = 1_000_000;
    int list_size = 10000;
    int trial = total_items / list_size;

    int pos = 5000;

    Random generator = new Random(1337);
    List<Integer> baseInputList = new ArrayList<>();
    for (int i = 0; i < list_size; i++) {
      baseInputList.add(generator.nextInt(5000));
    }

    List<List<Integer>> sortAndFindInputs = new ArrayList<>();
    List<List<Integer>> quickSelectInputs = new ArrayList<>();
    for (int i = 0; i < trial; i++) {
      sortAndFindInputs.add(new ArrayList<>(baseInputList));
      quickSelectInputs.add(new ArrayList<>(baseInputList));
    }

    List<Integer> sortAndFindResults = new ArrayList<>();
    List<Integer> quickSelectResults = new ArrayList<>();

    long start = System.currentTimeMillis();
    for (int i = 0; i < trial; i++) {
      Collections.sort(sortAndFindInputs.get(i));
      sortAndFindResults.add(sortAndFindInputs.get(i).get(pos));
    }
    long end = System.currentTimeMillis();
    long sortAndFindMs = end - start;
    System.err.println("Sort and find took " + sortAndFindMs + "ms");

    QuickSelect<Integer> qs = new QuickSelect<>();
    start = System.currentTimeMillis();
    for (int i = 0; i < trial; i++) {
      quickSelectResults.add(qs.findKSmallest(quickSelectInputs.get(i), pos));
    }
    end = System.currentTimeMillis();
    long quickSelectMs = end - start;
    System.err.println("QuickSelect took " + quickSelectMs + "ms");

    Assert.assertEquals(sortAndFindResults, quickSelectResults);
  }
}
