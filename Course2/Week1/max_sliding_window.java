import java.util.Scanner;

public class max_sliding_window {
    static int[] max_sliding_window_naive(int[] A, int w) {
        int[] solutions = new int[A.length - w + 1];
        int k = 0;
        for (int i = 0; i < A.length; i++) {
            int max = A[i];
            for (int j = i + 1; j < i + w; j++)
                max = Math.max(max, A[j]);
            solutions[k++] = max;
        }
        return solutions;
    }

    public static void main(String[] args) {
        runSolution();
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++)
            A[i] = scanner.nextInt();
        int w = scanner.nextInt();
        scanner.close();
        int[] solutions = max_sliding_window_naive(A, w);
        for (int s : solutions)
            System.out.println(s);
    }
}