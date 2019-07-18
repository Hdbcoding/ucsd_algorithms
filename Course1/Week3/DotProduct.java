import java.util.*;

public class DotProduct {
    private static long maxDotProduct(int[] a, int[] b) {
        //write your code here
        long result = 0;
        Arrays.sort(a);
        Arrays.sort(b);
        for (int i = a.length-1; i >= 0; i--) {
            result += (long)a[i] * b[i];
        }
        return result;
    }

    private static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = scanner.nextInt();
        }
        scanner.close();
        System.out.println(maxDotProduct(a, b));
    }
    
    private static void testSolution() {
        runTest(new int[] { 23 }, new int[] { 39 }, 897);
        runTest(new int[] { 1, 3, -5 }, new int[] { -2, 4, 1 }, 23);
    }
    
    private static void runTest(int[] a, int[] b, long expected) {
        long actual = maxDotProduct(a, b);
        if (actual != expected)
            System.out.println("Incorrect value for dot product, expected " + expected + ", but got " + actual);
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }
}

