import java.util.*;

public class LCS3 {
    static int lcs3(int[] a, int[] b, int[] c) {
        int[][][] distances = new int[a.length + 1][b.length + 1][c.length + 1];
        for (int i = 1; i <= a.length; i++) {
            int ai = a[i - 1];
            for (int j = 1; j <= b.length; j++) {
                int bj = b[j - 1];
                for (int k = 1; k <= c.length; k++) {
                    int ck = c[k - 1];
                    boolean allEqual = ai == bj && bj == ck;
                    int[] previousValues = getAllPrevious(distances, i, j, k, allEqual);
                    distances[i][j][k] = maxValue(previousValues);
                }
            }
        }
        return distances[a.length][b.length][c.length];
    }

    private static int[] getAllPrevious(int[][][] distances, int i, int j, int k, boolean allEqual) {
        return new int[] {
                distances[i - 1][j - 1][k - 1] + (allEqual ? 1 : 0),
                distances[i - 1][j - 1][k],
                distances[i - 1][j]    [k - 1],
                distances[i]    [j - 1][k - 1],
                distances[i]    [j]    [k - 1],
                distances[i]    [j - 1][k],
                distances[i - 1][j]    [k]
        };
    }

    private static int maxValue(int[] previousValues) {
        int max = 0;
        for (int i : previousValues)
            max = Math.max(i, max);
        return max;
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }

    static void testSolution() {
        runTest(new int[] { 1, 2, 3 }, new int[] { 2, 1, 3 }, new int[] { 1, 3, 5 }, 2);
        runTest(new int[] { 8, 3, 2, 1, 7 }, new int[] { 8, 2, 1, 3, 8, 10, 7 }, new int[] { 6, 8, 3, 1, 4, 7 }, 3);

    }

    static void runTest(int[] a, int[] b, int[] c, int expected) {
        int actual = lcs3(a, b, c);
        if (actual != expected)
            System.out.println(
                    "Wrong length of longest subsequence between " + Arrays.toString(a) + ", " + Arrays.toString(b)
                            + ", and " + Arrays.toString(c) + ". Expected " + expected + ", but got " + actual);
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int an = scanner.nextInt();
        int[] a = new int[an];
        for (int i = 0; i < an; i++) {
            a[i] = scanner.nextInt();
        }
        int bn = scanner.nextInt();
        int[] b = new int[bn];
        for (int i = 0; i < bn; i++) {
            b[i] = scanner.nextInt();
        }
        int cn = scanner.nextInt();
        int[] c = new int[cn];
        for (int i = 0; i < cn; i++) {
            c[i] = scanner.nextInt();
        }
        scanner.close();
        System.out.println(lcs3(a, b, c));
    }
}
