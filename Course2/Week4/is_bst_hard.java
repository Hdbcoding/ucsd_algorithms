import java.util.*;
import java.io.*;

public class is_bst_hard {
    static public void main(String[] args) throws IOException {
        runSolutionWithTallStack();
        testSolution();
    }

    static void runSolutionWithTallStack() {
        new Thread(null, () -> {
            try {
                runSolution();
            } catch (IOException e) {
            }
        }, "1", 1 << 26).start();
    }

    static void runSolution() throws IOException {
        Node[] tree = readTree();
        IsBST solver = new IsBST(tree);
        if (solver.isBinarySearchTree())
            System.out.println("CORRECT");
        else
            System.out.println("INCORRECT");
    }

    static Node[] readTree() throws IOException {
        FastScanner in = new FastScanner();
        int nodes = in.nextInt();
        Node[] tree = new Node[nodes];
        for (int i = 0; i < nodes; i++) {
            tree[i] = new Node(in.nextInt(), in.nextInt(), in.nextInt());
        }
        return tree;
    }

    static void testSolution() {
        // same test cases as is_bst should all still work
        runTest(new int[] { 2, 1, 2, 1, -1, -1, 3, -1, -1 }, true);
        runTest(new int[] { 1, 1, 2, 2, -1, -1, 3, -1, -1 }, false);
        runTest(new int[] {}, true);
        runTest(new int[] { 1, -1, 1, 2, -1, 2, 3, -1, 3, 4, -1, 4, 5, -1, -1 }, true);
        runTest(new int[] { 4, 1, 2, 2, 3, 4, 6, 5, 6, 1, -1, -1, 3, -1, -1, 5, -1, -1, 7, -1, -1 }, true);
        runTest(new int[] { 4, 1, -1, 2, 2, 3, 1, -1, -1, 5, -1, -1 }, false);
        // new test cases
        runTest(new int[] { 2, 1, 2, 1, -1, -1, 2, -1, -1 }, true);
        runTest(new int[] { 2, 1, 2, 2, -1, -1, 3, -1, -1 }, false);
        runTest(new int[] { 2147483647, -1, -1 }, true);
    }

    static void runTest(int[] data, boolean expected) {
        Node[] tree = processData(data);
        IsBST solver = new IsBST(tree);
        boolean actual = solver.isBinarySearchTree();
        if (actual != expected)
            System.out.println("Unexpected result of " + actual + " for tree " + solver);
    }

    static Node[] processData(int[] data) {
        Node[] tree = new Node[data.length / 3];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new Node(data[i * 3], data[i * 3 + 1], data[i * 3 + 2]);
        }
        return tree;
    }

    static class IsBST {
        Node[] tree;

        IsBST(Node[] tree) {
            this.tree = tree;
        }

        boolean isBinarySearchTree() {
            // Implement correct algorithm here
            return true;
        }

        @Override
        public String toString() {
            return "";
        }
    }

    static class Node {
        int key;
        int left;
        int right;

        Node(int key, int left, int right) {
            this.left = left;
            this.right = right;
            this.key = key;
        }
    }

    static class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
