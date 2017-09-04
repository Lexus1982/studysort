package me.alexand.studysort.model.tests;

/**
 * Результаты тестирования алгоритма сортировки
 * Created by Sidorov Alexander on 04.01.2017.
 * e-mail: asidorov84@gmail.com
 */
public class TestResult {
    private final int dataAmount;//Размер набора, на котором проводилось тестирование
    private final long time;//Время тестирования в мс

    public TestResult(int dataAmount, long time) {
        this.time = time;
        this.dataAmount = dataAmount;
    }

    public int getDataAmount() {
        return dataAmount;
    }

    public long getTime() {
        return time;
    }
}
