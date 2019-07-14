import java.util.*;

public class FibonacciHuge {
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
        long result = getFibonacciHugeFast(331, 10);
        if (result != 9)
            System.out.println("Mod for F331_10 is wrong");

        result = getFibonacciHugeFast(327305, 10);
        if (result != 5)
            System.out.println("Mod for F327305_10 is wrong");

        result = getFibonacciHugeFast(239, 1000);
        if (result != 161)
            System.out.println("Mod for F239_1000 is wrong");

        result = getFibonacciHugeFast(2816213588l, 239);
        if (result != 151)
            System.out.println("Mod for F2816213588_239 is wrong");
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

