import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class StronglyConnected {
    public static void main(String[] args) {
        runSolution();
        // testSolution();
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Graph g = new Graph(n);
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            g.addEdge(x, y);
        }
        scanner.close();
        System.out.println(numberOfStronglyConnectedComponents(g));
    }

    static void testSolution() {
        runTest(parseGraph(4, new int[] { 1, 2, 4, 1, 2, 3, 3, 1 }), 2);
        runTest(parseGraph(5, new int[] { 2, 1, 3, 2, 3, 1, 4, 3, 4, 1, 5, 2, 5, 3 }), 5);
    }

    static void runTest(Graph g, int expected) {
        int actual = numberOfStronglyConnectedComponents(g);
        if (expected != actual)
            System.out.println("Unexpected number of SCCs for graph: " + printGraph(g.adj) + "Expected " + expected
                    + " but got " + actual);
    }

    static Graph parseGraph(int nVertices, int[] edges) {
        int x, y;
        Graph g = new Graph(nVertices);
        for (int i = 0; i < edges.length - 1; i += 2) {
            x = edges[i];
            y = edges[i + 1];
            g.addEdge(x, y);
        }
        return g;
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

    private static int numberOfStronglyConnectedComponents(Graph g) {
        SCCCounter counter = new SCCCounter(g);
        return counter.count();
    }

    static class SCCCounter {
        Graph g;
        boolean[] visited;
        boolean[] visited_r;
        ArrayList<Integer> order_r;
        int num_scc = 0;

        public SCCCounter(Graph g) {
            this.g = g;
            this.visited = new boolean[g.adj.length];
            this.visited_r = new boolean[g.adj.length];
            this.order_r = new ArrayList<Integer>(g.adj.length);
        }

        int count() {
            dfs_r();
            Collections.reverse(order_r);
            count_scc();
            return num_scc;
        }

        void dfs_r() {
            for (int i = 0; i < g.adj_r.length; i++)
                if (!visited_r[i])
                    explore(i, g.adj_r, visited_r, true);
        }

        void explore(int i, ArrayList<Integer>[] adj, boolean[] v, boolean doPostAction) {
            v[i] = true;
            ArrayList<Integer> n = adj[i];
            for (int j : n)
                if (!v[j])
                    explore(j, adj, v, doPostAction);
            if (doPostAction) order_r.add(i);
        }

        void count_scc() {
            for (int i : order_r)
                if (!visited[i]) {
                    explore(i, g.adj, visited, false);
                    num_scc++;
                }
        }
    }

    static class Graph {
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] adj_r;

        Graph(int n) {
            adj = constructGraph(n);
            adj_r = constructGraph(n);
        }

        ArrayList<Integer>[] constructGraph(int length) {
            ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[length];
            for (int i = 0; i < length; i++) {
                adj[i] = new ArrayList<Integer>();
            }
            return adj;
        }

        void addEdge(int x, int y) {
            adj[x - 1].add(y - 1);
            adj_r[y - 1].add(x - 1);
        }
    }
}
