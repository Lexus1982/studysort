package me.alexand.studysort.view;

import me.alexand.studysort.Controller;
import me.alexand.studysort.model.SortingAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Форма для ввода параметров тестирования
 * Created by Sidorov Alexandуr on 20.01.17.
 * e-mail: asidorov84@gmail.com
 */
public class TestParametersForm extends JDialog {
    private static final String WINDOW_CAPTION = "Введите параметры тестирования";
    private static final String START_BUTTON_CAPTION = "Запуск";
    private static final String CANCEL_BUTTON_CAPTION = "Отмена";
    private static final String LEFT_LABEL_TEXT = "Максимальное время: %d <=";
    private static final String MIDDLE_LABEL_TEXT = "<= %d, шаг: %d <=";
    private static final String RIGHT_LABEL_TEXT = "<= %d";
    private static final int TEXT_FIELD_COLUMNS = 5;

    private final Controller controller = Controller.getInstance();

    public TestParametersForm() {
        setTitle(WINDOW_CAPTION);
        setModal(true);
        setResizable(false);
        setIconImage(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        JButton startButton = new JButton(START_BUTTON_CAPTION);
        startButton.addActionListener(new StartButtonClick());

        JButton cancelButton = new JButton(CANCEL_BUTTON_CAPTION);
        cancelButton.addActionListener(new CancelButtonClick());

        int panelsCounter = 0;

        for (SortingAlgorithm algorithm : SortingAlgorithm.values()) {
            getContentPane().add(initParametersPanel(algorithm), new GridBagConstraints(0, panelsCounter++, 3, 1, 0, 0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }

        getContentPane().add(startButton, new GridBagConstraints(1, panelsCounter, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));

        getContentPane().add(cancelButton, new GridBagConstraints(2, panelsCounter, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));

        pack();
    }

    public void showForm() {
        setVisible(true);
    }

    private JPanel initParametersPanel(final SortingAlgorithm algorithm) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), algorithm.getName()));

        JLabel leftLabel = new JLabel(String.format(LEFT_LABEL_TEXT, algorithm.getTimeRange().getMin()));
        JLabel middleLabel = new JLabel(String.format(MIDDLE_LABEL_TEXT, algorithm.getTimeRange().getMax(), algorithm.getStepRange().getMin()));
        JLabel rightLabel = new JLabel(String.format(RIGHT_LABEL_TEXT, algorithm.getStepRange().getMax()));

        //Поле для ввода максимального времени для теста
        JFormattedTextField timeTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());

        timeTextField.setColumns(TEXT_FIELD_COLUMNS);
        timeTextField.setValue(algorithm.getMaxTimePerTest());
        timeTextField.setHorizontalAlignment(JFormattedTextField.CENTER);
        timeTextField.addPropertyChangeListener(event -> {
            int inputValue = ((Number)timeTextField.getValue()).intValue();
            int value = inputValue;

            if (inputValue < algorithm.getTimeRange().getMin()) {
                value = algorithm.getTimeRange().getMin();
            }

            if (inputValue > algorithm.getTimeRange().getMax()) {
                value = algorithm.getTimeRange().getMax();
            }

            timeTextField.setValue(value);
            algorithm.setMaxTimePerTest(value);
        });

        //Поле для ввода шага
        JFormattedTextField stepTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());

        stepTextField.setColumns(TEXT_FIELD_COLUMNS);
        stepTextField.setValue(algorithm.getStep());
        stepTextField.setHorizontalAlignment(JFormattedTextField.CENTER);
        stepTextField.addPropertyChangeListener(event -> {
            int inputValue = ((Number)stepTextField.getValue()).intValue();
            int value = inputValue;

            if (inputValue < algorithm.getStepRange().getMin()) {
                value = algorithm.getStepRange().getMin();
            }

            if (inputValue > algorithm.getStepRange().getMax()) {
                value = algorithm.getStepRange().getMax();
            }

            stepTextField.setValue(value);
            algorithm.setStep(value);
        });

        panel.add(leftLabel);
        panel.add(timeTextField);
        panel.add(middleLabel);
        panel.add(stepTextField);
        panel.add(rightLabel);

        return panel;
    }

    private class StartButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            controller.runTests();
        }
    }

    private class CancelButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            controller.cancelTests();
        }
    }
}
