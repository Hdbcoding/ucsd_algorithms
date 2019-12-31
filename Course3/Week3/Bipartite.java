import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Bipartite {
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
        scanner.close();
        System.out.println(bipartite(adj));
    }

    static void testSolution() {
        runTest(parseGraph(4, new int[] { 1, 2, 4, 1, 2, 3, 3, 1 }), 0);
        runTest(parseGraph(5, new int[] { 5, 2, 4, 2, 3, 4, 1, 4 }), 1);
    }

    static void runTest(ArrayList<Integer>[] adj, int expected) {
        int actual = bipartite(adj);
        if (expected != actual)
            System.out.println(
                    "Unexpected result for graph: " + printGraph(adj) + "Expected " + expected + " but got " + actual);
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

    private static int bipartite(ArrayList<Integer>[] adj) {
        BipartiteSolver solver = new BipartiteSolver(adj);
        return solver.isBipartite() ? 1 : 0;
    }

    static class BipartiteSolver {
        ArrayList<Integer>[] adj;
        int[] dist;

        public BipartiteSolver(ArrayList<Integer>[] adj) {
            this.adj = adj;
            this.dist = new int[adj.length];
            for (int i = 0; i < adj.length; i++)
                dist[i] = -1;
        }

        boolean isBipartite() {
            for (int i = 0; i < adj.length; i++)
                BFS(i);
            for (int i = 0; i < adj.length; i++)
                if (!isBipartite(i))
                    return false;
            return true;
        }

        private void BFS(int i) {
            if (dist[i] != -1) return;
            dist[i] = 0;
            Queue<Integer> q = new LinkedList<Integer>();
            q.add(i);
            int u;
            int d;
            while (!q.isEmpty()) {
                u = q.poll();
                d = dist[u];
                for (int n : adj[u]) {
                    if (dist[n] == -1) {
                        dist[n] = d + 1;
                        q.add(n);
                    }
                }
            }
        }

        private boolean isBipartite(int i) {
            int m = dist[i] % 2;

            for (int n : adj[i])
                if (m == dist[n] % 2)
                    return false;

            return true;
        }
    }
}
