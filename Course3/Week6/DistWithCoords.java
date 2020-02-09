import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;
import java.lang.Math;
import java.lang.reflect.InvocationTargetException;

public class DistWithCoords {
    public static void main(String args[]) {
        runSolution();
        // testSolution();
    }

    static void runSolution() {
        DataScanner in = new StreamScanner();
        Graph g = parseData(in);
        AStarPQ a = new AStarPQ();
        a.preprocess(g);
        respondToQueries(a, in);
        in.close();
    }

    static void testSolution() {
        runTest(new int[] { 2, 1, 0, 0, 0, 1, 1, 2, 1, 4, 1, 1, 2, 2, 1, 2, 2, 1 }, new long[] { 0, 0, 1, -1 });
        runTest(new int[] { 4, 4, 0, 0, 0, 1, 2, 1, 2, 0, 1, 2, 1, 4, 1, 2, 2, 3, 2, 1, 3, 6, 1, 1, 3 },
                new long[] { 3 });

        // somehow the order of edges matters
        // this test passes
        runTest(new int[] { 9, 16, 2, 2, 3, 2, 8, 5, 4, 4, 2, 3, 4, 6, 1, 6, 9, 6, 4, 4, 1, 2, 9, 1, 2, 10, 2, 8, 10, 2,
                9, 3, 3, 5, 8, 3, 5, 14, 5, 4, 17, 5, 6, 6, 7, 2, 5, 7, 3, 12, 7, 4, 10, 7, 6, 4, 7, 6, 9, 8, 2, 9, 9,
                2, 7, 9, 9, 7, 2, 1, 8, 1, 9 }, new long[] { 19, 12 });

        // this test is the same as the previous, with the edges in a different order
        // and it fails!
        // resolution: heap changePriority didn't actually change priority
        runTest(new int[] { 9, 16, 2, 2, 3, 2, 8, 5, 4, 4, 2, 3, 4, 6, 1, 6, 9, 6, 4, 4, 1, 2, 10, 8, 2, 9, 9, 9, 7, 7,
                2, 5, 7, 4, 10, 7, 6, 9, 9, 2, 7, 3, 5, 8, 2, 9, 3, 3, 5, 14, 5, 6, 6, 2, 8, 10, 7, 3, 12, 7, 6, 4, 1,
                2, 9, 5, 4, 17, 2, 1, 8, 1, 9 }, new long[] { 19, 12 });

        int maxNumNodes = 1000;
        int maxWidth = 1000;
        int numTests = 1000;
        // stressTest(maxNumNodes, maxWidth, numTests);
        int i = 15;
        while (i-- > 0) {
            int seed = (int) System.currentTimeMillis();
            // stressCompare(seed, maxNumNodes, maxWidth, numTests, FloydWarshall.class);
            stressCompare(seed, maxNumNodes, maxWidth, numTests, Dijkstra.class);
            stressCompare(seed, maxNumNodes, maxWidth, numTests, DijkstraPQ.class);
            stressCompare(seed, maxNumNodes, maxWidth, numTests, AStar.class);
            stressCompare(seed, maxNumNodes, maxWidth, numTests, AStarPQ.class);
        }
    }

    static void runTest(int[] data, long[] expected) {
        DataScanner in = new ArrayScanner(data);
        Graph g = parseData(in);
        FloydWarshall fw = new FloydWarshall();
        fw.preprocess(g);
        Dijkstra d = new Dijkstra();
        d.preprocess(g);
        DijkstraPQ dpq = new DijkstraPQ();
        dpq.preprocess(g);
        AStar a = new AStar();
        a.preprocess(g);
        AStarPQ apq = new AStarPQ();
        apq.preprocess(g);
        Query[] queries = parseQueries(in);
        String expectedString = Arrays.toString(expected);
        boolean allTechniquesWork = evaluate(fw, queries, expectedString);
        allTechniquesWork &= evaluate(d, queries, expectedString);
        allTechniquesWork &= evaluate(dpq, queries, expectedString);
        allTechniquesWork &= evaluate(a, queries, expectedString);
        allTechniquesWork &= evaluate(apq, queries, expectedString);
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

    static Graph parseData(int[] data) {
        ArrayScanner in = new ArrayScanner(data);
        return parseData(in);
    }

    static Graph parseData(DataScanner in) {
        int n = in.nextInt();
        int m = in.nextInt();
        Graph g = new Graph(n);
        int x;
        int y;
        for (int i = 0; i < n; i++) {
            x = in.nextInt();
            y = in.nextInt();
            g.setCoordinate(i, x, y);
        }
        int c;
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

    static void stressTest(int maxNumNodes, int maxGraphWidth, int numTests) {
        int reportEvery = numTests / 10;
        Random r = new Random();
        FloydWarshall fw = new FloydWarshall();
        Dijkstra d = new Dijkstra();
        DijkstraPQ dpq = new DijkstraPQ();
        AStar a = new AStar();
        AStarPQ apq = new AStarPQ();

        for (int i = 0; i < numTests; i++) {
            if (i > 0 && i % reportEvery == 0) {
                System.out.println("test run " + i);
            }
            int[] data = generateData(maxNumNodes, maxGraphWidth, r);
            int n = data[0];
            Graph g = parseData(data);

            fw.preprocess(g);
            d.preprocess(g);
            dpq.preprocess(g);
            a.preprocess(g);
            apq.preprocess(g);

            boolean anyMistakes = false;
            int queries = nextInt(n * n, r);
            for (int j = 0; j < queries; j++) {
                int u = nextInt(n, r);
                int v = nextInt(n, r);
                long expected = fw.distance(u, v);
                try {
                    long actual_d = d.distance(u, v);
                    long actual_dpq = dpq.distance(u, v);
                    long actual_a = a.distance(u, v);
                    long actual_apq = apq.distance(u, v);

                    if (expected != actual_d || expected != actual_a 
                        || expected != actual_dpq || expected != actual_apq) {
                        anyMistakes = true;
                        System.out.println("\nUnexpected distance during test run " + i);
                        System.out.println("for nodes " + u + " to " + v);
                        System.out.println("expected " + expected);
                        System.out.println("dijkstra " + actual_d);
                        System.out.println("dijkstra_pq " + actual_dpq);
                        System.out.println("astar " + actual_a);
                        System.out.println("astar_pq " + actual_apq);
                    }
                } catch (Exception e) {
                    anyMistakes = true;
                    System.out.println("\nError thrown during test run " + i);
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

    static int[] generateData(int maxNumNodes, int maxGraphWidth, Random r) {
        int n = nextInt(maxNumNodes, r) + 1;
        int m = nextInt(n * 2, r);
        return fillEdges(n, m, maxGraphWidth, r);
    }

    static int[] fillEdges(int n, int m, int maxGraphWidth, Random r) {
        int[] data = new int[n * 2 + m * 3 + 2];
        data[0] = n;
        data[1] = m;
        for (int i = 0; i < n; i++) {
            int j = i * 2 + 2;
            data[j] = nextInt(maxGraphWidth, r);
            data[j + 1] = nextInt(maxGraphWidth, r);
        }
        for (int i = 0; i < m; i++) {
            int j = n * 2 + i * 3 + 2;
            int extraWidth = nextInt(maxGraphWidth, r) + 1;
            data[j] = nextInt(n, r) + 1;
            data[j + 1] = nextInt(n, r) + 1;
            data[j + 2] = getDistance(data, data[j], data[j + 1]) + extraWidth;
        }
        return data;
    }

    static <T extends GraphSolver> void stressCompare(int seed, int maxNumNodes, int maxGraphWidth, int numTests,
            Class<T> type) {
        Random r = new Random(seed);
        long startTime = System.currentTimeMillis();
        GraphSolver solver = new Instantiator<T>().getInstance(type);
        for (int i = 0; i < numTests; i++) {
            System.out.print(type.getName() + "elapsed: %" + (double) 100 * i / numTests + "\r");
            int[] data = generateData(maxNumNodes, maxGraphWidth, r);
            int n = data[0];
            Graph g = parseData(data);
            solver.preprocess(g);

            int queries = nextInt(n, r);
            for (int j = 0; j < queries; j++) {
                int u = nextInt(n, r);
                int v = nextInt(n, r);
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

    static int nextInt(int bound, Random r) {
        return Math.abs(r.nextInt(bound));
    }

    static int getDistance(int[] data, int i, int j) {
        return euclidean(data[i * 2], data[i * 2 + 1], data[j * 2], data[j * 2 + 1]);
    }

    static int euclidean(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public interface GraphSolver {
        long distance(int u, int v);

        void preprocess(Graph g);
    }

    static class FloydWarshall implements GraphSolver {
        long[][] distances;
        Graph g;

        public void preprocess(Graph g) {
            distances = new long[g.s][g.s];

            for (int i = 0; i < g.s; i++) {
                Arrays.fill(distances[i], -1);
                ArrayList<Integer> adj = g.adj[i];
                ArrayList<Integer> cst = g.cost[i];
                for (int j = 0; j < adj.size(); j++) {
                    int n = adj.get(j);
                    int w = cst.get(j);
                    long old = distances[i][n];
                    distances[i][n] = old == -1 || w < old ? w : old;
                }
                distances[i][i] = 0;
            }

            for (int k = 0; k < g.s; k++) {
                for (int i = 0; i < g.s; i++) {
                    for (int j = 0; j < g.s; j++) {
                        long oldD = distances[i][j];
                        long ik = distances[i][k];
                        long kj = distances[k][j];

                        if (ik == -1 || kj == -1)
                            continue;
                        if (oldD == -1 || oldD > ik + kj)
                            distances[i][j] = ik + kj;
                    }
                }
            }
        }

        public long distance(int s, int t) {
            return distances[s][t];
        }
    }

    static class Dijkstra implements GraphSolver {
        Graph g;
        long[] dist;
        NodeHeap h;

        public void preprocess(Graph g) {
            this.g = g;
            dist = new long[g.s];
            h = new NodeHeap(g.s);
        }

        public long distance(int s, int t) {
            if (s == t)
                return 0l;
            clear();
            visit(s, 0);
            while (!h.isEmpty()) {
                Node u = h.extractMin();
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
                h.addOrUpdate(nodeId, distance);
            }
        }
    }

    static class DijkstraPQ implements GraphSolver {
        Graph g;
        long[] dist;
        PriorityQueue<Node> h;

        public void preprocess(Graph g) {
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

    static class AStar implements GraphSolver {
        Graph g;
        long[] dist;
        NodeHeap h;
        int[] heuristic;
        int t;

        public void preprocess(Graph g) {
            this.g = g;
            dist = new long[g.s];
            h = new NodeHeap(g.s);
            heuristic = new int[g.s];
        }

        public long distance(int s, int t) {
            if (s == t)
                return 0;
            this.t = t;
            clear();
            visit(s, 0, getPotential(s));
            while (!h.isEmpty()) {
                Node u = h.extractMin();
                if (u.nodeId == t)
                    return dist[u.nodeId];
                process(u.nodeId);
            }

            return dist[t];
        }

        void clear() {
            Arrays.fill(dist, -1);
            Arrays.fill(heuristic, -1);
            h.clear();
        }

        void process(int u) {
            ArrayList<Integer> neighbors = g.adj[u];
            ArrayList<Integer> weights = g.cost[u];
            for (int i = 0; i < neighbors.size(); i++) {
                int nodeId = neighbors.get(i);
                int weight = weights.get(i);
                visit(nodeId, dist[u] + weight, getPotential(nodeId));
            }
        }

        void visit(int nodeId, long distance, int potential) {
            long oldDist = dist[nodeId];
            if (oldDist == -1 || oldDist > distance) {
                dist[nodeId] = distance;
                h.addOrUpdate(nodeId, distance + potential);
            }
        }

        int getPotential(int i) {
            if (heuristic[i] == -1)
                heuristic[i] = euclidean(g.x[i], g.y[i], g.x[t], g.y[t]);
            return heuristic[i];
        }
    }

    static class AStarPQ implements GraphSolver {
        Graph g;
        long[] dist;
        PriorityQueue<Node> h;
        int[] heuristic;
        int t;

        public void preprocess(Graph g) {
            this.g = g;
            dist = new long[g.s];
            h = new PriorityQueue<Node>(g.s);
            heuristic = new int[g.s];
        }

        public long distance(int s, int t) {
            if (s == t)
                return 0;
            this.t = t;
            clear();
            visit(s, 0, getPotential(s));
            while (!h.isEmpty()) {
                Node u = h.poll();
                if (u.nodeId == t)
                    return dist[u.nodeId];
                process(u.nodeId);
            }

            return dist[t];
        }

        void clear() {
            Arrays.fill(dist, -1);
            Arrays.fill(heuristic, -1);
            h.clear();
        }

        void process(int u) {
            ArrayList<Integer> neighbors = g.adj[u];
            ArrayList<Integer> weights = g.cost[u];
            for (int i = 0; i < neighbors.size(); i++) {
                int nodeId = neighbors.get(i);
                int weight = weights.get(i);
                visit(nodeId, dist[u] + weight, getPotential(nodeId));
            }
        }

        void visit(int nodeId, long distance, int potential) {
            long oldDist = dist[nodeId];
            if (oldDist == -1 || oldDist > distance) {
                dist[nodeId] = distance;
                h.add(new Node(nodeId, distance + potential));
            }
        }

        int getPotential(int i) {
            if (heuristic[i] == -1)
                heuristic[i] = euclidean(g.x[i], g.y[i], g.x[t], g.y[t]);
            return heuristic[i];
        }
    }

    static class Graph {
        int[] x;
        int[] y;
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] cost;
        int s;

        Graph(int s) {
            x = new int[s];
            y = new int[s];
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

        void setCoordinate(int i, int xi, int yi) {
            x[i] = xi;
            y[i] = yi;
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
                s.append("\ncoordinates: { x: " + x[i] + ", y: " + y[i] + " }");
                s.append("\nadjacencies: " + Arrays.toString(edges.toArray()));
                s.append("\nweights: " + Arrays.toString(weights.toArray()));
            }

            return s.toString();
        }
    }

    static class NodeHeap {
        Node[] heap;
        int[] nodeMap;
        int numNodes;

        NodeHeap(int numNodes) {
            heap = new Node[numNodes];
            nodeMap = new int[numNodes];
            Arrays.fill(nodeMap, -1);
            this.numNodes = 0;
        }

        void clear() {
            numNodes = 0;
            Arrays.fill(nodeMap, -1);
            Arrays.fill(heap, null);
        }

        boolean isEmpty() {
            return numNodes == 0;
        }

        Node extractMin() {
            if (isEmpty())
                return null;
            Node n = removeNode(0);
            siftDown(0);
            return n;
        }

        void addOrUpdate(int nodeId, long distance) {
            if (nodeMap[nodeId] != -1)
                changePriority(nodeId, distance);
            else
                add(nodeId, distance);
        }

        void delete(int nodeId) {
            int index = nodeMap[nodeId];
            if (index == -1)
                return;
            removeNode(index);
            heapify(index);
        }

        void add(int nodeId, long distance) {
            numNodes++;
            int index = numNodes - 1;
            Node n = new Node(nodeId, distance);
            updateNodeIndex(n, index);
            siftUp(index);
        }

        void changePriority(int nodeId, long distance) {
            int index = nodeMap[nodeId];
            if (index == -1)
                return;
            Node n = heap[index];
            n.distance = distance;
            heapify(index);
        }

        Node removeNode(int index) {
            Node n = heap[index];
            nodeMap[n.nodeId] = -1;
            numNodes--;
            Node lastNode = heap[numNodes];
            if (numNodes != 0 && lastNode != n) {
                heap[numNodes] = null;
                updateNodeIndex(lastNode, index);
            }
            return n;
        }

        void heapify(int index) {
            if (rule(index))
                siftDown(index);
            else
                siftUp(index);
        }

        void siftUp(int i) {
            if (i == 0)
                return;
            int p = parent(i);
            while (i != 0 && !rule(p, i)) {
                swap(p, i);
                i = p;
                p = parent(i);
            }
        }

        void siftDown(int i) {
            int swapIndex = i;
            do {
                i = swapIndex;
                int l = left(i), r = right(i);

                if (l < numNodes && !rule(swapIndex, l))
                    swapIndex = l;

                if (r < numNodes && !rule(swapIndex, r))
                    swapIndex = r;

                if (swapIndex != i) {
                    swap(i, swapIndex);
                }
            } while (swapIndex != i);
        }

        void swap(int i, int j) {
            Node ni = heap[i];
            Node nj = heap[j];
            updateNodeIndex(ni, j);
            updateNodeIndex(nj, i);
        }

        void updateNodeIndex(Node n, int i) {
            heap[i] = n;
            if (n != null)
                nodeMap[n.nodeId] = i;
        }

        int parent(int i) {
            return (i - 1) / 2;
        }

        int left(int i) {
            return i * 2 + 1;
        }

        int right(int i) {
            return i * 2 + 2;
        }

        // when checking the rule for a single index, compare the element with its
        // parent
        // if it has no parent, the rule is satisifed
        boolean rule(int cIndex) {
            if (cIndex == 0)
                return true;
            return rule(parent(cIndex), cIndex);
        }

        // rule must be true for the heap property to be satisfied
        // in this heap, the node with the shortest overall distance is on top
        boolean rule(int pIndex, int cIndex) {
            Node p = heap[pIndex];
            Node c = heap[cIndex];
            if (p.distance == c.distance)
                return p.nodeId <= c.nodeId;
            return p.distance < c.distance;
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
        public int compareTo(Node other) {
            return distance < other.distance ? -1 : distance > other.distance ? 1 : 0;
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

    // private static class Impl {
    // // Number of nodes
    // int n;
    // // Coordinates of nodes
    // int[] x;
    // int[] y;
    // // See description of these fields in the starters for friend_suggestion
    // ArrayList<Integer>[][] adj;
    // ArrayList<Integer>[][] cost;
    // Long[][] distance;
    // ArrayList<PriorityQueue<Entry>> queue;
    // boolean[] visited;
    // ArrayList<Integer> workset;
    // final Long INFINITY = Long.MAX_VALUE / 4;

    // Impl(int n) {
    // this.n = n;
    // visited = new boolean[n];
    // x = new int[n];
    // y = new int[n];
    // Arrays.fill(visited, false);
    // workset = new ArrayList<Integer>();
    // distance = new Long[][] {new Long[n], new Long[n]};
    // for (int i = 0; i < n; ++i) {
    // distance[0][i] = distance[1][i] = INFINITY;
    // }
    // queue = new ArrayList<PriorityQueue<Entry>>();
    // queue.add(new PriorityQueue<Entry>(n));
    // queue.add(new PriorityQueue<Entry>(n));
    // }

    // // See the description of this method in the starters for friend_suggestion
    // void clear() {
    // for (int v : workset) {
    // distance[0][v] = distance[1][v] = infty;
    // visited[v] = false;
    // }
    // workset.clear();
    // queue.get(0).clear();
    // queue.get(1).clear();
    // }

    // // See the description of this method in the starters for friend_suggestion
    // void visit(int side, int v, Long dist) {
    // // Implement this method yourself
    // }

    // // Returns the distance from s to t in the graph.
    // Long query(int s, int t) {
    // clear();
    // visit(0, s, 0L);
    // visit(1, t, 0L);
    // // Implement the rest of the algorithm yourself

    // return -1;
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
    // return cost < other.cost ? -1 : cost > other.cost ? 1 : 0;
    // }
    // }
    // }
}
