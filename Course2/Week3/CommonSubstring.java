import java.util.*;
import java.io.*;

public class CommonSubstring {
    static public void main(String[] args) {
        // runSolution();
        testSolution();
    }

    static void runSolution(){
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        in.lines().forEach(line -> {
            StringTokenizer tok = new StringTokenizer(line);
            String s = tok.nextToken();
            String t = tok.nextToken();
            Answer ans = solve(s, t);
            out.format("%d %d %d\n", ans.i, ans.j, ans.len);
        });
        out.close();
    }

    static void testSolution(){

    }

    static void runTest(String s, String t, Answer expected){
        Answer actual = solve(s, t);
        if (!expected.equals(actual))
            System.out.println("Unexpected result for " + s + ", " + t);
    }

    static Answer solve(String s, String t) {
        Answer ans = new Answer(0, 0, 0);
        for (int i = 0; i < s.length(); i++)
            for (int j = 0; j < t.length(); j++)
                for (int len = 0; i + len <= s.length() && j + len <= t.length(); len++)
                    if (len > ans.len && s.substring(i, i + len).equals(t.substring(j, j + len)))
                        ans = new Answer(i, j, len);
        return ans;
    }
    
    static class Answer {
        int i, j, len;
        Answer(int i, int j, int len) {
            this.i = i;
            this.j = j;
            this.len = len;
        }

        @Override
        public boolean equals(Object other){
            if (!(other instanceof Answer)) return false;
            Answer o = (Answer)other;
            return this.i == o.i && this.j == o.j && this.len == o.len;
        }
    }
}
