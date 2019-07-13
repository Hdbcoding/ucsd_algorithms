import java.util.Scanner;

public class Fibonacci {
  // private static long calc_fib_naive(int n) {
  //   if (n <= 1)
  //     return n;

  //   return calc_fib_naive(n - 1) + calc_fib_naive(n - 2);
  // }

  private static long calc_fib_fast(int n) {
    if (n <= 1)
      return n;
    
    long val_2 = 0; //n - 2
    long val_1 = 1; //n - 1
    for (int i = 2; i < n; i++) {
      long val = val_2 + val_1;
      val_2 = val_1;
      val_1 = val;
    }
    return val_2 + val_1;
  }

  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();
    in.close();

    System.out.println(calc_fib_fast(n));
  }
}
