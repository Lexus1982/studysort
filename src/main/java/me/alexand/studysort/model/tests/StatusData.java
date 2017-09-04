package me.alexand.studysort.model.tests;

import me.alexand.studysort.model.SortingAlgorithm;

/**
 * Промежуточные данные о процессе тестирования
 * Created by Sidorov Alexander on 21.01.2017.
 * e-mail: asidorov84@gmail.com
 */
public class StatusData {
    private final SortingAlgorithm algorithm;//Текущий тестируемый алгоритм
    private final int testNumber;//Номер теста
    private final long dataAmount;//Размер набора данных текущего теста
    private final long time;//Время выполнения теста в мс

    public StatusData(SortingAlgorithm algorithm, int testNumber, long dataAmount, long time) {
        this.algorithm = algorithm;
        this.testNumber = testNumber;
        this.dataAmount = dataAmount;
        this.time = time;
    }

    public SortingAlgorithm getAlgorithm() {
        return algorithm;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public long getDataAmount() {
        return dataAmount;
    }

    public long getTime() {
        return time;
    }
}
