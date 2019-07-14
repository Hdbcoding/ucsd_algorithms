import java.util.*;

public class GCD {
  private static int gcd_fast(int a, int b) {
    if (b > a)
      return gcd_fast(b, a);
      
    int rem = 0;
    while (b != 0) {
      rem = a % b;
      a = b;
      b = rem;
    }

    return a;
  }

  private static int gcd_naive(int a, int b) {
    int current_gcd = 1;
    for (int d = 2; d <= a && d <= b; ++d) {
      if (a % d == 0 && b % d == 0) {
        if (d > current_gcd) {
          current_gcd = d;
        }
      }
    }

    return current_gcd;
  }
  
  private static void test_solution() {
    int result = gcd_fast(18, 35);
    if (result != 1) {
      System.out.println("wrong result for 18, 35: " + result);
    }

    result = gcd_fast(1344, 217);
    if (result != 7) {
      System.out.println("wrong result for 1344, 217: " + result);
    }

    result = gcd_fast(28851538, 1183019);
    if (result != 17657) {
      System.out.println("wrong result for 18, 35: " + result);
    }
  }

  private static void run_solution() {
    Scanner scanner = new Scanner(System.in);
    int a = scanner.nextInt();
    int b = scanner.nextInt();
    scanner.close();

    System.out.println(gcd_fast(a, b));
  }

  public static void main(String args[]) {
    run_solution();
  }
}
