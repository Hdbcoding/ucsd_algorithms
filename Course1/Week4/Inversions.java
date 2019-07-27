import java.util.*;

public class Inversions {
    static long getNumberOfInversions(int[] a, int l, int r) {
        if (r <= l + 1)
            return 0;

        int m = (l + r) / 2;
        long left = getNumberOfInversions(a, l, m);
        long right = getNumberOfInversions(a, m, r);
        long split = mergeAndCountSplit(a, l, m, r);
        return left + right + split;
    }
    
    static long mergeAndCountSplit(int[] a, int l, int m, int r) {
        long split = 0;
        int[] copyTo = new int[r - l];
        int ci = 0;
        int li = l;
        int ri = m;

        while (li < m && ri < r) {
            if (a[li] <= a[ri])
                copyTo[ci++] = a[li++];
            else {
                copyTo[ci++] = a[ri++];
                split += (m - li);
            }
        }
        
        while (li < m) {
            copyTo[ci++] = a[li++];
        }

        while (ri < r)
            copyTo[ci++] = a[ri++];

        for (int i = l; i < r; i++)
            a[i] = copyTo[i - l];

        return split;
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        scanner.close();
        System.out.println(getNumberOfInversions(a, 0, a.length));
    }

    static void testSolution() {
        runTest(new int[] { 2, 3, 9, 2, 9 }, 2);
        runTest(new int[] { 5, 4, 3, 2, 1 }, 10);
        runTest(new int[] { 6, 1, 5, 2, 3 }, 6);
        runTest(new int[] { 1, 3, 5, 2, 4, 6 }, 3);
        runTest(new int[] { 6, 5, 4, 3, 2, 1 }, 15);
        runTest(new int[] { 7, 6, 5, 4, 3, 2, 1 }, 21);
        runTest(new int[] { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, 45);
    }
    
    static void runTest(int[] a, long expected) {
        long actual = getNumberOfInversions(a, 0, a.length);
        if (actual != expected)
            System.out.println("Expected: " + expected + ", but got: " + actual);
        
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }
}

