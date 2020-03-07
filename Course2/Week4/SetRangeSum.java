import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SetRangeSum {
    static int MODULO = 1000000001;
    static long last_sum_result = 0;

    public static void main(String[] args) throws IOException {
        runSolution();
        // testSolution(SimpleTree.class);
        // testSolution(RedBlackTree.class);
        // testSolution(AVLTree.class);
        // testSolution(SplayTree.class);
        // stressTest();
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
                tree.add(a1);
                break;
            case '-':
                tree.delete(a1);
                break;
            case '?':
                String found = (tree.contains(a1) ? "Found" : "Not found");
                return found;
            case 's':
                int a2 = getModulatedInput(q.arg2);
                last_sum_result = tree.sum(a1, a2);
                return last_sum_result + "";
        }
        return null;
    }

    static int getModulatedInput(int arg) {
        long v = arg + last_sum_result;
        // return (int) (v % MODULO + MODULO) % MODULO;
        return (int) (v % MODULO);
    }

    static <T extends SummingSet> void testSolution(Class<T> type) {
        runTest(new Query[] { new Query('?', 0), new Query('+', 0), new Query('?', 0), new Query('-', 0),
                new Query('?', 0) }, new String[] { "Not found", "Found", "Not found" }, type);
        runTest(new Query[] { new Query('+', 491572259), new Query('?', 491572259), new Query('?', 899375874),
                new Query('s', 310971296, 877523306), new Query('+', 352411209), },
                new String[] { "Found", "Not found", "491572259" }, type);
        runFileTest("01", type);
        runFileTest("04", type);
        runFileTest("05", type);
        runFileTest("20", type);
        runFileTest("36_early", type);
        runFileTest("36_early_3", type);
        runFileTest("36", type);
        runFileTest("83", type);
    }

    static void stressTest() {
        int numTests = 10000000;
        int maxNumQueries = 50;
        int maxInput = 10;

        Random r = new Random();
        for (int i = 0; i < numTests; i++) {
            System.out.print("Stress tests: %" + (double) 100 * i / numTests + "\r");
            Query[] data = generateData(maxNumQueries, maxInput, r);
            String expected = respondToQueries(new SimpleTree(), data);
            String actual = respondToQueries(new RedBlackTree(), data);
            if (!expected.equals(expected))
                System.out.println("Unexpected result, expected: " + expected + ", but got: " + actual + " for queries "
                        + Arrays.toString(data));
        }
    }

    static Query[] generateData(int maxNumQueries, int maxInput, Random r) {
        int n = nextInt(maxNumQueries, r) + 1;
        Query[] data = new Query[n];
        for (int i = 0; i < n; i++) {
            char type = nextType(r);
            int arg1 = nextInt(maxInput, r);
            int arg2 = 0;
            if (type == 's') {
                int temp = nextInt(maxInput, r);
                arg2 = Math.max(arg1, temp);
                arg1 = Math.min(arg1, temp);
            }
            data[i] = new Query(type, arg1, arg2);
        }
        return data;
    }

    static int nextInt(int bound, Random r) {
        return Math.abs(r.nextInt(bound));
    }

    static char nextType(Random r) {
        int i = nextInt(4, r);
        switch (i) {
            case 0:
                return '+';
            case 1:
                return '-';
            case 2:
                return '?';
            default:
                return 's';
        }
    }

    static String respondToQueries(SummingSet tree, Query[] queries) {
        ArrayList<String> actual = new ArrayList<>();
        last_sum_result = 0;
        for (Query q : queries) {
            String s = processQuery(tree, q);
            if (s != null)
                actual.add(s);
        }
        return Arrays.toString(actual.toArray());
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
            r.close();
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
            r.close();
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
        // validateSplayTree(tree);
    }

    static void validateSplayTree(SummingSet tree){
        if (tree instanceof SplayTree) {
            SplayTree st = (SplayTree) tree;
            System.out.println("validating tree");
            st.validateTree();
        }
    }

    static class SplayTree implements SummingSet {
        Node root;

        @Override
        public void add(int key) {
            Node n = insert(key);
            updateSumOfAllParents(n);
            splay(n);
        }

        @Override
        public void delete(int key) {
            Node n = remove(key);
            updateSumOfAllParents(n);
            splay(n);
        }

        @Override
        public boolean contains(int key) {
            if (root == null)
                return false;
            Node n = splayFind(key);
            return n != null && n.key == key;
        }

        @Override
        public long sum(int from, int to) {
            return st_sum(from, to);
        }

        long st_sum(int from, int to){
            if (root == null) return 0;
            NodePair np = split(root, from);
            Node lt_start = np.lt;
            Node ge_start = np.ge;
            NodePair np2 = split(ge_start, to);
            Node ge_start_lt_end = np2.lt;
            Node ge_end = np2.ge;
            long sum = getSum(ge_start_lt_end);
            if (ge_end != null && ge_end.key == to)
                sum += ge_end.key;
            
            merge(ge_start_lt_end, ge_end);
            merge(lt_start, root);

            return sum;
        }

        NodePair split(Node r, int key){
            //note - r must be a valid root - make sure not to drop anything
            root = r;
            if (r == null) return new NodePair(null, null);
            Node n = splayFind(key);
            if (n.key >= key) return cutLeft(n);
            return cutRight(n);
        }

        void merge(Node lt, Node ge){
            //note - ge must be a valid root - make sure not to drop anything
            if (ge != null) {
                root = ge;

                if (lt != null) {
                    ge = minimum(ge);
                    splay(ge);
                    ge.left = lt;
                    lt.parent = ge;
                    updateSum(ge);
                }
            } else {
                root = lt;
            }
        }

        NodePair cutLeft(Node n){
            Node lt = n.left;
            Node ge = n;
            if (lt != null) lt.parent = null;
            ge.left = null;
            updateSum(lt);
            updateSum(ge);
            return new NodePair(lt, ge);
        }

        NodePair cutRight(Node n){
            Node lt = n;
            Node ge = n.right;
            lt.right = null;
            if (ge != null) ge.parent = null;
            updateSum(lt);
            updateSum(ge);
            return new NodePair(lt, ge);
        }

        void updateSumOfAllParents(Node n){
            while (n != null){
                updateSum(n);
                n = n.parent;
            }
        }

        void updateSum(Node n){
            if (n == null) return;
            n.sum = getSum(n.left) + getSum(n.right) + n.key;
        }

        Node splayFind(int key) {
            Node n = find(key);
            splay(n);
            return n;
        }

        void splay(Node n) {
            if (n == null)
                return;
            Node p, gp;
            boolean pLeft, gpLeft;
            while (n.parent != null) {
                p = n.parent;
                gp = p.parent;
                pLeft = isLeftChild(n, p);
                if (gp != null) {
                    gpLeft = isLeftChild(p, gp);
                    if (pLeft && gpLeft) {
                        // leftZigZig
                        rightRotate(gp);
                        rightRotate(p);
                    } else if (!pLeft && !gpLeft) {
                        // rightZigZig
                        leftRotate(gp);
                        leftRotate(p);
                    } else if (pLeft && !gpLeft) {
                        // leftZigZag
                        rightRotate(p);
                        leftRotate(gp);
                    } else if (!pLeft && gpLeft) {
                        // rightZigZag
                        leftRotate(p);
                        rightRotate(gp);
                    }
                } else {
                    if (pLeft) {
                        rightRotate(p);
                    } else {
                        leftRotate(p);
                    }
                }
            }
        }

        boolean isLeftChild(Node c, Node p) {
            return c == p.left;
        }

        void leftRotate(Node x) {
            Node y = x.right;
            x.right = y.left;
            if (y.left != null)
                y.left.parent = x;
            y.parent = x.parent;
            if (x.parent == null)
                root = y;
            else if (x == x.parent.left)
                x.parent.left = y;
            else
                x.parent.right = y;
            y.left = x;
            x.parent = y;
            updateSum(x);
            updateSum(y);
        }

        void rightRotate(Node x) {
            Node y = x.left;
            x.left = y.right;
            if (y.right != null)
                y.right.parent = x;
            y.parent = x.parent;
            if (x.parent == null)
                root = y;
            else if (x == x.parent.left)
                x.parent.left = y;
            else
                x.parent.right = y;
            y.right = x;
            x.parent = y;
            updateSum(x);
            updateSum(y);
        }

        Node insert(int key) {
            Node p = find(key);
            // don't add duplicate keys
            if (p != null && p.key == key)
                return p;
            Node z = new Node(key);
            z.parent = p;
            if (p == null)
                root = z;
            else if (z.key < p.key)
                p.left = z;
            else
                p.right = z;
            return z;
        }

        Node remove(int key) {
            Node p = find(key);
            // if the key isnt in the set, nothing to delete
            if (p == null || p.key != key)
                return null;

            Node x = p.parent;
            // two simple cases - if p only has one child, promote it
            if (p.left == null)
                transplant(p, p.right);
            else if (p.right == null)
                transplant(p, p.left);
            else {
                Node next = minimum(p.right);
                x = next;
                // if next node is not p's right child, replace next with its own right child
                // also, replace next's right child with p's right child
                if (next.parent != p) {
                    x = next.parent;
                    transplant(next, next.right);
                    next.right = p.right;
                    next.right.parent = next;
                }
                // replace p with next. p has no left child, so set p's left child as next's
                // left child
                transplant(p, next);
                next.left = p.left;
                next.left.parent = next;
            }
            // return the node whose sum needs to be updated
            return x;
        }

        Node find(int key) {
            Node n = root;
            Node p = null;
            while (n != null) {
                p = n;
                if (n.key == key)
                    break;
                if (n.key > key)
                    n = n.left;
                else
                    n = n.right;
            }
            return p;
        }

        void transplant(Node u, Node v) {
            if (u.parent == null)
                root = v;
            else if (u.parent.left == u)
                u.parent.left = v;
            else
                u.parent.right = v;
            if (v != null)
                v.parent = u.parent;
        }

        Node next(Node n) {
            if (n == null) return n;
            if (n.right != null)
                return minimum(n.right);
            return firstRightAncestor(n);
        }

        Node minimum(Node n) {
            if (n == null) return n;
            while (n.left != null)
                n = n.left;
            return n;
        }

        Node firstRightAncestor(Node n) {
            if (n == null) return n;
            Node p = n.parent;
            while (p != null && p.right == n) {
                n = p;
                p = p.parent;
            }
            return p;
        }

        void validateTree() {
            if (root == null)
                return;
            Queue<Node> q = new LinkedList<>();
            q.add(root);
            while (!q.isEmpty()) {
                Node n = q.poll();
                addIfNotNull(q, n.left);
                addIfNotNull(q, n.right);
                long expected = getSum(n.left) + getSum(n.right) + n.key;
                if (n.sum != expected) {
                    System.out.println("Error at node with key " + n.key);
                    System.out.println("Incorrect sum, got: " + n.sum + ", but expected " + expected);
                    reportNode(n.parent, "parent");
                    reportNode(n.left, "left");
                    reportNode(n.right, "right");
                }
            }
        }

        void addIfNotNull(Queue<Node> q, Node n) {
            if (n == null)
                return;
            q.add(n);
        }

        long getSum(Node n) {
            return n == null ? 0 : n.sum;
        }

        void reportNode(Node n, String relationship) {
            if (n == null)
                return;
            System.out.println(relationship + ": {key: " + n.key + ", sum: " + n.sum + "}");
        }

        class Node {
            int key;
            long sum;
            Node parent;
            Node left;
            Node right;

            Node(int key) {
                this.key = key;
                this.sum = key;
            }
        }

        class NodePair {
            Node lt;
            Node ge;

            NodePair(Node lt, Node ge){
                this.lt = lt;
                this.ge = ge;
            }
        }
    }

    static class AVLTree implements SummingSet {
        Node root;

        @Override
        public void add(int key) {
            Node p = find(key);
            // don't add duplicates
            if (p != null && p.key == key)
                return;
            Node z = new Node(key);
            z.parent = p;
            if (p == null)
                root = z;
            else if (z.key < p.key)
                p.left = z;
            else
                p.right = z;
            rebalance(z);
        }

        @Override
        public void delete(int key) {
            Node p = find(key);
            // if the key isnt in the set, nothing to delete
            if (p == null || p.key != key)
                return;
            Node x = p.parent;
            // two simple cases - if p only has one child, promote it
            if (p.left == null) {
                transplant(p, p.right);
            } else if (p.right == null) {
                transplant(p, p.left);
            } else {
                Node next = minimum(p.right);
                // if next node is not p's right child, replace next with its own right child
                // also, replace next's right child with p's right child
                if (next.parent != p) {
                    x = next.parent;
                    transplant(next, next.right);
                    next.right = p.right;
                    next.right.parent = next;
                }
                // replace p with next. p has no left child, so set p's left child as next's
                // left child
                transplant(p, next);
                next.left = p.left;
                next.left.parent = next;
            }
            rebalance(x);
        }

        @Override
        public boolean contains(int key) {
            if (root == null)
                return false;
            Node n = find(key);
            return n != null && n.key == key;
        }

        @Override
        public long sum(int from, int to) {
            if (root == null)
                return 0;
            long sum = 0;
            Node n = find(from);
            while (n != null && n.key < from)
                n = next(n);

            while (n != null && n.key <= to) {
                sum += n.key;
                n = next(n);
            }

            return sum;
        }

        void rebalance(Node n) {
            Node p = null;
            while (n != null) {
                p = n.parent;
                int l = height(n.left);
                int r = height(n.right);
                if (l > r + 1)
                    rebalanceRight(n);
                else if (r > l + 1)
                    rebalanceLeft(n);
                adjustHeight(n);
                n = p;
            }
        }

        void rebalanceRight(Node n) {
            Node m = n.left;
            if (height(m.right) > height(m.left))
                leftRotate(m);
            rightRotate(n);
            adjustHeight(m);
            adjustHeight(n);
        }

        void rebalanceLeft(Node n) {
            Node m = n.right;
            if (height(m.left) > height(m.right))
                rightRotate(m);
            leftRotate(n);
            adjustHeight(m);
            adjustHeight(n);
        }

        int height(Node n) {
            if (n == null)
                return 0;
            return n.height;
        }

        void adjustHeight(Node n) {
            n.height = Math.max(height(n.left), height(n.right)) + 1;
        }

        Node find(int key) {
            Node n = root;
            Node p = null;
            while (n != null) {
                p = n;
                if (n.key == key)
                    break;
                if (n.key > key)
                    n = n.left;
                else
                    n = n.right;
            }
            return p;
        }

        void leftRotate(Node x) {
            Node y = x.right;
            x.right = y.left;
            if (y.left != null)
                y.left.parent = x;
            y.parent = x.parent;
            if (x.parent == null)
                root = y;
            else if (x == x.parent.left)
                x.parent.left = y;
            else
                x.parent.right = y;
            y.left = x;
            x.parent = y;
        }

        void rightRotate(Node x) {
            Node y = x.left;
            x.left = y.right;
            if (y.right != null)
                y.right.parent = x;
            y.parent = x.parent;
            if (x.parent == null)
                root = y;
            else if (x == x.parent.left)
                x.parent.left = y;
            else
                x.parent.right = y;
            y.right = x;
            x.parent = y;
        }

        void transplant(Node u, Node v) {
            if (u.parent == null)
                root = v;
            else if (u == u.parent.left)
                u.parent.left = v;
            else
                u.parent.right = v;
            if (v != null)
                v.parent = u.parent;
        }

        Node next(Node n) {
            if (n.right != null)
                return minimum(n.right);
            return firstRightAncestor(n);
        }

        Node minimum(Node n) {
            while (n.left != null)
                n = n.left;
            return n;
        }

        Node firstRightAncestor(Node n) {
            Node p = n.parent;
            while (p != null && p.right == n) {
                n = p;
                p = p.parent;
            }
            return p;
        }

        class Node {
            int key;
            Node parent;
            Node left;
            Node right;
            int height;

            Node(int key) {
                this.key = key;
            }
        }
    }

    static class RedBlackTree implements SummingSet {
        boolean black = true;
        boolean red = false;
        Node nill;
        Node root;

        RedBlackTree() {
            nill = new Node(-1);
            nill.left = nill.right = nill.parent = nill;
            nill.color = black;
            root = nill;
        }

        @Override
        public void add(int key) {
            Node p = find(key);
            // don't add duplicates
            if (p != nill && p.key == key)
                return;
            Node z = createNode(key);
            z.parent = p;
            if (p == nill)
                root = z;
            else if (z.key < p.key)
                p.left = z;
            else
                p.right = z;
            z.color = red;
            insertFixup(z);
        }

        void insertFixup(Node z) {
            Node y;
            while (z.parent.color == red) {
                if (z.parent == z.parent.parent.left) {
                    y = z.parent.parent.right;
                    if (y.color == red) {
                        z.parent.color = black;
                        y.color = black;
                        z.parent.parent.color = red;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.right) {
                            z = z.parent;
                            leftRotate(z);
                        }
                        z.parent.color = black;
                        z.parent.parent.color = red;
                        rightRotate(z.parent.parent);
                    }
                } else {
                    y = z.parent.parent.left;
                    if (y.color == red) {
                        z.parent.color = black;
                        y.color = black;
                        z.parent.parent.color = red;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.left) {
                            z = z.parent;
                            rightRotate(z);
                        }
                        z.parent.color = black;
                        z.parent.parent.color = red;
                        leftRotate(z.parent.parent);
                    }
                }
            }
            root.color = black;
        }

        @Override
        public void delete(int key) {
            Node z = find(key);
            // nothing to delete if the key isn't in the set
            if (z == nill || z.key != key)
                return;

            Node y = z;
            Node x = nill;
            boolean color = y.color;
            if (z.left == nill) {
                x = z.right;
                transplant(z, z.right);
            } else if (z.right == nill) {
                x = z.left;
                transplant(z, z.left);
            } else {
                y = minimum(z.right);
                color = y.color;
                x = y.right;
                if (y.parent == z) {
                    x.parent = y;
                } else {
                    transplant(y, y.right);
                    y.right = z.right;
                    y.right.parent = y;
                }
                transplant(z, y);
                y.left = z.left;
                y.left.parent = y;
                y.color = z.color;
            }
            if (color == black)
                deleteFixup(x);
        }

        void deleteFixup(Node x) {
            Node w;
            while (x != root && x.color == black) {
                if (x == x.parent.left) {
                    w = x.parent.right;
                    if (w.color == red) {
                        w.color = black;
                        x.parent.color = red;
                        leftRotate(x.parent);
                        w = x.parent.right;
                    }
                    if (w.left.color == black && w.right.color == black) {
                        w.color = red;
                        x = x.parent;
                    } else {
                        if (w.right.color == black) {
                            w.left.color = black;
                            w.color = red;
                            rightRotate(w);
                            w = x.parent.right;
                        }
                        w.color = x.parent.color;
                        x.parent.color = black;
                        w.right.color = black;
                        leftRotate(x.parent);
                        x = root;
                    }
                } else {
                    w = x.parent.left;
                    if (w.color == red) {
                        w.color = black;
                        x.parent.color = red;
                        rightRotate(x.parent);
                        w = x.parent.left;
                    }
                    if (w.left.color == black && w.right.color == black) {
                        w.color = red;
                        x = x.parent;
                    } else {
                        if (w.left.color == black) {
                            w.right.color = black;
                            w.color = red;
                            leftRotate(w);
                            w = x.parent.left;
                        }
                        w.color = x.parent.color;
                        x.parent.color = black;
                        w.left.color = black;
                        rightRotate(x.parent);
                        x = root;
                    }
                }
            }
            x.color = black;
        }

        @Override
        public boolean contains(int key) {
            if (root == nill)
                return false;
            Node n = find(key);
            return n != null && n.key == key;
        }

        @Override
        public long sum(int from, int to) {
            if (root == nill)
                return 0;
            long sum = 0;
            Node n = find(from);
            while (n != nill && n.key < from)
                n = next(n);

            while (n != nill && n.key <= to) {
                sum += n.key;
                n = next(n);
            }

            return sum;
        }

        Node find(int key) {
            Node n = root;
            Node p = nill;
            while (n != nill) {
                p = n;
                if (n.key == key)
                    break;
                if (n.key > key)
                    n = n.left;
                else
                    n = n.right;
            }
            return p;
        }

        Node createNode(int key) {
            Node n = new Node(key);
            n.left = n.right = n.parent = nill;
            return n;
        }

        void leftRotate(Node x) {
            Node y = x.right;
            x.right = y.left;
            if (y.left != nill)
                y.left.parent = x;
            y.parent = x.parent;
            if (x.parent == nill)
                root = y;
            else if (x == x.parent.left)
                x.parent.left = y;
            else
                x.parent.right = y;
            y.left = x;
            x.parent = y;
        }

        void rightRotate(Node x) {
            Node y = x.left;
            x.left = y.right;
            if (y.right != nill)
                y.right.parent = x;
            y.parent = x.parent;
            if (x.parent == nill)
                root = y;
            else if (x == x.parent.left)
                x.parent.left = y;
            else
                x.parent.right = y;
            y.right = x;
            x.parent = y;
        }

        void transplant(Node u, Node v) {
            if (u.parent == nill)
                root = v;
            else if (u == u.parent.left)
                u.parent.left = v;
            else
                u.parent.right = v;
            v.parent = u.parent;
        }

        Node next(Node n) {
            if (n.right != nill)
                return minimum(n.right);
            return firstRightAncestor(n);
        }

        Node minimum(Node n) {
            while (n.left != nill)
                n = n.left;
            return n;
        }

        Node firstRightAncestor(Node n) {
            Node p = n.parent;
            while (p != nill && p.right == n) {
                n = p;
                p = p.parent;
            }
            return p;
        }

        class Node {
            int key;
            Node left;
            Node right;
            Node parent;
            boolean color;

            Node(int key) {
                this.key = key;
            }
        }
    }

    static class SimpleTree implements SummingSet {
        Node root;

        @Override
        public void add(int key) {
            Node p = find(key);
            // don't add duplicate keys
            if (p != null && p.key == key)
                return;
            Node z = new Node(key);
            z.parent = p;
            if (p == null)
                root = z;
            else if (z.key < p.key)
                p.left = z;
            else
                p.right = z;
        }

        @Override
        public void delete(int key) {
            Node p = find(key);
            // if the key isnt in the set, nothing to delete
            if (p == null || p.key != key)
                return;
            // two simple cases - if p only has one child, promote it
            if (p.left == null)
                transplant(p, p.right);
            else if (p.right == null)
                transplant(p, p.left);
            else {
                Node next = minimum(p.right);
                // if next node is not p's right child, replace next with its own right child
                // also, replace next's right child with p's right child
                if (next.parent != p) {
                    transplant(next, next.right);
                    next.right = p.right;
                    next.right.parent = next;
                }
                // replace p with next. p has no left child, so set p's left child as next's
                // left child
                transplant(p, next);
                next.left = p.left;
                next.left.parent = next;
            }
        }

        @Override
        public boolean contains(int key) {
            if (root == null)
                return false;
            Node n = find(key);
            return n != null && n.key == key;
        }

        @Override
        public long sum(int from, int to) {
            if (root == null)
                return 0;
            long sum = 0;
            Node n = find(from);
            while (n != null && n.key < from)
                n = next(n);

            while (n != null && n.key <= to) {
                // long v = sum + n.key;
                // sum = ((v % MODULO) + MODULO) % MODULO;
                sum += n.key;
                n = next(n);
            }

            return sum;
        }

        Node find(int key) {
            Node n = root;
            Node p = null;
            while (n != null) {
                p = n;
                if (n.key == key)
                    break;
                if (n.key > key)
                    n = n.left;
                else
                    n = n.right;
            }
            return p;
        }

        void transplant(Node u, Node v) {
            if (u.parent == null)
                root = v;
            else if (u.parent.left == u)
                u.parent.left = v;
            else
                u.parent.right = v;
            if (v != null)
                v.parent = u.parent;
        }

        Node next(Node n) {
            if (n.right != null)
                return minimum(n.right);
            return firstRightAncestor(n);
        }

        Node minimum(Node n) {
            while (n.left != null)
                n = n.left;
            return n;
        }

        Node firstRightAncestor(Node n) {
            Node p = n.parent;
            while (p != null && p.right == n) {
                n = p;
                p = p.parent;
            }
            return p;
        }

        class Node {
            int key;
            Node parent;
            Node left;
            Node right;

            Node(int key) {
                this.key = key;
            }
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

        @Override
        public String toString() {
            return "{ type: " + type + " , arg1: " + arg1 + ", arg2: " + arg2 + " }";
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
