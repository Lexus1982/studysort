package me.alexand.studysort.view;

import me.alexand.studysort.model.SortingAlgorithm;
import me.alexand.studysort.model.tests.TestResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Окно результатов тестирования
 * Created by Sidorov Alexander on 13.01.17.
 * e-mail: asidorov84@gmail.com
 */
public class ResultsForm extends JDialog {
    private static final String WINDOW_CAPTION = "Результаты тестирования";
    private static final String CLOSE_BUTTON_CAPTION = "Закрыть";

    public ResultsForm(Map<SortingAlgorithm, List<TestResult>> testResults) throws HeadlessException {
        setTitle(WINDOW_CAPTION);
        setModal(true);
        setIconImage(null);

        JTabbedPane resultsTabbedPane = new JTabbedPane();

        for (Map.Entry<SortingAlgorithm, List<TestResult>> entry: testResults.entrySet()) {
            SortingAlgorithm algorithm = entry.getKey();
            List<TestResult> results = entry.getValue();

            resultsTabbedPane.addTab(algorithm.getName(), new ChartPanel(algorithm, results));
        }

        resultsTabbedPane.setBorder(BorderFactory.createEmptyBorder());
        resultsTabbedPane.setMinimumSize(resultsTabbedPane.getPreferredSize());

        JButton closeButton = new JButton(CLOSE_BUTTON_CAPTION);
        closeButton.addActionListener(new CloseButtonClick());

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        getContentPane().add(resultsTabbedPane, new GridBagConstraints(0,0 ,1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        getContentPane().add(closeButton, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        setMinimumSize(getPreferredSize());
        pack();
    }

    public  void showForm() {
        setVisible(true);
    }

    private class CloseButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }
}
