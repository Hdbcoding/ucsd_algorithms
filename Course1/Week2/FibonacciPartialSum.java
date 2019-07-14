import java.util.*;

public class FibonacciPartialSum {
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
    
    private static long getFibonacciSumFast(long n) {
        long sum = getFibonacciHugeFast(n + 2, 10) - 1;
        if (sum < 0)
            sum += 10;
        return sum;
    }

    private static long getFibonacciPartialSumFast(long from, long to) {
        long partialSum = getFibonacciSumFast(to) - getFibonacciSumFast(from - 1);
        if (partialSum < 0)
            partialSum += 10;
        return partialSum;
    }

    private static long getFibonacciPartialSumNaive(long from, long to) {
        long sum = 0;

        long current = 0;
        long next = 1;

        for (long i = 0; i <= to; ++i) {
            if (i >= from) {
                sum += current;
            }

            long new_current = next;
            next = next + current;
            current = new_current;
        }

        return sum % 10;
    }
    
    private static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        long from = scanner.nextLong();
        long to = scanner.nextLong();
        scanner.close();
        System.out.println(getFibonacciPartialSumFast(from, to));
    }
    
    private static void testSolution() {
        long result = getFibonacciPartialSumFast(3, 7);
        if (result != 1)
            System.out.println("Sum from 3 to 7 is wrong: " + result);

        result = getFibonacciPartialSumFast(10, 10);
        if (result != 5)
            System.out.println("Sum from 10 to 10 is wrong: " + result);

        result = getFibonacciPartialSumFast(10, 200);
        if (result != 2)
            System.out.println("Sum from 10 to 200 is wrong: " + result);

        result = getFibonacciPartialSumFast(1, 10000000000l);
        if (result != 5)
            System.out.println("Sum from 1 to 10000000000 is wrong: " + result);
    }
    
    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }
}

