package net.clarenceho.util;

import java.util.Collections;
import java.util.List;

public class QuickSelect<T extends Comparable<T>> {

  /**
   * Finds the k-th smallest element within the list. List will be partial sorted by this method.
   * k is 0 based (i.e. if list is sorted from smallest to largest, what is the value at list.get(k)).
   *
   * @param list
   * @param k
   * @return k-th smallest element. If list is null or empty, return null
   */
  public T findKSmallest(List<T> list, int k) {
    if (list == null || list.isEmpty()) {
      return null;
    }

    return select(list, 0, list.size()-1, k);
  }

  /**
   * Groups a list into two parts: those less than the pivot, and those greater than or equal to the pivot.
   * Lomuto partition scheme (https://en.m.wikipedia.org/wiki/Quicksort#Lomuto_partition_scheme).
   * Based on pseudo from https://en.m.wikipedia.org/wiki/Quickselect
   *
   * @param list
   * @param left
   * @param right
   * @param pivotIndex
   * @return
   */
  protected int partition(List<T> list, int left, int right, int pivotIndex) {
    T pivotValue = list.get(pivotIndex);
    Collections.swap(list, pivotIndex, right);    // move pivot to the last position
    int storeIndex = left;
    for (int i = left; i < right; i++) {
      if (list.get(i).compareTo(pivotValue) < 0) {
        Collections.swap(list, storeIndex, i);
        storeIndex++;
      }
    }
    Collections.swap(list, right, storeIndex);    // move pivot in place
    return storeIndex;
  }

  /**
   * Returns k-th smallest element of the list within left and right, both inclusive (i.e. left <= k <= right).
   * Based on pseudo code from https://en.m.wikipedia.org/wiki/Quickselect
   *
   * @param list
   * @param left
   * @param right
   * @param k
   * @return
   */
  protected T select(List<T> list, int left, int right, int k) {
    while (true) {
      if (left == right) {
        return list.get(left);
      }

      int pivotIndex = getRandomNumber(left, right);
      pivotIndex = partition(list, left, right, pivotIndex);
      // The pivot is in its final sorted position
      if (k == pivotIndex) {
        return list.get(k);
      } else if (k < pivotIndex) {
        right = pivotIndex - 1;
      } else {
        left = pivotIndex + 1;
      }
    }
  }

  private int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }
}
