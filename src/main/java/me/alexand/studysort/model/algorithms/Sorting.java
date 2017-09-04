package me.alexand.studysort.model.algorithms;

/**
 * Реализация алгоритмов сортировки. Каждая реализация алгоритма в качестве аргумента принимает массив
 * чисел, подлежащий сортировке. Результатом работы алгоритма является тот же массив, но упорядоченный
 * по возрастанию.
 *
 * Created by Sidorov Alexander on 04.01.2017.
 * e-mail: asidorov84@gmail.com
 */
public class Sorting {

    public static void bubble(int[] sourceData) {
        boolean isExchanged;

        for (int i = 0; i < sourceData.length; i++) {
            isExchanged = false;

            for (int j = 0; j < sourceData.length - i - 1; j++) {
                if (sourceData[j] > sourceData[j + 1]) {
                    swap(sourceData, j, j + 1);
                    isExchanged = true;
                }
            }

            if (!isExchanged) {
                break;
            }
        }
    }

    public static void insert(int[] sourceData) {
        for (int i = 1; i < sourceData.length; i++) {
            int j = i;

            while ( (j > 0) && (sourceData[j] < sourceData[j - 1]) ) {
                swap(sourceData, j, j - 1);
                j--;
            }
        }
    }

    public static void merge(int[] sourceData) {
        //Если массив пустой или содержит только один элемент, считаем что массив уже упорядочен
        if (sourceData.length <= 1) {
            return;
        }

        //Делим исходный массив пополам в два подмассива

        //Первая половина исходного массива
        int[] a = new int[sourceData.length / 2];
        System.arraycopy(sourceData, 0, a, 0, a.length);

        //Вторая половина исходного массива
        int[] b = new int[sourceData.length - a.length];
        System.arraycopy(sourceData,a.length, b, 0, b.length);

        //Каждый подмассив сортируем рекурсивно тем же алгоритмом
        merge(a);
        merge(b);

        //Объединяем подмассивы в исходный массив
        System.arraycopy(mergeSortArrays(a, b), 0, sourceData, 0, sourceData.length);
    }

    public static void shell(int[] sourceData) {
        int step = sourceData.length / 2;

        while (step > 0) {
            for (int i = step; i < sourceData.length; i++) {
                for (int j = i; j >= step; j = j - step) {
                    if (sourceData[j] < sourceData[j - step]) {
                        swap(sourceData, j, j - step);
                    } else {
                        break;
                    }
                }
            }
            step /= 2;
        }
    }

    /**
     * Объединение двух одинаково упорядоченных массивов
     * @param first - первый массив
     * @param second - второй массив
     * @return - упорядоченный массив из всех элементов массивов first и second
     */
    private static int[] mergeSortArrays(int[] first, int[] second) {
        int[] result = new int[first.length + second.length];

        int i = 0;
        int j = 0;
        int k = 0;

        while (i < first.length && j < second.length) {
            if (first[i] <= second[j]) {
                result[k++] = first[i++];
            } else {
                result[k++] = second[j++];
            }
        }

        if (i == first.length) {
            for (int l = j; l < second.length; l++) {
                result[k++] = second[l];
            }
        }

        if (j == second.length) {
            for (int l = i; l < first.length; l++) {
                result[k++] = first[l];
            }
        }

        return result;
    }

    private static void swap(int[] sourceData, int first, int second) {
        if (first == second) {
            return;
        }

        int tmp = sourceData[first];
        sourceData[first] = sourceData[second];
        sourceData[second] = tmp;
    }

}
