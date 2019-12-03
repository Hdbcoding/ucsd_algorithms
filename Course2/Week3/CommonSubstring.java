import java.util.*;
import java.io.*;

public class CommonSubstring {
    static public void main(String[] args) {
        // runSolution();
        // testSolution();
        runStressTest();
    }

    static void runSolution() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        CommonSubstringFinder finder = new NaiveSubstringFinder();
        in.lines().forEach(line -> {
            StringTokenizer tok = new StringTokenizer(line);
            String s = tok.nextToken();
            String t = tok.nextToken();
            finder.initialize(s, t);
            Answer ans = finder.solve();
            out.format("%d %d %d\n", ans.i, ans.j, ans.len);
        });
        out.close();
    }

    static void testSolution() {
        runTest("cool", "toolbox", new Answer(1, 1, 3));
        runTest("aaa", "bb", new Answer(0, 1, 0));
        runTest("aabaa", "babbaab", new Answer(0, 4, 3));
    }

    static void runTest(String s, String t, Answer expected) {
        CommonSubstringFinder finder = new BinarySubstringFinder();
        finder.initialize(s, t);
        Answer actual = finder.solve();
        complainAboutDiscrepancy(s, t, expected, actual);
    }

    static void complainAboutDiscrepancy(String s, String t, Answer expected, Answer actual) {
        if (!expected.equals(actual)) {
            boolean sameLength = expected.len == actual.len;
            boolean isValid = validSolution(s, t, actual);

            if (!(sameLength && isValid))
                System.out.println(
                        "Unexpected result for " + s + ", " + t + ". Expected: " + expected + ", but got: " + actual);
        }
    }

    static void runStressTest() {
        CommonSubstringFinder naive = new NaiveSubstringFinder();
        CommonSubstringFinder fast = new BinarySubstringFinder();
        for (int i = 0; i < 500; i++) {
            if ((i % 10) == 0){
                System.out.println(i);
            }
            String s = getRandomString();
            String t = getRandomString();

            naive.initialize(s, t);
            fast.initialize(s, t);

            Answer expected = naive.solve();
            Answer actual = fast.solve();

            complainAboutDiscrepancy(s, t, expected, actual);
        }
    }

    static String getRandomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        int targetStringLength = random.nextInt(500) + 1;
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) 
            (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    static class HashingSubstringFinder implements CommonSubstringFinder {
        String s;
        String t;
        int x = 31;
        int p1 = 1000000007;
        int p2 = 1000000009;
        int[] s_p1Hashes;
        int[] s_p2Hashes;
        int[] t_p1Hashes;
        int[] t_p2Hashes;
        int[] pow_x1;
        int[] pow_x2;

        @Override
        public void initialize(String s, String t) {
            // TODO Auto-generated method stub
        }

        @Override
        public Answer solve() {
            // TODO Auto-generated method stub
            return null;
        }

    }

    static class BinarySubstringFinder implements CommonSubstringFinder {
        String s;
        String t;

        @Override
        public void initialize(String s, String t) {
            this.s = s;
            this.t = t;
        }

        @Override
        public Answer solve() {
            Answer ans = new Answer(0, 0, 0);
            int shortest = 0;
            int longest = Math.min(s.length(), t.length());

            while (shortest <= longest) {
                int m = (shortest + longest) / 2;
                Answer best = findSubstring(m);
                if (best != null) {
                    ans = best;
                    shortest = m + 1;
                } else {
                    longest = m - 1;
                }
            }
            return ans;
        }

        private Answer findSubstring(int len) {
            for (int i = 0; i < s.length() - len + 1; i++) {
                for (int j = 0; j < t.length() - len + 1; j++) {
                    if (validSolution(s, t, i, j, len))
                        return new Answer(i, j, len);
                }
            }
            return null;
        }
    }

    static class NaiveSubstringFinder implements CommonSubstringFinder {
        String s;
        String t;

        @Override
        public void initialize(String s, String t) {
            this.s = s;
            this.t = t;
        }

        @Override
        public Answer solve() {
            Answer ans = new Answer(0, 0, 0);
            for (int i = 0; i < s.length(); i++)
                for (int j = 0; j < t.length(); j++)
                    for (int len = 0; i + len <= s.length() && j + len <= t.length(); len++)
                        if (len > ans.len && validSolution(s, t, i, j, len))
                            ans = new Answer(i, j, len);
            return ans;
        }
    }

    interface CommonSubstringFinder {
        void initialize(String s, String t);

        Answer solve();
    }

    private static boolean validSolution(String s, String t, Answer ans) {
        return validSolution(s, t, ans.i, ans.j, ans.len);
    }

    static boolean validSolution(String s, String t, int i, int j, int len) {
        return s.substring(i, i + len).equals(t.substring(j, j + len));
    }

    static class Answer {
        int i, j, len;

        Answer(int i, int j, int len) {
            this.i = i;
            this.j = j;
            this.len = len;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Answer))
                return false;
            Answer o = (Answer) other;
            return this.i == o.i && this.j == o.j && this.len == o.len;
        }

        @Override
        public String toString() {
            return "{ i: " + i + ", j: " + j + ", len: " + len + " }";
        }
    }
}
