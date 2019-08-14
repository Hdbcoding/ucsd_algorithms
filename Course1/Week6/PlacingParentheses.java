import java.util.Scanner;

public class PlacingParentheses {
    static class Expression {
        long[] nums;
        char[] ops;

        Expression(long[] _nums, char[] _ops) {
            this.nums = _nums;
            this.ops = _ops;
        }

        static Expression parse(String exp) {
            int length = exp.length();
            long[] nums = new long[length / 2 + 1];
            char[] ops = new char[length / 2];
            int ni = 0;
            int opi = 0;
            for (int i = 0; i < length; i++) {
                if (i % 2 == 0)
                    nums[ni++] = exp.charAt(i) - '0';
                else
                    ops[opi++] = exp.charAt(i);
            }
            return new Expression(nums, ops);
        }
    }

    static long getMaximValue(String exp) {
        Expression parsed = Expression.parse(exp);
        int n = parsed.nums.length;
        long[][] mins = new long[n + 1][n + 1];
        long[][] maxes = new long[n + 1][n + 1];
        for (int i = 1; i < n + 1; i++) {
            mins[i][i] = maxes[i][i] = parsed.nums[i - 1];
        }
        for (int s = 1; s < n; s++) {
            for (int i = 1; i <= n - s; i++) {
                int j = i + s;
                minMax(parsed.ops, mins, maxes, i, j);
            }
        }
        return maxes[1][n];
    }

    static void minMax(char[] ops, long[][] mins, long[][] maxes, int i, int j) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (int k = i; k < j; k++) {
            char ok = ops[k - 1];
            long minik = mins[i][k];
            long minkj = mins[k + 1][j];
            long maxik = maxes[i][k];
            long maxkj = maxes[k + 1][j];
            long a = eval(maxik, maxkj, ok);
            long b = eval(maxik, minkj, ok);
            long c = eval(minik, maxkj, ok);
            long d = eval(minik, minkj, ok);
            min = Math.min(min, Math.min(a, Math.min(b, Math.min(c, d))));
            max = Math.max(max, Math.max(a, Math.max(b, Math.max(c, d))));
        }
        mins[i][j] = min;
        maxes[i][j] = max;
    }

    static long eval(long a, long b, char op) {
        if (op == '+') {
            return a + b;
        } else if (op == '-') {
            return a - b;
        } else if (op == '*') {
            return a * b;
        } else {
            assert false;
            return 0;
        }
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        String exp = scanner.next();
        scanner.close();
        System.out.println(getMaximValue(exp));
    }

    static void testSolution() {
        runTest("1+5", 6);
        runTest("5-8+7*4-8+9", 200);
    }

    static void runTest(String exp, long expected) {
        long actual = getMaximValue(exp);
        if (actual != expected)
            System.out
                    .println("Wrong value for expression " + exp + ", expected: " + expected + ", but got: " + actual);
    }
}
