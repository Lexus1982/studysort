package me.alexand.studysort.model.tests;

import me.alexand.studysort.model.SortingAlgorithm;
import me.alexand.studysort.model.SortableData;

import java.util.concurrent.TimeUnit;

import static me.alexand.studysort.model.algorithms.Sorting.*;

/**
 * Измерение параметров работы указанного алгоритма сортировки для конкретного набора данных
 * Created by Sidorov Alexander on 04.01.2017.
 * e-mail: asidorov84@gmail.com
 */
public class Measurement {
    private final SortingAlgorithm algorithm;
    private final SortableData data;

    public Measurement(SortingAlgorithm algorithm, SortableData data) {
        this.algorithm = algorithm;
        this.data = data;
    }

    /**
     * Провести замер
     * @return объект TestResult
     */
    public TestResult run() {
        int[] sourceData = data.getSourceData();
        long timestamp0 = System.nanoTime();

        switch (algorithm) {
            case BUBBLE:
                bubble(sourceData);
                break;
            case INSERT:
                insert(sourceData);
                break;
            case MERGE:
                merge(sourceData);
                break;
            case SHELL:
                shell(sourceData);
                break;
            default:
        }

        long timestamp1 = System.nanoTime();

        return new TestResult(data.getDataAmount(),
                TimeUnit.MILLISECONDS.convert((timestamp1 - timestamp0), TimeUnit.NANOSECONDS));
    }
}
