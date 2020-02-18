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
		Rope rope = new Rope(in.next());
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
		Rope r = new Rope(word);
		for (Query q : queries) {
			r.process(q.i, q.j, q.k);
		}
		String actual = r.result();
		if (!actual.equals(expected))
			System.out.println("Unexpected result on word " + word + ". Expected " + expected + ", but got: " + actual);
	}

	static class Rope {
		String s;

		Rope(String s) {
			this.s = s;
		}

		void process(int i, int j, int k) {
			// Replace this code with a faster implementation
			String t = s.substring(0, i) + s.substring(j + 1);
			s = t.substring(0, k) + s.substring(i, j + 1) + t.substring(k);
		}

		String result() {
			return s;
		}
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
