import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

// TODO - I'll get to this one eventually I promise
public class SetRangeSum {
    static int MODULO = 1000000001;
    static long last_sum_result = 0;
    static boolean debug = false;

    public static void main(String[] args) throws IOException {
        // runSolution();
        testWithTallStack(SimpleTree.class);
    }

    static <T extends SummingSet> void testWithTallStack(Class<T> type) {
        new Thread(null, () -> {
            testSolution(type);
        }, "1", 1 << 26).start();
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
        int a1 = getModulatedInput(q.arg1);
        switch (q.type) {
            case '+':
                debugLog("add " + a1 + " (" + q.arg1 + ")");
                tree.add(a1);
                break;
            case '-':
                debugLog("del " + a1 + " (" + q.arg1 + ")");
                tree.delete(a1);
                break;
            case '?':
                String found = (tree.contains(a1) ? "Found" : "Not found");
                debugLog("has " + a1 + " (" + q.arg1 + "): " + found);
                return found;
            case 's':
                int a2 = getModulatedInput(q.arg2);
                last_sum_result = tree.sum(a1, a2);
                debugLog("sum " + a1 + " (" + q.arg1 + "), " + a2 + " (" + q.arg2 + "): " + last_sum_result);
                return last_sum_result + "";
        }
        return null;
    }

    private static int getModulatedInput(int arg) {
        long v = arg + last_sum_result;
        // return (int) (v % MODULO + MODULO) % MODULO;
        return (int) (v % MODULO);
    }

    static <T extends SummingSet> void testSolution(Class<T> type) {
        System.out.println("running simple case 1");
        runTest(new Query[] { new Query('?', 0), new Query('+', 0), new Query('?', 0), new Query('-', 0),
                new Query('?', 0) }, new String[] { "Not found", "Found", "Not found" }, type);
        runTest(new Query[] { new Query('+', 491572259), new Query('?', 491572259), new Query('?', 899375874),
                new Query('s', 310971296, 877523306), new Query('+', 352411209), },
                new String[] { "Found", "Not found", "491572259" }, type);
        System.out.println("done with simple case 2");
        runFileTest("01", type);
        runFileTest("04", type);
        runFileTest("05", type);
        runFileTest("20", type);
        runFileTest("36_early_2", type);
        runFileTest("36_early", type);
        runFileTest("36", type);
        runFileTest("83", type);
    }

    static void debugLog(String message) {
        if (debug)
            System.out.println(message);
    }

    static <T extends SummingSet> void runFileTest(String key, Class<T> type) {
        System.out.println("running file test: " + key);
        String root = "C:/test_data/ucsd_algorithms/2_4_4/";
        Query[] queries = readQueries(root + key);
        String[] expected = readExpected(root + key + ".a");
        runTest(queries, expected, type);
        System.out.println("done with test: " + key);
    }

    static Query[] readQueries(String fileName) {
        ArrayList<Query> queries = new ArrayList<>();
        try {
            BufferedReader r = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            r.readLine(); // dump the first line
            while ((line = r.readLine()) != null) {
                String[] tokens = line.split(" ");
                char type = tokens[0].charAt(0);
                int arg1 = Integer.parseInt(tokens[1]);
                int arg2 = tokens.length > 2 ? Integer.parseInt(tokens[2]) : 0;
                queries.add(new Query(type, arg1, arg2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queries.stream().toArray(Query[]::new);
    }

    static String[] readExpected(String fileName) {
        ArrayList<String> expected = new ArrayList<>();
        try {
            BufferedReader r = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            while ((line = r.readLine()) != null) {
                expected.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return expected.stream().toArray(String[]::new);
    }

    static <T extends SummingSet> void runTest(Query[] queries, String[] expected, Class<T> type) {
        ArrayList<String> actual = new ArrayList<>();
        last_sum_result = 0;
        SummingSet tree = new Instantiator<T>().getInstance(type);
        for (Query q : queries) {
            String s = processQuery(tree, q);
            if (s != null)
                actual.add(s);
        }

        String actualString = Arrays.toString(actual.toArray());
        String expectedString = Arrays.toString(expected);
        if (!expectedString.equals(actualString))
            System.out.println("Unexpected result, expected: " + expectedString + ", but got: " + actualString);
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
        SumNode root;

        @Override
        public void add(int key) {
            SumNode p = null;
            SumNode n = root;
            while (n != null){
                p = n;
                // unique keys
                if (n.key == key) return;
                if (n.key > key) n = n.left;
                else n = n.right;
            }

            SumNode z = new SumNode(key, key);
            z.parent = p;
            if (p == null) root = z;
            else if (z.key < p.key) p.left = z;
            else p.right = z;
        }

        void updateSumOfAllParents(SumNode p) {
            while (p != null) {
                updateSum(p);
                p = p.parent;
            }
        }

        void updateSum(SumNode p) {
            p.sum = getSum(p.left) + getSum(p.right) + p.key;
        }

        long getSum(SumNode n) {
            return n == null ? 0 : n.sum;
        }

        SumNode find(SumNode n, int key) {
            while (n != null && n.key != key) {
                if (n.key >= key)
                    n = n.left;
                else
                    n = n.right;
            }
            return n;
        }

        @Override
        public void delete(int key) {
            SumNode p = find(root, key);
            // if the key isnt in the set, nothing to delete
            if (p == null || p.key != key)
                return;
            // two simple cases - if p only has one child, promote it
            if (p.left == null)
                transplant(p, p.right);
            else if (p.right == null)
                transplant(p, p.left);
            else {
                SumNode next = minimum(p.right);
                // if next node is not p's right child, replace next with its own right child
                // also, replace next's right child with p's right child
                if (next.parent != p){
                    transplant(next, next.right);
                    next.right = p.right;
                    next.right.parent = next;
                }
                // replace p with next. p has no left child, so set p's left child as next's left child
                transplant(p, next);
                next.left = p.left;
                next.left.parent = next;
            }
        }

        void transplant(SumNode u, SumNode v){
            if (u.parent == null) root = v;
            else if (u.parent.left == u) u.parent.left = v;
            else u.parent.right = v;
            if (v != null) v.parent = u.parent;
        }

        SumNode next(SumNode n) {
            if (n.right != null)
                return minimum(n.right);
            return firstRightAncestor(n);
        }

        SumNode minimum(SumNode n) {
            while (n.left != null) n = n.left;
            return n;
        }

        SumNode firstRightAncestor(SumNode n) {
            SumNode p = n.parent;
            while (p != null && p.right == n){
                n = p;
                p = p.parent;
            }
            return p;
        }

        @Override
        public boolean contains(int key) {
            if (root == null)
                return false;
            SumNode n = find(root, key);
            return n != null && n.key == key;
        }

        @Override
        public long sum(int from, int to) {
            if (root == null)
                return 0;
            long sum = 0;
            SumNode n = find(root, from);

            while (n != null && n.key <= to) {
                if (n.key >= from) {
                    // long v = sum + n.key;
                    // sum = ((v % MODULO) + MODULO) % MODULO;
                    sum += n.key;
                }
                n = next(n);
            }

            return sum;
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

        SumNode(int key, long sum) {
            this.key = key;
            this.sum = sum;
        }

        SumNode(int key, long sum, SumNode left, SumNode right, SumNode parent) {
            this(key, sum);
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
        T getInstance(Class<T> type) {
            try {
                return type.getDeclaredConstructor().newInstance(new Object[0]);
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
