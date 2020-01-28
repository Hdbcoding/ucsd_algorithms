import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class FriendSuggestion {
    public static void main(String args[]) {
        // runSolution();
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

    static void testSolution() {
        runTest(new int[] { 2, 1, 1, 2, 1, 4, 1, 1, 2, 2, 1, 2, 2, 1 }, new long[] { 0, 0, 1, -1 });
        runTest(new int[] { 4, 4, 1, 2, 1, 4, 1, 2, 2, 3, 2, 1, 3, 5, 1, 1, 3 }, new long[] { 3 });
        runTest(new int[] { 5, 20, 1, 2, 667, 1, 3, 677, 1, 4, 700, 1, 5, 622, 2, 1, 118, 2, 3, 325, 2, 4, 784, 2, 5,
                11, 3, 1, 585, 3, 2, 956, 3, 4, 551, 3, 5, 559, 4, 1, 503, 4, 2, 722, 4, 3, 331, 4, 5, 366, 5, 1, 880,
                5, 2, 883, 5, 3, 461, 5, 4, 228, 10, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 2, 1, 2, 2, 2, 3, 2, 4, 2, 5 },
                new long[] { 0, 667, 677, 700, 622, 118, 0, 325, 239, 11 });
        stressTest();
    }

    static void runTest(int[] data, long[] expected) {
        // todo - parse data to fill bd and q
        BidirectionalDijkstra bd = parseData(data);
        int qIndex = data[1] * 3 + 2;
        int q = data[qIndex];
        ArrayList<Query> queries = new ArrayList<Query>(q);
        long[] actual = new long[q];
        for (int i = 0; i < q; i++) {
            int u, v;
            u = data[i * 2 + qIndex + 1];
            v = data[i * 2 + qIndex + 2];
            queries.add(new Query(u - 1, v - 1));
        }

        for (int i = 0; i < queries.size(); i++) {
            Query query = queries.get(i);
            actual[i] = bd.query(query.u, query.v);
        }

        String eString = Arrays.toString(expected);
        String aString = Arrays.toString(actual);
        String qString = Arrays.toString(queries.toArray());

        if (!eString.equals(aString)) {
            System.out.println("Unexpected result for graph:\n" + bd.g + "\n and queries " + qString + ".\nExpected: "
                    + eString + ", but got: " + aString);
        }
    }

    static BidirectionalDijkstra parseData(int[] data) {
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

    private static void stressTest() {
        int graphSize = 5;
        int numTests = 30;
        Random r = new Random();

        for (int i = 0; i < numTests; i++) {
            int n = nextInt(graphSize, r) + 1;
            int m = nextInt(n * n, r);
            int[] data = fillEdges(n, m, r);
            BidirectionalDijkstra bd = parseData(data);
            long[][] distances = calculateDistancesNaive(bd.g);

            boolean anyMistakes = false;
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    long expected = distances[j][k];
                    try {
                        long actual = bd.query(j, k);

                        if (expected != actual) {
                            anyMistakes = true;
                            System.out.println("Unexpected distance between nodes " + j + ", and " + k + ". Expected "
                                    + expected + ", but got " + actual);
                        }
                    } catch (Exception e) {
                        System.out.println("\nError thrown during test run " + i + ", " + e);
                        System.out.println("error for nodes " + j + ", and " + k);
                        e.printStackTrace();
                        System.out.println(bd.g);
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

        BidirectionalDijkstra(int n) {
            this.n = n;

            this.g = new Graph(n);
            this.gr = new Graph(n);
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

            boolean getFromG = true;
            while (!g.queue.isEmpty() && !gr.queue.isEmpty()) {
                boolean foundMatch = false;
                int nodeId;
                if (getFromG) {
                    nodeId = g.advanceStep();
                    foundMatch = gr.dist[nodeId] < Long.MAX_VALUE;
                } else {
                    nodeId = gr.advanceStep();
                    foundMatch = g.dist[nodeId] < Long.MAX_VALUE;
                }
                if (foundMatch)
                    return smallestCrossingDistance(nodeId);
                getFromG = !getFromG;
            }

            return -1l;
        }

        private long smallestCrossingDistance(int nodeId) {
            long dist = g.dist[nodeId] + gr.dist[nodeId];
            for (int i = 0; i < n; i++) {
                if (i == nodeId)
                    continue;
                if (g.dist[i] == Long.MAX_VALUE)
                    continue;
                long di = g.dist[i];

                ArrayList<Integer> neighbors = g.adj[i];
                ArrayList<Integer> costs = g.cost[i];
                for (int j = 0; j < neighbors.size(); j++) {
                    int n = neighbors.get(j);
                    if (gr.dist[n] == Long.MAX_VALUE)
                        continue;
                    long cost = costs.get(j);
                    long newDist = di + gr.dist[n] + cost;
                    dist = Math.min(newDist, dist);
                }
            }
            return dist;
        }

        // Reinitialize the data structures before new query after the previous query
        void clear() {
            g.clear();
            gr.clear();
        }

        void visit(Graph g, int i, long distance) {
            g.visit(i, distance);
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
            ArrayList<Integer> neighbors = adj[e.node];
            ArrayList<Integer> costs = cost[e.node];
            for (int i = 0; i < neighbors.size(); i++) {
                int nodeId = neighbors.get(i);
                int cost = costs.get(i);
                long oldDist = dist[nodeId];
                long newDist = e.cost + cost;
                if (oldDist > newDist) {
                    queue.remove(entries[i]);
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
            this.queue.clear();
        }

        void visit(int i, long d) {
            entries[i] = new Entry(d, i);
            dist[i] = d;
            queue.add(entries[i]);
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
