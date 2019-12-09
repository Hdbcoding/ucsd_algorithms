import java.util.*;
import java.io.*;

public class tree_orders {

	static public void main(String[] args) {
		// runSolutionWithTallStack();
		testSolution();
	}

	static void runSolutionWithTallStack() {
		new Thread(null, new Runnable() {
			public void run() {
				try {
					runSolution();
				} catch (IOException e) {
				}
			}
		}, "1", 1 << 26).start();
	}

	static void runSolution() throws IOException {
		ProblemInstance p = readProblem();
		BinarySearchTree tree = new BinarySearchTree();
		tree.AssembleTree(p);
		print(tree.inOrder());
		print(tree.preOrder());
		print(tree.postOrder());
	}

	static ProblemInstance readProblem() throws IOException {
		FastScanner in = new FastScanner();
		int n = in.nextInt();
		int[] key = new int[n];
		int[] left = new int[n];
		int[] right = new int[n];
		for (int i = 0; i < n; i++) {
			key[i] = in.nextInt();
			left[i] = in.nextInt();
			right[i] = in.nextInt();
		}
		return new ProblemInstance(key, left, right);
	}

	static void print(List<Integer> x) {
		for (Integer a : x) {
			System.out.print(a + " ");
		}
		System.out.println();
	}

	static void testSolution() {
		runTest(new int[] { 4, 2, 5, 1, 3 }, new int[] { 1, 3, -1, -1, -1 }, new int[] { 2, 4, -1, -1, -1 },
				new int[] { 1, 2, 3, 4, 5 }, new int[] { 4, 2, 1, 3, 5 }, new int[] { 1, 3, 2, 5, 4 });
		runTest(new int[] { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90 }, new int[] { 7, -1, -1, 8, 3, -1, 1, 5, -1, -1 },
				new int[] { 2, -1, 6, 9, -1, -1, -1, 4, -1, -1 }, new int[] { 50, 70, 80, 30, 90, 40, 0, 20, 10, 60 },
				new int[] { 0, 70, 50, 40, 30, 80, 90, 20, 60, 10 },
				new int[] { 50, 80, 90, 30, 40, 70, 10, 60, 20, 0 });
	}

	static void runTest(int[] key, int[] left, int[] right, int[] eInOrder, int[] ePreOrder, int[] ePostOrder) {
		ProblemInstance p = new ProblemInstance(key, left, right);
		BinarySearchTree tree = new BinarySearchTree();
		tree.AssembleTree(p);
		Object[] aInOrder = tree.inOrder().toArray();
		Object[] aPreOrder = tree.preOrder().toArray();
		Object[] aPostOrder = tree.postOrder().toArray();

		complainAboutMismatch(eInOrder, aInOrder, "inOrder");
		complainAboutMismatch(ePreOrder, aPreOrder, "preOrder");
		complainAboutMismatch(ePostOrder, aPostOrder, "postOrder");
	}

	private static void complainAboutMismatch(int[] eInOrder, Object[] aInOrder, String name) {
		String expected = Arrays.toString(eInOrder);
		String actual = Arrays.toString(aInOrder);
		if (!expected.equals(actual))
			System.out.println("Unexpected result for " + name + ". Expected: " + expected + ", but got: " + actual);
	}

	static class ProblemInstance {
		int[] key, left, right;

		ProblemInstance(int[] key, int[] left, int[] right) {
			this.key = key;
			this.left = left;
			this.right = right;
		}
	}

	static class BinarySearchTree {
		TreeNode root;

		void AssembleTree(ProblemInstance p) {
		}

		List<Integer> inOrder() {
			return new ArrayList<Integer>();
		}

		List<Integer> preOrder() {
			return new ArrayList<Integer>();
		}

		List<Integer> postOrder() {
			return new ArrayList<Integer>();
		}
	}

	static class TreeNode {
		int value;
		TreeNode left;
		TreeNode right;

		public TreeNode(int value) {
			this.value = value;
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
