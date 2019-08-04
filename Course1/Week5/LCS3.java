import java.util.*;

public class LCS3 {
    static int lcs3(int[] a, int[] b, int[] c) {
        // Write your code here
        return Math.min(Math.min(a.length, b.length), c.length);
    }

    public static void main(String[] args) {
        testSolution();
        // runSolution();
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
