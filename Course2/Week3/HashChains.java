import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HashChains {
    static int multiplier = 263;
    static int prime = 1000000007;

    public static void main(String[] args) throws IOException {
        runSolution();
        testSolution();
    }

    static void runSolution() throws IOException {
        List<String> elems = new ArrayList<>();
        FastScanner in = new FastScanner();
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        int bucketCount = in.nextInt();
        int queryCount = in.nextInt();
        for (int i = 0; i < queryCount; i++) {
            String result = processQuery(readQuery(in), elems, bucketCount, out);
            if (result != null) {
                out.println(result);
            }
        }
        out.close();
    }

    static void testSolution() {

    }

    static void runTest() {

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

    static String processQuery(Query query, List<String> elems, int bucketCount, PrintWriter out) {
        switch (query.type) {
        case "add":
            if (!elems.contains(query.s))
                elems.add(0, query.s);
            return null;
        case "del":
            if (elems.contains(query.s))
                elems.remove(query.s);
            return null;
        case "find":
            return searchResult(elems.contains(query.s));
        case "check":
            return listElements(query, elems, bucketCount);
        default:
            throw new RuntimeException("Unknown query: " + query.type);
        }
    }

    static String listElements(Query query, List<String> elems, int bucketCount) {
        List<String> matching = new ArrayList<>();
        for (String cur : elems)
            if (hashFunc(cur, bucketCount) == query.ind)
                matching.add(cur);

        return String.join(" ", matching);
    }

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
