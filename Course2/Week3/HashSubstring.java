import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class HashSubstring {
    public static void main(String[] args) throws IOException {
        runSolution();
        // testSolution();
    }

    static void runSolution() throws IOException {
        FastScanner in = new FastScanner();
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        printOccurrences(getOccurencesRobinKarp(readInput(in)), out);
        out.close();
    }

    static void testSolution() {
        runTest(new Data("aba", "abacaba"), new int[] { 0, 4 });
        runTest(new Data("Test", "testTesttesT"), new int[] { 4 });
        runTest(new Data("aaaaa", "baaaaaaa"), new int[] { 1, 2, 3 });
    }

    static void runTest(Data input, int[] expected) {
        List<Integer> actual = getOccurencesRobinKarp(input);
        String eString = Arrays.toString(expected);
        String aString = Arrays.toString(actual.toArray());
        if (!eString.equals(aString))
            System.out.println("Unexpected result for " + input.text + ", " + input.pattern + ". Expected " + eString
                    + ", but got " + aString);
    }

    static List<Integer> getOccurrencesNaive(Data input) {
        String pattern = input.pattern, text = input.text;
        int m = pattern.length(), n = text.length();
        List<Integer> occurrences = new ArrayList<Integer>();
        for (int i = 0; i + m <= n; ++i) {
            if (patternMatched(pattern, text, i))
                occurrences.add(i);
        }
        return occurrences;
    }

    static Data readInput(FastScanner in) throws IOException {
        String pattern = in.next();
        String text = in.next();
        return new Data(pattern, text);
    }

    static void printOccurrences(List<Integer> ans, PrintWriter out) throws IOException {
        for (Integer cur : ans) {
            out.print(cur);
            out.print(" ");
        }
    }

    static boolean patternMatched(String pattern, String text, int i) {
        boolean equal = true;
        for (int j = 0; j < pattern.length(); ++j) {
            if (pattern.charAt(j) != text.charAt(i + j)) {
                equal = false;
                break;
            }
        }
        return equal;
    }

    static List<Integer> getOccurencesRobinKarp(Data input) {
        String pattern = input.pattern, text = input.text;
        List<Integer> result = new ArrayList<>();
        long[] hashes = getSubstringHashes(text, pattern.length());
        long pHash = getHashCode(pattern);

        for (int i = 0; i + pattern.length() <= text.length(); i++) {
            long tHash = hashes[i];
            if (pHash != tHash)
                continue;
            if (patternMatched(pattern, text, i))
                result.add(i);
        }

        return result;
    }

    static long[] getSubstringHashes(String text, int pLength) {
        int indexLastPattern = text.length() - pLength;
        long[] hashes = new long[indexLastPattern + 1];
        hashes[indexLastPattern] = getHashCode(text, indexLastPattern, pLength);

        long y = pow_x(pLength);
        for (int i = indexLastPattern - 1; i >= 0; i--){
            char next = text.charAt(i);
            char prev = text.charAt(i + pLength);
            long v = hashes[i+1] * x + next - y * prev;
            hashes[i] = safeModulo(v);
        }

        return hashes;
    }

    static long pow_x(int pLength) {
        long y = 1;
        for (int i = 0; i < pLength; i++){
            long v = y * x;
            y = safeModulo(v);
        }
        return y;
    }

    static long getHashCode(String pattern) {
        return getHashCode(pattern, 0, pattern.length());
    }

    static long x = 31;
    static long p = 1000000007;
    static long getHashCode(String t, int i, int l) {
        long hash = 0;

        for (int j = l - 1; j >= 0; j--) {
            char c = t.charAt(i + j);
            long v = hash * x + c;
            hash = safeModulo(v);
        }

        return hash;
    }

    private static long safeModulo(long v) {
        return ((v % p) + p) % p;
    }

    static class Data {
        String pattern;
        String text;

        public Data(String pattern, String text) {
            this.pattern = pattern;
            this.text = text;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

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
