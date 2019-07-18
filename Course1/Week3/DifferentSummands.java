import java.util.*;

public class DifferentSummands {
    private static List<Integer> optimalSummands(int n) {
        List<Integer> summands = new ArrayList<Integer>();
        if (n <= 2)
            summands.add(n);
        else {
            int summand = 1;
            while (n > 2 * summand) {
                summands.add(summand);
                n -= summand++;
            }
            summands.add(n);
        }
        return summands;
    }

    private static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> summands = optimalSummands(n);
        System.out.println(summands.size());
        scanner.close();
        for (Integer summand : summands) {
            System.out.print(summand + " ");
        }
    }

    private static void testSolution() {
        runTest(0, 1);
        runTest(2, 1);
        runTest(3, 2);
        runTest(8, 3);
        runTest(32, 7);
    }
    
    private static void runTest(int n, int expectedSize) {
        List<Integer> summands = optimalSummands(n);
        if (summands.size() != expectedSize)
            System.out.println("Incorrect value for num summands, expected " + expectedSize + ", but got " + summands.size());
    }
    
    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }
}

