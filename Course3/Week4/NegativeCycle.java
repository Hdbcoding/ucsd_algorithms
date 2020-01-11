import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class NegativeCycle {
    public static void main(String[] args) {
        runSolution();
        // testSolution();
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
        scanner.close();
        System.out.println(negativeCycle(g));
    }

    static void testSolution() {
        runTest(new int[] { 4, 4, 1, 2, -5, 4, 1, 2, 2, 3, 2, 3, 1, 1, }, 1);
        runTest(new int[] { 6, 6, 1, 2, 1, 2, 3, 1, 3, 1, 1, 4, 5, -1, 5, 6, -1, 6, 4, -1 }, 1);
    }

    static void runTest(int[] data, int expected) {
        Graph g = processData(data);
        int actual = negativeCycle(g);
        if (actual != expected)
            System.out.println("Unexpected result for negative cycles for graph:\n" + g + "Expected: " + expected
                    + ", but got " + actual);
    }

    static Graph processData(int[] data) {
        Graph g = new Graph(data[0]);
        for (int i = 2; i < data.length - 2; i += 3) {
            int x = data[i];
            int y = data[i + 1];
            int w = data[i + 2];
            g.addEdge(x, y, w);
        }
        return g;
    }

    static int negativeCycle(Graph g) {
        boolean hasCycle = false;
        Long[] dist = new Long[g.s];
        int src = 0;
        while (!hasCycle && src != -1){
            hasCycle = bellmanFordCheck(g, dist, src);
            src = findNextUnvisitedNode(dist, src);
        }

        return hasCycle ? 1 : 0;
    }

    private static boolean bellmanFordCheck(Graph g, Long[] dist, int src) {
        boolean hasCycle = false;
        dist[src] = 0l;
        Stack<Integer> toVisit = new Stack<Integer>();

        for (int k = 0; k < g.s; k++){
            boolean hasChanges = false;
            boolean[] justVisited = new boolean[g.s];
            toVisit.add(src);

            while (!toVisit.isEmpty()){
                int i = toVisit.pop();
                justVisited[i] = true;
                ArrayList<Integer> adj = g.adj[i];
                ArrayList<Integer> cost = g.cost[i];
                for (int j = 0; j < adj.size(); j++){
                    int n = adj.get(j);
                    int c = cost.get(j);
                    hasChanges |= relax(i, n, c, dist);
                    if (!justVisited[n]) toVisit.add(n);
                }
            }
            if (!hasChanges) break;
            if (k == (g.s - 1)) hasCycle = true;
        }

        return hasCycle;
    }

    private static int findNextUnvisitedNode(Long[] dist, int src) {
        for (int i = src; i < dist.length; i ++){
            if (dist[i] == null) return i;
        }
        return -1;
    }

    // returns true if this edge was relaxed
    private static boolean relax(int u, int v, int cost, Long[] dist) {
        Long src = dist[u];
        if (src == null) return false;
        Long oldDist = dist[v];
        Long newDist = src + cost;
        if (oldDist == null || newDist < oldDist) {
            dist[v] = newDist;
            return true;
        }

        return false;
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
