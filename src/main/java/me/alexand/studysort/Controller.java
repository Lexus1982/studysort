package me.alexand.studysort;

import me.alexand.studysort.model.tests.StatusData;
import me.alexand.studysort.model.tests.Testing;
import me.alexand.studysort.model.SortingAlgorithm;
import me.alexand.studysort.model.tests.TestResult;
import me.alexand.studysort.view.MainForm;
import me.alexand.studysort.view.ResultsForm;
import me.alexand.studysort.view.TestParametersForm;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;


/**
 * Контроллер приложения
 * Created by Sidorov Alexander on 11.01.17.
 * e-mail: asidorov84@gmail.com
 */
public class Controller {
    private static final String DESCRIPTION_FILE_LOAD_ERROR_MESSAGE = "Ошибка загрузки файла с описанием";
    private static final String TESTING_ERROR_MESSAGE = "Ошибка при тестировании";
    private static final String INPUT_PARAMETERS_MESSAGE = "Введите параметры тестирования...";
    private static final String STATUS_MESSAGE = "Тестирование алгоритма: %s (тест = %d, элементов = %d, время = %d)";
    private static Controller instance;

    private MainForm mainForm;
    private Testing testing;

    private Map<SortingAlgorithm, URL> images = new TreeMap<>();//Файлы анимаций алгоритмов
    private Map<SortingAlgorithm, HTMLDocument> descriptionDocuments = new TreeMap<>();//Документы с описанием алгоритмов
    private Map<SortingAlgorithm, List<TestResult>> testResults = new TreeMap<>();//Результаты тестирования

    private boolean isTestsRun = false;
    private boolean isTestsDone = false;

    private Controller() {
        for (SortingAlgorithm algorithm : SortingAlgorithm.values()) {
            images.put(algorithm, getClass().getResource("/gifs/" + algorithm.name().toLowerCase() + ".gif"));
            descriptionDocuments.put(algorithm, loadDescriptionDocument(algorithm));
        }
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }

        return instance;
    }

    public Document getDescriptionDocument(SortingAlgorithm algorithm) {
        return descriptionDocuments.get(algorithm);
    }

    public URL getImageURL(SortingAlgorithm algorithm) {
        return images.get(algorithm);
    }

    public boolean isTestsRun() {
        return isTestsRun;
    }

    public boolean isTestsDone() {
        return isTestsDone;
    }

    public void start() {
        mainForm = new MainForm();
        mainForm.showForm();
    }

    public void exit() {
        System.exit(0);
    }

    public void startTesting() {
        mainForm.updateStatusBar(INPUT_PARAMETERS_MESSAGE);
        TestParametersForm testParametersForm = new TestParametersForm();
        testParametersForm.setLocationRelativeTo(mainForm);
        testParametersForm.showForm();
    }

    public void runTests() {
        testing = new Testing();
        testing.execute();
        isTestsRun = true;
    }

    public void updateTestingStatus(StatusData data) {
        String message = String.format(STATUS_MESSAGE, data.getAlgorithm().getName(), data.getTestNumber(),
                data.getDataAmount(), data.getTime());

        mainForm.updateStatusBar(message);
    }

    public void testsDone() {
        try {
            testResults = testing.get();
            isTestsRun = false;
            isTestsDone = true;
            mainForm.testsDone();
        }
        catch (InterruptedException | ExecutionException  exception) {
            isTestsRun = false;
            isTestsDone = false;
            mainForm.showErrorMessage(TESTING_ERROR_MESSAGE, exception.getMessage());
        }
        catch (CancellationException cancelException) {
            isTestsRun = false;
            isTestsDone = false;
        }
    }

    public void cancelTests() {
        if (isTestsRun) {
            testing.cancel(true);
        }

        mainForm.cancelTests();
    }

    public void showResults() {
        if (isTestsDone) {
            ResultsForm resultsForm = new ResultsForm(testResults);
            resultsForm.setLocationRelativeTo(mainForm);
            resultsForm.showForm();
        }
    }

    private HTMLDocument loadDescriptionDocument(SortingAlgorithm algorithm) {
        HTMLDocument document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        InputStream inputStream = getClass().getResourceAsStream("/html/" + algorithm.name().toLowerCase() + ".html");

        try (InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8")) {
            new HTMLEditorKit().read(reader, document, 0);
        }
        catch (IOException | BadLocationException exception) {
            mainForm.showErrorMessage(DESCRIPTION_FILE_LOAD_ERROR_MESSAGE, exception.getMessage());
        }

        return document;
    }

}
