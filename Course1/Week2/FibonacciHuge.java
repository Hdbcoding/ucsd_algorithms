import java.util.*;

public class FibonacciHuge {
    private static long getFibonacciHugeFast(long n, long m) {
        if (n <= 1)
            return n;

        long val_2 = 0;
        long val_1 = 1;
        long val = 0;
        for (long i = 2; i < n; ++i) {
            val = (val_2 + val_1) % m;
            val_2 = val_1;
            val_1 = val;
        }

        return (val_2 + val_1) % m;
    }

    private static long getFibonacciHugeNaive(long n, long m) {
        if (n <= 1)
            return n;

        long previous = 0;
        long current = 1;

        for (long i = 0; i < n - 1; ++i) {
            long tmp_previous = previous;
            previous = current;
            current = tmp_previous + current;
        }

        return current % m;
    }
    
    private static Inputs getInputs() {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        long m = scanner.nextLong();
        scanner.close();
        return new Inputs(n, m);
    }

    private static void runSolution() {
        Inputs inputs = getInputs();
        // System.out.println(getFibonacciHugeNaive(inputs.number, inputs.mod));
        System.out.println(getFibonacciHugeFast(inputs.number, inputs.mod));
    }

    private static void testSolution() {
        long shouldBe9 = getFibonacciHugeFast(331, 10);
        if (shouldBe9 != 9)
            System.out.println("Mod for F331 is wrong");

        long shouldBe5 = getFibonacciHugeFast(327305, 10);
        if (shouldBe5 != 5)
            System.out.println("Mod for F327305 is wrong");
    }
    
    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }

    static class Inputs {
        public long number;
        public long mod;

        public Inputs(long _number, long _mod) {
            this.number = _number;
            this.mod = _mod;
        }
    }
}

