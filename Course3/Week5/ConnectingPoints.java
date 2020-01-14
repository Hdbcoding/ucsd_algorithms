import java.util.Arrays;
import java.util.Scanner;

public class ConnectingPoints {
    public static void main(String[] args) {
        // runSolution();
        testSolution();
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Graph g = new Graph(n);
        for (int i = 0; i < n; i++) {
            g.setPoint(i, scanner.nextInt(), scanner.nextInt());
        }
        scanner.close();
        System.out.println(minimumDistance(g));
    }

    static void testSolution() {
        runTest(new int[] { 4, 0, 0, 0, 1, 1, 0, 1, 1 }, 3);
        runTest(new int[] { 5, 0, 0, 0, 2, 1, 1, 3, 0, 3, 2 }, 7.064495102);
    }

    static void runTest(int[] data, double expected) {
        Graph g = parseGraph(data);
        double actual = minimumDistance(g);
        if (!closeEnough(expected, actual))
            System.out.println("Unexpected result for x: " + Arrays.toString(g.x) + ", y: " + Arrays.toString(g.y)
                    + ", expected: " + expected + ", but got: " + actual);
    }

    private static Graph parseGraph(int[] data) {
        Graph g = new Graph(data[0]);
        for (int i = 0; i < (data.length - 1) / 2; i++) {
            int x = data[i * 2 + 1];
            int y = data[i * 2 + 2];
            g.setPoint(i, x, y);
        }
        return g;
    }

    private static boolean closeEnough(double expected, double actual) {
        double epsilon = 1e-5;
        return (actual > expected - epsilon) && (actual < expected + epsilon);
    }

    static double minimumDistance(Graph g) {
        double result = 0.;
        // write your code here
        return result;
    }

    static class Graph {
        int[] x;
        int[] y;
        int s;

        Graph(int s) {
            this.s = s;
            this.x = new int[s];
            this.y = new int[s];
        }

        public void setPoint(int i, int x, int y) {
            this.x[i] = x;
            this.y[i] = y;
        }
    }
}
