import java.util.Arrays;
import java.util.Scanner;

public class PointsAndSegments {
    static class Point implements Comparable<Point>{
        int index;
        int value;

        Point(int i, int v) {
            index = i;
            value = v;
        }

        @Override
        public int compareTo(Point o) {
            return value - o.value;
        }
    }

    private static int[] fastCountSegments(int[] starts, int[] ends, int[] points) {
        int[] cnt = new int[points.length];
        Point[] pointObjs = new Point[points.length];
        for (int i = 0; i < points.length; i++)
            pointObjs[i] = new Point(i, points[i]);
        

        Arrays.sort(starts);
        Arrays.sort(ends);
        //note: need to keep original index
        Arrays.sort(pointObjs);
        int si = 0, ei = 0, pi = 0;
        Point point = null;
        for (int i = 0; i < points.length; i++) {
            point = pointObjs[i];
            while (si < starts.length && starts[si] <= point.value)
                si++;
            while (ei < ends.length && ends[ei] < point.value)
                ei++;
            cnt[point.index] = Math.max(0, si - ei);
        }
        return cnt;
    }

    private static int[] naiveCountSegments(int[] starts, int[] ends, int[] points) {
        int[] cnt = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < starts.length; j++) {
                if (starts[j] <= points[i] && points[i] <= ends[j]) {
                    cnt[i]++;
                }
            }
        }
        return cnt;
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n, m;
        n = scanner.nextInt();
        m = scanner.nextInt();
        int[] starts = new int[n];
        int[] ends = new int[n];
        int[] points = new int[m];
        for (int i = 0; i < n; i++) {
            starts[i] = scanner.nextInt();
            ends[i] = scanner.nextInt();
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        scanner.close();
        // use fastCountSegments
        int[] cnt = fastCountSegments(starts, ends, points);
        for (int x : cnt) {
            System.out.print(x + " ");
        }
    }

    static void testSolution() {
        runTest(new int[] { 0, 7 }, new int[] { 5, 10 }, new int[] { 1, 6, 11 }, new int[] { 1, 0, 0 });
        runTest(new int[] { -10 }, new int[] { 10 }, new int[] { -100, 100, 0 }, new int[] { 0, 0, 1 });
        runTest(new int[] { 0, -3, 7 }, new int[] { 5, 2, 10 }, new int[] { 1, 6 }, new int[] { 2, 0 });
        runTest(new int[] { -60, 17, -25, -83, 20 }, new int[] { -19, 31, 17, 31, 97 }, new int[] { 17 },
                new int[] { 3 });
    }

    static void runTest(int[] starts, int[] ends, int[] points, int[] expected) {
        int[] counts = fastCountSegments(starts, ends, points);
        checkSequencesEqual(counts, expected, "Segment check failed");
    }

    static void checkSequencesEqual(int[] a, int[] b, String message) {
        if (!sequenceEqual(a, b)) {
            String aString = Arrays.toString(a);
            String expectedString = Arrays.toString(b);
            System.out.println(message + ", expected: " + expectedString + ", but got: " + aString);
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
        // testSolution();
        runSolution();
    }
}
