import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class FriendSuggestion {
    public static void main(String args[]) {
        runSolution();
    }

    static void runSolution(){
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
            System.out.println(bd.query(u-1, v-1));
        }
        in.close();
    }

    static class BidirectionalDijkstra {
        // Number of nodes
        int n;
        Graph g;
        Graph gr;
        // visited[v] == true iff v was visited either by forward or backward search.
        boolean[] visited;

        BidirectionalDijkstra(int n) {
            this.n = n;

            this.g = new Graph(n);
            this.gr = new Graph(n);

            visited = new boolean[n];
        }

        void addEdge(int x, int y, int c) {
            g.addEdge(x, y, c);
            gr.addEdge(x, y, c);
        }

        // Returns the distance from s to t in the graph.
        long query(int s, int t) {
            clear();
            g.visit(s, 0l);
            gr.visit(t, 0l);
            // Implement the rest of the algorithm yourself
 
            return -1l;
        }

        // Reinitialize the data structures before new query after the previous query
        void clear() {
            g.clear();
            gr.clear();
            Arrays.fill(visited, false);
        }

        void visit (Graph g, int i, long distance){
            g.visit(i, distance);
            visited[i] = true;
        }
    }

    static class Graph{
        int n;
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] cost;
        long[] dist;
        PriorityQueue<Entry> queue;

        Graph(int n){
            this.n = n;

            this.adj = generateAdjacency();
            this.cost = generateAdjacency();
            this.dist = new long[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            this.queue = new PriorityQueue<Entry>(n);
        }

        ArrayList<Integer>[] generateAdjacency(){
            ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
            for(int i = 0; i < n; i++){
                adj[i] = new ArrayList<Integer>();
            }
            return adj;
        }

        void addEdge(int x, int y, int c){
            adj[x].add(y);
            cost[x].add(c);
        }

        void clear(){
            Arrays.fill(dist, Long.MAX_VALUE);
            this.queue.clear();
        }

        // todo - implement
        void visit(int i, long d){

        }
    }


    static class Entry implements Comparable<Entry>
    {
        Long cost;
        int node;
      
        public Entry(Long cost, int node)
        {
            this.cost = cost;
            this.node = node;
        }
     
        public int compareTo(Entry other)
        {
            return cost < other.cost ? -1 : cost > other.cost ? 1 : 0;
        }
    }
}
