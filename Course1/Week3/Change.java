import java.util.Scanner;

public class Change {
    private static int getChange(int m) {
        int numCoins = 0;
        int[] denominations = { 10, 5, 1 };
        for (int i = 0; i < 3; i++) {
            int coin = denominations[i];
            if (m >= coin) {
                int coinsUsed = m / coin;
                numCoins += coinsUsed;
                m -= coinsUsed * coin;
            }
        }
        return numCoins;
    }

    private static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        scanner.close();
        System.out.println(getChange(m));
    }

    private static void testSolution() {
        runTest(2, 2);
        runTest(28, 6);
        runTest(30, 3);
        runTest(10, 1);
        runTest(5, 1);
        runTest(1, 1);
    }

    private static void runTest(int input, int expected) {
        int result = getChange(input);
        if (result != expected)
            System.out.println("wrong change for " + input + ": " + result);
    }

    public static void main(String[] args) {
        testSolution();
        // runSolution();
    }
}

