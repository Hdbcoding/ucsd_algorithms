import java.util.*;

public class Partition3 {
    private static int partition3(int[] A) {
        // write your code here
        return 0;
    }

    public static void main(String[] args) {
        testSolution();
        // runSolution();
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }
        scanner.close();
        System.out.println(partition3(A));
    }
    
    static void testSolution() {
        
    }
}
