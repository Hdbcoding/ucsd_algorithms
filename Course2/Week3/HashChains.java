import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class HashChains {
    public static void main(String[] args) throws IOException {
        runSolution();
        // testSolution();
    }

    static void runSolution() throws IOException {
        FastScanner in = new FastScanner();
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        int bucketCount = in.nextInt();
        Table t = new ChainHashTable(bucketCount);
        int queryCount = in.nextInt();
        for (int i = 0; i < queryCount; i++) {
            String result = processQuery(readQuery(in), t);
            if (result != null) {
                out.println(result);
            }
        }
        out.close();
    }

    static void testSolution() {
        runTest(5,
                new Query[] { new Query("add", "world"), new Query("add", "HellO"), new Query("check", 4),
                        new Query("find", "World"), new Query("find", "world"), new Query("del", "world"),
                        new Query("check", 4), new Query("del", "HellO"), new Query("add", "luck"),
                        new Query("add", "GooD"), new Query("check", 2), new Query("del", "good") },
                new String[] { "HellO world", "no", "yes", "HellO", "GooD luck" });
        runTest(4,
                new Query[] { new Query("add", "test"), new Query("add", "test"), new Query("find", "test"),
                        new Query("del", "test"), new Query("find", "test"), new Query("find", "Test"),
                        new Query("add", "Test"), new Query("find", "Test") },
                new String[] { "yes", "no", "no", "yes" });
        runTest(3,
                new Query[] { new Query("check", 0), new Query("find", "help"), new Query("add", "help"),
                        new Query("add", "del"), new Query("add", "add"), new Query("find", "add"),
                        new Query("find", "del"), new Query("del", "del"), new Query("find", "del"),
                        new Query("check", 0), new Query("check", 1), new Query("check", 2) },
                new String[] { "", "no", "yes", "yes", "no", "", "add help", "" });
    }

    static void runTest(int bucketCount, Query[] input, String[] expected) {
        Table t = new ChainHashTable(bucketCount);
        ArrayList<String> results = new ArrayList<>();
        for (Query q : input) {
            String result = processQuery(q, t);
            if (result != null)
                results.add(result);
        }

        String actualString = Arrays.toString(results.toArray());
        String expectedString = Arrays.toString(expected);

        if (!actualString.equals(expectedString))
            System.out.println("Unexpected result - expected: " + expectedString + ", but got: " + actualString);
    }

    static Query readQuery(FastScanner in) throws IOException {
        String type = in.next();
        if (!type.equals("check")) {
            String s = in.next();
            return new Query(type, s);
        } else {
            int ind = in.nextInt();
            return new Query(type, ind);
        }
    }

    static String processQuery(Query query, Table t) {
        switch (query.type) {
        case "add":
            t.add(query.s);
            return null;
        case "del":
            t.del(query.s);
            return null;
        case "find":
            return searchResult(t.contains(query.s));
        case "check":
            return String.join(" ", t.getBucket(query.ind));
        default:
            throw new RuntimeException("Unknown query: " + query.type);
        }
    }

    static class NaiveTable implements Table {
        List<String> elems;
        int bucketCount;

        NaiveTable(int bucketCount) {
            elems = new ArrayList<String>();
            this.bucketCount = bucketCount;
        }

        @Override
        public void add(String s) {
            if (!elems.contains(s))
                elems.add(0, s);
        }

        @Override
        public void del(String s) {
            if (elems.contains(s))
                elems.remove(s);
        }

        @Override
        public boolean contains(String s) {
            return elems.contains(s);
        }

        @Override
        public List<String> getBucket(int bucketId) {
            List<String> matching = new ArrayList<>();
            for (String cur : elems)
                if (hashFunc(cur, bucketCount) == bucketId)
                    matching.add(cur);
            return matching;
        }

    }

    static class ChainHashTable implements Table {
        ArrayList<LinkedList<String>> elems;
        int bucketCount;

        ChainHashTable(int bucketCount){
            this.bucketCount = bucketCount;
            this.elems = new ArrayList<LinkedList<String>>(bucketCount);
            for (int i = 0; i < bucketCount; i++)
                elems.add(new LinkedList<String>());
        }

        @Override
        public void add(String s) {
            LinkedList<String> l = getBucket(s);
            if (!l.contains(s)) l.addFirst(s);
        }

        @Override
        public void del(String s) {
            LinkedList<String> l = getBucket(s);
            if (l.contains(s)) l.remove(s);
        }

        @Override
        public boolean contains(String s) {
            LinkedList<String> l = getBucket(s);
            return l.contains(s);
        }

        @Override
        public List<String> getBucket(int bucketId) {
            return elems.get(bucketId);
        }

        private LinkedList<String> getBucket(String s) {
            int index = hashFunc(s, bucketCount);
            LinkedList<String> l = elems.get(index);
            return l;
        }
    }

    interface Table {
        void add(String s);

        void del(String s);

        boolean contains(String s);

        List<String> getBucket(int bucketId);
    }

    static int multiplier = 263;
    static int prime = 1000000007;

    static int hashFunc(String s, int bucketCount) {
        long hash = 0;
        for (int i = s.length() - 1; i >= 0; --i)
            hash = (hash * multiplier + s.charAt(i)) % prime;
        return (int) hash % bucketCount;
    }

    static String searchResult(boolean wasFound) {
        return wasFound ? "yes" : "no";
    }

    static class Query {
        String type;
        String s;
        int ind;

        public Query(String type, String s) {
            this.type = type;
            this.s = s;
        }

        public Query(String type, int ind) {
            this.type = type;
            this.ind = ind;
        }
    }

    static class FastScanner {
        BufferedReader reader;
        StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
