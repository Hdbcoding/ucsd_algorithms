import java.io.*;
import java.util.*;

public class BinarySearch {

    static int binarySearch(int[] a, int x) {
        int left = 0, right = a.length - 1, middle, value;
        while (left <= right) {
            middle = (left + right) / 2;
            value = a[middle];
            if (value == x) return middle;
            else if (value < x) left = middle + 1;
            else right = middle - 1;
        }
        return -1;
    }

    static int linearSearch(int[] a, int x) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == x)
                return i;
        }
        return -1;
    }
    
    static void runSolution() {
        FastScanner scanner = new FastScanner(System.in);
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
        for (int i = 0; i < m; i++) {
            //replace with the call to binarySearch when implemented
            System.out.print(binarySearch(a, b[i]) + " ");
        }
    }
    
    static void testSolution() {
        runTest(new int[] { 1, 5, 8, 12, 13 }, 8, 2);
        runTest(new int[] { 1, 5, 8, 12, 13 }, 1, 0);
        runTest(new int[] { 1, 5, 8, 12, 13 }, 23, -1);
        runTest(new int[] { 1, 5, 8, 12, 13 }, 11, -1);
        runTest(new int[] { 1, 5, 8, 12, 13 }, 6, -1);
        runTest(new int[] { 1, 5, 8, 12, 13 }, 5, 1);
        runTest(new int[] { 1, 5, 8, 12, 13 }, 13, 4);
    }
    
    static void runTest(int[] a, int x, int expected) {
        int actual = binarySearch(a, x);
        if (actual != expected)
            System.out.println("Expected " + expected + ", but result was " + actual);
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
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
