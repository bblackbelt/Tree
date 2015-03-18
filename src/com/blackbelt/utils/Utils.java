package com.blackbelt.utils;

import java.util.Arrays;

/**
 * Created by emanuele on 15.03.15.
 */
public class Utils {

    public static long getFirstPrime(long n) {
        if (n <= 2) {
            return 1;
        }
        for (long i = n + 1; ; i++) {
            if (isPrime(i)) {
                return i;
            }
        }
    }

    public static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        long sqrtN = (long) Math.sqrt(n) + 1;
        for (long i = 6L; i <= sqrtN; i += 6) {
            if (n % (i - 1) == 0 || n % (i + 1) == 0) return false;
        }
        return true;
    }

    public static int roundup2(int v) {
        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        v++;
        return v;
    }

    public static long pow(long a, long n) {
        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return a;
        }
        if ((n & 1) == 0) {
            return pow(a * a, n >> 1);
        } else {
            return a * pow(a * a, (n - 1) >> 1);
        }
    }

    private static void swap(int[] array, int firstIndex, int endIndex) {
        int tmp = array[firstIndex];
        array[firstIndex] = array[endIndex];
        array[endIndex] = tmp;
    }

    private static int partition(int[] array, int start, int end) {
        int pivotIndex = start + (end - start) / 2;
        int pivot = array[pivotIndex];
        swap(array, pivotIndex, end);
        int cutIndex = start - 1;
        for (int i = start; i < end; i++) {
            if (array[i] < pivot) {
                swap(array, ++cutIndex, i);
            }
        }
        swap(array, ++cutIndex, end);
        return cutIndex;
    }

    private static void simpleQuickSort(int[] a, int lo, int hi) {
        if (lo > hi) {
            return;
        }
        int cutIndex = partition(a, lo, hi);
        simpleQuickSort(a, lo, cutIndex - 1);
        simpleQuickSort(a, cutIndex + 1, hi);
    }

    public static void sort(int[] a) {
       heapSort(a);
       // simpleQuickSort(a, 0, a.length - 1);
        assert isSorted(a, 0, a.length - 1);
    }

    private static void heapify(int[] a, int size, int index) {
        int leftIndex = (index << 1) + 1;
        int rightIndex = (leftIndex) + 1;
        int largestIndex = index;
        if (leftIndex < size && a[leftIndex] > a[largestIndex]) {
            largestIndex = leftIndex;
        }
        if (rightIndex < size && a[rightIndex] > a[largestIndex]) {
            largestIndex = rightIndex;
        }
        if (largestIndex != index) {
            swap(a, largestIndex, index);
            heapify(a, size, largestIndex);
        }
    }

    private static void heapSort(int[] a) {
        buildHeap(a);
        int lastSwapIndex = a.length - 1;
        for (int i = a.length - 1; i >= 0; i--) {
            swap(a, 0, lastSwapIndex);
            heapify(a, lastSwapIndex, 0);
            lastSwapIndex--;
        }
    }

    private static void buildHeap(int[] a) {
        for (int i = a.length / 2 - 1; i >= 0; i--) {
            heapify(a, a.length, i);
        }
    }

    private static void mergeSort(int[] a) {
        int[] tmp = new int[a.length];
    }

    private static boolean isSorted(int[] a, int lo, int hi) {
        for (int i = lo; i < hi - 1; i++)
            if (isGreater(a[i], a[i + 1])) {
                return false;
            }
        return true;
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo; i < hi - 1; i++)
            if (isGreater(a[i], a[i + 1])) {
                return false;
            }
        return true;
    }

    private static boolean isGreater(Comparable first, Comparable second) {
        return first.compareTo(second) > 0;
    }
}
