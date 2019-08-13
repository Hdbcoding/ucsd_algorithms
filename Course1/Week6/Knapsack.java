import java.util.*;

public class Knapsack {
  static int optimalWeight(int W, int[] w) {
    Arrays.sort(w);
    // write you code here
    int[][] values = new int[W + 1][w.length + 1];
    for (int i = 1; i <= w.length; i++) {
      int wi = w[i - 1];
      for (int j = 1; j <= W; j++) {
        values[j][i] = values[j][i - 1];
        if (wi <= j)
          values[j][i] = Math.max(values[j][i], values[j - wi][i - 1] + wi);
      }
    }

    return values[W][w.length];
  }

  public static void main(String[] args) {
    // testSolution();
    runSolution();
  }

  static void runSolution() {
    Scanner scanner = new Scanner(System.in);
    int W, n;
    W = scanner.nextInt();
    n = scanner.nextInt();
    int[] w = new int[n];
    for (int i = 0; i < n; i++) {
      w[i] = scanner.nextInt();
    }
    scanner.close();
    System.out.println(optimalWeight(W, w));
  }

  static void testSolution() {
    runTest(10, new int[] { 1, 4, 8 }, 9);
    runTest(10, new int[] { 5, 4, 4 }, 9);
    runTest(10, new int[] { 7, 4, 4 }, 8);
    runTest(20, new int[] { 5, 7, 12, 18 }, 19);
    runTest(20, new int[] { 5, 7, 12, 18, 25 }, 19);
  }

  static void runTest(int W, int[] w, int expected) {
    int actual = optimalWeight(W, w);
    if (actual != expected) {
      System.out.println(
          "Incorrect value for " + W + ", " + Arrays.toString(w) + ", expected: " + expected + ", but got: " + actual);
    }
  }
}
