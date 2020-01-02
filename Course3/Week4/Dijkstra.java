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
        public String toString() {
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

        Node removeNode(int index) {
            Node n = heap[index];
            nodeMap[n.nodeId] = -1;
            numNodes--;
            Node lastNode = heap[numNodes];
            if (numNodes != 0 && lastNode != n) {
                heap[numNodes] = null;
                updateNode(lastNode, index);
            }
            return n;
        }

        void add(int nodeId, long distance) {
            numNodes++;
            int index = numNodes - 1;
            Node n = new Node(nodeId, distance);
            updateNode(n, index);
            siftUp(index);
        }

        void delete(int nodeId) {
            int index = nodeMap[nodeId];
            if (index == -1)
                return;
            removeNode(index);
            heapify(index);
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
            updateNode(ni, j);
            updateNode(nj, i);
        }

        void updateNode(Node n, int i) {
            heap[i] = n;
            if (n != null){
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
