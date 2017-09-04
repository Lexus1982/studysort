package me.alexand.studysort.view;

import me.alexand.studysort.model.SortingAlgorithm;
import me.alexand.studysort.model.tests.TestResult;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Панель для рисования графика результатов тестирования
 * На входе должно быть минимум два результата
 * Created by Sidorov Alexandr on 14.01.2017.
 * e-mail: asidorov84@gmail.com
 */
public class ChartPanel extends JComponent {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private static final int CAPTION_HEIGHT = 40;
    private static final int CANVAS_BORDER = 2;
    private static final int GRAPH_BORDER = 15;

    private static final int GAP_BETWEEN_LABEL_AND_AXIS = 10;
    private static final int GAP_BETWEEN_GRAPH_AND_GRID = 15;
    private static final int CELL_WIDTH = 25;
    private static final int CELL_HEIGHT = 25;

    private static final Color BACKGROUND = Color.DARK_GRAY;
    private static final Color CAPTION_COLOR = Color.WHITE;
    private static final Color AXIS_COLOR = Color.WHITE;
    private static final Color GRID_COLOR = Color.GRAY;
    private static final Color GRAPH_COLOR = Color.GREEN;

    private static final String CAPTION_TEMPLATE = "Результаты тестирования алгоритма - %s, Всего выполнено тестов - %d";
    private static final String TIME_LABEL_SUFFIX = " мс";

    private final SortingAlgorithm algorithm;
    private final List<TestResult> results;
    private final int maxDataAmount;
    private final int minDataAmount;
    private final long maxTime;
    private final long minTime;

    public ChartPanel(SortingAlgorithm algorithm, List<TestResult> results) {
        this.algorithm = algorithm;
        this.results = results;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //Ищем минимальное и максимальное значения замеров тестов
        Collections.sort(results, Comparator.comparing(TestResult::getTime));

        maxTime = results.get(results.size() - 1).getTime();
        minTime = results.get(0).getTime();

        //Ищем минимальное и максимальное значения количества элементов в тестовых наборах
        Collections.sort(results, Comparator.comparing(TestResult::getDataAmount));

        maxDataAmount = results.get(results.size() - 1).getDataAmount();
        minDataAmount = results.get(0).getDataAmount();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        clear(g2d);
        printCaption(g2d);

        g2d.setFont(g2d.getFont().deriveFont(10.0f));
        FontMetrics metrics = g2d.getFontMetrics();

        //Вычисляем координаты начала осей координат
        int startPointX = CANVAS_BORDER + GRAPH_BORDER + metrics.stringWidth(String.valueOf(maxTime) + TIME_LABEL_SUFFIX) + GAP_BETWEEN_LABEL_AND_AXIS;
        int startPointY = getHeight() - (CANVAS_BORDER + GRAPH_BORDER + metrics.stringWidth(String.valueOf(maxDataAmount)) + GAP_BETWEEN_LABEL_AND_AXIS);

        //Вычисляем координаты концов осей
        int axisAmountX = getWidth() - CANVAS_BORDER - GRAPH_BORDER;
        int axisAmountY = startPointY;

        int axisTimeX = startPointX;
        int axisTimeY = CANVAS_BORDER + CAPTION_HEIGHT + GRAPH_BORDER;

        //Вычисляем ширину и длину области сетки для вывода графика
        int gridWidth = axisAmountX - startPointX - GAP_BETWEEN_GRAPH_AND_GRID;
        int gridHeight = startPointY - axisTimeY - GAP_BETWEEN_GRAPH_AND_GRID;

        //Вычисляем коэффициенты сжатия
        double factorAmount = (gridWidth * 1.0) / (( maxDataAmount - minDataAmount) * 1.0);
        double factorTime = (gridHeight * 1.0) / (( maxTime - minTime) * 1.0);

        //Рисуем сетку

        int currentPointAmount = startPointX + CELL_WIDTH;

        while (currentPointAmount < startPointX + gridWidth) {
            int labelAmount = (int)((currentPointAmount - startPointX) / factorAmount + minDataAmount);

            drawDottedLine(g2d, currentPointAmount, startPointY, currentPointAmount, startPointY - gridHeight);
            drawAmountLabel(g2d, String.valueOf(labelAmount), currentPointAmount, startPointY + GAP_BETWEEN_LABEL_AND_AXIS);

            currentPointAmount += CELL_WIDTH;
        }

        int currentPointTime = startPointY - CELL_HEIGHT;

        while (currentPointTime > startPointY - gridHeight) {
            int labelTime = (int)((startPointY - currentPointTime) / factorTime + minTime);

            drawDottedLine(g2d, startPointX, currentPointTime, startPointX + gridWidth, currentPointTime);
            drawTimeLabel(g2d, String.valueOf(labelTime) + TIME_LABEL_SUFFIX, startPointX - GAP_BETWEEN_LABEL_AND_AXIS, currentPointTime);

            currentPointTime -= CELL_HEIGHT;
        }

        //Рисуем график

        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(GRAPH_COLOR);

        int prevPointX = startPointX;
        int prevPointY = startPointY;

        for (TestResult result : results) {

            int currentAmountData = result.getDataAmount();
            long currentTestTime = result.getTime();

            int currentPointX = startPointX + (int)(factorAmount * (currentAmountData - minDataAmount));
            int currentPointY = startPointY - (int)Math.round(factorTime * (currentTestTime - minTime));

            g2d.drawLine(prevPointX, prevPointY, currentPointX, currentPointY);

            prevPointX = currentPointX;
            prevPointY = currentPointY;
        }

        //Рисуем оси

        g2d.setColor(AXIS_COLOR);

        //Рисуем ось Amount
        //сама ось
        g2d.drawLine(startPointX - 4, startPointY , axisAmountX, axisAmountY);
        //стартовое значение
        drawAmountLabel(g2d, String.valueOf(minDataAmount), startPointX, startPointY + GAP_BETWEEN_LABEL_AND_AXIS);
        //стрелка
        g2d.fillPolygon(new int[]{axisAmountX + 4, axisAmountX - 4, axisAmountX - 4},
                new int[]{startPointY, startPointY - 4, startPointY + 4}, 3);

        //Рисуем ось Time
        //сама ось
        g2d.drawLine(startPointX, startPointY + 4, axisTimeX, axisTimeY);
        //стартовое значение
        drawTimeLabel(g2d, String.valueOf(minTime) + TIME_LABEL_SUFFIX, startPointX - GAP_BETWEEN_LABEL_AND_AXIS, startPointY);
        //стрелка
        g2d.fillPolygon(new int[]{startPointX, startPointX - 5, startPointX + 4},
                new int[]{axisTimeY - 4, axisTimeY + 4, axisTimeY + 4}, 3);

        g2d.setStroke(oldStroke);
    }

    private void drawTimeLabel(Graphics2D g2d, String label, int posX, int posY) {
        FontMetrics metrics = g2d.getFontMetrics();
        int halfTextHeight = (metrics.getAscent() - metrics.getDescent()) / 2;

        g2d.drawString(label, posX - metrics.stringWidth(label), posY + halfTextHeight);
    }

    private void drawAmountLabel(Graphics2D g2d, String label, int posX, int posY) {
        FontMetrics metrics = g2d.getFontMetrics();
        int halfTextHeight = (metrics.getAscent() - metrics.getDescent()) / 2;

        Font oldFont = g2d.getFont();
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(90), 0, 0);
        g2d.setFont(g2d.getFont().deriveFont(affineTransform));

        g2d.drawString(label, posX - halfTextHeight, posY);

        g2d.setFont(oldFont);
    }

    private void drawDottedLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        Color oldColor = g2d.getColor();
        g2d.setColor(GRID_COLOR);
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, new float[]{2.0f}, 0.0f));
        g2d.drawLine(x1, y1, x2, y2);
        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);
    }

    private void clear(Graphics2D g2d) {
        g2d.setColor(BACKGROUND);
        g2d.fillRect(CANVAS_BORDER, CANVAS_BORDER, getWidth() - 2 * CANVAS_BORDER, getHeight() - 2 * CANVAS_BORDER);
    }

    private void printCaption(Graphics2D g2d) {
        String caption = String.format(CAPTION_TEMPLATE, algorithm.getName(), results.size());

        g2d.setColor(CAPTION_COLOR);
        g2d.setFont(g2d.getFont().deriveFont(12.0f));

        FontMetrics metrics = g2d.getFontMetrics();
        int textHeight = metrics.getHeight();
        int textWidth = metrics.stringWidth(caption);

        g2d.drawString(caption, (getWidth() - textWidth) / 2, CANVAS_BORDER + (CAPTION_HEIGHT + textHeight) / 2);
    }
}
