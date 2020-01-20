import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Clustering {
    public static void main(String[] args) {
        runSolution();
        // testSolution();
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Graph g = new Graph(n);
        for (int i = 0; i < n; i++) {
            g.setPoint(i, scanner.nextInt(), scanner.nextInt());
        }
        int k = scanner.nextInt();
        scanner.close();
        System.out.println(clustering(g, k));
    }

    static void testSolution() {
        runTest(new int[] { 12, 7, 6, 4, 3, 5, 1, 1, 7, 2, 7, 5, 7, 3, 3, 7, 8, 2, 8, 4, 4, 6, 7, 2, 6, 3 },
                2.828427124746);
        runTest(new int[] { 8, 3, 1, 1, 2, 4, 6, 9, 8, 9, 9, 8, 9, 3, 11, 4, 12, 4 }, 5);
    }

    static void runTest(int[] data, double expected) {
        Graph g = parseGraph(data);
        int k = data[data.length - 1];
        double actual = clustering(g, k);
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

    private static double clustering(Graph g, int k) {
        DisjointSet set = new DisjointSet(g.s);
        ArrayList<Edge> edges = CalculateAllEdges(g);
        int numClusters = g.s;
        double minDistance = -1.;

        for (Edge e : edges){
            if (set.find(e.i) != set.find(e.j)){
                if (numClusters == k){
                    minDistance = e.cost;
                    break;
                }
                set.merge(e.i, e.j);
                numClusters--;
            }
        }
        return minDistance;
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

        double distance(int i, int j) {
            int dx = x[i] - x[j];
            int dy = y[i] - y[j];
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    static ArrayList<Edge> CalculateAllEdges(Graph g) {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (int i = 0; i < g.s; i++) {
            for (int j = i + 1; j < g.s; j++) {
                Edge e = new Edge();
                e.i = i;
                e.j = j;
                e.cost = g.distance(i, j);
                edges.add(e);
            }
        }
        Collections.sort(edges);
        return edges;
    }

    static class DisjointSet {
        int[] parent;
        int[] rank;

        DisjointSet(int s) {
            parent = new int[s];
            for (int i = 0; i < s; i++)
                parent[i] = i;
            rank = new int[s];
        }

        int find(int i) {
            if (parent[i] != i)
                parent[i] = find(parent[i]);
            return parent[i];
        }

        void merge(int i, int j) {
            i = find(i);
            j = find(j);
            if (i == j)
                ;

            if (rank[i] > rank[j])
                parent[j] = i;
            else {
                parent[i] = j;
                if (rank[i] == rank[j])
                    rank[j]++;
            }
        }
    }

    static class Edge implements Comparable<Edge> {
        int i;
        int j;
        double cost;

        @Override
        public int compareTo(Edge o) {
            if (cost < o.cost) return -1;
            if (cost > o.cost) return 1;
            return 0;
        }
    }
}
