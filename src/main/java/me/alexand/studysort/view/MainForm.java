package me.alexand.studysort.view;

import me.alexand.studysort.Controller;
import me.alexand.studysort.model.SortingAlgorithm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Главное окно приложения
 * Created by Sidorov Alexander on 10.01.2017.
 * e-mail: asidorov84@gmail.com
 */
public class MainForm extends JFrame {
    private static final String WINDOW_CAPTION = "Методы сортировки";
    private static final String TABBED_PANE_CAPTION = "Описание алгоритмов";
    private static final String IMAGE_PANEL_CAPTION = "Анимация алгоритма";
    private static final String STATUS_BAR_INITIAL_MESSAGE = "Нажмите Старт, чтобы начать тестирование";
    private static final String STATUS_BAR_DONE_MESSAGE = "Тестирование завершено";
    private static final String START_BUTTON_CAPTION = "Старт";
    private static final String STOP_BUTTON_CAPTION = "Отмена";
    private static final String RESULTS_BUTTON_CAPTION = "Результаты";
    private static final String ABOUT_BUTTON_CAPTION = "О программе";
    private static final String EXIT_BUTTON_CAPTION = "Выход";
    private static final String[] ABOUT_MESSAGE = {"Курсовая работа по дисциплине Программирование",
            "Тема: Методы сортировки",
            "Выполнил: Сидоров Александр Станиславович"};


    private static final int WIDTH = 800;
    private static final int IMAGE_PANEL_WIDTH = 320;
    private static final int HEIGHT = 600;
    private static final int BORDER = 2;

    private Controller controller = Controller.getInstance();

    private JTabbedPane algorithmsDescriptionTabbedPane = new JTabbedPane();
    private JLabel animationAlgorithm = new JLabel();
    private JProgressBar testingStatusBar = new JProgressBar();

    private JButton startStopButton = new JButton(START_BUTTON_CAPTION);
    private JButton resultsButton = new JButton(RESULTS_BUTTON_CAPTION);
    private JButton aboutButton = new JButton(ABOUT_BUTTON_CAPTION);
    private JButton exitButton = new JButton(EXIT_BUTTON_CAPTION);

    public MainForm() throws HeadlessException {
        super(WINDOW_CAPTION);

        initGUI();

        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setMinimumSize(getPreferredSize());
    }

    public void showForm() {
        setVisible(true);
    }

    public void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void updateStatusBar(String message) {
        testingStatusBar.setString(message);
    }

    public void cancelTests() {
        startStopButton.setText(START_BUTTON_CAPTION);
        resultsButton.setEnabled(false);
        resetStatusBar();
    }

    public void testsDone() {
        startStopButton.setText(START_BUTTON_CAPTION);
        resultsButton.setEnabled(true);
        resetStatusBar();
        updateStatusBar(STATUS_BAR_DONE_MESSAGE);
    }

    private void initGUI() {
        initTabbedPane();
        initImagePanel();
        initControlPanel();
        pack();
    }

    private void initTabbedPane() {
        JPanel tabbedPanel = new JPanel(new GridBagLayout());
        tabbedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), TABBED_PANE_CAPTION));
        tabbedPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        algorithmsDescriptionTabbedPane.addChangeListener(new ChangeTabClick());

        tabbedPanel.add(algorithmsDescriptionTabbedPane, new GridBagConstraints(0, 0, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(BORDER, BORDER, BORDER, BORDER), 0, 0));

        for (SortingAlgorithm algorithm : SortingAlgorithm.values()) {
            JTextPane textPane = new JTextPane();

            textPane.setEditable(false);
            textPane.setContentType("text/html");
            textPane.setDocument(controller.getDescriptionDocument(algorithm));

            JScrollPane scrollPane = new JScrollPane(textPane);

            algorithmsDescriptionTabbedPane.addTab(algorithm.getName(), scrollPane);
        }

        getContentPane().add(BorderLayout.CENTER, tabbedPanel);
    }

    private void initImagePanel() {
        JPanel imagePanel = new JPanel();

        imagePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), IMAGE_PANEL_CAPTION));
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.setPreferredSize(new Dimension(IMAGE_PANEL_WIDTH, HEIGHT));

        SortingAlgorithm currentAlgorithm = SortingAlgorithm.values()[algorithmsDescriptionTabbedPane.getSelectedIndex()];
        animationAlgorithm.setIcon(new ImageIcon(controller.getImageURL(currentAlgorithm)));

        imagePanel.add(animationAlgorithm);

        getContentPane().add(BorderLayout.EAST, imagePanel);
    }

    private void initControlPanel() {
        startStopButton.addActionListener(new StartStopButtonClick());

        resultsButton.addActionListener(new resultsButtonClick());
        resultsButton.setEnabled(controller.isTestsDone());

        testingStatusBar.setStringPainted(true);
        testingStatusBar.setString(STATUS_BAR_INITIAL_MESSAGE);

        aboutButton.addActionListener(new AboutButtonClick());
        exitButton.addActionListener(new ExitButtonClick());

        JPanel southPanel = new JPanel(new GridBagLayout());
        southPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER), BorderFactory.createEtchedBorder()));

        southPanel.add(startStopButton, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(BORDER, BORDER, BORDER, BORDER), 0, 0));

        southPanel.add(resultsButton, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(BORDER, BORDER, BORDER, BORDER), 0, 0));

        southPanel.add(testingStatusBar, new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(BORDER, BORDER, BORDER, BORDER), 0, 0));

        southPanel.add(aboutButton, new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(BORDER, BORDER, BORDER, BORDER), 0, 0));

        southPanel.add(exitButton, new GridBagConstraints(4, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(BORDER, BORDER, BORDER, BORDER), 0, 0));

        getContentPane().add(BorderLayout.SOUTH, southPanel);
    }

    private void resetStatusBar() {
        testingStatusBar.setValue(testingStatusBar.getMinimum());
        testingStatusBar.setIndeterminate(false);
        testingStatusBar.setString(STATUS_BAR_INITIAL_MESSAGE);
    }

    private class StartStopButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isStartButton = !controller.isTestsRun();

            if (isStartButton) {
                startStopButton.setText(STOP_BUTTON_CAPTION);
                controller.startTesting();
            } else {
                startStopButton.setText(START_BUTTON_CAPTION);
                controller.cancelTests();
            }

            resultsButton.setEnabled(false);
        }
    }

    private class resultsButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controller.showResults();
        }
    }

    private class ChangeTabClick implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            SortingAlgorithm currentAlgorithm = SortingAlgorithm.values()[algorithmsDescriptionTabbedPane.getSelectedIndex()];
            animationAlgorithm.setIcon(new ImageIcon(controller.getImageURL(currentAlgorithm)));
        }
    }

    private class AboutButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(MainForm.this, ABOUT_MESSAGE, ABOUT_BUTTON_CAPTION, JOptionPane.PLAIN_MESSAGE);
        }
    }

    private class ExitButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controller.exit();
        }
    }
}
