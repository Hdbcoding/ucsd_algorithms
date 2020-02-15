import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class DistPreprocessSmall {
    public static void main(String[] args) {
        runSolution();
        testSolution();
    }

    static void testSolution() {

    }

    static void runSolution() {

    }

    public interface GraphSolver {
        long distance(int u, int v);

        void preprocess(DataScanner in);
    }

    static class FloydWarshall implements GraphSolver {
        long[][] distances;
        Graph g;

        public void preprocess(DataScanner in) {
            preprocess(parseGraph(in));
        }

        void preprocess(Graph g) {
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
