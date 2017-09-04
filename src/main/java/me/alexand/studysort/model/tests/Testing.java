package me.alexand.studysort.model.tests;

import me.alexand.studysort.Controller;
import me.alexand.studysort.model.SortableData;
import me.alexand.studysort.model.SortingAlgorithm;

import javax.swing.*;
import java.util.*;

/**
 * Полное тестирование всех представленных в SortingAlgorithm алгоритмов
 * Класс является наследником SwingWorker, т.к. выполняется в отдельном фоновом потоке.
 * Created by Sidorov Alexander on 11.01.17.
 * e-mail: asidorov84@gmail.com
 */
public class Testing extends SwingWorker<Map<SortingAlgorithm, List<TestResult>>, StatusData> {
    private final Controller controller = Controller.getInstance();

    /**
     * Тело фонового потока для проведения тестирования
     * Уведомляет контроллер о статусе выполнения после прохождения каждого теста
     * @return Список результатов для каждого алгоритма
     * @throws Exception - возможные исключения
     */
    @Override
    protected Map<SortingAlgorithm, List<TestResult>> doInBackground() throws Exception {
        Map<SortingAlgorithm, List<TestResult>> testsResults = new TreeMap<>();

        //Для каждого алгоритма запускается серия тестов с разными наборами данных
        //до тех пор, пока время выполнения теста не превысит заданный предел для текущего алгоритма,
        //либо количество чисел в наборе не превысит максимальное значение беззнакового
        //четырехбайтового целового (Integer.MAX_VALUE - максимальный размер массива в Java)

        //Количество элементов в наборе данных для теста начинается с фиксированного значения
        //для данного алгоритма (algorithm.getStartAmount()) и для каждого последующего теста
        //увеличивается на algorithm.getStep()

        for (SortingAlgorithm algorithm : SortingAlgorithm.values()) {
            // Тип Long выбран с целью исключения переполнения при увеличении шага
            long dataAmount = algorithm.getStartAmount();

            List<TestResult> results = new LinkedList<>();

            int testNumber = 1;

            while (dataAmount < Integer.MAX_VALUE) {
                //Подготавливаем данные для теста и сам тест
                SortableData data = new SortableData((int)dataAmount);
                Measurement measurement = new Measurement(algorithm, data);

                //Уведомляем JVM, что сборку мусора лучше провести сейчас,
                //чем во время выполнения теста
                System.gc();

                //Запускаем тестирование текущего алгоритма с определенным набором данных
                TestResult currentTestResult = measurement.run();

                //Уведомляем основной поток о статусе тестирования
                publish(new StatusData(algorithm, testNumber, dataAmount, currentTestResult.getTime()));

                testNumber++;

                results.add(currentTestResult);

                //Если время работы теста превысило допустимый предел - переходим к следующему алгоритму
                if (currentTestResult.getTime() > algorithm.getMaxTimePerTest()) {
                    break;
                }

                dataAmount += algorithm.getStep();
            }

            testsResults.put(algorithm, results);
        }

        return testsResults;
    }

    /**
     * Обработка уведомлений о ходе выполнения потока
     */
    @Override
    protected void process(List<StatusData> chunks) {
        if (isCancelled()) {
            return;
        }

        chunks.forEach(controller::updateTestingStatus);
    }

    /**
     * Вызывается когда тестирование законечно или его отменили
     */
    @Override
    protected void done() {
        controller.testsDone();
    }
}
