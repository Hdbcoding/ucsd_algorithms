import java.util.*;

public class FibonacciSumSquares {
    private static long getPisanoPeriod(long m) {
        long val_2 = 0;
        long val_1 = 1;
        long val = 0;
        for (int i = 0; i < m * m; i++) {
            val = (val_2 + val_1) % m;
            val_2 = val_1;
            val_1 = val;
            if (val_2 == 0 && val_1 == 1)
                return i + 1;
        }
        return 0; // shouldn't reach this line unless m <= 0
    }

    private static long getFibonacciHugeFast(long n, long m) {
        if (n <= 1)
            return n;

        long remainder = n % getPisanoPeriod(m);
        if (remainder <= 1)
            return remainder;

        long val_2 = 0;
        long val_1 = 1;
        long val = 0;
        for (long i = 2; i < remainder; ++i) {
            val = (val_2 + val_1) % m;
            val_2 = val_1;
            val_1 = val;
        }

        return (val_2 + val_1) % m;
    }

    private static long getFibonacciSumSquaresFast(long n){
        if (n <= 1) return n;

        long current = getFibonacciHugeFast(n, 10);
        long next = getFibonacciHugeFast(n + 1, 10);

        return (current * next) % 10;
    }

    private static long getFibonacciSumSquaresNaive(long n) {
        if (n <= 1)
            return n;

        long previous = 0;
        long current = 1;
        long sum = 1;

        for (long i = 0; i < n - 1; ++i) {
            long tmp_previous = previous;
            previous = current;
            current = tmp_previous + current;
            sum += current * current;
        }

        return sum % 10;
    }
    
    private static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        scanner.close();
        System.out.println(getFibonacciSumSquaresFast(n));
    }

    private static void testSolution() {
        long result = getFibonacciSumSquaresFast(7);
        if (result != 3)
            System.out.println("Sum of squares of first 7 is wrong: " + result);
                
        result = getFibonacciSumSquaresFast(73);
        if (result != 1)
            System.out.println("Sum of squares of first 73 is wrong: " + result);
                
        result = getFibonacciSumSquaresFast(1234567890);
        if (result != 0)
            System.out.println("Sum of squares of first 1234567890 is wrong: " + result);
    }
    
    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }
}

