import java.io.*;
import java.util.*;

// TODO - I'll get to this one eventually I promise
class RopeProblem {
	public static void main(String[] args) throws IOException {
		// runSolution();
		testSolution();
	}

	static void runSolution() throws IOException {
		FastScanner in = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);
		NaiveRope rope = new NaiveRope();
		rope.initialize(in.next());
		for (int q = in.nextInt(); q > 0; q--) {
			int i = in.nextInt();
			int j = in.nextInt();
			int k = in.nextInt();
			rope.process(i, j, k);
		}
		out.println(rope.result());
		out.close();
	}

	static void testSolution() {
		runTest("hlelowrold", new Query[] { new Query(1, 1, 2), new Query(6, 6, 7) }, "helloworld");
		runTest("abcdef", new Query[] { new Query(0, 1, 1), new Query(4, 5, 0) }, "efcabd");
	}

	static void runTest(String word, Query[] queries, String expected) {
		Rope r = new Rope();
		r.initialize(word);
		for (Query q : queries) {
			r.process(q.i, q.j, q.k);
		}
		String actual = r.result();
		if (!actual.equals(expected))
			System.out.println("Unexpected result on word " + word + ". Expected " + expected + ", but got: " + actual);
	}

	static class Rope implements StringOrganizer {
		Node root;

		@Override
		public void initialize(String s) {
			root = new Node();
			root.value = s;
		}

		@Override
		public void process(int i, int j, int k) {
			// cut off the string before i
			NodePair np = split(root, i);
			Node lt_i = np.lt;
			Node gt_i = np.gt;

			// cut off the string after j
			np = split(gt_i, j);
			Node gt_i_lt_j = np.lt;
			Node gt_j = np.gt;

			// merge the segments around the desired segment
			merge(lt_i, gt_j);

			// cut off the rest of the string at k
			np = split(root, k);
			Node lt_k = np.lt;
			Node gt_k = np.gt;

			// merge the stuff before k with the segment cut out of the string earlier
			merge(lt_k, gt_i_lt_j);
			// merge te stuff after k to complete the string in a new order
			merge(root, gt_k);
		}

		@Override
		public String result() {
			StringBuilder sb = new StringBuilder();
			Stack<Node> toPrint = new Stack<Node>();
			toPrint.push(root);
			Node n;
			while (!toPrint.isEmpty()) {
				n = toPrint.pop();
				if (n == null)
					continue;
				if (n.value != null)
					sb.append(n.value);
				toPrint.push(n.left);
				toPrint.push(n.right);
			}
			return sb.toString();
		}

		NodePair split(Node r, int i) {
			root = r;
			if (r == null)
				return new NodePair(null, null);
			Node n = splayFind(i);
			i -= n.numLeft;
			if (i == 0)
				return cutLeft(n);
			else if (i < n.value.length())
				return cutMiddle(n, i);
			else
				return cutRight(n);
		}

		void merge(Node lt, Node gt) {
			if (gt != null) {
				root = gt;
				if (lt != null) {
					while (gt.left != null)
						gt = gt.left;
					splay(gt);
					gt.left = lt;
					lt.parent = gt;
					updateCounts(gt);
				}
			} else {
				root = lt;
			}
		}

		NodePair cutLeft(Node n) {
            Node lt = n.left;
            Node gt = n;
            if (lt != null) lt.parent = null;
            gt.left = null;
            updateCounts(lt);
            updateCounts(gt);
            return new NodePair(lt, gt);
		}

		NodePair cutMiddle(Node n, int i) {
			String lString = n.value.substring(0, i);
			String rString = n.value.substring(i + 1);
			Node lt = n;
			lt.value = lString;
			Node gt = new Node();
			gt.value = rString;
			gt.right = lt.right;
			lt.right = null;
			if (gt.right != null) gt.right.parent = gt;
			updateCounts(lt);
			updateCounts(gt);
			return new NodePair(lt, gt);
		}

		NodePair cutRight(Node n) {
            Node lt = n;
            Node ge = n.right;
            lt.right = null;
            if (ge != null) ge.parent = null;
            updateCounts(lt);
            updateCounts(ge);
            return new NodePair(lt, ge);
		}

		void updateCounts(Node n) {
			if (n == null)
				return;
			n.numLeft = getCount(n.left);
			n.numUnder = n.numLeft + getCount(n.right);
		}

		int getCount(Node n) {
			if (n == null)
				return 0;
			return n.numUnder + n.value.length();
		}

		Node splayFind(int i) {
			if (root == null)
				return null;
			Node n = find(i);
			splay(n);
			return n;
		}

		Node find(int i) {
			Node n = root;
			Node p = null;
			while (n != null) {
				p = n;
				int remaining = i - n.numLeft;
				// if the current index is somewhere within this node
				if (remaining >= 0 && remaining < n.value.length()) {
					break;
				}
				if (remaining < 0) {
					n = n.left;
				} else {
					i = remaining - n.value.length();
					n = n.right;
				}
			}
			return p;
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
            updateCounts(x);
            updateCounts(y);
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
            updateCounts(x);
            updateCounts(y);
        }

		class Node {
			String value;
			int numLeft;
			int numUnder;
			Node left;
			Node right;
			Node parent;
		}

		class NodePair {
			Node lt;
			Node gt;

			NodePair(Node lt, Node gt) {
				this.lt = lt;
				this.gt = gt;
			}

			NodePair() {
			}
		}
	}

	static class NaiveRope implements StringOrganizer {
		String s;

		public void initialize(String s) {
			this.s = s;
		}

		public void process(int i, int j, int k) {
			String t = s.substring(0, i) + s.substring(j + 1);
			s = t.substring(0, k) + s.substring(i, j + 1) + t.substring(k);
		}

		public String result() {
			return s;
		}
	}

	interface StringOrganizer {
		void initialize(String s);

		void process(int i, int j, int k);

		String result();
	}

	static class Query {
		int i;
		int j;
		int k;

		Query(int i, int j, int k) {
			this.i = i;
			this.j = j;
			this.k = k;
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
