import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SetRangeSum {
    static int MODULO = 1000000001;
    static int last_sum_result = 0;

    public static void main(String[] args) throws IOException {
        // runSolution();
        SetRangeSum.<SimpleTree>testSolution();
    }

    static void runSolution() throws IOException {
        FastScanner in = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);

        int n = in.nextInt();
        SplayTree tree = new SplayTree();
        for (int i = 0; i < n; i++) {
            Query q = Query.read(in);
            String s = processQuery(tree, q);
            if (s != null)
                out.println(s);
        }
        out.close();
    }

    static String processQuery(SummingSet tree, Query q) {
        switch (q.type) {
        case '+':
            tree.add(getModulatedInput(q.arg1));
            break;
        case '-':
            tree.delete(getModulatedInput(q.arg1));
            break;
        case '?':
            return (tree.contains(getModulatedInput(q.arg1)) ? "Found" : "Not found");
        case 's':
            long res = tree.sum(getModulatedInput(q.arg1), getModulatedInput(q.arg2));
            last_sum_result = (int) (res % MODULO);
            return res + "";
        }
        return null;
    }

    private static int getModulatedInput(int arg) {
        return (arg + last_sum_result) % MODULO;
    }

    static <T extends SummingSet> void testSolution() {
        SetRangeSum.<T>runTest(new Query[] { new Query('?', 1), new Query('+', 1), new Query('?', 1), new Query('+', 2),
                new Query('s', 1, 2), new Query('+', 1000000000), new Query('?', 1000000000),
                new Query('-', 1000000000), new Query('?', 1000000000), new Query('s', 999999999, 1000000000),
                new Query('-', 2), new Query('?', 2), new Query('-', 0), new Query('+', 9), new Query('s', 0, 9) },
                new String[] { "Not found", "Found", "3", "Found", "Not found", "1", "Not found", "10" });
        SetRangeSum.<T>runTest(new Query[] { new Query('?', 0), new Query('+', 0), new Query('?', 0), new Query('-', 0),
                new Query('?', 0) }, new String[] { "Not found", "Found", "Not found" });
        SetRangeSum.<T>runTest(
                new Query[] { new Query('+', 491572259), new Query('?', 491572259), new Query('?', 899375874),
                        new Query('s', 310971296, 877523306), new Query('+', 352411209), },
                new String[] { "Found", "Not found", "491572259" });
    }

    static <T extends SummingSet> void runTest(Query[] queries, String[] expected) {
        ArrayList<String> actual = new ArrayList<>();
        last_sum_result = 0;
        SummingSet tree = new Instantiator<T>().getInstance();
        for (Query q : queries) {
            String s = processQuery(tree, q);
            if (s != null)
                actual.add(s);
        }

        String actualString = Arrays.toString(actual.toArray());
        String expectedString = Arrays.toString(expected);
        if (!expectedString.equals(actualString))
            System.out.println("Unexpected result, expected: " + expected + ", but got: " + actual);
    }

    static class SplayTree implements SummingSet {
        SumNode root = null;

        public void add(int x) {
            SumNode left = null;
            SumNode right = null;
            SumNode new_vertex = null;
            NodePair leftRight = split(root, x);
            left = leftRight.left;
            right = leftRight.right;
            if (right == null || right.key != x) {
                new_vertex = new SumNode(x, x, null, null, null);
            }
            root = merge(merge(left, new_vertex), right);
        }

        public void delete(int x) {
            // TODO - implement
        }

        public boolean contains(int x) {
            // TODO - implement
            return false;
        }

        public long sum(int from, int to) {
            NodePair leftMiddle = split(root, from);
            SumNode left = leftMiddle.left;
            SumNode middle = leftMiddle.right;
            NodePair middleRight = split(middle, to + 1);
            middle = middleRight.left;
            SumNode right = middleRight.right;
            long ans = 0;
            // TODO - implement
            return ans;
        }

        void update(SumNode v) {
            if (v == null)
                return;
            v.sum = v.key + (v.left != null ? v.left.sum : 0) + (v.right != null ? v.right.sum : 0);
            if (v.left != null) {
                v.left.parent = v;
            }
            if (v.right != null) {
                v.right.parent = v;
            }
        }

        void smallRotation(SumNode v) {
            SumNode parent = v.parent;
            if (parent == null) {
                return;
            }
            SumNode grandparent = v.parent.parent;
            if (parent.left == v) {
                SumNode m = v.right;
                v.right = parent;
                parent.left = m;
            } else {
                SumNode m = v.left;
                v.left = parent;
                parent.right = m;
            }
            update(parent);
            update(v);
            v.parent = grandparent;
            if (grandparent != null) {
                if (grandparent.left == parent) {
                    grandparent.left = v;
                } else {
                    grandparent.right = v;
                }
            }
        }

        void bigRotation(SumNode v) {
            if (v.parent.left == v && v.parent.parent.left == v.parent) {
                // Zig-zig
                smallRotation(v.parent);
                smallRotation(v);
            } else if (v.parent.right == v && v.parent.parent.right == v.parent) {
                // Zig-zig
                smallRotation(v.parent);
                smallRotation(v);
            } else {
                // Zig-zag
                smallRotation(v);
                smallRotation(v);
            }
        }

        SumNode splay(SumNode v) {
            if (v == null)
                return null;
            while (v.parent != null) {
                if (v.parent.parent == null) {
                    smallRotation(v);
                    break;
                }
                bigRotation(v);
            }
            return v;
        }

        // Searches for the given key in the tree with the given root
        // and calls splay for the deepest visited node after that.
        // Returns pair of the result and the new root.
        // If found, result is a pointer to the node with the given key.
        // Otherwise, result is a pointer to the node with the smallest
        // bigger key (next value in the order).
        // If the key is bigger than all keys in the tree,
        // then result is null.
        NodePair find(SumNode root, int key) {
            SumNode v = root;
            SumNode last = root;
            SumNode next = null;
            while (v != null) {
                if (v.key >= key && (next == null || v.key < next.key)) {
                    next = v;
                }
                last = v;
                if (v.key == key) {
                    break;
                }
                if (v.key < key) {
                    v = v.right;
                } else {
                    v = v.left;
                }
            }
            root = splay(last);
            return new NodePair(next, root);
        }

        NodePair split(SumNode root, int key) {
            NodePair result = new NodePair();
            NodePair findAndRoot = find(root, key);
            root = findAndRoot.right;
            result.right = findAndRoot.left;
            if (result.right == null) {
                result.left = root;
                return result;
            }
            result.right = splay(result.right);
            result.left = result.right.left;
            result.right.left = null;
            if (result.left != null) {
                result.left.parent = null;
            }
            update(result.left);
            update(result.right);
            return result;
        }

        SumNode merge(SumNode left, SumNode right) {
            if (left == null)
                return right;
            if (right == null)
                return left;
            while (right.left != null) {
                right = right.left;
            }
            right = splay(right);
            right.left = left;
            update(right);
            return right;
        }
    }

    static class SimpleTree implements SummingSet {
        @Override
        public void add(int key) {
            // TODO Auto-generated method stub
        }

        @Override
        public void delete(int key) {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean contains(int key) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public long sum(int from, int to) {
            // TODO Auto-generated method stub
            return 0;
        }

    }

    interface SummingSet {
        void add(int key);

        void delete(int key);

        boolean contains(int key);

        long sum(int from, int to);
    }

    static class SumNode {
        int key;
        // Sum of all the keys in the subtree - remember to update
        // it after each operation that changes the tree.
        long sum;
        SumNode left;
        SumNode right;
        SumNode parent;

        SumNode(int key, long sum, SumNode left, SumNode right, SumNode parent) {
            this.key = key;
            this.sum = sum;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }

    static class NodePair {
        SumNode left;
        SumNode right;

        NodePair() {
        }

        NodePair(SumNode left, SumNode right) {
            this.left = left;
            this.right = right;
        }
    }

    static class Query {
        char type;
        int arg1;
        int arg2;

        Query(char type, int arg1, int arg2) {
            this(type, arg1);
            this.arg2 = arg2;
        }

        Query(char type, int arg1) {
            this.type = type;
            this.arg1 = arg1;
        }

        static Query read(FastScanner in) throws IOException {
            char type = in.nextChar();
            int arg1 = in.nextInt();
            int arg2 = 0;
            if (type == 's')
                arg2 = in.nextInt();
            return new Query(type, arg1, arg2);
        }
    }

    static class Instantiator<T> {
        Class<T> dummy;

        T getInstance() {
            try {
                return dummy.getDeclaredConstructor().newInstance(new Object[0]);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
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

        char nextChar() throws IOException {
            return next().charAt(0);
        }
    }
}
