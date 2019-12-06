import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class MatchingWithMismatches {
    static public void main(String[] args) {
        // runSolution();
        testSolution();
    }

    static void runSolution() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        MismatchMathcer matcher = new NaiveMismatchMatcher();
        in.lines().forEach(line -> {
            StringTokenizer tok = new StringTokenizer(line);
            int k = Integer.valueOf(tok.nextToken());
            String s = tok.nextToken();
            String t = tok.nextToken();
            matcher.initialize(s, t);
            List<Integer> ans = matcher.solve(k);
            out.format("%d ", ans.size());
            out.println(ans.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(" ")));
        });
        out.close();
    }

    static void testSolution() {
        runTest(0, "ababab", "baaa", new int[] { });
        runTest(1, "ababab", "baaa", new int[] { 1 });
        runTest(1, "xabcabc", "ccc", new int[] { });
        runTest(2, "xabcabc", "ccc", new int[] { 1, 2, 3, 4 });
        runTest(3, "aaa", "xxx", new int[] { 0 });
    }

    static void runTest(int k, String s, String t, int[] expected) {
        MismatchMathcer matcher = new NaiveMismatchMatcher();
        matcher.initialize(s, t);
        List<Integer> actual = matcher.solve(k);
        String expectedString = Arrays.toString(expected);
        String actualString = Arrays.toString(actual.toArray());
        if (!expectedString.equals(actualString))
            System.out.println("Unexpected result for " + k + ", " + s + ", " + t + ". Expected " + expectedString
                    + ", but got " + actualString);

    }

    static class NaiveMismatchMatcher implements MismatchMathcer {
        String s;
        String t;

        @Override
        public void initialize(String s, String t) {
            this.s = s;
            this.t = t;
        }

        @Override
        public List<Integer> solve(int k) {
            List<Integer> matches = new ArrayList<>();
            for (int i = 0; i <= s.length() - t.length(); i++) {
                if (isMatch(k, i))
                    matches.add(i);
            }
            return matches;
        }

        private boolean isMatch(int k, int i) {
            int misMatches = 0;
            for (int j = 0; j < t.length(); j++) {
                if (t.charAt(j) != s.charAt(i + j))
                    ++misMatches;
                if (misMatches > k)
                    return false;
            }
            return true;
        }

    }

    interface MismatchMathcer {
        void initialize(String s, String t);

        List<Integer> solve(int k);
    }
}
