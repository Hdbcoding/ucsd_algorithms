import java.util.Scanner;

public class max_sliding_window {
    static void max_sliding_window_naive(int[] A, int w) {
        for (int i = 0; i < A.length; i++) {
            int max = A[i];
            for (int j = i + 1; j < i + w; j++)
                max = Math.max(max, A[j]);
            System.out.println(max);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++)
            A[i] = scanner.nextInt();
        int w = scanner.nextInt();
        scanner.close();
        max_sliding_window_naive(A, w);
    }
}