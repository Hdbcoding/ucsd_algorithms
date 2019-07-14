import java.util.*;

public class FibonacciLastDigit {
    private static int getFibonacciLastDigitFast(int n) {
        if (n <= 1)
            return n;

        int val_2 = 0;
        int val_1 = 1;
        int val = 0;
        for (int i = 2; i < n; ++i) {
            val = (val_2 + val_1) % 10;
            val_2 = val_1;
            val_1 = val;
        }

        return (val_2 + val_1) % 10;
    }

    private static int getFibonacciLastDigitNaive(int n) {
        if (n <= 1)
            return n;

        int previous = 0;
        int current = 1;

        for (int i = 0; i < n - 1; ++i) {
            int tmp_previous = previous;
            previous = current;
            current = tmp_previous + current;
        }

        return current % 10;
    }

    private static void testSolution() {
        long shouldBe9 = getFibonacciLastDigitFast(331);
        if (shouldBe9 != 9)
            System.out.println("Mod for F331 is wrong");

        long shouldBe5 = getFibonacciLastDigitFast(327305);
        if (shouldBe5 != 5)
            System.out.println("Mod for F327305 is wrong");
    }
    
    private static int getInput() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.close();
        return n;
    }
    
    public static void main(String[] args) {
        // testSolution();
        System.out.println(getFibonacciLastDigitFast(getInput()));
    }
}

