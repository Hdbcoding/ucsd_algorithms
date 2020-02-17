import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class DistPreprocessSmall {
    public static void main(String[] args) {
        // runSolution();
        testSolution();
    }

    static void runSolution() {
        DataScanner in = new StreamScanner();
        Dijkstra2 a = new Dijkstra2();
        a.preprocess(in);
        System.out.println("Ready");
        respondToQueries(a, in);
        in.close();
    }

    static void testSolution() {
        // test cases from previous problems
        runTest(new int[] { 2, 1, 1, 2, 1, 4, 1, 1, 2, 2, 1, 2, 2, 1 }, new long[] { 0, 0, 1, -1 });
        runTest(new int[] { 4, 4, 1, 2, 1, 4, 1, 2, 2, 3, 2, 1, 3, 6, 1, 1, 3 }, new long[] { 3 });
        runTest(new int[] { 9, 16, 1, 2, 9, 1, 2, 10, 2, 8, 10, 2, 9, 3, 3, 5, 8, 3, 5, 14, 5, 4, 17, 5, 6, 6, 7, 2, 5,
                7, 3, 12, 7, 4, 10, 7, 6, 4, 7, 6, 9, 8, 2, 9, 9, 2, 7, 9, 9, 7, 2, 1, 8, 1, 9 },
                new long[] { 19, 12 });
        runTest(new int[] { 9, 16, 1, 2, 10, 8, 2, 9, 9, 9, 7, 7, 2, 5, 7, 4, 10, 7, 6, 9, 9, 2, 7, 3, 5, 8, 2, 9, 3, 3,
                5, 14, 5, 6, 6, 2, 8, 10, 7, 3, 12, 7, 6, 4, 1, 2, 9, 5, 4, 17, 2, 1, 8, 1, 9 }, new long[] { 19, 12 });
        runTest(new int[] { 4, 6, 1, 2, 200000, 1, 3, 200000, 1, 4, 1000000, 2, 3, 1000000, 2, 4, 200000, 3, 4, 200000,
                6, 1, 2, 1, 3, 1, 4, 2, 3, 2, 4, 3, 4 },
                new long[] { 200000, 200000, 400000, 1000000, 200000, 200000 });
        runTest(new int[] { 8, 5, 7, 6, 6, 1, 4, 9, 3, 5, 6, 1, 2, 7, 3, 8, 15, 2, 1, 4, 3, 8 }, new long[] { 9, 15 });
        runTest(new int[] { 18, 36, 1, 2, 1838, 1, 3, 793, 2, 4, 1166, 2, 1, 1838, 3, 1, 793, 3, 5, 1009, 4, 6, 1019, 4,
                2, 1166, 4, 7, 412, 4, 8, 1118, 5, 9, 1627, 5, 3, 1009, 6, 10, 223, 6, 11, 1941, 6, 4, 1019, 6, 12,
                3220, 7, 4, 412, 7, 13, 1303, 8, 4, 1118, 8, 14, 1897, 9, 15, 479, 9, 16, 806, 9, 5, 1627, 9, 17, 1582,
                10, 18, 412, 10, 6, 223, 10, 12, 3361, 11, 6, 1941, 12, 10, 3361, 12, 6, 3220, 13, 7, 1303, 14, 8, 1897,
                15, 9, 479, 16, 9, 806, 17, 9, 1582, 18, 10, 412, 10, 2, 17, 10, 8, 6, 8, 11, 13, 16, 2, 15, 14, 15, 8,
                6, 5, 7, 1, 17, 11 }, new long[] { 6849, 2360, 2137, 4675, 6073, 9927, 8030, 5825, 3416, 10975 });
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

        int maxNumNodes = 100;
        int maxWidth = 1000000;
        int numTests = 10000;
        stressTest(maxNumNodes, maxWidth, numTests);
        runComparisons(maxNumNodes, maxWidth, numTests);
    }

    static void runComparisons(int maxNumNodes, int maxWidth, int numTests) {
        int i = 15;
        while (i-- > 0) {
            int seed = (int) System.currentTimeMillis();
            stressCompare(seed, maxNumNodes, maxWidth, numTests, Dijkstra.class);
            stressCompare(seed, maxNumNodes, maxWidth, numTests, Dijkstra2.class);
        }
    }

    static <T extends GraphSolver> void stressCompare(int seed, int maxNumNodes, int maxGraphWidth, int numTests,
            Class<T> type) {
        Random r = new Random(seed);
        long startTime = System.currentTimeMillis();
        GraphSolver solver = new Instantiator<T>().getInstance(type);
        for (int i = 0; i < numTests; i++) {
            System.out.print(type.getName() + "elapsed: %" + (double) 100 * i / numTests + "\r");
            int[] data = generateData(maxNumNodes, maxGraphWidth, r);
            solver.preprocess(new ArrayScanner(data));

            int queries = nextInt(data[0], r);
            for (int j = 0; j < queries; j++) {
                int u = nextInt(data[0], r);
                int v = nextInt(data[0], r);
                try {
                    solver.distance(u, v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println(type.getName() + " total time (ms): " + (endTime - startTime));
    }

    static void runTest(int[] data, long[] expected) {
        DataScanner in = new ArrayScanner(data);
        Graph g = parseGraph(in);
        TwoWayGraph g2 = TwoWayGraph.fromGraph(g);
        Dijkstra d = new Dijkstra();
        d.preprocess(g);
        Dijkstra2 d2 = new Dijkstra2();
        d2.preprocess(g2);
        Query[] queries = parseQueries(in);
        String expectedString = Arrays.toString(expected);
        boolean allTechniquesWork = evaluate(d, queries, expectedString);
        allTechniquesWork &= evaluate(d2, queries, expectedString);
        if (!allTechniquesWork) {
            System.out.println("Queries: " + Arrays.toString(queries));
            System.out.println("Graph: " + g);
        }
    }

    static boolean evaluate(GraphSolver solver, Query[] queries, String expectedString) {
        long[] actual = respondToQueries(solver, queries);
        String actualString = Arrays.toString(actual);
        boolean worked = actualString.equals(expectedString);
        if (!worked) {
            System.out.println("Unexpected result for technique " + solver.getClass().getName());
            System.out.println("Expected: " + expectedString + ", but got " + actualString);
        }
        return worked;
    }

    static void stressTest(int maxNumNodes, int maxGraphWidth, int numTests) {
        Random r = new Random();
        Dijkstra d = new Dijkstra();
        Dijkstra2 d2 = new Dijkstra2();

        for (int i = 0; i < numTests; i++) {
            System.out.print("Stress tests: %" + (double) 100 * i / numTests + "\r");
            int[] data = generateData(maxNumNodes, maxGraphWidth, r);
            int n = data[0];
            Graph g = parseGraph(data);
            TwoWayGraph g2 = TwoWayGraph.fromGraph(g);

            d.preprocess(g);
            d2.preprocess(g2);

            boolean anyMistakes = false;
            int queries = nextInt(n, r);
            for (int j = 0; j < queries; j++) {
                int u = nextInt(n, r);
                int v = nextInt(n, r);
                long expected = d.distance(u, v);
                try {
                    long actual_d2 = d2.distance(u, v);
                    if (expected != actual_d2) {
                        anyMistakes = true;
                        System.out.println("\n\n\n\nUnexpected distance during test run " + i);
                        System.out.println("for nodes " + u + " to " + v);
                        System.out.println("expected " + expected);
                        System.out.println("dijkstra2 " + actual_d2);
                    }
                } catch (Exception e) {
                    anyMistakes = true;
                    System.out.println("\n\n\n\nError thrown during test run " + i);
                    System.out.println("for nodes " + u + " to " + v);
                    System.out.println("expected " + expected);
                    e.printStackTrace();
                }
            }

            if (anyMistakes) {
                System.out.println("\nn: " + data[0] + ", m: " + data[1]);
                System.out.println(Arrays.toString(data));
                System.out.println(g);
            }
        }
    }

    public interface GraphSolver {
        long distance(int u, int v);

        void preprocess(DataScanner in);
    }

    static class Dijkstra implements GraphSolver {
        Graph g;
        long[] dist;
        PriorityQueue<Node> h;

        public void preprocess(DataScanner in) {
            preprocess(parseGraph(in));
        }

        void preprocess(Graph g) {
            this.g = g;
            dist = new long[g.s];
            h = new PriorityQueue<Node>(g.s);
        }

        public long distance(int s, int t) {
            if (s == t)
                return 0l;
            clear();
            visit(s, 0);
            while (!h.isEmpty()) {
                Node u = h.poll();
                if (u.nodeId == t)
                    return dist[t];
                process(u.nodeId);
            }

            return dist[t];
        }

        void clear() {
            Arrays.fill(dist, -1);
            h.clear();
        }

        void process(int u) {
            ArrayList<Integer> neighbors = g.adj[u];
            ArrayList<Integer> weights = g.cost[u];
            for (int i = 0; i < neighbors.size(); i++) {
                int nodeId = neighbors.get(i);
                int weight = weights.get(i);
                visit(nodeId, dist[u] + weight);
            }
        }

        void visit(int nodeId, long distance) {
            long oldDist = dist[nodeId];
            if (oldDist == -1 || oldDist > distance) {
                dist[nodeId] = distance;
                h.add(new Node(nodeId, distance));
            }
        }
    }

    static class Dijkstra2 implements GraphSolver {
        TwoWayGraph g;
        long[][] dist;
        PriorityQueue<Node>[] h;
        boolean[][] visited;
        ArrayList<Integer> workset;

        public void preprocess(DataScanner in) {
            preprocess(parseTwoWayGraph(in));
        }

        public void preprocess(TwoWayGraph g) {
            this.g = g;
            dist = new long[][] { new long[g.s], new long[g.s] };
            h = (PriorityQueue<Node>[]) new PriorityQueue[] { new PriorityQueue<Node>(g.s),
                    new PriorityQueue<Node>(g.s) };
            visited = new boolean[][] { new boolean[g.s], new boolean[g.s] };
            workset = new ArrayList<Integer>();
        }

        public long distance(int s, int t) {
            if (s == t)
                return 0l;
            clear();
            visit(0, s, 0);
            visit(1, t, 0);

            while (!h[0].isEmpty() && !h[1].isEmpty()) {
                Node v = h[0].poll();
                process(0, v.nodeId);
                if (visited[1][v.nodeId])
                    return shortestPath();
                visited[0][v.nodeId] = true;

                Node v_r = h[1].poll();
                process(1, v_r.nodeId);
                if (visited[0][v_r.nodeId])
                    return shortestPath();
                visited[1][v_r.nodeId] = true;
            }

            return -1l;
        }

        void clear() {
            Arrays.fill(dist[0], -1);
            Arrays.fill(dist[1], -1);
            h[0].clear();
            h[1].clear();
            Arrays.fill(visited[0], false);
            Arrays.fill(visited[1], false);
            workset.clear();
        }

        void process(int side, int u) {
            ArrayList<Integer> neighbors = g.adj[side][u];
            ArrayList<Integer> weights = g.cost[side][u];
            for (int i = 0; i < neighbors.size(); i++) {
                int nodeId = neighbors.get(i);
                int weight = weights.get(i);
                visit(side, nodeId, dist[side][u] + weight);
            }
        }

        void visit(int side, int nodeId, long distance) {
            long oldDist = dist[side][nodeId];
            if (oldDist == -1 || oldDist > distance) {
                dist[side][nodeId] = distance;
                h[side].add(new Node(nodeId, distance));
                workset.add(nodeId);
            }
        }

        long shortestPath() {
            long distance = -1l;
            for (int u : workset) {
                long f = dist[0][u];
                if (f == -1)
                    continue;
                long r = dist[1][u];
                if (r == -1)
                    continue;

                if (distance == -1 || (f + r) < distance) {
                    distance = f + r;
                }
            }
            return distance;
        }
    }

    static class ContractionHierarchy implements GraphSolver {
        ContractionGraph g;
        long[][] dist;
        PriorityQueue<Node>[] h;
        boolean[][] visited;

        @Override
        public void preprocess(DataScanner in) {
            preprocess(parseContractionGraph(in));
        }

        void preprocess(ContractionGraph g) {
            this.g = g;
            dist = new long[][] { new long[g.s], new long[g.s] };
            h = (PriorityQueue<Node>[]) new PriorityQueue[] { new PriorityQueue<Node>(g.s),
                    new PriorityQueue<Node>(g.s) };
            visited = new boolean[][] { new boolean[g.s], new boolean[g.s] };

            PriorityQueue<ImportantNode> q = createImportantNodes();
            while (!q.isEmpty()) {
                ImportantNode v = q.poll();
                ArrayList<Shortcut> shortcuts = contract(v);
                if (q.isEmpty() || v.importance < q.peek().importance) {
                    finalizeNode(v, shortcuts);
                } else
                    q.add(v);
            }
        }

        PriorityQueue<ImportantNode> createImportantNodes() {
            PriorityQueue<ImportantNode> q = new PriorityQueue<ImportantNode>(g.s);
            for (int i = 0; i < g.s; i++)
                q.add(new ImportantNode(i, 0));
            return q;
        }

        ArrayList<Shortcut> contract(ImportantNode n) {
            int v = n.nodeId;
            ArrayList<Shortcut> shortcuts = new ArrayList<Shortcut>();
            HashSet<Integer> shortcutCover = new HashSet<Integer>();
            ArrayList<Integer> incoming = g.adj[1][v];
            ArrayList<Integer> incomingCost = g.cost[1][v];
            ArrayList<Integer> outgoing = g.adj[0][v];
            ArrayList<Integer> outgoingCost = g.adj[1][v];
            int successorLimit = calculateSuccessorLimit(outgoing, outgoingCost);
            if (!incoming.isEmpty() && !outgoing.isEmpty()) {
                for (int i = 0; i < incoming.size(); i++) {
                    int u = incoming.get(i);
                    int uCost = incomingCost.get(i);
                    // uCost + successorLimit = (l(u,v) + l(v, w) - l(w', w))
                    witnessSearch(v, u, uCost + successorLimit);
                    for (int j = 0; j < outgoing.size(); j++) {
                        int w = outgoing.get(i);
                        int wCost = outgoing.get(i);
                        if (!foundWitness(uCost, w, wCost)) {
                            shortcuts.add(new Shortcut(u, w, uCost + wCost));
                            shortcutCover.add(u);
                            shortcutCover.add(w);
                        }
                    }
                }
            }

            n.importance = (shortcuts.size() - g.getIncomingEdges(v) - g.getOutgoingEdges(v))
                    + g.getContractedNeighbors(v) + shortcutCover.size() + g.getNodeLevel(v);
            return shortcuts;
        }
        
        int calculateSuccessorLimit(ArrayList<Integer> adj, ArrayList<Integer> cost) {
            // calculate max l(v, w) - l(w', w);
            int max = 0;
            for (int i = 0; i < adj.size(); i++) {
                int w = adj.get(i);
                int wc = cost.get(i);
                ArrayList<Integer> wCost = g.cost[1][w];
                for (int j = 0; j < wCost.size(); j++) {
                    int wpc = wCost.get(j);
                    max = Math.max(max, wc - wpc);
                }
            }

            return max;
        }
        
        void witnessSearch(int v, int u, int limit) {
            // run dijkstra, limiting distance and/or number of hops
            clear();
            int hops = 5;
            h[0].add(new Node(u, 0));
            dist[0][u] = 0;
            while (hops-- > 0 && !h[0].isEmpty()) {
                Node n = h[0].poll();
                if (n.distance > limit)
                    return;
                    witnessProcess(n.nodeId, v);
            }
        }

        void witnessProcess(int u, int v) {
            ArrayList<Integer> neighbors = g.adj[0][u];
            ArrayList<Integer> weights = g.cost[0][u];
            for (int i = 0; i < neighbors.size(); i++) {
                int nodeId = neighbors.get(i);
                // witness paths may not pass through v
                if (nodeId == v)
                    continue;
                int weight = weights.get(i);
                visit(0, nodeId, dist[0][u] + weight);
            }
        }
        
        boolean foundWitness(int uCost, int w, int wCost) {
            // witness path found for w if, for some w' predecessor of w: d(u, w') + l(w', w) <= l(u, v) + l(v, w)
            ArrayList<Integer> pre = g.adj[1][w];
            ArrayList<Integer> cost = g.cost[1][w];
            for (int i = 0; i < pre.size(); i++) {
                int wp = pre.get(i);
                int wpCost = cost.get(i);
                if (dist[0][wp] == -1)
                    continue;
                if (dist[0][wp] + wpCost <= uCost + wCost) return true;
            }

            return false;
        }

        void finalizeNode(ImportantNode n, ArrayList<Shortcut> shortcuts) {
            g.updateImportance(n.nodeId, n.importance);
            g.updateNeighborNodeLevels(n.nodeId);
            g.commitShortcuts(n.nodeId, shortcuts);
        }

        @Override
        public long distance(int u, int v) {
            if (u == v)
                return 0;
            clear();
            // TODO - run a bidirectional dijkstra search using the augmented graph
            // TODO - termination condition is not the same as traditional bidirectional dijkstra
            return 0;
        }
        
        void clear() {
            Arrays.fill(dist[0], -1);
            Arrays.fill(dist[1], -1);
            h[0].clear();
            h[1].clear();
            Arrays.fill(visited[0], false);
            Arrays.fill(visited[1], false);
            // workset.clear();
        }

        void process(int side, int u) {
            ArrayList<Integer> neighbors = g.adj[side][u];
            ArrayList<Integer> weights = g.cost[side][u];
            for (int i = 0; i < neighbors.size(); i++) {
                int nodeId = neighbors.get(i);
                int weight = weights.get(i);
                visit(side, nodeId, dist[side][u] + weight);
            }
        }

        void visit(int side, int nodeId, long distance) {
            long oldDist = dist[side][nodeId];
            if (oldDist == -1 || oldDist > distance) {
                dist[side][nodeId] = distance;
                h[side].add(new Node(nodeId, distance));
                // workset.add(nodeId);
            }
        }

    }

    static Graph parseGraph(int[] data) {
        ArrayScanner in = new ArrayScanner(data);
        return parseGraph(in);
    }

    static Graph parseGraph(DataScanner in) {
        int n = in.nextInt();
        int m = in.nextInt();
        Graph g = new Graph(n);
        int x, y, c;
        for (int i = 0; i < m; i++) {
            x = in.nextInt();
            y = in.nextInt();
            c = in.nextInt();
            g.addEdge(x - 1, y - 1, c);
        }
        return g;
    }

    static TwoWayGraph parseTwoWayGraph(DataScanner in) {
        int n = in.nextInt();
        int m = in.nextInt();
        TwoWayGraph g = new TwoWayGraph(n);
        int x, y, c;
        for (int i = 0; i < m; i++) {
            x = in.nextInt();
            y = in.nextInt();
            c = in.nextInt();
            g.addEdge(x - 1, y - 1, c);
        }
        return g;
    }

    static ContractionGraph parseContractionGraph(DataScanner in) {
        int n = in.nextInt();
        int m = in.nextInt();
        ContractionGraph g = new ContractionGraph(n);
        int x, y, c;
        for (int i = 0; i < m; i++) {
            x = in.nextInt();
            y = in.nextInt();
            c = in.nextInt();
            g.addEdge(x - 1, y - 1, c);
        }
        return g;
    }

    static Query[] parseQueries(DataScanner in) {
        int t = in.nextInt();
        Query[] queries = new Query[t];
        int u, v;
        for (int i = 0; i < t; i++) {
            u = in.nextInt();
            v = in.nextInt();
            queries[i] = new Query(u - 1, v - 1);
        }
        return queries;
    }

    static void respondToQueries(GraphSolver solver, DataScanner in) {
        int t = in.nextInt();
        int u, v;
        for (int i = 0; i < t; i++) {
            u = in.nextInt();
            v = in.nextInt();
            System.out.println(solver.distance(u - 1, v - 1));
        }
    }

    static long[] respondToQueries(GraphSolver solver, Query[] queries) {
        long[] results = new long[queries.length];
        Query q;
        for (int i = 0; i < queries.length; i++) {
            q = queries[i];
            results[i] = solver.distance(q.u, q.v);
        }
        return results;
    }

    static int[] generateData(int maxNumNodes, int maxGraphWidth, Random r) {
        int n = nextInt(maxNumNodes, r) + 1;
        int m = nextInt(n * n, r);
        return fillEdges(n, m, maxGraphWidth, r);
    }

    static int[] fillEdges(int n, int m, int maxGraphWidth, Random r) {
        int[] data = new int[n * 2 + m * 3 + 2];
        data[0] = n;
        data[1] = m;
        for (int i = 0; i < m; i++) {
            int j = i * 3 + 2;
            data[j] = nextInt(n, r) + 1;
            data[j + 1] = nextInt(n, r) + 1;
            data[j + 2] = nextInt(maxGraphWidth, r) + 1;
        }
        return data;
    }

    static int nextInt(int bound, Random r) {
        return Math.abs(r.nextInt(bound));
    }

    static class Graph {
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] cost;
        int s;

        Graph(int s) {
            adj = constructList(s);
            cost = constructList(s);
            this.s = s;
        }

        ArrayList<Integer>[] constructList(int s) {
            ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[s];
            for (int i = 0; i < s; i++) {
                adj[i] = new ArrayList<Integer>();
            }
            return adj;
        }

        void addEdge(int i, int j, int w) {
            adj[i].add(j);
            cost[i].add(w);
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();

            for (int i = 0; i < adj.length; i++) {
                ArrayList<Integer> edges = adj[i];
                ArrayList<Integer> weights = cost[i];
                s.append("\nnode " + i + ": ");
                s.append("\nadjacencies: " + Arrays.toString(edges.toArray()));
                s.append("\nweights: " + Arrays.toString(weights.toArray()));
            }

            return s.toString();
        }
    }

    static class TwoWayGraph {
        ArrayList<Integer>[][] adj;
        ArrayList<Integer>[][] cost;
        int s;

        TwoWayGraph(int s) {
            adj = constructTwoWayList(s);
            cost = constructTwoWayList(s);
            this.s = s;
        }

        ArrayList<Integer>[][] constructTwoWayList(int s) {
            ArrayList<Integer>[][] adj = (ArrayList<Integer>[][]) new ArrayList[2][];
            adj[0] = constructList(s);
            adj[1] = constructList(s);
            return adj;
        }

        ArrayList<Integer>[] constructList(int s) {
            ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[s];
            for (int i = 0; i < s; i++) {
                adj[i] = new ArrayList<Integer>();
            }
            return adj;
        }

        void addEdge(int i, int j, int w) {
            adj[0][i].add(j);
            cost[0][i].add(w);
            adj[1][j].add(i);
            cost[1][j].add(w);
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();

            for (int i = 0; i < adj[0].length; i++) {
                ArrayList<Integer> edges = adj[0][i];
                ArrayList<Integer> weights = cost[0][i];
                s.append("\nnode " + i + ": ");
                s.append("\nadjacencies: " + Arrays.toString(edges.toArray()));
                s.append("\nweights: " + Arrays.toString(weights.toArray()));
            }

            return s.toString();
        }

        static TwoWayGraph fromGraph(Graph g) {
            TwoWayGraph g2 = new TwoWayGraph(g.s);
            for (int u = 0; u < g.s; u++) {
                ArrayList<Integer> adj = g.adj[u];
                ArrayList<Integer> cost = g.cost[u];
                for (int j = 0; j < adj.size(); j++) {
                    int v = adj.get(j);
                    int c = cost.get(j);
                    g2.addEdge(u, v, c);
                }
            }
            return g2;
        }
    }

    static class ContractionGraph {
        ArrayList<Integer>[][] adj;
        ArrayList<Integer>[][] cost;
        int[] contractedNeighbors;
        boolean[] isContracted;
        int[] nodeLevel;
        int[] importance;
        int s;

        ContractionGraph(int s) {
            adj = constructTwoWayList(s);
            cost = constructTwoWayList(s);
            contractedNeighbors = new int[s];
            isContracted = new boolean[s];
            nodeLevel = new int[s];
            importance = new int[s];
            this.s = s;
        }

        ArrayList<Integer>[][] constructTwoWayList(int s) {
            ArrayList<Integer>[][] adj = (ArrayList<Integer>[][]) new ArrayList[2][];
            adj[0] = constructList(s);
            adj[1] = constructList(s);
            return adj;
        }

        ArrayList<Integer>[] constructList(int s) {
            ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[s];
            for (int i = 0; i < s; i++) {
                adj[i] = new ArrayList<Integer>();
            }
            return adj;
        }

        void addEdge(int i, int j, int w) {
            // TODO - ignore self edges
            // TODO - ignore bigger edges
            adj[0][i].add(j);
            cost[0][i].add(w);
            adj[1][j].add(i);
            cost[1][j].add(w);
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();

            for (int i = 0; i < adj[0].length; i++) {
                ArrayList<Integer> edges = adj[0][i];
                ArrayList<Integer> weights = cost[0][i];
                s.append("\nnode " + i + ": ");
                s.append("\nadjacencies: " + Arrays.toString(edges.toArray()));
                s.append("\nweights: " + Arrays.toString(weights.toArray()));
            }

            return s.toString();
        }

        static ContractionGraph fromGraph(Graph g) {
            ContractionGraph g2 = new ContractionGraph(g.s);
            for (int u = 0; u < g.s; u++) {
                ArrayList<Integer> adj = g.adj[u];
                ArrayList<Integer> cost = g.cost[u];
                for (int j = 0; j < adj.size(); j++) {
                    int v = adj.get(j);
                    int c = cost.get(j);
                    g2.addEdge(u, v, c);
                }
            }
            return g2;
        }

        int getIncomingEdges(int nodeId) {
            return adj[1][nodeId].size();
        }

        int getOutgoingEdges(int nodeId) {
            return adj[0][nodeId].size();
        }

        int getContractedNeighbors(int nodeId) {
            return contractedNeighbors[nodeId];
        }

        int getNodeLevel(int nodeId) {
            return nodeLevel[nodeId];
        }

        void updateImportance(int nodeId, int importance) {
            this.importance[nodeId] = importance;
        }

        void updateNeighborNodeLevels(int nodeId) {
            int level = nodeLevel[nodeId] + 1;
            updateNeighborNodeLevels(adj[0][nodeId], level);
            updateNeighborNodeLevels(adj[1][nodeId], level);
        }
        
        void updateNeighborNodeLevels(ArrayList<Integer> neighbors, int level) {
            for (int i = 0; i < neighbors.size(); i++) {
                int nodeId = neighbors.get(i);
                nodeLevel[nodeId] = Math.max(nodeLevel[nodeId], level);
            }
        }

        void commitShortcuts(int nodeId, ArrayList<Shortcut> shortcuts) {
            // TODO - for each shortcut around nodeId, add shortcut edge, delete intermediate edges
        }
    }

    static class Node implements Comparable<Node> {
        int nodeId;
        long distance;

        Node(int nodeId, long distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "{ nodeId: " + nodeId + ", distance: " + distance + " }";
        }

        @Override
        public int compareTo(Node o) {
            return distance < o.distance ? -1 : distance > o.distance ? 1 : 0;
        }
    }

    static class ImportantNode implements Comparable<ImportantNode> {
        int nodeId;
        int importance;

        ImportantNode(int nodeId, int importance) {
            this.nodeId = nodeId;
            this.importance = importance;
        }

        @Override
        public int compareTo(ImportantNode o) {
            return importance < o.importance ? -1 : importance > o.importance ? 1 : 0;
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
            return "{ u: " + u + ", v: " + v + " }";
        }
    }

    static class Shortcut {
        int u, v, c;

        Shortcut(int u, int v, int c){
            this.u = u;
            this.v = v;
            this.c = c;
        }
    }

    public interface DataScanner {
        int nextInt();

        void close();
    }

    static class StreamScanner implements DataScanner {
        Scanner in;

        StreamScanner() {
            in = new Scanner(System.in);
        }

        public int nextInt() {
            return in.nextInt();
        }

        public void close() {
            in.close();
        }
    }

    static class ArrayScanner implements DataScanner {
        int i = 0;
        int[] data;

        ArrayScanner(int[] data) {
            this.data = data;
        }

        public int nextInt() {
            return data[i++];
        }

        public void close() {
        }
    }

    static class Instantiator<T> {
        T getInstance(Class<T> type) {
            try {
                return type.getDeclaredConstructor().newInstance(new Object[0]);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    // public static void main(String args[]) {
    // Scanner in = new Scanner(System.in);
    // int n = in.nextInt();
    // int m = in.nextInt();
    // Impl ch = new Impl(n);
    // @SuppressWarnings("unchecked")
    // ArrayList<Integer>[][] tmp1 = (ArrayList<Integer>[][])new ArrayList[2][];
    // ch.adj = tmp1;
    // @SuppressWarnings("unchecked")
    // ArrayList<Long>[][] tmp2 = (ArrayList<Long>[][])new ArrayList[2][];
    // ch.cost = tmp2;
    // for (int side = 0; side < 2; ++side) {
    // @SuppressWarnings("unchecked")
    // ArrayList<Integer>[] tmp3 = (ArrayList<Integer>[])new ArrayList[n];
    // ch.adj[side] = tmp3;
    // @SuppressWarnings("unchecked")
    // ArrayList<Long>[] tmp4 = (ArrayList<Long>[])new ArrayList[n];
    // ch.cost[side] = tmp4;
    // for (int i = 0; i < n; i++) {
    // ch.adj[side][i] = new ArrayList<Integer>();
    // ch.cost[side][i] = new ArrayList<Long>();
    // }
    // }

    // for (int i = 0; i < m; i++) {
    // int x, y;
    // Long c;
    // x = in.nextInt();
    // y = in.nextInt();
    // c = in.nextLong();
    // ch.adj[0][x - 1].add(y - 1);
    // ch.cost[0][x - 1].add(c);
    // ch.adj[1][y - 1].add(x - 1);
    // ch.cost[1][y - 1].add(c);
    // }

    // ch.preprocess();
    // System.out.println("Ready");

    // int t = in.nextInt();

    // for (int i = 0; i < t; i++) {
    // int u, v;
    // u = in.nextInt();
    // v = in.nextInt();
    // System.out.println(ch.query(u-1, v-1));
    // }
    // }

    // private static class Impl {
    // // See the descriptions of these fields in the starter for friend_suggestion
    // int n;
    // ArrayList<Integer>[][] adj;
    // ArrayList<Long>[][] cost;
    // Long[][] distance;
    // ArrayList<PriorityQueue<Entry>> queue;
    // boolean[] visited;
    // ArrayList<Integer> workset;
    // final Long INFINITY = Long.MAX_VALUE / 4;

    // // Position of the node in the node ordering
    // Integer[] rank;
    // // Level of the node for level heuristic in the node ordering
    // Long[] level;

    // Impl(int n) {
    // this.n = n;
    // visited = new boolean[n];
    // Arrays.fill(visited, false);
    // workset = new ArrayList<Integer>();
    // rank = new Integer[n];
    // level = new Long[n];
    // distance = new Long[][] {new Long[n], new Long[n]};
    // for (int i = 0; i < n; ++i) {
    // distance[0][i] = distance[1][i] = INFINITY;
    // level[i] = 0L;
    // rank[i] = 0;
    // }
    // queue = new ArrayList<PriorityQueue<Entry>>();
    // queue.add(new PriorityQueue<Entry>(n));
    // queue.add(new PriorityQueue<Entry>(n));
    // }

    // // Preprocess the graph
    // void preprocess() {
    // // This priority queue will contain pairs (importance, node) with the least
    // important node in the head
    // PriorityQueue<Entry> q = new PriorityQueue<Entry>(n);
    // // Implement this method yourself
    // }

    // void add_edge(int side, int u, int v, Long c) {
    // for (int i = 0; i < adj[side][u].size(); ++i) {
    // int w = adj[side][u].get(i);
    // if (w == v) {
    // Long cc = min(cost[side][u].get(i), c);
    // cost[side][u].set(i, cc);
    // return;
    // }
    // }
    // adj[side][u].add(v);
    // cost[side][u].add(c);
    // }

    // void apply_shortcut(Shortcut sc) {
    // add_edge(0, sc.u, sc.v, sc.cost);
    // add_edge(1, sc.v, sc.u, sc.cost);
    // }

    // void clear() {
    // for (int v : workset) {
    // distance[0][v] = distance[1][v] = INFINITY;
    // visited[v] = false;
    // }
    // workset.clear();
    // queue.get(0).clear();
    // queue.get(1).clear();
    // }

    // void mark_visited(int u) {
    // visited[u] = true;
    // workset.add(u);
    // }

    // // See the description of this method in the starter for friend_suggestion
    // boolean visit(int side, int v, Long dist) {
    // // Implement this method yourself
    // return false;
    // }

    // // Add the shortcuts corresponding to contracting node v. Return v's
    // importance.
    // Long shortcut(int v) {
    // // Implement this method yourself

    // // Compute the node importance in the end
    // Long shortcuts = 0;
    // Long vlevel = 0L;
    // Long neighbors = 0L;
    // Long shortcutCover = 0L;
    // // Compute the correct values for the above heuristics before computing the
    // node importance
    // Long importance = (shortcuts - adj[0][v].size() - adj[1][v].size()) +
    // neighbors + shortcutCover + vlevel;
    // return importance;
    // }

    // // Returns the distance from s to t in the graph
    // Long query(int s, int t) {
    // if (s == t) {
    // return 0L;
    // }
    // visit(0, s, 0L);
    // visit(1, t, 0L);
    // Long estimate = INFINITY;
    // // Implement the rest of the algorithm yourself
    // return estimate == INFINITY ? -1 : estimate;
    // }

    // class Entry implements Comparable<Entry>
    // {
    // Long cost;
    // int node;

    // public Entry(Long cost, int node)
    // {
    // this.cost = cost;
    // this.node = node;
    // }

    // public int compareTo(Entry other)
    // {
    // if (cost == other.cost) {
    // return node < other.node ? -1 : node > other.node ? 1: 0;
    // }
    // return cost < other.cost ? -1 : cost > other.cost ? 1 : 0;
    // }
    // }

    // class Shortcut
    // {
    // int u;
    // int v;
    // Long cost;

    // public Shortcut(int u, int v, Long c)
    // {
    // this.u = u;
    // this.v = v;
    // cost = c;
    // }
    // }
    // }
}
