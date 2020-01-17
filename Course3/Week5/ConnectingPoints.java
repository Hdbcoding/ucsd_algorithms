import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
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
        System.out.println(prim(g));
    }

    static void testSolution() {
        runTest(new int[] { 4, 0, 0, 0, 1, 1, 0, 1, 1 }, 3);
        runTest(new int[] { 5, 0, 0, 0, 2, 1, 1, 3, 0, 3, 2 }, 7.064495102);
    }

    static void runTest(int[] data, double expected) {
        Graph g = parseGraph(data);
        double aPrim = prim(g);
        double aKruskal = kruskal(g);
        if (!closeEnough(expected, aPrim) || !closeEnough(expected, aKruskal))
            System.out.println("Unexpected result for x: " + Arrays.toString(g.x) + ", y: " + Arrays.toString(g.y)
                    + ", expected: " + expected + ", but got (prim): " + aPrim + ", and got (kruskal): " + aKruskal);
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

    static double kruskal(Graph g) {
        DisjointSet set = new DisjointSet(g.s);
        ArrayList<Edge> edges = CalculateAllEdges(g);
        double totalCost = 0.;

        for (Edge e : edges){
            if (set.find(e.i) != set.find(e.j)){
                set.merge(e.i, e.j);
                totalCost += e.cost;
            }
        }

        return totalCost;
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

    static double prim(Graph g) {
        double totalCost = 0.;
        boolean[] visited = new boolean[g.s];
        int[] parent = new int[g.s];
        Arrays.fill(parent, -1);
        double[] cost = new double[g.s];
        Arrays.fill(cost, Double.MAX_VALUE);
        cost[0] = 0;
        PriorityQueue<Integer> h = new PriorityQueue<Integer>(g.s, new HeapRule(cost));
        h.add(0);

        while (!h.isEmpty()) {
            int i = h.poll();
            totalCost += cost[i];
            visited[i] = true;
            for (int j = 0; j < g.s; j++) {
                if (visited[j])
                    continue;
                double oldC = cost[j];
                double newC = g.distance(i, j);
                if (newC < oldC) {
                    h.remove(j);
                    cost[j] = newC;
                    parent[j] = i;
                    h.add(j);
                }
            }
        }

        return totalCost;
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

        void setPoint(int i, int x, int y) {
            this.x[i] = x;
            this.y[i] = y;
        }

        double distance(int i, int j) {
            int dx = x[i] - x[j];
            int dy = y[i] - y[j];
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    static class HeapRule implements Comparator<Integer> {
        double[] cost;

        HeapRule(double[] cost) {
            this.cost = cost;
        }

        @Override
        public int compare(Integer i, Integer j) {
            double ci = cost[i];
            double cj = cost[j];
            if (ci < cj)
                return -1;
            if (ci > cj)
                return 1;
            return 0;
        }
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
