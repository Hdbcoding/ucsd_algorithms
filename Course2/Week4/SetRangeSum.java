import java.io.*;
import java.util.*;

public class SetRangeSum {
    static int MODULO = 1000000001;
    static int last_sum_result = 0;

    public static void main(String[] args) throws IOException {
        runSolution();
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

    static String processQuery(SplayTree tree, Query q) {
        switch (q.type) {
        case '+':
            tree.insert((q.arg1 + last_sum_result) % MODULO);
            break;
        case '-':
            tree.erase((q.arg1 + last_sum_result) % MODULO);
            break;
        case '?':
            return (tree.find((q.arg1 + last_sum_result) % MODULO) ? "Found" : "Not found");
        case 's':
            long res = tree.sum((q.arg1 + last_sum_result) % MODULO, (q.arg2 + last_sum_result) % MODULO);
            last_sum_result = (int) (res % MODULO);
            return res + "";
        }
        return null;
    }

    static void testSolution() {
        runTest(new Query[] { new Query('?', 1), new Query('+', 1), new Query('?', 1), new Query('+', 2),
                new Query('s', 1, 2), new Query('+', 1000000000), new Query('?', 1000000000),
                new Query('-', 1000000000), new Query('?', 1000000000), new Query('s', 999999999, 1000000000),
                new Query('-', 2), new Query('?', 2), new Query('-', 0), new Query('+', 9), new Query('s', 0, 9) },
                new String[] { "Not found", "Found", "3", "Found", "Not found", "1", "Not found", "10" });
        runTest(new Query[] { new Query('?', 0), new Query('+', 0), new Query('?', 0), new Query('-', 0),
                new Query('?', 0) }, new String[] { "Not found", "Found", "Not found" });
        runTest(new Query[] { new Query('+', 491572259), new Query('?', 491572259), new Query('?', 899375874),
                new Query('s', 310971296, 877523306), new Query('+', 352411209), },
                new String[] { "Found", "Not found", "491572259" });
    }

    static void runTest(Query[] queries, String[] expected) {
        ArrayList<String> actual = new ArrayList<>();
        last_sum_result = 0;
        SplayTree tree = new SplayTree();
        for (int i = 0; i < queries.length; i++) {
            String s = processQuery(tree, queries[i]);
            if (s != null)
                actual.add(s);
        }

        String actualString = Arrays.toString(actual.toArray());
        String expectedString = Arrays.toString(expected);
        if (!expectedString.equals(actualString))
            System.out.println("Unexpected result, expected: " + expected + ", but got: " + actual);
    }

    static class SplayTree {
        Vertex root = null;

        void insert(int x) {
            Vertex left = null;
            Vertex right = null;
            Vertex new_vertex = null;
            VertexPair leftRight = split(root, x);
            left = leftRight.left;
            right = leftRight.right;
            if (right == null || right.key != x) {
                new_vertex = new Vertex(x, x, null, null, null);
            }
            root = merge(merge(left, new_vertex), right);
        }

        void erase(int x) {
            // TODO - implement
        }

        boolean find(int x) {
            // TODO - implement
            return false;
        }

        long sum(int from, int to) {
            VertexPair leftMiddle = split(root, from);
            Vertex left = leftMiddle.left;
            Vertex middle = leftMiddle.right;
            VertexPair middleRight = split(middle, to + 1);
            middle = middleRight.left;
            Vertex right = middleRight.right;
            long ans = 0;
            // TODO - implement
            return ans;
        }

        void update(Vertex v) {
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

        void smallRotation(Vertex v) {
            Vertex parent = v.parent;
            if (parent == null) {
                return;
            }
            Vertex grandparent = v.parent.parent;
            if (parent.left == v) {
                Vertex m = v.right;
                v.right = parent;
                parent.left = m;
            } else {
                Vertex m = v.left;
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

        void bigRotation(Vertex v) {
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

        Vertex splay(Vertex v) {
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
        VertexPair find(Vertex root, int key) {
            Vertex v = root;
            Vertex last = root;
            Vertex next = null;
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
            return new VertexPair(next, root);
        }

        VertexPair split(Vertex root, int key) {
            VertexPair result = new VertexPair();
            VertexPair findAndRoot = find(root, key);
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

        Vertex merge(Vertex left, Vertex right) {
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

    static class Vertex {
        int key;
        // Sum of all the keys in the subtree - remember to update
        // it after each operation that changes the tree.
        long sum;
        Vertex left;
        Vertex right;
        Vertex parent;

        Vertex(int key, long sum, Vertex left, Vertex right, Vertex parent) {
            this.key = key;
            this.sum = sum;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }

    static class VertexPair {
        Vertex left;
        Vertex right;

        VertexPair() {
        }

        VertexPair(Vertex left, Vertex right) {
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
