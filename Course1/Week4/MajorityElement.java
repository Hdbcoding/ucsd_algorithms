import java.util.*;
import java.io.*;

public class MajorityElement {
    static int getMajorityElement(int[] a) {
        // find an element that passes the majority smell test
        int index = 0, count = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[index] == a[i])
                count++;
            else
                count--;
            if (count == 0) {
                index = i;
                count = 1;
            }
        }
        // check that this element actually is the majority
        int majorityElement = a[index];
        int majoritySize = a.length / 2;
        count = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == majorityElement && ++count > majoritySize )
                return majorityElement;
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
        if (getMajorityElement(a) != -1) {
            System.out.println(1);
        } else {
            System.out.println(0);
        }
    }
    
    static void testSolution() {
        runTest(new int[] { 2, 3, 9, 2, 2 }, 2);
        runTest(new int[] { 1, 2, 3, 4 }, -1);
        runTest(new int[] { 1, 2, 3, 1 }, -1);
    }
    
    static void runTest(int[] a, int expected) {
        int actual = getMajorityElement(a);
        if (actual != expected)
            System.out.println("Expected " + expected + ", but result was " + actual);
    }

    public static void main(String[] args) {
        testSolution();
        //runSolution();
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

