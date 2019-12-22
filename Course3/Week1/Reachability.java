import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Reachability {
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
        System.out.println(reach(adj, x, y));
    }

    static void addEdgeToAdjacencyList(ArrayList<Integer>[] adj, int x, int y) {
        adj[x - 1].add(y - 1);
        adj[y - 1].add(x - 1);
    }

    static ArrayList<Integer>[] constructGraph(int length) {
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[length];
        for (int i = 0; i < length; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        return adj;
    }

    static void testSolution() {
        runTest(parseGraph(4, new int[] { 1, 2, 3, 2, 4, 3, 1, 4 }), 0, 3, 1);
        runTest(parseGraph(4, new int[] { 1, 2, 3, 2 }), 0, 3, 0);
    }

    static ArrayList<Integer>[] parseGraph(int n, int[] edges) {
        int x, y;
        ArrayList<Integer>[] adj = constructGraph(n);
        for (int i = 0; i < edges.length - 1; i += 2) {
            x = edges[i];
            y = edges[i + 1];
            addEdgeToAdjacencyList(adj, x, y);
        }
        return adj;
    }

    static void runTest(ArrayList<Integer>[] adj, int x, int y, int expected) {
        int actual = reach(adj, x, y);
        if (actual != expected)
            System.out.println(
                    "Unexpected result for " + printGraph(adj) + ", x: " + x + ", y: " + y + ". Expected: " + expected);
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

    static int reach(ArrayList<Integer>[] adj, int x, int y) {
        boolean[] visited = new boolean[adj.length];
        explore(x, adj, visited);
        return visited[y] ? 1 : 0;
    }

    static void explore(int x, ArrayList<Integer>[] adj, boolean[] visited) {
        visited[x] = true;
        ArrayList<Integer> neighbors = adj[x];
        for (Integer y : neighbors)
            if (!visited[y])
                explore(y, adj, visited);
    }
}
