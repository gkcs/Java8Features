package ml;

/**
 * 1) Brute force Approach
 * 2) Greedy Approach
 * 3) Evolutionary Algorithm Approach
 */

public class TheImpossibleProblem {

    public static void main(String args[]) {
        final InputReader reader = new InputReader(System.in);
        final int n = reader.readInt(), a[] = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = reader.readInt();
        }
        System.out.println();
    }

    private static int[][] bruteForce(int a[]) {
        //generate all binary numbers between 1 and 2^n --> zeros and ones
        //if the ith bit in the binary number is one-> the ith element belongs to group 2
        //if the ith bit in the binary number is zero-> the ith element belongs to group 1
        //find the difference in the group sums
        //take the result such that difference is minimum
        return new int[a.length][2];
    }

    //Partition problem

    //difference the two groups
    //individual --> random binary
    private static int[][] geneticAlgorithm(int a[]){
        //Higher fitness --> Higher probabilty to mate--> Higher chance of offspring
        //Some common attributes from both parents
        //replace the parents with the children
        return null;
    }
}