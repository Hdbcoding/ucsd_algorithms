import java.util.*;

public class Dijkstra {
    public static void main(String[] args) {
        // runSolution();
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
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        scanner.close();
        System.out.println(distance(g, x, y));
    }

    static void testSolution() {
        runTest(new int[] { 4, 4, 1, 2, 1, 4, 1, 2, 2, 3, 2, 1, 3, 5, 1, 3 }, 3);
        runTest(new int[] { 5, 9, 1, 2, 4, 1, 3, 2, 2, 3, 2, 3, 2, 1, 2, 4, 2, 3, 5, 4, 5, 4, 1, 2, 5, 3, 3, 4, 4, 1,
                5 }, 6);
        runTest(new int[] { 3, 3, 1, 2, 7, 1, 3, 5, 2, 3, 2, 3, 2 }, -1);
    }

    static void runTest(int[] data, int expected) {
        Graph g = processData(data);
        int s = data[data.length - 2] - 1;
        int t = data[data.length - 1] - 1;

        int actual = distance(g, s, t);
        if (actual != expected)
            System.out.println("Unexpected distance between nodes s: " + s + " and t: " + t + " for graph: " + g
                    + "Expected " + expected + ", but got " + actual);
    }

    static Graph processData(int[] data) {
        Graph g = new Graph(data[0]);
        for (int i = 2; i < data.length - 5; i += 3) {
            int x = data[i] - 1;
            int y = data[i + 1] - 1;
            int w = data[i + 3] - 1;
            g.addEdge(x, y, w);
        }
        return g;
    }

    static int distance(Graph g, int s, int t) {
        return -1;
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
