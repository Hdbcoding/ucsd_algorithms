import java.util.*;
import java.io.*;

public class CommonSubstring {
    static public void main(String[] args) {
        runSolution();
        // testSolution();
        // runStressTest();
    }

    static void runSolution() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        CommonSubstringFinder finder = new HashingSubstringFinder();
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
        runTest("aabaababaacc", "cc", new Answer(10, 0, 2));
        runTest("cc", "aabaababaacc", new Answer(0, 10, 2));
    }

    static void runTest(String s, String t, Answer expected) {
        CommonSubstringFinder finder = new HashingSubstringFinder();
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
        CommonSubstringFinder naive = new BinarySubstringFinder();
        CommonSubstringFinder fast = new HashingSubstringFinder();
        for (int i = 0; i < 1000; i++) {
            if ((i % 10) == 0) {
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
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    static int x = 31;
    static int p1 = 1000000007;
    static int p2 = 1000000009;
    static int[] pow_x1;
    static int[] pow_x2;

    static class HashingSubstringFinder implements CommonSubstringFinder {
        String s;
        HashHelper s_helper;
        String t;
        HashHelper t_helper;

        @Override
        public void initialize(String s, String t) {
            this.s = s;
            s_helper = new HashHelper(s);
            s_helper.initialize();

            this.t = t;
            t_helper = new HashHelper(t);
            t_helper.initialize();

            buildPowersOfX(Math.max(s_helper.l, t_helper.l));
        }

        @Override
        public Answer solve() {
            Answer ans = new Answer(0, 0, 0);
            int shortest = 1;
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

        Answer findSubstring(int l) {
            int y1 = pow_x1[l - 1];
            int y2 = pow_x2[l - 1];
            DoubleHashSet set = buildHashSet(s_helper, l, y1, y2);
            return findMatchingSubstring(set, t_helper, l, y1, y2);
        }

        DoubleHashSet buildHashSet(HashHelper helper, int l, int y1, int y2) {
            int numHashes = helper.l - l + 1;
            DoubleHashSet set = new DoubleHashSet(numHashes);

            for (int i = 0; i < numHashes; i++) {
                DoubleHash dh = getDoubleHash(helper, y1, y2, i, l);
                set.add(dh.h1, dh.h2, i, l);
            }

            return set;
        }

        DoubleHash getDoubleHash(HashHelper helper, int y1, int y2, int i, int l) {
            int h1 = getHashCode(helper.p1Hashes, p1, y1, i, l);
            int h2 = getHashCode(helper.p2Hashes, p2, y2, i, l);
            return new DoubleHash(h1, h2);
        }

        int getHashCode(int[] hashes, int p, int y, int i, int l) {
            long v = hashes[i + l - 1] - (long) y * (i == 0 ? 0 : hashes[i - 1]);
            return safeModulo(v, p);
        }

        private Answer findMatchingSubstring(DoubleHashSet set, HashHelper helper, int l, int y1, int y2) {
            int numHashes = helper.l - l + 1;
            for (int j = 0; j < numHashes; j++) {
                DoubleHash dh = getDoubleHash(helper, y1, y2, j, l);
                HashRecord r = set.get(dh.h1, dh.h2);
                if (r != null)
                    return new Answer(r.i, j, l);
            }
            return null;
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
        }

        class DoubleHash {
            int h1;
            int h2;

            DoubleHash(int h1, int h2) {
                this.h1 = h1;
                this.h2 = h2;
            }
        }

        class DoubleHashSet {
            ArrayList<LinkedList<HashRecord>> set1;
            ArrayList<LinkedList<HashRecord>> set2;
            int bucketCount;

            DoubleHashSet(int bucketCount) {
                this.bucketCount = bucketCount;
                this.set1 = new ArrayList<LinkedList<HashRecord>>(bucketCount);
                this.set2 = new ArrayList<LinkedList<HashRecord>>(bucketCount);
                for (int i = 0; i < bucketCount; i++) {
                    set1.add(new LinkedList<HashRecord>());
                    set2.add(new LinkedList<HashRecord>());
                }
            }

            void add(int h1, int h2, int i, int l) {
                addToBucket(set1, h1, i, l);
                addToBucket(set2, h2, i, l);
            }

            HashRecord get(int h1, int h2) {
                LinkedList<HashRecord> l1 = getBucket(set1, h1);
                LinkedList<HashRecord> l2 = getBucket(set2, h2);

                HashRecord r1 = getFirst(l1, h1);
                HashRecord r2 = getFirst(l2, h2);
                if (r1 == null || r2 == null)
                    return null;
                if (r1.i != r2.i)
                    return null;
                return r1;
            }

            private HashRecord getFirst(LinkedList<HashRecord> l, int h) {
                for (HashRecord r : l)
                    if (r.hash == h)
                        return r;
                return null;
            }

            void addToBucket(ArrayList<LinkedList<HashRecord>> set, int h, int i, int l) {
                LinkedList<HashRecord> l1 = getBucket(set, h);
                if (!listContains(l1, h))
                    l1.addFirst(new HashRecord(h, i, l));
            }

            private boolean listContains(LinkedList<HashRecord> l1, int h) {
                return l1.stream().anyMatch(r -> r.hash == h);
            }

            LinkedList<HashRecord> getBucket(ArrayList<LinkedList<HashRecord>> set, int hash) {
                return set.get(hash % bucketCount);
            }
        }

        class HashRecord {
            int hash;
            int i;
            int l;

            HashRecord(int hash, int i, int l) {
                this.hash = hash;
                this.i = i;
                this.l = l;
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

        Answer findSubstring(int len) {
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

    static boolean validSolution(String s, String t, Answer ans) {
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
