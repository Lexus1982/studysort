package me.alexand.studysort.model;

/**
 * Перечисление алгоритмов сортировки
 * Created by Sidorov Alexander on 10.01.17.
 * e-mail: asidorov84@gmail.com
 */
public enum SortingAlgorithm {
    BUBBLE("Пузырьковый", new Range(200, 1000), new Range(50, 1000)),
    INSERT("Вставками", new Range(200, 1000), new Range(50, 1000)),
    MERGE("Слиянием", new Range(50, 1000), new Range(50, 10000)),
    SHELL("Шелла", new Range(50, 1000), new Range(50, 10000));

    private static final int START_AMOUNT = 500;//Начальное количество элементов в наборе

    private final String name;//Название алгоритма
    private int maxTimePerTest;//Максимальное время выполнения одного теста в мс
    private int step;//Шаг для увеличения количества элементов в наборе
    private final Range timeRange;//Диапазон значений времени
    private final Range stepRange;//Диапазон значений шага

    SortingAlgorithm(String name, Range timeRange, Range stepRange) {
        this.name = name;
        this.timeRange = timeRange;
        this.stepRange = stepRange;
        this.maxTimePerTest = timeRange.getMin();
        this.step = stepRange.getMin();
    }

    public String getName() {
        return name;
    }

    public int getMaxTimePerTest() {
        return maxTimePerTest;
    }

    public int getStep() {
        return step;
    }

    public int getStartAmount() {
        return START_AMOUNT;
    }

    public void setMaxTimePerTest(final int preferMaxTime) {
        this.maxTimePerTest = preferMaxTime;
    }

    public void setStep(final int preferStep) {
        this.step = preferStep;
    }

    public Range getTimeRange() {
        return timeRange;
    }

    public Range getStepRange() {
        return stepRange;
    }

    public static class Range {
        private final int min;
        private final int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }
    }
}
