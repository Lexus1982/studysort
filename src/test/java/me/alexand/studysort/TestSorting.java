package me.alexand.studysort;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import me.alexand.studysort.model.algorithms.Sorting;


/**
 * Тестирование правильности работы алгоритмов сортировки
 * Created by Sidorov Alexander on 10.05.17.
 * e-mail: asidorov84@gmail.com
 */
public class TestSorting {
    private int[] sourceArray;
    private static final int[] sortedArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    @Before
    public void setupTestData() {
        sourceArray = new int[]{4, 2, 1, 5, 9, 0, 3, 7, 6, 8};
    }

    @Test
    public void testBubbleSort() {
        Sorting.bubble(sourceArray);
        Assert.assertArrayEquals(sortedArray, sourceArray);
    }

    @Test
    public void testInsertSort() {
        Sorting.insert(sourceArray);
        Assert.assertArrayEquals(sortedArray, sourceArray);
    }
    @Test
    public void testMergeSort() {
        Sorting.merge(sourceArray);
        Assert.assertArrayEquals(sortedArray, sourceArray);
    }
    @Test
    public void testShellSort() {
        Sorting.shell(sourceArray);
        Assert.assertArrayEquals(sortedArray, sourceArray);
    }
}
