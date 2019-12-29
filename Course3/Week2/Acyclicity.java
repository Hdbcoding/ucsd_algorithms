import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Acyclicity {
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
        System.out.println(acyclic(adj));
    }

    static void testSolution() {
        runTest(parseGraph(4, new int[] { 1, 2, 4, 1, 2, 3, 3, 1 }), 1);
        runTest(parseGraph(5, new int[] { 1, 2, 2, 3, 1, 3, 3, 4, 1, 4, 2, 5, 3, 5
        }), 0);
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

    static void runTest(ArrayList<Integer>[] adj, int expected) {
        int actual = acyclic(adj);
        if (actual != expected)
            System.out.println("Unexpected result for " + printGraph(adj) + ". Expected: " + expected);
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

    private static int acyclic(ArrayList<Integer>[] adj) {
        CyclicityChecker checker = new CyclicityChecker(adj);
        return checker.hasCycles() ? 1 : 0;
    }

    static class CyclicityChecker {
        ArrayList<Integer>[] adj;
        boolean[] visited;
        int[] post;
        int postIndex = 0;

        CyclicityChecker(ArrayList<Integer>[] adj) {
            this.adj = adj;
            visited = new boolean[adj.length];
            post = new int[adj.length];
        }

        boolean hasCycles() {
            dfs();
            return verifyEdges();
        }

        void dfs() {
            for (int i = 0; i < adj.length; i++)
                if (!visited[i])
                    explore(i);
        }

        private void explore(int i) {
            visited[i] = true;
            ArrayList<Integer> n = adj[i];
            for (int j : n)
                if (!visited[j])
                    explore(j);
            post[i] = postIndex++;
        }

        boolean verifyEdges() {
            for (int i = 0; i < adj.length; i++) {
                ArrayList<Integer> n = adj[i];
                for (int j : n) {
                    if (post[i] < post[j])
                        return true;
                }
            }
            return false;
        }
    }
}
