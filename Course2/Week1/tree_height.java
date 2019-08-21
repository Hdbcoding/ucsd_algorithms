import java.util.*;
import java.io.*;

public class tree_height {
	class Node {
		int id;
		ArrayList<Node> children;
		Node parent;
		boolean seen;

		Node(int id){
			this.id = id;
			this.children = new ArrayList<Node>();
		}
	}

	class TreeHeight {
		int n;
		int[] parent;

		TreeHeight() {
		}
		
		TreeHeight(int _n, int[] _parent) {
			this.n = _n;
			this.parent = _parent;
		}

		int computeHeight() {
			Node[] tree = new Node[n];
			boolean[] checked = new boolean[n];
			Node root = new Node(-1);
			for (int i = 0; i < n; i++) {
				if (checked[i])
					continue;
				if (parent[i] == -1) {
					root.id = i;
					tree[i] = root;
					continue;
				}

				Node lastChild = tree[i] = new Node(i);
				checked[i] = true;
				int j = parent[i];
				while (true) {
					Node nj;
					if (checked[j])
						nj = tree[j];
					else
						tree[j] = nj = new Node(j);
					nj.children.add(lastChild);
					lastChild.parent = nj;

					if (parent[j] == -1) {
						root = nj;
						break;
					}
					checked[j] = true;
					j = parent[j];
					lastChild = nj;
				}
			}


			int maxHeight = 0;
			// Replace this code with a faster implementation
			for (int vertex = 0; vertex < n; vertex++) {
				int height = 0;
				for (int i = vertex; i != -1; i = parent[i])
					height++;
				maxHeight = Math.max(maxHeight, height);
			}
			return maxHeight;
		}
	}

	static public void main(String[] args) throws IOException {
		new tree_height().testSolution();
		// runSolution();
	}

	void testSolution() {
		runTest(5, new int[] { -1, 0, 4, 0, 3 }, 4);
		runTest(5, new int[] { 4, -1, 4, 1, 1 }, 3);
	}
	
	void runTest(int n, int[] parents, int expected) {
		TreeHeight th = new TreeHeight(n, parents);
		int actual = th.computeHeight();
		if (actual != expected)
			System.out.println("Incorrect height for " + Arrays.toString(parents) + ", expected: " + expected
					+ ", but got " + actual);
	}

	static void runSolution() {
		new Thread(null, new Runnable() {
			public void run() {
				try {
					new tree_height().run();
				} catch (IOException e) {
				}
			}
		}, "1", 1 << 26).start();
	}

	void run() throws IOException {
		TreeHeight tree = read();
		System.out.println(tree.computeHeight());
	}

	TreeHeight read() throws IOException {
		FastScanner in = new FastScanner();
		int n = in.nextInt();
		int[] parent = new int[n];
		for (int i = 0; i < n; i++) {
			parent[i] = in.nextInt();
		}
		return new TreeHeight(n, parent);
	}

	class FastScanner {
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
