import java.util.*;

public class ShortestPaths {
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
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            g.addEdge(x, y, w);
        }
        int s = scanner.nextInt() - 1;
        scanner.close();
        shortestPaths(g, s);
        for (int i = 0; i < n; i++) {
            System.out.println(g.getResult(i));
        }
    }

    static void testSolution() {
        runTest(new int[] { 6, 7, 1, 2, 10, 2, 3, 5, 1, 3, 100, 3, 5, 7, 5, 4, 10, 4, 3, -18, 6, 1, -1, 1 },
                new String[] { "0", "10", "-", "-", "-", "*" });
        runTest(new int[] { 5, 4, 1, 2, 1, 4, 1, 2, 2, 3, 2, 3, 1, -5, 4 }, new String[] { "-", "-", "-", "0", "*" });
    }

    static void runTest(int[] data, String[] expected) {
        Graph g = processData(data);
        int s = data[data.length - 1] - 1;
        shortestPaths(g, s);
        String[] actual = processResult(g);
        String eString = Arrays.toString(expected);
        String aString = Arrays.toString(actual);
        if (!eString.equals(aString))
            System.out.println("Unexpected paths result for node " + s + " in graph:\n" + g + "Expected: " + eString
                    + ", but got: " + aString);
    }

    static Graph processData(int[] data) {
        Graph g = new Graph(data[0]);
        for (int i = 2; i < data.length - 3; i += 3) {
            int x = data[i];
            int y = data[i + 1];
            int w = data[i + 2];
            g.addEdge(x, y, w);
        }
        return g;
    }

    static String[] processResult(Graph g) {
        int n = g.adj.length;
        String[] result = new String[n];
        for (int i = 0; i < n; i++) {
            result[i] = g.getResult(i);
        }
        return result;
    }

    static void shortestPaths(Graph g, int src) {
        g.dist[src] = 0l;
        Stack<Integer> toVisit = new Stack<Integer>();

        for (int k = 0; k < g.s; k++) {
            boolean[] visited = new boolean[g.s];
            boolean lastRound = k == (g.s - 1);
            toVisit.add(src);

            while (!toVisit.isEmpty()) {
                int i = toVisit.pop();
                visited[i] = true;
                ArrayList<Integer> adj = g.adj[i];
                ArrayList<Integer> cost = g.cost[i];
                for (int j = 0; j < adj.size(); j++) {
                    int n = adj.get(j);
                    int c = cost.get(j);
                    if (relax(i, n, c, g.dist) && lastRound) {
                        g.hasCycle[i] = true;
                        g.hasCycle[n] = true;
                    }
                    if (!visited[n])
                        toVisit.add(n);
                }
            }

        }
    }

    // returns true if this edge was relaxed
    private static boolean relax(int u, int v, int cost, long[] dist) {
        long src = dist[u];
        if (src == Long.MAX_VALUE)
            return false;
        long oldDist = dist[v];
        long newDist = src + cost;
        if (newDist < oldDist) {
            dist[v] = newDist;
            return true;
        }

        return false;
    }

    static class Graph {
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] cost;
        long[] dist;
        boolean[] hasCycle;
        int s;

        Graph(int s) {
            adj = constructList(s);
            cost = constructList(s);
            dist = new long[s];
            hasCycle = new boolean[s];

            this.s = s;
        }

        ArrayList<Integer>[] constructList(int s) {
            ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[s];
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
        public String toString() {
            StringBuilder s = new StringBuilder();

            for (int i = 0; i < adj.length; i++) {
                ArrayList<Integer> edges = adj[i];
                ArrayList<Integer> weights = cost[i];
                s.append("node " + i + ": ");
                s.append("adjacencies: " + Arrays.toString(edges.toArray()));
                s.append("; weights: " + Arrays.toString(weights.toArray()));
                s.append("; hasCycle: " + hasCycle[i]);
                s.append("; distance: " + dist[i]);
                s.append("\n");
            }

            return s.toString();
        }

        String getResult(int i) {
            if (dist[i] == Long.MAX_VALUE) {
                return "*";
            } else if (hasCycle[i]) {
                return "-";
            } else {
                return dist[i] + "";
            }
        }
    }

}
