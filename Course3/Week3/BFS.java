import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class BFS {
    public static void main(String[] args) {
        runSolution();
        // testSolution();
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = constructGraph(n);
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            addEdgeToAdjacencyList(adj, x, y);
        }
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        scanner.close();
        System.out.println(distance(adj, x, y));
    }

    static void testSolution() {
        runTest(parseGraph(4, new int[] { 1, 2, 4, 1, 2, 3, 3, 1 }), 1, 3, 2);
        runTest(parseGraph(5, new int[] { 5, 2, 1, 3, 3, 4, 1, 4 }), 2, 4, -1);
    }

    static void runTest(ArrayList<Integer>[] adj, int s, int t, int expected) {
        int actual = distance(adj, s, t);
        if (expected != actual)
            System.out.println("Unexpected distance between nodes" + s + " and " + t + " for graph: " + printGraph(adj)
                    + "Expected " + expected + " but got " + actual);
    }

    static ArrayList<Integer>[] constructGraph(int length) {
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[length];
        for (int i = 0; i < length; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        return adj;
    }

    static void addEdgeToAdjacencyList(ArrayList<Integer>[] adj, int x, int y) {
        adj[x - 1].add(y - 1);
        adj[y - 1].add(x - 1);
    }

    static ArrayList<Integer>[] parseGraph(int nVertices, int[] edges) {
        int x, y;
        ArrayList<Integer>[] adj = constructGraph(nVertices);
        for (int i = 0; i < edges.length - 1; i += 2) {
            x = edges[i];
            y = edges[i + 1];
            addEdgeToAdjacencyList(adj, x, y);
        }
        return adj;
    }

    static String printGraph(ArrayList<Integer>[] adj) {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < adj.length; i++) {
            ArrayList<Integer> edges = adj[i];
            s.append("node " + i + ": ");
            s.append(Arrays.toString(edges.toArray()));
            s.append("\n");
        }

        return s.toString();
    }

    static int distance(ArrayList<Integer>[] adj, int s, int t) {
        BFSSolver solver = new BFSSolver(adj);
        return solver.distance(s, t);
    }

    static class BFSSolver {
        ArrayList<Integer>[] adj;
        int[] dist;

        public BFSSolver(ArrayList<Integer>[] adj) {
            this.adj = adj;
            this.dist = new int[adj.length];
            for (int i = 0; i < adj.length; i++)
                dist[i] = -1;
        }

        int distance(int s, int t) {
            if (s == t)
                return 0;
            dist[s] = 0;

            Queue<Integer> q = new LinkedList<Integer>();
            q.add(s);
            int u;
            int d;
            while (!q.isEmpty()) {
                u = q.poll();
                d = dist[u];
                for (int n : adj[u]) {
                    if (dist[n] == -1) {
                        dist[n] = d + 1;
                        q.add(n);
                        if (n == t)
                            return dist[n];
                    }
                }
            }

            return -1;
        }
    }
}
