import java.io.*;
import java.util.*;

import static java.lang.Math.*;

public class Closest {

    static class Point implements Comparable<Point> {
        long x, y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point o) {
            return o.y == y ? Long.signum(x - o.x) : Long.signum(y - o.y);
        }
    }

    static double minimalDistance(int[] x, int y[]) {
        double ans = Double.POSITIVE_INFINITY;
        //write your code here
        return ans;
    }

    static void runSolution() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new PrintWriter(System.out);
        int n = nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = nextInt();
            y[i] = nextInt();
        }
        System.out.println(minimalDistance(x, y));
        writer.close();
    }

    static void testSolution() {
        runTest(new int[] { 0, 3 }, new int[] { 0, 4 }, 5d);
        runTest(new int[] { 7, 1, 4, 7 }, new int[] { 7, 100, 8, 7 }, 0d);
        runTest(new int[] { 4, -2, -3, -1, 2, -4, 1, -1, 3, -4, -2 }, new int[] { 4, -2, -4, 3, 3, 0, 1, -1, -1, 2, 4 }, 1.414213d);
    }
    
    static void runTest(int[] x, int y[], double expected) {
        double actual = minimalDistance(x, y);
        if (actual - 1E-5 > expected || actual + 1E-5 < expected) 
            System.out.println("wrong minimum distance, expected: " + expected + ", but got: " + actual);
    }

    public static void main(String[] args) {
        runSolution();
    }

    static BufferedReader reader;
    static PrintWriter writer;
    static StringTokenizer tok = new StringTokenizer("");


    static String next() {
        while (!tok.hasMoreTokens()) {
            String w = null;
            try {
                w = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (w == null)
                return null;
            tok = new StringTokenizer(w);
        }
        return tok.nextToken();
    }

    static int nextInt() {
        return Integer.parseInt(next());
    }
}
