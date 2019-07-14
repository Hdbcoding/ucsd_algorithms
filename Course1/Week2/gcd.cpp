#include <iostream>

int gcd_fast(int a, int b){
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

int gcd_naive(int a, int b) {
  int current_gcd = 1;
  for (int d = 2; d <= a && d <= b; d++) {
    if (a % d == 0 && b % d == 0) {
      if (d > current_gcd) {
        current_gcd = d;
      }
    }
  }
  return current_gcd;
}

int main() {
  int a, b;
  std::cin >> a >> b;
  std::cout << gcd_naive(a, b) << std::endl;
  return 0;
}
