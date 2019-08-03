import java.util.Scanner;

public class ChangeDP {
    //note, denominations are 4, 3, 1
    static int getChange(int m) {
        int[] change = new int[Math.max(m + 1, 5)];
        change[0] = 0;
        change[1] = 1;
        change[2] = 2;
        change[3] = 1;
        change[4] = 1;

        for (int i = 5; i <= m; i++) {
            change[i] = Math.min(change[i - 4] + 1, Math.min(change[i - 3] + 1, change[i - 1] + 1));
        }

        return change[m];
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        scanner.close();
        System.out.println(getChange(m));
    }

    static void testSolution() {
        runTest(2, 2);
        runTest(6, 2);
        runTest(7, 2);
        runTest(8, 2);
        runTest(9, 3);
        runTest(34, 9);
    }
    
    static void runTest(int m, int expected) {
        int actual = getChange(m);
        if (actual != expected) System.out.println("Incorrect change calculated for " + m 
            + ", expected: " + expected + ", but got: " + actual);
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }
}
