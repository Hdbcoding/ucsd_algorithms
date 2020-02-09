import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class FriendSuggestion {
    public static void main(String args[]) {
        // runSolution1();
        // runSolution2();
        // runSolutionNaive();
        testSolution();
    }
    static void runSolution1() {
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

    static void runSolution2() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        BidirectionalDijkstra2 bd = new BidirectionalDijkstra2(n);

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
        // counting matches when a node has been considered but not finalized
        runTest(new int[] { 7, 13, 1, 1, 83, 1, 2, 13, 1, 2, 78, 1, 5, 63, 2, 5, 4, 3, 2, 10, 3, 7, 1, 4, 5, 98, 5, 3,
                23, 6, 2, 51, 6, 1, 94, 6, 7, 85, 7, 2, 40, 1, 6, 7 }, new long[] { 79 });
        // incorrect results in some cases with no-available-path situations
        runTest(new int[] { 10, 16, 1, 4, 95, 3, 1, 57, 3, 8, 85, 5, 8, 66, 6, 7, 31, 6, 4, 13, 6, 7, 17, 7, 9, 19, 8,
                6, 61, 8, 1, 89, 8, 1, 4, 9, 9, 98, 9, 4, 60, 9, 6, 47, 9, 10, 89, 10, 7, 46, 4, 6, 1, 7, 1, 9, 1, 10,
                1 }, new long[] { -1, -1, -1, -1 });
        // missing path problem
        runTest(new int[] { 6, 9, 1, 6, 97, 1, 1, 54, 1, 2, 53, 1, 6, 89, 2, 1, 71, 3, 4, 13, 5, 3, 60, 5, 2, 85, 6, 1,
                9, 1, 5, 6 }, new long[] { 245 });
        // missing path problem
        runTest(new int[] { 8, 10, 1, 5, 2, 1, 7, 77, 2, 7, 98, 2, 3, 29, 3, 6, 21, 4, 6, 18, 5, 8, 19, 6, 8, 58, 7, 3,
                64, 7, 2, 24, 1, 7, 8 }, new long[] { 132 });
        // missing path
        runTest(new int[] { 7, 13, 1, 6, 68, 1, 2, 19, 2, 1, 76, 2, 1, 10, 4, 2, 20, 4, 5, 88, 4, 3, 24, 4, 3, 40, 4, 1,
                13, 5, 7, 20, 5, 6, 1, 6, 4, 64, 7, 2, 94, 1, 2, 7 }, new long[] { 250 });

        // stressTest();
        stressCompare();
    }

    static void runTest(int[] data, long[] expected) {
        BidirectionalDijkstra bd = parseBD1(data);
        BidirectionalDijkstra2 bd2 = parseBD2(data);
        ArrayList<Query> queries = parseQueries(data);
        long[] actual = new long[queries.size()];
        long[] actual2 = new long[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            Query query = queries.get(i);
            try {
                actual[i] = bd.query(query.u, query.v);
                actual2[i] = bd2.query(query.u, query.v);
            } catch (Exception e) {
                System.out.println("exception thrown on query " + i);
                e.printStackTrace();
                actual[i] = -2;
            }
        }

        String eString = Arrays.toString(expected);
        String aString = Arrays.toString(actual);
        String a2String = Arrays.toString(actual2);
        String qString = Arrays.toString(queries.toArray());

        if (!eString.equals(aString)) {
            System.out.println("imp1: Unexpected result for graph:\n" + bd.g + "\n and queries " + qString
                    + ".\nExpected: " + eString + ", but got: " + aString);
        }
        if (!eString.equals(a2String)) {
            System.out.println("imp2: Unexpected result for graph:\n" + bd.g + "\n and queries " + qString
                    + ".\nExpected: " + eString + ", but got: " + a2String);
        }
    }

    static BidirectionalDijkstra parseBD1(int[] data) {
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

    static BidirectionalDijkstra2 parseBD2(int[] data) {
        int n = data[0];
        int m = data[1];
        BidirectionalDijkstra2 bd = new BidirectionalDijkstra2(n);
        for (int i = 0; i < m; i++) {
            int x, y, c;
            x = data[i * 3 + 2];
            y = data[i * 3 + 3];
            c = data[i * 3 + 4];
            bd.addEdge(x - 1, y - 1, c);
        }
        return bd;
    }

    static ArrayList<Query> parseQueries(int[] data) {
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

    static void stressTest() {
        int graphSize = 100;
        int numTests = 10000;
        Random r = new Random();

        for (int i = 0; i < numTests; i++) {
            if (i > 0 && i % 100 == 0)
                System.out.println("test run " + i);
            int n = nextInt(graphSize, r) + 1;
            int m = nextInt(n * 2, r);
            int[] data = fillEdges(n, m, r);
            BidirectionalDijkstra bd = parseBD1(data);
            long[][] distances = calculateDistancesNaive(bd.g);
            BidirectionalDijkstra2 bd2 = parseBD2(data);

            boolean anyMistakes = false;
            for (int j = 0; j < n; j++)
            for (int k = 0; k < n; k++) {
                long expected = distances[j][k];
                try {
                    long actual = bd.query(j, k);
                    long actual2 = bd2.query(j, k);

                    if (expected != actual || expected != actual2) {
                        anyMistakes = true;
                        System.out.println("\nUnexpected distance during test run " + i);
                        System.out.println("for nodes " + j + " to " + k);
                        System.out.println("expected " + expected);
                        System.out.println("actual " + actual);
                        System.out.println("actual2 " + actual2);
                    }
                } catch (Exception e) {
                    anyMistakes = true;
                    System.out.println("\nError thrown during test run " + i);
                    System.out.println("for nodes " + j + " to " + k);
                    System.out.println("expected " + expected);
                    e.printStackTrace();
                }
            }

            if (anyMistakes) {
                System.out.println("\nn: " + n + ", m: " + m);
                System.out.println(bd.g);
            }
        }
    }

    static void stressCompare() {
        int graphSize = 100;
        int numTests = 10000;
        Random r = new Random();
        for (int i = 0; i < numTests; i++) {
            if (i > 0 && i % 100 == 0)
                System.out.println("naive - test run " + i);
            int n = nextInt(graphSize, r) + 1;
            int m = nextInt(n * 2, r);
            int[] data = fillEdges(n, m, r);

            BidirectionalDijkstra bd = parseBD1(data);
            long[][] distances = calculateDistancesNaive(bd.g);
            long a = 0;
            for (int j = 0; j < n; j++)
            for (int k = 0; k < n; k++)
                a = distances[j][k];
            if (i > numTests) System.out.println(a);

        }

        for (int i = 0; i < numTests; i++){
            if (i > 0 && i % 100 == 0)
                System.out.println("bd - test run " + i);
            int n = nextInt(graphSize, r) + 1;
            int m = nextInt(n * 2, r);
            int[] data = fillEdges(n, m, r);
            BidirectionalDijkstra bd = parseBD1(data);
            long a = 0;
            for (int j = 0; j < n; j++)
            for (int k = 0; k < n; k++)
                a = bd.query(j, k);
            if (i > numTests) System.out.println(a);
        }

        for (int i = 0; i < numTests; i++){
            if (i > 0 && i % 100 == 0)
                System.out.println("bd - test run " + i);
            int n = nextInt(graphSize, r) + 1;
            int m = nextInt(n * 2, r);
            int[] data = fillEdges(n, m, r);
            BidirectionalDijkstra2 bd2 = parseBD2(data);
            long a = 0;
            for (int j = 0; j < n; j++)
            for (int k = 0; k < n; k++)
                a = bd2.query(j, k);
            if (i > numTests) System.out.println(a);
        }
    }

    static long[][] calculateDistancesNaive(Graph g) {
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
                if (visited[nodeId])
                    return smallestCrossingDistance(nodeId);
                visited[nodeId] = true;

                nodeId = gr.advanceStep();
                if (visited[nodeId])
                    return smallestCrossingDistance(nodeId);
                visited[nodeId] = true;
            }

            return -1l;
        }

        private long smallestCrossingDistance(int nodeId) {
            long dist = -1;
            for (int i : g.workset) {
                long gd = g.dist[i];

                for (int j : gr.workset) {
                    long grd = gr.dist[j];
                    int edge = getEdge(i, j);
                    if (edge == -1)
                        continue;

                    long d = gd + grd + edge;
                    if (d < dist || dist == -1)
                        dist = d;
                }
            }
            return dist;
        }

        private int getEdge(int i, int j) {
            if (i == j)
                return 0;

            ArrayList<Integer> neighbors = g.adj[i];
            ArrayList<Integer> costs = g.cost[i];
            int distance = -1;
            for (int k = 0; k < neighbors.size(); k++) {
                if (neighbors.get(k) == j) {
                    int cost = costs.get(k);
                    distance = distance == -1 ? cost : Math.min(distance, cost);
                }
            }
            return distance;
        }

        // Reinitialize the data structures before new query after the previous query
        void clear() {
            g.clear();
            gr.clear();
            Arrays.fill(visited, false);
        }
    }

    static class Graph {
        int n;
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] cost;
        long[] dist;
        PriorityQueue<Entry> queue;
        boolean[] visited;
        ArrayList<Integer> workset = new ArrayList<Integer>();

        Graph(int n) {
            this.n = n;

            this.adj = generateAdjacency();
            this.cost = generateAdjacency();
            this.dist = new long[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            this.queue = new PriorityQueue<Entry>(n);
            this.visited = new boolean[n];
        }

        public int advanceStep() {
            Entry e = queue.poll();
            visited[e.node] = true;
            ArrayList<Integer> neighbors = adj[e.node];
            ArrayList<Integer> costs = cost[e.node];
            for (int i = 0; i < neighbors.size(); i++) {
                int nodeId = neighbors.get(i);
                // if (visited[nodeId] || nodeId == e.node) continue;
                int cost = costs.get(i);
                visit(nodeId, e.cost + cost);
            }

            // clear out visited nodes
            while (!queue.isEmpty() && visited[queue.peek().node])
                queue.poll();

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
            Arrays.fill(visited, false);
            queue.clear();
            workset.clear();
        }

        void visit(int i, long d) {
            if (dist[i] > d) {
                dist[i] = d;
                queue.add(new Entry(d, i));
                workset.add(i);
            }
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

    static class BidirectionalDijkstra2 {
        // Number of nodes
        int n;
        // adj[0] and cost[0] store the initial graph, adj[1] and cost[1] store the
        // reversed graph.
        // Each graph is stored as array of adjacency lists for each node. adj stores
        // the edges,
        // and cost stores their costs.
        ArrayList<Integer>[][] adj;
        ArrayList<Integer>[][] cost;
        // distance[0] and distance[1] correspond to distance estimates in the forward
        // and backward searches.
        Long[][] distance;
        // Two priority queues, one for forward and one for backward search.
        ArrayList<PriorityQueue<Entry>> queue;
        // visited[v] == true iff v was visited either by forward or backward search.
        boolean[][] visited;
        // List of all the nodes which were visited either by forward or backward
        // search.
        ArrayList<Integer> workset;
        final Long INFINITY = Long.MAX_VALUE / 4;

        BidirectionalDijkstra2(int n) {
            this.n = n;
            visited = new boolean[2][n];
            workset = new ArrayList<Integer>();
            distance = new Long[][] { new Long[n], new Long[n] };
            for (int i = 0; i < n; ++i) {
                distance[0][i] = distance[1][i] = INFINITY;
            }
            queue = new ArrayList<PriorityQueue<Entry>>();
            queue.add(new PriorityQueue<Entry>(n));
            queue.add(new PriorityQueue<Entry>(n));

            adj = (ArrayList<Integer>[][]) new ArrayList[2][];
            cost = (ArrayList<Integer>[][]) new ArrayList[2][];
            for (int side = 0; side < 2; ++side) {
                adj[side] = (ArrayList<Integer>[]) new ArrayList[n];
                cost[side] = (ArrayList<Integer>[]) new ArrayList[n];
                for (int i = 0; i < n; i++) {
                    adj[side][i] = new ArrayList<Integer>();
                    cost[side][i] = new ArrayList<Integer>();
                }
            }
        }

        void addEdge(int x, int y, int c) {
            adj[0][x].add(y);
            cost[0][x].add(c);
            adj[1][y].add(x);
            cost[1][y].add(c);
        }

        // Reinitialize the data structures before new query after the previous query
        void clear() {
            for (int v : workset) {
                distance[0][v] = distance[1][v] = INFINITY;
                visited[0][v] = visited[1][v] = false;
            }
            workset.clear();
            queue.get(0).clear();
            queue.get(1).clear();
        }

        // Try to relax the distance from direction side to node v using value dist.
        void visit(int side, int v, Long dist) {
            // Implement this method yourself
            if (distance[side][v] > dist) {
                distance[side][v] = dist;
                queue.get(side).add(new Entry(distance[side][v], v));
                workset.add(v);
            }
        }

        int extractMin(int side) {
            Entry e = queue.get(side).poll();
            return e.node;
        }

        void Process(int side, int u, ArrayList<Integer>[] adj, ArrayList<Integer>[] cost) {
            for (int i = 0; i < adj[u].size(); ++i) {
                int v = adj[u].get(i);
                int w = cost[u].get(i);
                visit(side, v, distance[side][u] + w);
            }

        }

        Long ShortestPath(int v) {
            Long dist = INFINITY;
            for (int u : workset) {
                if (distance[0][u] + distance[1][u] < dist) {
                    dist = distance[0][u] + distance[1][u];
                }
            }
            if (dist == INFINITY)
                return -1L;
            return dist;
        }

        // Returns the distance from s to t in the graph.
        Long query(int s, int t) {
            clear();
            visit(0, s, 0L);
            visit(1, t, 0L);
            // Implement the rest of the algorithm yourself
            while (!queue.get(0).isEmpty() && !queue.get(1).isEmpty()) {
                int v = extractMin(0);
                Process(0, v, adj[0], cost[0]);
                if (visited[1][v] == true)
                    return ShortestPath(v);
                visited[0][v] = true;

                int v_r = extractMin(1);
                Process(1, v_r, adj[1], cost[1]);
                if (visited[0][v_r] == true)
                    return ShortestPath(v_r);
                visited[1][v_r] = true;
            }
            return -1L;
        }
    }
}
