package ml;

import java.util.Arrays;

public class CorrelationFinder {
    public double findCorrelation(int attribute[], int result[]) {
        int[] sortedAttributes = getSortedCopy(attribute);
        double[] attRanks = getRanks(sortedAttributes);
        System.out.println(Arrays.toString(attRanks));
        int[] sortedResults = getSortedCopy(result);
        double[] resultRanks = getRanks(sortedResults);
        long sumOfSquareDistances = 0;
        for (int i = 0; i < attribute.length; i++) {
            sumOfSquareDistances += square(attRanks[Arrays.binarySearch(sortedAttributes, attribute[i])]
                    - resultRanks[Arrays.binarySearch(sortedResults, result[i])]);
        }
        return 1 - (6d * sumOfSquareDistances) / (attribute.length * (square(attribute.length) - 1));
    }

    public double findCorrelation(double attribute[], int result[]) {
        double[] sortedAttributes = getSortedCopy(attribute);
        double[] attRanks = getRanks(sortedAttributes);
        System.out.println(Arrays.toString(attRanks));
        int[] sortedResults = getSortedCopy(result);
        double[] resultRanks = getRanks(sortedResults);
        long sumOfSquareDistances = 0;
        for (int i = 0; i < attribute.length; i++) {
            sumOfSquareDistances += square(attRanks[Arrays.binarySearch(sortedAttributes, attribute[i])]
                    - resultRanks[Arrays.binarySearch(sortedResults, result[i])]);
        }
        return 1 - (6d * sumOfSquareDistances) / (attribute.length * (square(attribute.length) - 1));
    }

    private int[] getSortedCopy(int[] attribute) {
        int copy[] = new int[attribute.length];
        System.arraycopy(attribute, 0, copy, 0, attribute.length);
        Arrays.sort(copy);
        return copy;
    }

    private double[] getSortedCopy(double[] attribute) {
        double copy[] = new double[attribute.length];
        System.arraycopy(attribute, 0, copy, 0, attribute.length);
        Arrays.sort(copy);
        return copy;
    }

    private double[] getRanks(double[] copy) {
        int count = 1;
        int rankSum = 1;
        int start = 0;
        double ranks[] = new double[copy.length];
        for (int i = 1; i < copy.length; i++) {
            if (copy[i] == copy[i - 1]) {
                count++;
                rankSum = rankSum + i;
            } else {
                for (int j = start; j < i; j++) {
                    ranks[j] = rankSum / (double) count;
                }
                rankSum = i;
                count = 1;
                start = i;
            }
        }
        if (count > 0) {
            ranks[start] = rankSum / (double) count;
        }
        return ranks;
    }

    private double[] getRanks(int[] copy) {
        int count = 1;
        int rankSum = 1;
        int start = 0;
        double ranks[] = new double[copy.length];
        for (int i = 1; i < copy.length; i++) {
            if (copy[i] == copy[i - 1]) {
                count++;
                rankSum = rankSum + i;
            } else {
                for (int j = start; j < i; j++) {
                    ranks[j] = rankSum / (double) count;
                }
                rankSum = i;
                count = 1;
                start = i;
            }
        }
        if (count > 0) {
            ranks[start] = rankSum / (double) count;
        }
        return ranks;
    }

    private double square(double i) {
        return i * i;
    }
}
