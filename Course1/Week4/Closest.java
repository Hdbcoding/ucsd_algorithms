import java.io.*;
import java.util.*;

import static java.lang.Math.*;

public class Closest {

    static class Point {
        long x, y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    static Comparator<Point> orderByX = new Comparator<Point>() {
        public int compare(Point p1, Point p2) {
            return Long.signum(p1.x - p2.x);
        }
    };

    static Comparator<Point> orderByY = new Comparator<Point>() {
        public int compare(Point p1, Point p2) {
            return Long.signum(p1.y - p2.y);
        }
    };

    static double minimalDistanceNaive(int[] x, int[] y) {
        Point[] points = getPoints(x, y);
        return bruteForce(points, 0, points.length);
    }

    static double minimalDistance(int[] x, int y[]) {
        Point[] points = getPoints(x, y);
        if (points.length <= 3)
            return bruteForce(points, 0, points.length);
        Arrays.sort(points, orderByX);
        return minimalDistanceRecursive(points, 0, points.length);
    }

    private static Point[] getPoints(int[] x, int[] y) {
        Point[] points = new Point[x.length];
        for (int i = 0; i < x.length; i++)
            points[i] = new Point(x[i], y[i]);
        return points;
    }

    static double minimalDistanceRecursive(Point[] points, int l, int r) {
        if (r - l <= 3)
            return bruteForce(points, l, r);
        int m = (l + r) / 2;
        double dl = minimalDistanceRecursive(points, l, m);
        double dr = minimalDistanceRecursive(points, m, r);
        return minimalSplitPoint(points, l, r, m, Math.min(dl, dr));
    }

    private static double bruteForce(Point[] points, int l, int r) {
        double d = Double.POSITIVE_INFINITY;
        for (int i = l; i < r - 1; i++) {
            for (int j = i + 1; j < r; j++) {
                d = Math.min(d, distance(points[i], points[j]));
            }
        }
        return d;
    }

    private static double distance(Point point, Point point2) {
        double dx = point.x - point2.x;
        double dy = point.y - point2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static double minimalSplitPoint(Point[] points, int l, int r, int m, double d) {
        ArrayList<Point> centerPoints = getCenterPoints(points, l, r, m, d);
        double min = d;
        for (int i = 0; i < centerPoints.size(); i++)
            for (int j = i + 1; j < Math.min(i + 7, centerPoints.size()); j++)
                min = Math.min(min, distance(centerPoints.get(i), centerPoints.get(j)));

        return min;
    }

    static ArrayList<Point> getCenterPoints(Point[] points, int l, int r, int m, double d) {
        ArrayList<Point> centerPoints = new ArrayList<Point>(r - l);
        Point middle = points[m];
        for (int i = l; i < r; i++) {
            Point pi = points[i];
            if (pi.x > middle.x - d && pi.x < middle.x + d)
                centerPoints.add(pi);
        }
        centerPoints.sort(orderByY);
        return centerPoints;
    }

    static void testSolution() {
        runTest(new int[] { 0, 3 }, new int[] { 0, 4 }, 5d);
        runTest(new int[] { 7, 1, 4, 7 }, new int[] { 7, 100, 8, 7 }, 0d);
        runTest(new int[] { 4, -2, -3, -1, 2, -4, 1, -1, 3, -4, -2 }, new int[] { 4, -2, -4, 3, 3, 0, 1, -1, -1, 2, 4 },
                1.414213d);

        for (int i = 0; i < 50000; i++) {
            runStressTest(5000, 10000000);
        }
    }

    static void runStressTest(int n, int max) {
        int[] x = createCoordArray(n, max);
        int[] y = createCoordArray(n, max);
        double brute = minimalDistanceNaive(x, y);
        double fast = minimalDistance(x, y);
        System.out.println("checking stress test, brute: " + brute + ", fast: " + fast);
        if (fast - 1E-5 > brute || fast + 1E-5 < brute) {
            System.out.println("x: " + Arrays.toString(x));
            System.out.println("y: " + Arrays.toString(y));
        }
    }

    private static int[] createCoordArray(int n, int max) {
        int[] coords = new int[n];
        Random r = new Random();
        for (int i = 0; i < n; i++)
            coords[i] = r.nextInt(max);
        return coords;
    }

    static void runTest(int[] x, int y[], double expected) {
        double actual = minimalDistance(x, y);
        if (actual - 1E-5 > expected || actual + 1E-5 < expected)
            System.out.println("wrong minimum distance, expected: " + expected + ", but got: " + actual);
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
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
