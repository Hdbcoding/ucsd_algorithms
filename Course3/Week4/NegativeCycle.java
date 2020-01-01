import java.util.ArrayList;
import java.util.Scanner;

public class NegativeCycle {
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
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            g.addEdge(x, y, w);
        }
        scanner.close();
        System.out.println(negativeCycle(g));
    }

    static void testSolution() {
        runTest(new int[] { 4, 4, 1, 2, -5, 4, 1, 2, 2, 3, 2, 3, 1, 1, }, 1);

    }

    static void runTest(int[] data, int expected) {
        Graph g = processData(data);
        int actual = negativeCycle(g);
        if (actual != expected)
            System.out.println("Unexpected result for negative cycles for graph: " + g + "Expected: " + expected
                    + ", but got " + actual);
    }

    static Graph processData(int[] data) {
        Graph g = new Graph(data[0]);
        for (int i = 2; i < data.length - 3; i += 3) {
            int x = data[i] - 1;
            int y = data[i + 1] - 1;
            int w = data[i + 3] - 1;
            g.addEdge(x, y, w);
        }
        return g;
    }

    static int negativeCycle(Graph g) {
        // write your code here
        return 0;
    }

    static class Graph {
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] cost;

        Graph(int s) {
            adj = constructList(s);
            cost = constructList(s);
        }

        ArrayList<Integer>[] constructList(int s) {
            ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[length];
            for (int i = 0; i < s; i++) {
                adj[i] = new ArrayList<Integer>();
            }
            return adj;
        }

        void addEdge(int s, int t, int w) {
            adj[s - 1].add(t - 1);
            cost[s - 1].add(w);
        }

        @Override
        public String toString(){
            StringBuilder s = new StringBuilder();
    
            for (int i = 0; i < adj.length; i++) {
                ArrayList<Integer> edges = adj[i];
                ArrayList<Integer> weights = cost[i];
                s.append("node " + i + ": ");
                s.append("adjacencies: " + Arrays.toString(edges.toArray()));
                s.append("weights: " + Arrays.toString(weights.toArray()));
                s.append("\n");
            }
    
            return s.toString();
        }
    }
}
