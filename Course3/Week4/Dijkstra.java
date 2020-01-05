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
        runTest(new int[] { 5, 9, 1, 2, 4, 1, 3, 2, 2, 3, 2, 3, 2, 1, 2, 4, 2, 3, 5,
        4, 5, 4, 1, 2, 5, 3, 3, 4, 4, 1,
        5 }, 6);
        runTest(new int[] { 3, 3, 1, 2, 7, 1, 3, 5, 2, 3, 2, 3, 2 }, -1);
        runTest(new int[] { 1, 0, 1, 1 }, 0);
        runTest(new int[] { 2, 2, 1, 2, 500, 1, 2, 1, 1, 2 }, 1);
        runTest(new int[] { 3, 7, 2, 1, 5, 1, 3, 2, 3, 3, 9, 3, 3, 3, 1, 3, 1, 2, 3,
        8, 2, 1, 4, 2, 1 }, 4);
        runTest(new int[] { 3, 6, 1, 2, 9, 2, 1, 9, 2, 2, 9, 3, 1, 9, 2, 2, 8, 3, 1, 1, 3, 2 }, 10);
        runTest(new int[] { 4, 8, 1, 1, 6, 3, 4, 8, 2, 1, 7, 3, 2, 9, 1, 2, 3, 3, 2, 9, 3, 4, 4, 3, 2, 5, 3, 1 }, 12);
        runStressTest();
    }

    static void runTest(int[] data, long expected) {
        Graph g = processData(data);
        int s = data[data.length - 2] - 1;
        int t = data[data.length - 1] - 1;
        try {
            long actual = distance(g, s, t);
            complain(expected, g, data, actual);
        } catch (Exception e) {
            System.out.println("\nException thrown during static test: " + e);
            reportGraphAndExpected(data, g, expected);
        }
    }

    static void runStressTest() {
        int graphSize = 100;
        int numTests = 10000;
        Random r = new Random();

        for (int i = 0; i < numTests; i++) {
            int n = nextInt(graphSize, r) + 1;
            int m = nextInt(n * n, r);
            int[] data = new int[4 + m * 3];
            int s = nextInt(n, r);
            int t = nextInt(n, r);
            data[0] = n;
            data[1] = m;
            data[data.length - 2] = s + 1;
            data[data.length - 1] = t + 1;

            fillEdges(data, n, m, r);

            Graph g = processData(data);
            long expected = distanceNaive(g, s, t);
            try {
                long actual = distance(g, s, t);

                complain(expected, g, data, actual);

                // if (i % 1000 == 0) {
                //     System.out.println("\nTest run " + i);
                //     if (actual == expected)
                //     reportGraphAndExpected(data, g, expected);
                // }
            } catch (Exception e) {
                System.out.println("\nError thrown during test run " + i + ", " + e);
                reportGraphAndExpected(data, g, expected);
            }
        }
    }

    private static void reportGraphAndExpected(int[] data, Graph g, long expected) {
        System.out.println("raw data: " + Arrays.toString(data));
        System.out.println("graph: \n" + g);
        System.out.println("Expected: " + expected);
    }

    private static void complain(long expected, Graph g, int[] data, long actual) {
        if (actual != expected) {
            System.out.println("\nUnexpected distance in graph - got: " + actual);
            reportGraphAndExpected(data, g, expected);
        }
    }

    private static int nextInt(int bound, Random r) {
        return Math.abs(r.nextInt(bound));
    }

    static void fillEdges(int[] data, int n, int m, Random r) {
        int lengthBound = 10;
        for (int i = 0; i < m; i++) {
            int j = i * 3 + 2;
            data[j] = nextInt(n, r) + 1;
            data[j + 1] = nextInt(n, r) + 1;
            data[j + 2] = nextInt(lengthBound, r) + 1;
        }
    }

    static Graph processData(int[] data) {
        Graph g = new Graph(data[0]);
        for (int i = 2; i < data.length - 4; i += 3) {
            int x = data[i];
            int y = data[i + 1];
            int w = data[i + 2];
            g.addEdge(x, y, w);
        }
        return g;
    }

    static long distanceNaive(Graph g, int s, int t) {
        if (s == t)
            return 0;
        long[][] m = new long[g.s][g.s];

        for (int i = 0; i < g.s; i++) {
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

        for (int k = 0; k < g.s; k++) {
            for (int i = 0; i < g.s; i++) {
                for (int j = 0; j < g.s; j++) {
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

        return m[s][t];
    }

    static Long distance(Graph g, int s, int t) {
        if (s == t)
            return 0l;

        long[] dist = new long[g.s];
        Arrays.fill(dist, -1);
        int[] prev = new int[g.s];
        Arrays.fill(prev, -1);
        NodeHeap h = new NodeHeap(g.s);
        dist[s] = 0l;
        h.addOrUpdate(s, 0);
        while (!h.isEmpty()) {
            Node u = h.extractMin();
            ArrayList<Integer> neighbors = g.adj[u.nodeId];
            ArrayList<Integer> weights = g.cost[u.nodeId];
            for (int i = 0; i < neighbors.size(); i++) {
                int nodeId = neighbors.get(i);
                int weight = weights.get(i);
                long oldDist = dist[nodeId];
                long newDist = u.distance + weight;
                if (oldDist == -1 || oldDist > newDist) {
                    dist[nodeId] = newDist;
                    prev[nodeId] = u.nodeId;
                    h.addOrUpdate(nodeId, newDist);
                }
            }
        }

        return dist[t];
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

        void addEdge(int x, int y, int w) {
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
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

    static class NodeHeap {
        Node[] heap;
        int[] nodeMap; // tracks index of node in heap
        int numNodes;

        NodeHeap(int numNodes) {
            heap = new Node[numNodes];
            nodeMap = new int[numNodes];
            Arrays.fill(nodeMap, -1);
            this.numNodes = 0;
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

        void add(int nodeId, long distance) {
            numNodes++;
            int index = numNodes - 1;
            Node n = new Node(nodeId, distance);
            updateNodeIndex(n, index);
            siftUp(index);
        }

        void delete(int nodeId) {
            int index = nodeMap[nodeId];
            if (index == -1)
                return;
            removeNode(index);
            heapify(index);
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
            if (!rule(p, i)) {
                swap(p, i);
                siftUp(p);
            }
        }

        void siftDown(int i) {
            int l = left(i), r = right(i), swapIndex = i;

            if (l < numNodes && !rule(swapIndex, l))
                swapIndex = l;

            if (r < numNodes && !rule(swapIndex, r))
                swapIndex = r;

            if (swapIndex != i) {
                swap(i, swapIndex);
                siftDown(swapIndex);
            }
        }

        void swap(int i, int j) {
            Node ni = heap[i];
            Node nj = heap[j];
            updateNodeIndex(ni, j);
            updateNodeIndex(nj, i);
        }

        void updateNodeIndex(Node n, int i) {
            heap[i] = n;
            if (n != null) {
                nodeMap[n.nodeId] = i;
            }
        }

        boolean isEmpty() {
            return numNodes == 0;
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

        // when checking the rule for a single index, compare the element with its
        // parent
        // if it has no parent, the rule is satisifed
        boolean rule(int cIndex) {
            if (cIndex == 0)
                return true;
            return rule(parent(cIndex), cIndex);
        }

        int parent(int i) {
            return (i - 1) / 2;
        }

        int left(int i) {
            return i + i + 1;
        }

        int right(int i) {
            return i + i + 2;
        }
    }

    static class Node {
        int nodeId;
        long distance;

        Node(int nodeId, long distance2) {
            this.nodeId = nodeId;
            this.distance = distance2;
        }
    }
}
