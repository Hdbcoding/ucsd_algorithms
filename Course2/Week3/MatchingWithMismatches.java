import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class MatchingWithMismatches {
    static public void main(String[] args) {
        runSolution();
        testSolution();
    }

    static void runSolution() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        in.lines().forEach(line -> {
            StringTokenizer tok = new StringTokenizer(line);
            int k = Integer.valueOf(tok.nextToken());
            String s = tok.nextToken();
            String t = tok.nextToken();
            List<Integer> ans = solve(k, s, t);
            out.format("%d ", ans.size());
            out.println(ans.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(" ")));
        });
        out.close();
    }

    static void testSolution() {
    }

    static void runTest() {

    }

    static List<Integer> solve(int k, String text, String pattern) {
        ArrayList<Integer> pos = new ArrayList<>();
        return pos;
    }
}
