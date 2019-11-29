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
		int q = in.nextInt();
		for (int i = 0; i < q; i++) {
			int a = in.nextInt();
			int b = in.nextInt();
			int l = in.nextInt();
			out.println(ask(s, a, b, l) ? "Yes" : "No");
		}
		out.close();
	}

	static void testSolution(){

	}

	static void runTest(){

	}

	static boolean ask(String s, int a, int b, int l) {
		return s.substring(a, a + l).equals(s.substring(b, b + l));
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
