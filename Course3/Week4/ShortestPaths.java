import java.util.*;

public class ShortestPaths {
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
        int s = scanner.nextInt() - 1;
        scanner.close();
        shortestPaths(g, s);
        for (int i = 0; i < n; i++) {
            if (g.reachable[i] == 0) {
                System.out.println('*');
            } else if (g.shortest[i] == 0) {
                System.out.println('-');
            } else {
                System.out.println(g.distance[i]);
            }
        }
    }

    static void testSolution() {
        runTest(new int[] { 6, 7, 1, 2, 10, 2, 3, 5, 1, 3, 100, 3, 5, 7, 5, 4, 10, 4, 3, -18, 6, 1, -1, 1 },
                new String[] { "0", "10", "-", "-", "-", "*" });
        runTest(new int[] { 5, 4, 1, 2, 1, 4, 1, 2, 2, 3, 2, 3, 1, -5, 4 }, new String[] { "-", "-", "-", "0", "*" });
    }

    static void runTest(int[] data, String[] expected) {
        Graph g = processData(data);
        int s = data[data.length - 1];
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
        for (int i = 2; i < data.length - 4; i += 3) {
            int x = data[i];
            int y = data[i + 1];
            int w = data[i + 3];
            g.addEdge(x, y, w);
        }
        return g;
    }

    static String[] processResult(Graph g) {
        int n = g.adj.length;
        String[] result = new String[n];
        for (int i = 0; i < n; i++) {
            if (g.reachable[i] == 0) {
                result[i] = "*";
            } else if (g.shortest[i] == 0) {
                result[i] = "-";
            } else {
                result[i] = g.distance[i] + "";
            }
        }
        return result;
    }

    static void shortestPaths(Graph g, int s) {
        // write your code here
    }

    static class Graph {
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] cost;
        long[] distance;
        int[] reachable;
        int[] shortest;

        Graph(int s) {
            adj = constructList(s);
            cost = constructList(s);
            constructResultLists(s);
        }

        ArrayList<Integer>[] constructList(int s) {
            ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[length];
            for (int i = 0; i < s; i++) {
                adj[i] = new ArrayList<Integer>();
            }
            return adj;
        }

        private void constructResultLists(int s) {
            long distance[] = new long[s];
            int reachable[] = new int[s];
            int shortest[] = new int[s];
            for (int i = 0; i < s; i++) {
                distance[i] = Long.MAX_VALUE;
                reachable[i] = 0;
                shortest[i] = 1;
            }
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
                s.append("; weights: " + Arrays.toString(weights.toArray()));
                s.append("\n");
            }
    
            return s.toString();
        }
    }

}
