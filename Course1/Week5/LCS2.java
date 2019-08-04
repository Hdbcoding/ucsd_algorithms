import java.util.*;

public class LCS2 {
    static int lcs2(int[] a, int[] b) {
        int[][] distances = new int[a.length + 1][b.length + 1];

        for (int i = 1; i <= a.length; i++) {
            int ai = a[i - 1];
            for (int j = 1; j <= b.length; j++) {
                int bj = b[j - 1];
                int i1j1 = distances[i - 1][j - 1] + (ai == bj ? 1 : 0);
                int i1j0 = distances[i - 1][j];
                int i0j1 = distances[i][j - 1];
                distances[i][j] = Math.max(i1j1, Math.max(i1j0, i0j1));
            }
        }

        return distances[a.length][b.length];
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }

    static void testSolution() {
        runTest(new int[] { 2, 7, 5 }, new int[] { 2, 5 }, 2);
        runTest(new int[] { 7 }, new int[] { 1, 2, 3, 4 }, 0);
        runTest(new int[] { 2, 7, 8, 3 }, new int[] { 5, 2, 8, 7 }, 2);
        runTest(new int[] { 2, 3, 1, 2, 3 }, new int[] { 1, 2, 3 }, 3);
        runTest(new int[] { 2, 2, 2, 3 }, new int[] { 2, 3 }, 2);
        runTest(new int[] { 2, 7, 7, 7, 5 }, new int[] { 2, 7, 7, 5 }, 4);
        runTest(new int[] { 3, 3, 1 }, new int[] { 1, 3, 3 }, 2);
        runTest(new int[] { 2, 7, 5, 2 }, new int[] { 2, 5 }, 2);
    }

    static void runTest(int[] a, int[] b, int expected) {
        int actual = lcs2(a, b);
        if (actual != expected)
            System.out.println("Wrong length of longest subsequence between " + Arrays.toString(a) + ", and "
                    + Arrays.toString(b) + ". Expected " + expected + ", but got " + actual);
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int m = scanner.nextInt();
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = scanner.nextInt();
        }
        scanner.close();

        System.out.println(lcs2(a, b));
    }
}
