import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class FriendSuggestion {
    public static void main(String args[]) {
        // runSolution();
        // runSolutionNaive();
        testSolution();
    }

    static void runSolution() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        BidirectionalDijkstra bd = new BidirectionalDijkstra(n);

        for (int i = 0; i < m; i++) {
            int x, y, c;
            x = in.nextInt();
            y = in.nextInt();
            c = in.nextInt();
            bd.addEdge(x - 1, y - 1, c);
        }

        int t = in.nextInt();

        for (int i = 0; i < t; i++) {
            int u, v;
            u = in.nextInt();
            v = in.nextInt();
            System.out.println(bd.query(u - 1, v - 1));
        }
        in.close();
    }

    static void runSolutionNaive() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();

        Graph g = new Graph(n);
        for (int i = 0; i < m; i++) {
            int x, y, c;
            x = in.nextInt();
            y = in.nextInt();
            c = in.nextInt();
            g.addEdge(x - 1, y - 1, c);
        }

        int t = in.nextInt();
        long[][] distances = calculateDistancesNaive(g);
        for (int i = 0; i < t; i++) {
            int u, v;
            u = in.nextInt();
            v = in.nextInt();
            System.out.println(distances[u - 1][v - 1]);
        }
        in.close();
    }

    static void testSolution() {
        runTest(new int[] { 2, 1, 1, 2, 1, 4, 1, 1, 2, 2, 1, 2, 2, 1 }, new long[] { 0, 0, 1, -1 });
        runTest(new int[] { 4, 4, 1, 2, 1, 4, 1, 2, 2, 3, 2, 1, 3, 5, 1, 1, 3 }, new long[] { 3 });
        runTest(new int[] { 5, 20, 1, 2, 667, 1, 3, 677, 1, 4, 700, 1, 5, 622, 2, 1, 118, 2, 3, 325, 2, 4, 784, 2, 5,
                11, 3, 1, 585, 3, 2, 956, 3, 4, 551, 3, 5, 559, 4, 1, 503, 4, 2, 722, 4, 3, 331, 4, 5, 366, 5, 1, 880,
                5, 2, 883, 5, 3, 461, 5, 4, 228, 10, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 2, 1, 2, 2, 2, 3, 2, 4, 2, 5 },
                new long[] { 0, 667, 677, 700, 622, 118, 0, 325, 239, 11 });
        // getting bad distances with multiple edges out of the same node -- fixed
        runTest(new int[] { 4, 4, 2, 3, 72, 2, 3, 89, 2, 1, 62, 3, 4, 21, 1, 2, 4 }, new long[] { 93 });
        // getting errors with self-referencing nodes
        runTest(new int[] { 4, 7, 1, 2, 72, 1, 2, 29, 2, 2, 7, 2, 2, 8, 3, 4, 79, 3, 4, 76, 3, 2, 78, 2, 1, 2, 3, 2 },
                new long[] { 29, 78 });
        // some null pointer exception
        runTest(new int[] { 3, 5, 2, 3, 58, 2, 3, 2, 3, 1, 47, 3, 3, 40, 3, 3, 21, 1, 1, 3 }, new long[] { -1 });

        // some miscalculation, not sure why yet
        // was because of counting matches when a node has been considered but not
        // finalized
        runTest(new int[] { 7, 13, 1, 1, 83, 1, 2, 13, 1, 2, 78, 1, 5, 63, 2, 5, 4, 3, 2, 10, 3, 7, 1, 4, 5, 98, 5, 3,
                23, 6, 2, 51, 6, 1, 94, 6, 7, 85, 7, 2, 40, 1, 6, 7 }, new long[] { 79 });

        stressTest();
    }

    static void runTest(int[] data, long[] expected) {
        BidirectionalDijkstra bd = parseGraphs(data);
        ArrayList<Query> queries = parseQueries(data);
        long[] actual = new long[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            Query query = queries.get(i);
            try {
                actual[i] = bd.query(query.u, query.v);
            } catch (Exception e) {
                System.out.println("exception thrown on query " + i);
                e.printStackTrace();
                actual[i] = -2;
            }
        }

        String eString = Arrays.toString(expected);
        String aString = Arrays.toString(actual);
        String qString = Arrays.toString(queries.toArray());

        if (!eString.equals(aString)) {
            System.out.println("Unexpected result for graph:\n" + bd.g + "\n and queries " + qString + ".\nExpected: "
                    + eString + ", but got: " + aString);
        }
    }

    static BidirectionalDijkstra parseGraphs(int[] data) {
        int n = data[0];
        int m = data[1];
        BidirectionalDijkstra bd = new BidirectionalDijkstra(n);
        for (int i = 0; i < m; i++) {
            int x, y, c;
            x = data[i * 3 + 2];
            y = data[i * 3 + 3];
            c = data[i * 3 + 4];
            bd.addEdge(x - 1, y - 1, c);
        }
        return bd;
    }

    private static ArrayList<Query> parseQueries(int[] data) {
        int qIndex = data[1] * 3 + 2;
        int q = data[qIndex];
        ArrayList<Query> queries = new ArrayList<Query>(q);
        for (int i = 0; i < q; i++) {
            int u, v;
            u = data[i * 2 + qIndex + 1];
            v = data[i * 2 + qIndex + 2];
            queries.add(new Query(u - 1, v - 1));
        }
        return queries;
    }

    private static void stressTest() {
        int graphSize = 10;
        int numTests = 100;
        Random r = new Random();

        for (int i = 0; i < numTests; i++) {
            if (i > 0 && i % 100 == 0)
                System.out.println("test run " + i);
            int n = nextInt(graphSize, r) + 1;
            int m = nextInt(n * 2, r);
            int[] data = fillEdges(n, m, r);
            BidirectionalDijkstra bd = parseGraphs(data);
            long[][] distances = calculateDistancesNaive(bd.g);

            boolean anyMistakes = false;
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    long expected = distances[j][k];
                    try {
                        long actual = bd.query(j, k);

                        if (expected != actual) {
                            anyMistakes = true;
                            System.out.println("\nUnexpected distance during test run " + i);
                            System.out.println("for nodes " + j + " to " + k);
                            System.out.println("n: " + n + ", m: " + m);
                            System.out.println("expected " + expected);
                            System.out.println("actual " + actual);
                        }
                    } catch (Exception e) {
                        anyMistakes = true;
                        System.out.println("\nError thrown during test run " + i);
                        System.out.println("for nodes " + j + " to " + k);
                        System.out.println("n: " + n + ", m: " + m);
                        System.out.println("expected " + expected);
                        e.printStackTrace();
                    }
                }
            }
            if (anyMistakes) {
                System.out.println(bd.g);
            }
        }
    }

    private static long[][] calculateDistancesNaive(Graph g) {
        long[][] m = new long[g.n][g.n];

        for (int i = 0; i < g.n; i++) {
            Arrays.fill(m[i], -1);
            ArrayList<Integer> adj = g.adj[i];
            ArrayList<Integer> cst = g.cost[i];
            for (int j = 0; j < adj.size(); j++) {
                int n = adj.get(j);
                int w = cst.get(j);
                long old = m[i][n];
                m[i][n] = old == -1 || w < old ? w : old;
            }
            m[i][i] = 0;
        }

        for (int k = 0; k < g.n; k++) {
            for (int i = 0; i < g.n; i++) {
                for (int j = 0; j < g.n; j++) {
                    long oldD = m[i][j];
                    long ik = m[i][k];
                    long kj = m[k][j];

                    if (ik == -1 || kj == -1)
                        continue;
                    if (oldD == -1 || oldD > ik + kj)
                        m[i][j] = ik + kj;
                }
            }
        }
        return m;
    }

    static int[] fillEdges(int n, int m, Random r) {
        int[] data = new int[m * 3 + 2];
        data[0] = n;
        data[1] = m;
        int lengthBound = 100;
        for (int i = 0; i < m; i++) {
            int j = i * 3 + 2;
            data[j] = nextInt(n, r) + 1;
            data[j + 1] = nextInt(n, r) + 1;
            data[j + 2] = nextInt(lengthBound, r) + 1;
        }
        return data;
    }

    static int nextInt(int bound, Random r) {
        return Math.abs(r.nextInt(bound));
    }

    static class BidirectionalDijkstra {
        // Number of nodes
        int n;
        Graph g;
        Graph gr;
        boolean[] visited;
        ArrayList<Integer> workset = new ArrayList<Integer>();

        BidirectionalDijkstra(int n) {
            this.n = n;

            this.g = new Graph(n);
            this.gr = new Graph(n);
            this.visited = new boolean[n];
        }

        void addEdge(int x, int y, int c) {
            g.addEdge(x, y, c);
            gr.addEdge(y, x, c);
        }

        // Returns the distance from s to t in the graph.
        long query(int s, int t) {
            clear();
            g.visit(s, 0l);
            gr.visit(t, 0l);
            // Implement the rest of the algorithm yourself

            while (!g.queue.isEmpty() && !gr.queue.isEmpty()) {
                int nodeId = g.advanceStep();
                if (visited[nodeId]) return smallestCrossingDistance(nodeId);
                visited[nodeId] = true;
                workset.add(nodeId);

                nodeId = gr.advanceStep();
                if (visited[nodeId]) return smallestCrossingDistance(nodeId);
                visited[nodeId] = true;
                workset.add(nodeId);
            }

            return -1l;
        }

        private long smallestCrossingDistance(int nodeId) {
            long dist = Long.MAX_VALUE;
            for (int i : workset)
            for (int j : workset) {
                long gd = g.dist[i];
                if (gd == Long.MAX_VALUE) continue;
                long grd = gr.dist[j];
                if (grd == Long.MAX_VALUE) continue;
                int edge = getEdge(i, j);
                if (edge == -1) continue;

                long d = gd + grd + edge;
                if ( d < dist) dist = d;
            }
            // for (int i = 0; i < n; i++) {
            //     if (i == nodeId)
            //         continue;
            //     if (g.dist[i] == Long.MAX_VALUE)
            //         continue;
            //     long di = g.dist[i];

            //     ArrayList<Integer> neighbors = g.adj[i];
            //     ArrayList<Integer> costs = g.cost[i];
            //     for (int j = 0; j < neighbors.size(); j++) {
            //         int n = neighbors.get(j);
            //         if (gr.dist[n] == Long.MAX_VALUE)
            //             continue;
            //         long cost = costs.get(j);
            //         long newDist = di + gr.dist[n] + cost;
            //         dist = Math.min(newDist, dist);
            //     }
            // }
            return dist;
        }

        private int getEdge(int i, int j) {
            if (i == j) return 0;
            ArrayList<Integer> neighbors = g.adj[i];
            ArrayList<Integer> costs = g.cost[i];
            int distance = -1;
            for (int k = 0; k < neighbors.size(); k++){
                if (neighbors.get(k) == j){
                    if (distance == -1) distance = costs.get(k);
                    else distance = Math.min(distance, costs.get(k));
                }
            }
            return distance;
        }

        // Reinitialize the data structures before new query after the previous query
        void clear() {
            g.clear();
            gr.clear();
            Arrays.fill(visited, false);
            workset.clear();
        }
    }

    static class Graph {
        int n;
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] cost;
        long[] dist;
        PriorityQueue<Entry> queue;
        Entry[] entries;

        Graph(int n) {
            this.n = n;

            this.adj = generateAdjacency();
            this.cost = generateAdjacency();
            this.dist = new long[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            this.queue = new PriorityQueue<Entry>(n);
            this.entries = new Entry[n];
        }

        public int advanceStep() {
            Entry e = queue.poll();
            entries[e.node] = null;
            ArrayList<Integer> neighbors = adj[e.node];
            ArrayList<Integer> costs = cost[e.node];
            for (int i = 0; i < neighbors.size(); i++) {
                int nodeId = neighbors.get(i);
                int cost = costs.get(i);
                long oldDist = dist[nodeId];
                long newDist = e.cost + cost;
                if (oldDist > newDist) {
                    visit(nodeId, newDist);
                }
            }
            return e.node;
        }

        ArrayList<Integer>[] generateAdjacency() {
            ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
            for (int i = 0; i < n; i++) {
                adj[i] = new ArrayList<Integer>();
            }
            return adj;
        }

        void addEdge(int x, int y, int c) {
            adj[x].add(y);
            cost[x].add(c);
        }

        void clear() {
            Arrays.fill(dist, Long.MAX_VALUE);
            Arrays.fill(entries, null);
            this.queue.clear();
        }

        void visit(int i, long d) {
            dist[i] = d;
            queue.add(new Entry(d, i));
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();

            for (int i = 0; i < adj.length; i++) {
                ArrayList<Integer> edges = adj[i];
                ArrayList<Integer> weights = cost[i];
                if (i > 0)
                    s.append("\n");
                s.append("node " + i + ": ");
                s.append("adjacencies: " + Arrays.toString(edges.toArray()));
                s.append("; weights: " + Arrays.toString(weights.toArray()));
            }

            return s.toString();
        }
    }

    static class Entry implements Comparable<Entry> {
        Long cost;
        int node;

        public Entry(Long cost, int node) {
            this.cost = cost;
            this.node = node;
        }

        public int compareTo(Entry other) {
            return cost < other.cost ? -1 : cost > other.cost ? 1 : 0;
        }
    }

    static class Query {
        int u;
        int v;

        Query(int u, int v) {
            this.u = u;
            this.v = v;
        }

        @Override
        public String toString() {
            return "{u: " + u + ", v: " + v + "}";
        }
    }
}
