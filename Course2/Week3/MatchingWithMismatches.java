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
        MismatchMatcher matcher = new NaiveMismatchMatcher();
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
        runNextMismatchTests();
        runIsMatchTests();
        runFullTests();
    }

    static void runFullTests() {
        runTest(0, "ababab", "baaa", new int[] {});
        runTest(1, "ababab", "baaa", new int[] { 1 });
        runTest(1, "xabcabc", "ccc", new int[] {});
        runTest(2, "xabcabc", "ccc", new int[] { 1, 2, 3, 4 });
        runTest(3, "aaa", "xxx", new int[] { 0 });
    }

    static void runTest(int k, String s, String t, int[] expected) {
        System.out.println("Checking mismatches: " + k + ", " + s + ", " + t + ".");
        MismatchMatcher matcher = new HashingMismatchMatcher();
        matcher.initialize(s, t);
        List<Integer> actual = matcher.solve(k);
        String expectedString = Arrays.toString(expected);
        String actualString = Arrays.toString(actual.toArray());
        if (!expectedString.equals(actualString))
            System.out.println("Unexpected result. Expected " + expectedString + ", but got " + actualString);
    }

    static void runNextMismatchTests() {
        findNextMismatchTest("ababab", "baaa", 0, 0, 3, 0);
        findNextMismatchTest("ababab", "baaa", 0, 1, 3, 1);
        findNextMismatchTest("ababab", "baaa", 0, 2, 3, 3);
        findNextMismatchTest("ababab", "baaa", 0, 3, 3, 3);
        findNextMismatchTest("ababab", "baaa", 1, 1, 4, 3);
        findNextMismatchTest("ababab", "baaa", 1, 2, 4, 3);
        findNextMismatchTest("ababab", "baaa", 1, 3, 4, 3);
        findNextMismatchTest("ababab", "baaa", 1, 4, 4, 5);
        findNextMismatchTest("ababab", "baaa", 2, 2, 5, 2);
        findNextMismatchTest("ababab", "baaa", 2, 3, 5, 3);
        findNextMismatchTest("ababab", "baaa", 2, 4, 5, 5);
        findNextMismatchTest("ababab", "baaa", 2, 5, 5, 5);
    }

    static void findNextMismatchTest(String s, String t, int orig_l, int l, int r, int expected) {
        System.out.println("Checking mismatch: " + s + ", " + t + ", between " + l + " and " + r + ".");
        HashingMismatchMatcher matcher = new HashingMismatchMatcher();
        matcher.initialize(s, t);
        int actual = matcher.findNextMismatch(orig_l, l, r);
        if (actual != expected)
            System.out.println("Unexpected mismatch. Expected: " + expected + ", but got: " + actual);
    }

    static void runIsMatchTests(){
        isMatchTest("ababab", "baaa", 1, 0, false);
        isMatchTest("ababab", "baaa", 1, 1, true);
        isMatchTest("ababab", "baaa", 1, 2, false);
    }

    static void isMatchTest(String s, String t, int k, int i, boolean expected){
        System.out.println("Checking is match: " + s + ", " + t + ", at " + i + " with " + k + " mismatche(s) allowed.");
        HashingMismatchMatcher matcher = new HashingMismatchMatcher();
        matcher.initialize(s, t);
        boolean actual = matcher.isMatch(k, i);
        if (actual != expected)
            System.out.println("Unexpected match. Expected: " + expected + ", but got: " + actual);
    }

    static int x = 31;
    static int p1 = 1000000007;
    static int p2 = 1000000009;
    static int[] pow_x1;
    static int[] pow_x2;

    static class HashingMismatchMatcher implements MismatchMatcher {
        String s;
        HashHelper s_helper;
        String t;
        HashHelper t_helper;

        @Override
        public void initialize(String s, String t) {
            if (this.s == null || !this.s.equals(s)) {
                this.s = s;
                s_helper = new HashHelper(s);
                s_helper.initialize();
            }

            if (this.t == null || !this.t.equals(t)) {
                this.t = t;
                t_helper = new HashHelper(t);
                t_helper.initialize();
            }

            buildPowersOfX(Math.max(s_helper.l, t_helper.l));
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

        boolean isMatch(int k, int i) {
            int misMatches = 0;
            int end = i + t.length() - 1;
            int orig_i = i;
            while (i <= end) {
                i = findNextMismatch(orig_i, i, end);
                if (i <= end)
                    ++misMatches;
                if (misMatches > k)
                    return false;
                i++;
            }
            return true;
        }

        int findNextMismatch(int orig_l, int l, int r) {
            while (l <= r) {
                int m = (l + r) / 2;
                int t_i = l - orig_l;
                int len = m - l + 1;
                if (matchingSubstrings(l, t_i, len))
                    l = m + 1;
                else
                    r = m - 1;
            }
            return l;
        }

        boolean matchingSubstrings(int s_i, int t_i, int len) {
            int sHash1 = s_helper.getHashCode1(s_i, len);
            int tHash1 = t_helper.getHashCode1(t_i, len);
            if (sHash1 != tHash1) return false;
            
            int sHash2 = s_helper.getHashCode2(s_i, len);
            int tHash2 = t_helper.getHashCode2(t_i, len);
            return sHash2 == tHash2;
        }

        class HashHelper {
            String s;
            int l;
            int[] p1Hashes;
            int[] p2Hashes;

            HashHelper(String s) {
                this.s = s;
                l = s.length();
                p1Hashes = new int[l];
                p2Hashes = new int[l];
            }

            void initialize() {
                p1Hashes[0] = s.charAt(0);
                p2Hashes[0] = s.charAt(0);

                for (int i = 1; i < l; i++) {
                    char c = s.charAt(i);
                    setPrefixHash(p1, p1Hashes, i, c);
                    setPrefixHash(p2, p2Hashes, i, c);
                }
            }

            void setPrefixHash(int p, int[] hashes, int i, char c) {
                long v = (long) x * hashes[i - 1] + c;
                hashes[i] = safeModulo(v, p);
            }

            int getHashCode1(int i, int len) {
                int y = pow_x1[len - 1];
                return getHashCode(i, len, y, p1, p1Hashes);
            }

            int getHashCode2(int i, int len) {
                int y = pow_x2[len - 1];
                return getHashCode(i, len, y, p2, p2Hashes);
            }

            int getHashCode(int i, int len, int y, int p, int[] hashes) {
                long v = hashes[i + len - 1] - (long) y * (i == 0 ? 0 : hashes[i - 1]);
                return safeModulo(v, p);
            }
        }
    }

    static int safeModulo(long v, int p) {
        return (int) ((v % p) + p) % p;
    }

    static void buildPowersOfX(int l) {
        if (pow_x1 == null)
            buildPowersOfXFromScratch(l);
        else if (pow_x1.length < l)
            expandPowersOfX(l);
    }

    static void buildPowersOfXFromScratch(int l) {
        pow_x1 = new int[l];
        pow_x2 = new int[l];
        int y1 = 1;
        int y2 = 1;
        for (int i = 0; i < l; i++) {
            y1 = setPowX(y1, p1, pow_x1, i);
            y2 = setPowX(y2, p2, pow_x2, i);
        }
    }

    static void expandPowersOfX(int l) {
        int[] pow_x1_new = new int[l];
        int[] pow_x2_new = new int[l];
        int i = 0;
        for (; i < pow_x1.length; i++) {
            pow_x1_new[i] = pow_x1[i];
            pow_x2_new[i] = pow_x2[i];
        }
        int y1 = pow_x1[i - 1];
        int y2 = pow_x2[i - 1];
        for (; i < l; i++) {
            y1 = setPowX(y1, p1, pow_x1_new, i);
            y2 = setPowX(y2, p2, pow_x2_new, i);
        }
        pow_x1 = pow_x1_new;
        pow_x2 = pow_x2_new;
    }

    static int setPowX(int y, int p, int[] pow_x, int i) {
        long v = (long) y * x;
        y = safeModulo(v, p);
        pow_x[i] = y;
        return y;
    }

    static class NaiveMismatchMatcher implements MismatchMatcher {
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

        boolean isMatch(int k, int i) {
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

    interface MismatchMatcher {
        void initialize(String s, String t);

        List<Integer> solve(int k);
    }
}
