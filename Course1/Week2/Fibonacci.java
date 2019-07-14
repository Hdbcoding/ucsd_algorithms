import java.util.Scanner;

public class Fibonacci {
  private static long calc_fib_naive(int n) {
    if (n <= 1)
      return n;

    return calc_fib_naive(n - 1) + calc_fib_naive(n - 2);
  }

  private static long calc_fib_fast(int n) {
    if (n <= 1)
      return n;

    long val_2 = 0; // n - 2
    long val_1 = 1; // n - 1
    long val = val_2 + val_1;
    for (int i = 2; i < n; i++) {
      val = val_2 + val_1;
      val_2 = val_1;
      val_1 = val;
    }
    return val_2 + val_1;
  }

  private static void testSolution() {
    if (calc_fib_fast(3) != 2) {
      System.out.println("Small is wrong");
      return;
    }
    if (calc_fib_fast(10) != 55) {
      System.out.println("large is wrong");
      return;
    }
    for (int n = 0; n < 20; ++n)
      if (calc_fib_fast(n) != calc_fib_naive(n)) {
        System.out.println("disagrees with naive");
        return;
      }
  }

  private static int getNumber() {
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();
    in.close();
    return n;
  }

  public static void main(String args[]) {
    // System.out.println(calc_fib_naive(getNumber()));
    // testSolution();
    System.out.println(calc_fib_fast(getNumber()));
  }
}
