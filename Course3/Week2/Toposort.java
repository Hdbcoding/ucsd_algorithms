import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Toposort {
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
        ArrayList<Integer> order = toposort(adj);
        for (int x : order) {
            System.out.print(x + " ");
        }
    }

    static void testSolution() {
        runTest(parseGraph(4, new int[] { 1, 2, 4, 1, 3, 1 }), new int[] { 4, 3, 1, 2 });
        runTest(parseGraph(4, new int[] { 3, 1 }), new int[] { 2, 3, 1, 4 });
        runTest(parseGraph(5, new int[] { 2, 1, 3, 2, 3, 1, 4, 3, 4, 1, 5, 2, 5, 3 }), new int[] { 5, 4, 3, 2, 1 });
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

    static void runTest(ArrayList<Integer>[] adj, int[] expected) {
        ArrayList<Integer> actual = toposort(adj);
        String actualString = Arrays.toString(actual.toArray());
        String expectedString = Arrays.toString(expected);
        if (!expectedString.equals(actualString))
            System.out.println("Unexpected ordering for graph:\n" + printGraph(adj) + "Expected: " + expectedString
                    + ", but got: " + actualString);
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

    static ArrayList<Integer> toposort(ArrayList<Integer>[] adj) {
        TopoSorter sorter = new TopoSorter(adj);
        return sorter.sort();
    }

    static class TopoSorter {
        ArrayList<Integer>[] adj;
        boolean[] visited;
        ArrayList<Integer> order;

        TopoSorter(ArrayList<Integer>[] adj) {
            this.adj = adj;
            this.visited = new boolean[adj.length];
            this.order = new ArrayList<Integer>(adj.length);
        }

        ArrayList<Integer> sort() {
            dfs();
            Collections.reverse(order);
            return order;
        }

        void dfs() {
            for (int i = 0; i < adj.length; i++)
                if (!visited[i])
                    explore(i);
        }

        void explore(int i) {
            visited[i] = true;
            ArrayList<Integer> n = adj[i];
            for (int j : n)
                if (!visited[j])
                    explore(j);
            order.add(i + 1);
        }
    }
}
