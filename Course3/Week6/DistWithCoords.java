import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;

public class DistWithCoords {
    public static void main(String args[]) {
        // runSolution();
        testSolution();
    }

    static void runSolution() {
        DataScanner in = new StreamScanner();
        Graph g = parseData(in);
        AStar a = new AStar(g);
        respondToQueries(a, in);
        in.close();
    }

    static void testSolution() {
        runTest(new int[] { 2, 1, 0, 0, 0, 1, 1, 2, 1, 4, 1, 1, 2, 2, 1, 2, 2, 1 }, new long[] { 0, 0, 1, -1 });
        runTest(new int[] { 4, 4, 0, 0, 0, 1, 2, 1, 2, 0, 1, 2, 1, 4, 1, 2, 2, 3, 2, 1, 3, 6, 1, 1, 3 },
                new long[] { 3 });
    }

    static void runTest(int[] data, long[] expected) {
        DataScanner in = new ArrayScanner(data);
        Graph graph = parseData(in);
        FloydWarshall floydWarshall = new FloydWarshall(graph);
        Dijkstra dijkstra = new Dijkstra(graph);
        AStar aStar = new AStar(graph);
        Query[] queries = parseQueries(in);
        String expectedString = Arrays.toString(expected);
        boolean allTechniquesWork = evaluate(floydWarshall, queries, expectedString);
        allTechniquesWork &= evaluate(dijkstra, queries, expectedString);
        allTechniquesWork &= evaluate(aStar, queries, expectedString);
        if (!allTechniquesWork) {
            System.out.println("Queries: " + Arrays.toString(queries));
            System.out.println("Graph: " + graph);
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

    public interface GraphSolver {
        long distance(int u, int v);
    }

    static class FloydWarshall implements GraphSolver {
        long[][] distances;
        Graph g;

        FloydWarshall(Graph g){
            this.g = g;
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

        public long distance(int s, int t){
            return distances[s][t];
        }
    }

    static class Dijkstra implements GraphSolver {
        Graph g;

        Dijkstra(Graph g) {
            this.g = g;
        }

        public long distance(int s, int t) {
            if (s == t) return 0l;

            long[] dist = new long[g.s];
            Arrays.fill(dist, -1);
            NodeHeap h = new NodeHeap(g.s);
            dist[s] = 0;
            h.addOrUpdate(s, 0);
            while (!h.isEmpty()){
                Node u = h.extractMin();
                ArrayList<Integer> neighbors = g.adj[u.nodeId];
                ArrayList<Integer> weights = g.cost[u.nodeId];
                for (int i = 0; i < neighbors.size(); i++){
                    int nodeId = neighbors.get(i);
                    int weight = weights.get(i);
                    long oldDist = dist[nodeId];
                    long newDist = u.distance + weight;
                    if (oldDist == -1 || oldDist > newDist){
                        dist[nodeId] = newDist;
                        h.addOrUpdate(nodeId, newDist);
                    }
                }
            }

            return dist[t];
        }
    }

    static class AStar implements GraphSolver {
        Graph g;

        AStar(Graph g) {
            this.g = g;
        }

        public long distance(int s, int t) {
            return -1l;
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

        NodeHeap(int numNodes){
            heap = new Node[numNodes];
            nodeMap = new int[numNodes];
            Arrays.fill(nodeMap, -1);
            this.numNodes = 0;
        }

        boolean isEmpty(){
            return numNodes == 0;
        }

        Node extractMin(){
            if (isEmpty())
                return null;
            Node n = removeNode(0);
            siftDown(0);
            return n;
        }

        void addOrUpdate(int nodeId, long distance){
            if (nodeMap[nodeId] != -1)
                changePriority(nodeId, distance);
            else add(nodeId, distance);
        }

        void delete(int nodeId){
            int index = nodeMap[nodeId];
            if (index == -1) return;
            removeNode(index);
            heapify(index);
        }

        void add(int nodeId, long distance){
            numNodes++;
            int index = numNodes - 1;
            Node n = new Node(nodeId, distance);
            updateNodeIndex(n, index);
            siftUp(index);
        }

        void changePriority(int nodeId, long distance){
            int index = nodeMap[nodeId];
            if (index == -1) return;
            Node n = heap[index];
            heapify(index);
        }

        Node removeNode(int index){
            Node n = heap[index];
            nodeMap[n.nodeId] = -1;
            numNodes--;
            Node lastNode = heap[numNodes];
            if (numNodes != 0 && lastNode != n){
                heap[numNodes] = null;
                updateNodeIndex(lastNode, index);
            }
            return n;
        }

        void heapify(int index){
            if (rule(index)) siftDown(index);
            else siftUp(index);
        }

        void siftUp(int i){
            if (i == 0) return;
            int p = parent(i);
            if (!rule(p, i)) {
                swap(p, i);
                siftUp(p);
            }
        }

        void siftDown(int i){
            int l = left(i), r = right(i), swapIndex = i;

            if (l < numNodes && !rule(swapIndex, l))
                swapIndex = l;
            
            if (r < numNodes && !rule(swapIndex, r))
                swapIndex = r;

            if (swapIndex != i){
                swap(i, swapIndex);
                siftDown(swapIndex);
            }
        }

        void swap(int i, int j){
            Node ni = heap[i];
            Node nj = heap[j];
            updateNodeIndex(ni, j);
            updateNodeIndex(nj, i);
        }

        void updateNodeIndex(Node n, int i){
            heap[i] = n;
            if (n != null) nodeMap[n.nodeId] = i;
        }

        int parent(int i){
            return (i - 1) / 2;
        }

        int left(int i){
            return i * 2 + 1;
        }

        int right(int i){
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

    static class Node {
        int nodeId;
        long distance;

        Node(int nodeId, long distance2) {
            this.nodeId = nodeId;
            this.distance = distance2;
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

        @Override
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

        @Override
        public int nextInt() {
            return data[i++];
        }

        public void close() {
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
