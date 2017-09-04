package me.alexand.studysort;
import java.awt.*;

/**
 * Основной класс приложения с методом main
 * Created by Sidorov Alexander on 04.01.2017.
 * e-mail: asidorov84@gmail.com
 */
public class StudySort {
    public static void main(String[] args) throws Exception {
        EventQueue.invokeLater(() -> Controller.getInstance().start());
    }
}
