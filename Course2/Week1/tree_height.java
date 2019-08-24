import java.util.*;
import java.io.*;

public class tree_height {
	static class Node {
		int id;
		ArrayList<Node> children;

		Node(int id) {
			this.id = id;
			this.children = new ArrayList<Node>();
		}
	}

	static Node generateTree(int n, int[] parents) {
		Node[] tree = new Node[n];
		Node root = new Node(-1);
		for (int i = 0; i < n; i++) {
			if (tree[i] != null)
				continue;
			if (parents[i] == -1) {
				root.id = i;
				tree[i] = root;
				continue;
			}

			Node lastChild = tree[i] = new Node(i);
			int j = parents[i];
			while (true) {
				Node nj;
				if (tree[j] != null)
					nj = tree[j];
				else
					tree[j] = nj = new Node(j);
				nj.children.add(lastChild);

				if (parents[j] == -1) {
					root = nj;
					break;
				}
				j = parents[j];
				lastChild = nj;
			}
		}

		return root;
	}

	// something is wrong with either the traversal or the tree construction, leading to infinite loops
	static int computeHeightByGeneratingTree(int n, int[] parents) {
		Node root = generateTree(n, parents);
		int maxHeight = 0;
		Queue<Node> q = new LinkedList<Node>();
		q.add(root);
		while (true) {
			int count = q.size();
			if (count == 0)
				return maxHeight;
			else
				maxHeight++;

			while (count > 0) {
				count--;
				Node parent = q.poll();
				for (Node child : parent.children)
					q.add(child);
			}
		}
	}

	static int computeHeightMemo(int n, int[] parents) {
		int max = 0;
		int[] heights = new int[n];

		for (int i = 0; i < n; i++) {
			if (heights[i] != 0)
				continue;
			// traverse up the parents of this node, until we find the root. 
			// Use the traversal to calculate the height of this node
			int height = 0;
			for (int p = i; p != -1; p = parents[p]) {
				// if we have seen an ancestor, stop traversing
				if (heights[p] != 0) {
					height += heights[p];
					break;
				}
				height++;
			}
			max = Math.max(max, height);

			// to save time later, fill in any parents with missing heights
			for (int p = i; p != -1; p = parents[p]) {
				if (heights[p] != 0)
					break;
				heights[p] = height--;
			}
		}
		return max;
	}

	static int computeHeightNaive(int n, int[] parents) {
		int maxHeight = 0;
		for (int vertex = 0; vertex < n; vertex++) {
			int height = 0;
			for (int i = vertex; i != -1; i = parents[i])
				height++;
			maxHeight = Math.max(maxHeight, height);
		}
		return maxHeight;
	}

	static public void main(String[] args) throws IOException {
		// new tree_height().testSolution();
		runSolution();
	}

	void testSolution() {
		runTest(5, new int[] { -1, 0, 4, 0, 3 }, 4);
		runTest(5, new int[] { 4, -1, 4, 1, 1 }, 3);
		runTest(10, new int[] { 9, 7, 5, 5, 2, 9, 9, 9, 2, -1 }, 4);
		runTest(100, new int[] { 96, 61, 95, 34, 12, 26, 48, 42, 69, 74, 90, 67, 8, 53, 65, 0, 14, 47, 88, 8, 49, 4, 93,
				75, 6, 29, -1, 62, 87, 12, 42, 52, 1, 46, 4, 83, 14, 75, 72, 95, 15, 86, 29, 53, 85, 78, 65, 31, 5, 96,
				6, 74, 87, 24, 15, 90, 22, 85, 20, 46, 78, 97, 50, 97, 69, 19, 21, 61, 92, 5, 22, 47, 63, 1, 93, 28, 20,
				34, 52, 21, 72, 88, 67, 0, 86, 49, 63, 48, 28, 25, 50, 83, 31, 19, 62, 24, 64, 64, 92, 25 }, 8);
		runTest(100, new int[] { 59, 30, 0, 1, 80, 51, 83, 25, 40, 35, 59, 77, 22, 31, 47, 41, 56, 36, 68, 9, 89, 6, 72,
				44, 5, 54, 23, 63, 70, 87, 90, 22, 8, 18, 71, 92, 97, 29, 48, 97, 14, 62, 55, 89, 15, 15, 82, 55, 88, 2,
				19, 36, 0, 91, 64, 81, 69, 11, 60, 56, 34, 77, 98, 70, 96, 20, 50, 57, 8, 4, 13, 93, 65, 57, 53, 3, 20,
				5, 64, 3, 21, -1, 95, 67, 30, 4, 18, 34, 6, 94, 46, 23, 27, 53, 75, 7, 58, 86, 52, 14 }, 70);
	}

	void runTest(int n, int[] parents, int expected) {
		int actual = computeHeightMemo(n, parents);
		if (actual != expected)
			System.out.println("Incorrect height for " + Arrays.toString(parents) + ", expected: " + expected
					+ ", but got " + actual);
	}

	static void runSolution() throws IOException {
		FastScanner in = new FastScanner();
		int n = in.nextInt();
		int[] parent = new int[n];
		for (int i = 0; i < n; i++) {
			parent[i] = in.nextInt();
		}
		System.out.println(computeHeightMemo(n, parent));
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
