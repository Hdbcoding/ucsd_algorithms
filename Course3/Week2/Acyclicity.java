import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Acyclicity {
    public static void main(String[] args) {
        // runSolution();
        testSolution();
    }

    static void runSolution(){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = constructGraph(n);
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            addEdgeToAdjacencyList(adj, x, y);
        }
        scanner.close();
        System.out.println(acyclic(adj));
    }

    private static void addEdgeToAdjacencyList(ArrayList<Integer>[] adj, int x, int y) {
        adj[x - 1].add(y - 1);
        // no reverse edge this time
    }

    static ArrayList<Integer>[] constructGraph(int length) {
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[length];
        for (int i = 0; i < length; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        return adj;
    }

    static void testSolution(){

    }

    static void runTest(ArrayList<Integer>[] adj, int expected){
        int actual = acyclic(adj);
        if (actual != expected)
            System.out.println(
                    "Unexpected result for " + printGraph(adj) + ". Expected: " + expected);
    }

    static String printGraph(ArrayList<Integer>[] adj) {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < adj.length; i++) {
            ArrayList<Integer> edges = adj[i];
            s.append("node " + i + ": ");
            s.append(Arrays.toString(edges.toArray()));
            s.append("\n");
        }

        return s.toString();
    }

    private static int acyclic(ArrayList<Integer>[] adj) {
        //write your code here
        return 0;
    }

    class CyclicityChecker {

    }
}

