package ml;

import java.util.Arrays;

/**
 * 1) Brute Force Approach - Back tracking
 * 2) Greedy Approach - Fast, suboptimal
 * 3) Evolutionary Algorithm
 */

public class TheImpossibleProblem {

    public static void main(String args[]) {
        final InputReader reader = new InputReader(System.in);
        final int n = reader.readInt(), a[] = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = reader.readInt();
        }
        System.out.println(Arrays.deepToString(bruteForce(a)));
    }

    private static int[][] bruteForce(int a[]) {
        //generate all possible binary numbers between 1 and 2^20
        //if the ith bit in the binary number is 1: add element to group 2
        //if the ith bit in the binary number is 0: add element to group 1
        //find the difference in sums
        //print the best possible combination
        return null;
    }
}


