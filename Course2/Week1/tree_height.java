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

	static int computeHeight(int n, int[] parents) {
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

		// // Replace this code with a faster implementation
		// for (int vertex = 0; vertex < n; vertex++) {
		// int height = 0;
		// for (int i = vertex; i != -1; i = parent[i])
		// height++;
		// maxHeight = Math.max(maxHeight, height);
		// }
		// return maxHeight;
	}

	static public void main(String[] args) throws IOException {
		new tree_height().testSolution();
		// runSolution();
	}

	void testSolution() {
		runTest(5, new int[] { -1, 0, 4, 0, 3 }, 4);
		runTest(5, new int[] { 4, -1, 4, 1, 1 }, 3);
		runTest(10, new int[] { 9, 7, 5, 5, 2, 9, 9, 9, 2, -1 }, 4);
		runTest(100, new int[] { 59, 30, 0, 1, 80, 51, 83, 25, 40, 35, 59, 77, 22, 31, 47, 41, 56, 36, 68, 9, 89, 6, 72,
				44, 5, 54, 23, 63, 70, 87, 90, 22, 8, 18, 71, 92, 97, 29, 48, 97, 14, 62, 55, 89, 15, 15, 82, 55, 88, 2,
				19, 36, 0, 91, 64, 81, 69, 11, 60, 56, 34, 77, 98, 70, 96, 20, 50, 57, 8, 4, 13, 93, 65, 57, 53, 3, 20,
				5, 64, 3, 21, -1, 95, 67, 30, 4, 18, 34, 6, 94, 46, 23, 27, 53, 75, 7, 58, 86, 52, 14 }, 70);
	}

	void runTest(int n, int[] parents, int expected) {
		int actual = computeHeight(n, parents);
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
		System.out.println(computeHeight(n, parent));
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
