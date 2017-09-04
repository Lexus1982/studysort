package me.alexand.studysort.model;

import java.util.Random;

/**
 * Множество случайных чисел для дальнейшей сортировки
 * Created by Sidorov Alexander on 04.01.2017.
 * e-mail: asidorov84@gmail.com
 */
public class SortableData {
    private static final int MIN_VALUE = -100;
    private static final int MAX_VALUE = 100;

    private final int[] sourceData;
    private final int dataAmount;

    /**
     * Контструктор создает случаный набор из dataAmount чисел
     * @param dataAmount - количество чисел в множестве
     */
    public SortableData(int dataAmount) {
        this.dataAmount = dataAmount;
        sourceData = new int[dataAmount];
        fill();
    }

    public int[] getSourceData() {
        return sourceData;
    }

    public int getDataAmount() {
        return dataAmount;
    }

    /**
     * Заполняем набор случайными значениями от MIN_VALUE до MAX_VALUE
     */
    private void fill() {
        Random randomGenerator = new Random();

        for (int index = 0; index < dataAmount; index++) {
            sourceData[index] = MIN_VALUE + randomGenerator.nextInt(MAX_VALUE - MIN_VALUE);
        }
    }
}
