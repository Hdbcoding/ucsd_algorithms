import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class StronglyConnected {
    public static void main(String[] args) {
        runSolution();
        testSolution();
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

    }

    static void runTest() {

    }

    static ArrayList<Integer>[] parseGraph(int nVertices, int[] edges) {
        int x, y;
        Graph g = new Graph(n);
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
        // write your code here
        return 0;
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
