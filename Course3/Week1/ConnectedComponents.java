import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ConnectedComponents {
    public static void main(String[] args) {
        runSolution();
        // testSolution();
    }

    static void runSolution(){
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
        System.out.println(numberOfComponents(adj));
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

    static void testSolution(){
        runTest(parseGraph(4, new int[]{1, 2, 3, 2}), 2);
    }

    private static ArrayList<Integer>[] parseGraph(int nVertices, int[] edges) {
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
        int actual = numberOfComponents(adj);
        if (actual != expected)
            System.out.println(
                    "Unexpected result for " + printGraph(adj) + ". Expected: " + expected);
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

    static int numberOfComponents(ArrayList<Integer>[] adj) {
        int result = 0;
        boolean[] visited = new boolean[adj.length];
        for (int i = 0; i < adj.length; i++){
            if (!visited[i]) {
                explore(i, adj, visited);
                result++;
            }
        }
        return result;
    }

    private static void explore(int i, ArrayList<Integer>[] adj, boolean[] visited) {
        visited[i] = true;
        for (int j : adj[i])
            if (!visited[j]) explore(j, adj, visited);
    }
}

