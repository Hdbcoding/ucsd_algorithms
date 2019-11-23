import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HashChains {

    // FastScanner in;
    // PrintWriter out;
    // // store all strings in one list
    // List<String> elems;
    // // for hash function
    // int bucketCount;
    // int multiplier = 263;
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
            processQuery(readQuery(in), elems, out);
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

    static void processQuery(Query query, List<String> elems, PrintWriter out) {
        switch (query.type) {
        case "add":
            if (!elems.contains(query.s))
                elems.add(0, query.s);
            break;
        case "del":
            if (elems.contains(query.s))
                elems.remove(query.s);
            break;
        case "find":
            printSearchResult(elems.contains(query.s), out);
            break;
        case "check":
            printElements(query, elems, out);
            break;
        default:
            throw new RuntimeException("Unknown query: " + query.type);
        }
    }

    private static void printElements(Query query, List<String> elems, PrintWriter out) {
        for (String cur : elems)
            if (hashFunc(cur) == query.ind)
                out.print(cur + " ");
        out.println();
        // Uncomment the following if you want to play with the program interactively.
        // out.flush();
    }

    static int hashFunc(String s, int multiplier, int prime, int bucketCount) {
        long hash = 0;
        for (int i = s.length() - 1; i >= 0; --i)
            hash = (hash * multiplier + s.charAt(i)) % prime;
        return (int) hash % bucketCount;
    }

    static void printSearchResult(boolean wasFound, PrintWriter out) {
        out.println(wasFound ? "yes" : "no");
        // Uncomment the following if you want to play with the program interactively.
        // out.flush();
    }

    // public void processQueries() throws IOException {
    // elems = new ArrayList<>();
    // in = new FastScanner();
    // out = new PrintWriter(new BufferedOutputStream(System.out));
    // bucketCount = in.nextInt();
    // int queryCount = in.nextInt();
    // for (int i = 0; i < queryCount; ++i) {
    // processQuery(readQuery());
    // }
    // out.close();
    // }

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
