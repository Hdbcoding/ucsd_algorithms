import java.io.*;
import java.util.*;

public class Sorting {
    static Random random = new Random();

    static int[] partition3(int[] a, int l, int r) {
        int x = a[l];
        int m1 = l;
        int m2 = l;

        for (int i = l + 1; i <= r; i++) {
            if (a[i] < x) {
                m1++;
                m2++;
                swap(a, m1, i);
                swap(a, m2, i);
            } else if (a[i] == x) {
                m2++;
                swap(a, m2, i);
            }
        }
        swap(a, l, m2);
        int[] m = { m1, m2 };
        return m;
    }

    static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    static int partition2(int[] a, int l, int r) {
        int x = a[l];
        int j = l;
        for (int i = l + 1; i <= r; i++) {
            if (a[i] <= x) {
                j++;
                swap(a, i, j);
            }
        }
        swap(a, l, j);
        return j;
    }

    static void randomizedQuickSort(int[] a, int l, int r) {
        if (l >= r) {
            return;
        }
        int k = random.nextInt(r - l + 1) + l;
        swap(a, l, k);
        
        // int m = partition2(a, l, r);
        // randomizedQuickSort(a, l, m - 1);
        // randomizedQuickSort(a, m + 1, r);

        int[] m = partition3(a, l, r);
        randomizedQuickSort(a, l, m[0] - 1);
        randomizedQuickSort(a, m[1] + 1, r);
    }

    static void runSolution() {
        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        randomizedQuickSort(a, 0, n - 1);
        for (int i = 0; i < n; i++) {
            System.out.print(a[i] + " ");
        }
    }

    static void testSolution() {
        runTest(new int[] { 2, 3, 9, 2, 2 }, new int[] { 2, 2, 2, 3, 9 });
        runTest(new int[] { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 }, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        runTest(new int[] { 2, 1, 2, 3, 5, 5, 2, 7, 9 }, new int[] { 1, 2, 2, 2, 3, 5, 7, 9 });
        runTest(new int[] { 4, 2, 5, 1, 3, 4, 4, 8, 2, 3, 9 }, new int[] { 1, 2, 2, 3, 3, 4, 4, 4, 5, 8, 9 });
    }

    static void runTest(int[] a, int[] expected) {
        randomizedQuickSort(a, 0, a.length - 1);
        if (!sequenceEqual(a, expected)) {
            String aString = Arrays.toString(a);
            String expectedString = Arrays.toString(expected);
            System.out.println("Sort failed, expected: " + expectedString + ", but got: " + aString);
        }
    }

    static boolean sequenceEqual(int[] a, int[] b) {
        if (a.length != b.length)
            return false;

        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i])
                return false;

        return true;
    }

    public static void main(String[] args) {
        testSolution();
        // runSolution();
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}
