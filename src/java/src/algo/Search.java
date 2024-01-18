package src.algo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Search {
    private static final InputStreamReader INPUT_READER = new InputStreamReader(System.in);
    private static final BufferedReader BUFFERED_READER = new BufferedReader(INPUT_READER);

    public static void main(String[] args) throws IOException {
        int count = 1000;
        int bound = 1003;
        Integer[] integers1 = createRandomIntegerArray(count, 0, bound, 0L);
        System.out.println(Arrays.toString(integers1));
        System.out.print("Input a number to find index: ");
        int item = Integer.parseInt(BUFFERED_READER.readLine());
        SearchReport report = linearSearch(integers1, item);
        System.out.printf("Found %d at %d in %d times.\n", item, report.index, report.searchCount);
        Integer[] integers2 = createSortedRandomIntegerArray(count, 0, bound, 0L);
        System.out.println(Arrays.toString(integers2));
        System.out.print("Input a number to find index: ");
        int item2 = Integer.parseInt(BUFFERED_READER.readLine());
        SearchReport report2 = binarySearch(integers2, item2);
        System.out.printf("Found %d at %d in %d times.\n", item, report2.index, report2.searchCount);
    }

    public static Integer[] createRandomIntegerArray(int count, int startInclusive, int endExclusive, long seed) {
        Random random = new Random(seed);
        return random
            .ints(count, startInclusive, endExclusive)
            .boxed()
            .toArray(Integer[]::new);
    }

    public static Integer[] createSortedRandomIntegerArray(int count, int startInclusive, int endExclusive, long seed) {
        Random random = new Random(seed);
        return random
            .ints(count, startInclusive, endExclusive)
            .sorted()
            .boxed()
            .toArray(Integer[]::new);
    }

    public static <T extends Comparable<T>> SearchReport linearSearch(T[] array, T item) {
        return linearSearch(array, item, Comparator.naturalOrder());
    }

    public static <T> SearchReport linearSearch(T[] array, T item, Comparator<? super T> comparator) {
        int length = array.length;
        if (length == 0) {
            return new SearchReport(0, -1);
        }
        int count = 0;
        for (int i = 0; i < length; i++) {
            T target = array[i];
            if (target == item || comparator.compare(target, item) == 0) {
                return new SearchReport(count, i);
            }
            count++;
        }
        return new SearchReport(count, -1);
    }

    public static <T extends Comparable<T>> SearchReport binarySearch(T[] array, T item) {
        return binarySearch(array, item, Comparator.naturalOrder());
    }

    public static <T> SearchReport binarySearch(T[] array, T item, Comparator<? super T> comparator) {
        int length = array.length;
        if (length == 0) {
            return new SearchReport(0, -1);
        }
        int count = 0;
        int upperBound = length;
        int lowerBound = 0;
        while (lowerBound != upperBound) {
            int pivot = (upperBound + lowerBound) / 2;
            T target = array[pivot];
            int comparison;
            if (target == item || (comparison = comparator.compare(item, target)) == 0) {
                return new SearchReport(count, pivot);
            }
            if (comparison < 0) {
                upperBound = pivot;
            } else {
                lowerBound = pivot + 1;
            }
            count++;
        }
        return new SearchReport(count, -1);
    }

    public static class SearchReport {
        final int searchCount;
        final int index;

        SearchReport(int searchCount, int index) {
            this.searchCount = searchCount;
            this.index = index;
        }
    }
}
