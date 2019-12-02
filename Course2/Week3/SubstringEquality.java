import java.util.*;
import java.io.*;

public class SubstringEquality {
	static public void main(String[] args) throws IOException {
		runSolution();
		// testSolution();
		// stressTestSolution();
	}

	static void runSolution() throws IOException {
		FastScanner in = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);
		String s = in.next();
		SubstringMatcher matcher = new DoubleHashingMatcher();
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
		System.out.println("bbbabbabaa particular failures");
		runTest("bbbabbabaa", new Query[]{
			new Query(0, 1, 2), new Query(0, 4, 2), new Query(0, 1, 1), new Query(0, 2, 1),  new Query(0, 4, 1),
			new Query(0, 5, 1), new Query(0, 7, 1)
		}, new String[]{
			"Yes", "Yes", "Yes", "Yes", "Yes", "Yes", "Yes"
		});

		runTest("trololo",
				new Query[] { new Query(0, 0, 7), new Query(2, 4, 3), new Query(3, 5, 1), new Query(1, 3, 2) },
				new String[] { "Yes", "Yes", "Yes", "No" });

		// length 10
		System.out.println("bbbabbabaa test 10");
		runTest("bbbabbabaa", new Query[] { new Query(0, 0, 10) }, new String[] { "Yes" });
		
		// length 9
		System.out.println("bbbabbabaa test 9");
		runTest("bbbabbabaa", new Query[] { new Query(0, 1, 9), }, new String[] { "No" });
		
		// length 8 (3)
		System.out.println("bbbabbabaa test 8");
		runTest("bbbabbabaa", new Query[] { new Query(0, 1, 8), new Query(0, 2, 8), new Query(1, 2, 8) },
				new String[] { "No", "No", "No" });
		
		// length 7 (6)
		System.out.println("bbbabbabaa test 7");
		runTest("bbbabbabaa", new Query[] { new Query(0, 1, 7), new Query(0, 2, 7), new Query(0, 3, 7),
				new Query(1, 2, 7), new Query(1, 3, 7), new Query(2, 3, 7) },
				new String[] { "No", "No", "No", "No", "No", "No" });
		
		// length 6 (10)
		System.out.println("bbbabbabaa test 6");
		runTest("bbbabbabaa",
				new Query[] { new Query(0, 1, 6), new Query(0, 2, 6), new Query(0, 3, 6), new Query(0, 4, 6),
						new Query(1, 2, 6), new Query(1, 3, 6), new Query(1, 4, 6), new Query(2, 3, 6),
						new Query(2, 4, 6), new Query(3, 4, 6) },
				new String[] { "No", "No", "No", "No", "No", "No", "No", "No", "No", "No" });
		
		// length 5 (15)
		System.out.println("bbbabbabaa test 5");
		runTest("bbbabbabaa",
				new Query[] { new Query(0, 1, 5), new Query(0, 2, 5), new Query(0, 3, 5), new Query(0, 4, 5),
						new Query(0, 5, 5), new Query(1, 2, 5), new Query(1, 3, 5), new Query(1, 4, 5),
						new Query(1, 5, 5), new Query(2, 3, 5), new Query(2, 4, 5), new Query(2, 5, 5),
						new Query(3, 4, 5), new Query(3, 5, 5), new Query(4, 5, 5) },
				new String[] { "No", "No", "No", "No", "No", "No", "No", "No", "No", "No", "No", "No", "No", "No",
						"No" });
		
		// length 4 (21)
		System.out.println("bbbabbabaa test 4");
		runTest("bbbabbabaa", new Query[] { new Query(0, 1, 4), new Query(0, 2, 4), new Query(0, 3, 4),
				new Query(0, 4, 4), new Query(0, 5, 4), new Query(0, 6, 4), new Query(1, 2, 4), new Query(1, 3, 4),
				new Query(1, 4, 4), new Query(1, 5, 4), new Query(1, 6, 4), new Query(2, 3, 4), new Query(2, 4, 4),
				new Query(2, 5, 4), new Query(2, 6, 4), new Query(3, 4, 4), new Query(3, 5, 4), new Query(3, 6, 4),
				new Query(4, 5, 4), new Query(4, 6, 4), new Query(5, 6, 4) },
				new String[] { "No", "No", "No", "No", "No", "No", "No", "No", "Yes", "No", "No", "No", "No", "No",
						"No", "No", "No", "No", "No", "No", "No" });
		
		// length 3 (28)
		System.out.println("bbbabbabaa test 3");
		runTest("bbbabbabaa", new Query[] { new Query(0, 1, 3), new Query(0, 2, 3), new Query(0, 3, 3),
				new Query(0, 4, 3), new Query(0, 5, 3), new Query(0, 6, 3), new Query(0, 7, 3), new Query(1, 2, 3),
				new Query(1, 3, 3), new Query(1, 4, 3), new Query(1, 5, 3), new Query(1, 6, 3), new Query(1, 7, 3),
				new Query(2, 3, 3), new Query(2, 4, 3), new Query(2, 5, 3), new Query(2, 6, 3), new Query(2, 7, 3),
				new Query(3, 4, 3), new Query(3, 5, 3), new Query(3, 6, 3), new Query(3, 7, 3), new Query(4, 5, 3),
				new Query(4, 6, 3), new Query(4, 7, 3), new Query(5, 6, 3), new Query(5, 7, 3), new Query(6, 7, 3) },
				new String[] { "No", "No", "No", "No", "No", "No", "No", "No", "No", "Yes", "No", "No", "No", "No",
						"No", "Yes", "No", "No", "No", "No", "No", "No", "No", "No", "No", "No", "No", "No" });
		
		// length 2 (36)
		System.out.println("bbbabbabaa test 2 - 0th index tests");
		runTest("bbbabbabaa",
				new Query[] { new Query(0, 1, 2), new Query(0, 2, 2), new Query(0, 3, 2), new Query(0, 4, 2),
						new Query(0, 5, 2), new Query(0, 6, 2), new Query(0, 7, 2), new Query(0, 8, 2) },
				new String[] { "Yes", "No", "No", "Yes", "No", "No", "No", "No" });
		System.out.println("bbbabbabaa test 2 - 1+ index tests");
		runTest("bbbabbabaa", new Query[] { new Query(1, 2, 2), new Query(1, 3, 2), new Query(1, 4, 2),
				new Query(1, 5, 2), new Query(1, 6, 2), new Query(1, 7, 2), new Query(1, 8, 2), new Query(2, 3, 2),
				new Query(2, 4, 2), new Query(2, 5, 2), new Query(2, 6, 2), new Query(2, 7, 2), new Query(2, 8, 2),
				new Query(3, 4, 2), new Query(3, 5, 2), new Query(3, 6, 2), new Query(3, 7, 2), new Query(3, 8, 2),
				new Query(4, 5, 2), new Query(4, 6, 2), new Query(4, 7, 2), new Query(4, 8, 2), new Query(5, 6, 2),
				new Query(5, 7, 2), new Query(5, 8, 2), new Query(6, 7, 2), new Query(6, 8, 2), new Query(7, 8, 2) },
				new String[] { "No", "No", "Yes", "No", "No", "No", "No", "No", "No", "Yes", "No", "Yes", "No", "No",
						"No", "Yes", "No", "No", "No", "No", "No", "No", "No", "Yes", "No", "No", "No", "No" });
		
		// length 1 (45)
		System.out.println("bbbabbabaa test 1 - 0th index tests");
		runTest("bbbabbabaa",
				new Query[] { new Query(0, 1, 1), new Query(0, 2, 1), new Query(0, 3, 1), new Query(0, 4, 1),
						new Query(0, 5, 1), new Query(0, 6, 1), new Query(0, 7, 1), new Query(0, 8, 1),
						new Query(0, 9, 1) },
				new String[] { "Yes", "Yes", "No", "Yes", "Yes", "No", "Yes", "No", "No" });
		System.out.println("bbbabbabaa test 1 - 1+ index tests");
		runTest("bbbabbabaa", new Query[] { new Query(1, 2, 1), new Query(1, 3, 1), new Query(1, 4, 1),
				new Query(1, 5, 1), new Query(1, 6, 1), new Query(1, 7, 1), new Query(1, 8, 1), new Query(1, 9, 1),
				new Query(2, 3, 1), new Query(2, 4, 1), new Query(2, 5, 1), new Query(2, 6, 1), new Query(2, 7, 1),
				new Query(2, 8, 1), new Query(2, 9, 1), new Query(3, 4, 1), new Query(3, 5, 1), new Query(3, 6, 1),
				new Query(3, 7, 1), new Query(3, 8, 1), new Query(3, 9, 1), new Query(4, 5, 1), new Query(4, 6, 1),
				new Query(4, 7, 1), new Query(4, 8, 1), new Query(4, 9, 1), new Query(5, 6, 1), new Query(5, 7, 1),
				new Query(5, 8, 1), new Query(5, 9, 1), new Query(6, 7, 1), new Query(6, 8, 1), new Query(6, 9, 1),
				new Query(7, 8, 1), new Query(7, 9, 1), new Query(8, 9, 1) },
				new String[] { "Yes", "No", "Yes", "Yes", "No", "Yes", "No", "No", "No", "Yes", "Yes", "No", "Yes",
						"No", "No", "No", "No", "Yes", "No", "Yes", "Yes", "Yes", "No", "Yes", "No", "No", "No", "Yes",
						"No", "No", "No", "Yes", "Yes", "No", "No", "Yes" });
	}
	
	static void runTest(String s, Query[] queries, String[] expected) {
		SubstringMatcher matcher = new DoubleHashingMatcher();
		matcher.initialize(s);
		String[] actual = new String[queries.length];
		for (int i = 0; i < queries.length; i++) {
			Query q = queries[i];
			actual[i] = toYesNo(matcher.ask(q.a, q.b, q.l));
		}

		String aString = Arrays.toString(actual);
		String eString = Arrays.toString(expected);
		if (!aString.equals(eString))
			System.out.println("Unexpected result, expected: " + eString + ", but got: " + aString);
	}

	static void stressTestSolution() {
		runStressTest("trololo");
		runStressTest("bbbabbabaa");
		runStressTest("trololololololololo");
		runStressTest("abcabcabcabcabcabcabcababababababcdddacbdcdddabcdddabcdddabcdabcdabcdddddabcdabcdbacdbabdbdbdbdbdhabcbdbdbdbdbddddddabcabcabcabcabcabcabcababababababcdddacbdcdddabcdddabcdddabcdabcdabcdddddabcdabcdbacdbabdbdbdbdbdhabcbdbdbdbdbddddddabcabcabcabcabcabcabcababababababcdddacbdcdddabcdddabcdddabcdabcdabcdddddabcdabcdbacdbabdbdbdbdbdhabcbdbdbdbdbddddddabcabcabcabcabcabcabcababababababcdddacbdcdddabcdddabcdddabcdabcdabcdddddabcdabcdbacdbabdbdbdbdbdhabcbdbdbdbdbddddddabcabcabcabcabcabcabcababababababcdddacbdcdddabcdddabcdddabcdabcdabcdddddabcdabcdbacdbabdbdbdbdbdhabcbdbdbdbdbddddddabcabcabcabcabcabcabcababababababcdddacbdcdddabcdddabcdddabcdabcdabcdddddabcdabcdbacdbabdbdbdbdbdhabcbdbdbdbdbddddddabcabcabcabcabcabcabcababababababcdddacbdcdddabcdddabcdddabcdabcdabcdddddabcdabcdbacdbabdbdbdbdbdhabcbdbdbdbdbddddddabcabcabcabcabcabcabcababababababcdddacbdcdddabcdddabcdddabcdabcdabcdddddabcdabcdbacdbabdbdbdbdbdhabcbdbdbdbdbdddddd");
	}

	static void runStressTest(String s){
		System.out.println("Running stress test for string: " + s);
		int l = s.length();
		SubstringMatcher slow = new NaiveSubstringMatcher();
		slow.initialize(s);
		SubstringMatcher fast = new DoubleHashingMatcher();
		fast.initialize(s);

		for (int k = 1; k <= l; k++){
			System.out.println("new substring length: " + k + ". num substrings: " + (l - k + 1));
			for (int i = 0; i <= l - k; i++){
				for (int j = i; j <= l - k; j++){
					boolean fastAnswer = fast.ask(i, j, k);
					boolean slowAnswer = slow.ask(i, j, k);
					if (slowAnswer != fastAnswer){
						System.out.println("Different result between slow and fast for " + i + ", " + j + ", " + k);
					}
				}
			}
		}
	}

	static class DoubleHashingMatcher implements SubstringMatcher {
		String s;
		int p1 = 1000000007;
		int[] p1Hashes;
		int p2 = 1000000009;
		int[] p2Hashes;
		int x = 31;

		@Override
		public void initialize(String s) {
			int l = s.length();
			this.s = s;
			p1Hashes = new int[l];
			p2Hashes = new int[l];

			p1Hashes[0] = s.charAt(0);
			p2Hashes[0] = s.charAt(0);

			for (int i = 1; i < l; i++) {
				char c = s.charAt(i);
				setPrefixHash(p1, p1Hashes, i, c);
				setPrefixHash(p2, p2Hashes, i, c);
			}
		}

		private void setPrefixHash(int p, int[] hashes, int i, char c) {
			long v = (long)x * hashes[i - 1] + c;
			hashes[i] = safeModulo(v, p);
		}

		private int safeModulo(long v, int p) {
			return (int)((v % p) + p) % p;
		}

		@Override
		public boolean ask(int a, int b, int l) {
			return hashesMatch(a, b, l, p1, p1Hashes) && hashesMatch(a, b, l, p2, p2Hashes);
		}

		private boolean hashesMatch(int a, int b, int l, int p, int[] hashes) {
			int y = pow_x(l, p);
			int aHash = getHashCode(a, l, y, p, hashes);
			int bHash = getHashCode(b, l, y, p, hashes);
			return aHash == bHash;
		}

		private int pow_x(int l, int p) {
			int y = 1;
			for (int i = 0; i < l; i++) {
				long v = (long)y * x;
				y = safeModulo(v, p);
			}
			return y;
		}

		private int getHashCode(int a, int l, int y, int p, int[] hashes) {
			long v = hashes[a + l - 1] - (long)y * (a == 0 ? 0 : hashes[a - 1]);
			return safeModulo(v, p);
		}
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
