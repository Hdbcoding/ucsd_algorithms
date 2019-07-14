import java.util.*;

public class LCM {
  private static int gcd(int a, int b) {
    if (b > a)
      return gcd(b, a);

    int rem = 0;
    while (b != 0) {
      rem = a % b;
      a = b;
      b = rem;
    }

    return a;
  }
  
  private static long lcm_fast(int a, int b){
    return ((long) a * b) / gcd(a, b);
  }

  private static long lcm_naive(int a, int b) {
    for (long l = 1; l <= (long) a * b; ++l)
      if (l % a == 0 && l % b == 0)
        return l;

    return (long) a * b;
  }

  private static void run_solution() {
    Scanner scanner = new Scanner(System.in);
    int a = scanner.nextInt();
    int b = scanner.nextInt();
    scanner.close();

    System.out.println(lcm_fast(a, b));
  }

  private static void test_solution() {
    long result = lcm_fast(6, 8);
    if (result != 24) {
      System.out.println("wrong result for 6, 8: " + result);
    }
    
    result = lcm_fast(761457, 614573);
    if (result != 467970912861l){
      System.out.println("wrong result for 761457, 614573: " + result);
    }
    
    result = lcm_fast(14159572, 63967072);
    if (result != 226436590403296l){
      System.out.println("wrong result for 6, 8: " + result);
    }
  }

  public static void main(String args[]) {
    run_solution();
    // test_solution();
  }
}
