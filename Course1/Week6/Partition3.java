import java.util.*;

public class Partition3 {
    private static int partition3(int[] A) {
        int total = sum(A);
        if (total % 3 != 0)
            return 0;
        int goal = total / 3;
        Arrays.sort(A);
        if (A[A.length - 1] > goal)
            return 0;

        boolean[] results = new boolean[1 << A.length];
        int[] totals = new int[1 << A.length];
        results[0] = true;

        // check each possible combination of elements
        for (int past = 0; past < (1 << A.length); past++) {
            if (!results[past])
                continue;
            int pastTotal = totals[past];
            int remaining = goal - totals[past] % goal;
            // for each combination, consider adding the ith element
            for (int i = 0; i < A.length; i++) {
                int future = past | (1 << i);
                // if the ith element isn't already evaluated
                if (past != future && !results[future]) {
                    int num = A[i];
                    // check if the ith element could lead to a valid sum
                    if (num <= remaining) {
                        results[future] = true;
                        totals[future] = pastTotal + num;
                    } else
                        break;
                }
            }
        }

        // write your code here
        return results[(1 << A.length) - 1] ? 1 : 0;
    }

    private static int sum(int[] a) {
        int result = 0;
        for (int i = 0; i < a.length; i++)
            result += a[i];
        return result;
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }
        scanner.close();
        System.out.println(partition3(A));
    }
    
    static void testSolution() {
        runTest(new int[] { 3, 3, 3, 3 }, 0);
        runTest(new int[] { 3, 3, 3 }, 1);
        runTest(new int[] { 17, 59, 34, 57, 17, 23, 67, 1, 18, 2, 59 }, 1);
        runTest(new int[] { 17, 59, 34, 57, 17, 23, 67, 1, 18, 2, 59, 1 }, 0);
        runTest(new int[] { 1, 2, 3, 4, 5, 5, 7, 7, 8, 10, 12, 19, 25 }, 1);
        runTest(new int[] { 7, 3, 2, 1, 5, 4, 8 }, 1);
    }
    
    static void runTest(int[] A, int expected) {
        int actual = partition3(A);
        if (expected != actual) {
            System.out.println(
                    "Incorrect result for " + Arrays.toString(A) + ", expected: " + expected + ", but got: " + actual);
        }
    }
}
