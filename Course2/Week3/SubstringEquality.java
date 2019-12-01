import java.util.*;
import java.io.*;

public class SubstringEquality {
	static public void main(String[] args) throws IOException {
		runSolution();
		testSolution();
	}

	static void runSolution() throws IOException {
		FastScanner in = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);
		String s = in.next();
		SubstringMatcher matcher = new NaiveSubstringMatcher();
		matcher.initialize(s);
		int q = in.nextInt();
		for (int i = 0; i < q; i++) {
			int a = in.nextInt();
			int b = in.nextInt();
			int l = in.nextInt();
			out.println(toYesNo(matcher.ask(a, b, l)));
		}
		out.close();
	}

	static String toYesNo(boolean v) {
		return v ? "Yes" : "No";
	}

	static void testSolution() {
		runTest("trololo",
				new Query[] { new Query(0, 0, 7), new Query(2, 4, 3), new Query(3, 5, 1), new Query(1, 3, 2) },
				new String[] { "Yes", "Yes", "Yes", "No" });
	}

	static void runTest(String s, Query[] queries, String[] expected) {
		SubstringMatcher matcher = new NaiveSubstringMatcher();
		matcher.initialize(s);
		String[] actual = new String[queries.length];
		for (int i = 0; i < queries.length; i++) {
			Query q = queries[i];
			actual[i] = toYesNo(matcher.ask(q.a, q.b, q.l));
		}

		String aString = Arrays.toString(actual);
		String eString = Arrays.toString(expected);
		if (aString != eString)
			System.out.println("Unexpected result, expected: " + eString + ", but got: " + aString);
	}

	static class NaiveSubstringMatcher implements SubstringMatcher {
		String s;

		@Override
		public void initialize(String s) {
			this.s = s;
		}

		@Override
		public boolean ask(int a, int b, int l) {
			return s.substring(a, a + l).equals(s.substring(b, b + l));
		}

	}

	interface SubstringMatcher {
		void initialize(String s);

		boolean ask(int a, int b, int l);
	}

	static class Query {
		int a;
		int b;
		int l;

		Query(int a, int b, int l) {
			this.a = a;
			this.b = b;
			this.l = l;
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
