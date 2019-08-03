import java.util.*;

public class PrimitiveCalculator {
    // starting from 1, multiply by 2, multiply by 3, or add 1
    // find minimum operations to get to n
    static List<Integer> optimal_sequence(int n) {
        int[] optimal = constructOptimalSolutions(n);
        return reconstructSequence(optimal, n);
    }

    private static List<Integer> reconstructSequence(int[] optimal, int n) {
        List<Integer> sequence = new ArrayList<Integer>();
        while (n >= 1) {
            int best = optimal[n];
            sequence.add(n);
            if ((n % 3 == 0) && (optimal[n / 3] == best - 1)) {
                n /= 3;
            } else if ((n % 2 == 0) && (optimal[n / 2] == best - 1)) {
                n /= 2;
            } else {
                n -= 1;
            }
        }
        Collections.reverse(sequence);
        return sequence;
    }

    private static int[] constructOptimalSolutions(int n) {
        int[] optimal = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            boolean has3 = i % 3 == 0;
            boolean has2 = i % 2 == 0;
            int best = optimal[i - 1];
            if (has3 && has2) {
                best = Math.min(best, Math.min(optimal[i / 3], optimal[i / 2]));
            } else if (has3) {
                best = Math.min(best, optimal[i / 3]);
            } else if (has2) {
                best = Math.min(best, optimal[i / 2]);
            }
            optimal[i] = best + 1;
        }
        return optimal;
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.close();
        List<Integer> sequence = optimal_sequence(n);
        System.out.println(sequence.size() - 1);
        for (Integer x : sequence) {
            System.out.print(x + " ");
        }
    }

    static void testSolution() {
        runTest(1, new int[] { 1 });
        runTest(5, new int[] { 1, 2, 4, 5 });
        runTest(96234, new int[] { 1, 3, 9, 10, 11, 22, 66, 198, 594, 1782, 5346, 16038, 16039, 32078, 96234 });
        runTest(949250, new int[] { 1, 3, 9, 27, 81, 243, 244, 732, 2196, 2197, 6591, 6592, 13184, 26368, 52736, 158208, 474624, 474625, 949250 });
    }

    static void runTest(int n, int[] expected) {
        List<Integer> actual = optimal_sequence(n);
        if (actual.size() != expected.length)
            System.out.println("Different number of operations for n = " + n + ", exptected " + expected.length + " - "
                    + Arrays.toString(expected) + ", but got " + actual.size() + " - " + actual);
        else
            checkSequenceEqual("Different sequence for n = " + n, actual, expected);
    }

    static void checkSequenceEqual(String message, List<Integer> a, int[] b) {
        if (!sequenceEqual(a, b))
            System.out.println(message + ", expected: " + Arrays.toString(b) + ", but got: " + a);
    }

    static boolean sequenceEqual(List<Integer> a, int[] b) {
        if (a.size() != b.length)
            return false;

        for (int i = 0; i < a.size(); i++)
            if (a.get(i) != b[i])
                return false;

        return true;
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }
}
