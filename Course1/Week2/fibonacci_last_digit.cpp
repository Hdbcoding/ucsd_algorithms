#include <iostream>

int get_fibonacci_last_digit_fast(int n) {
    if (n <= 1)
        return n;

    int val_2 = 0;
    int val_1 = 1;
    int val = 0;
    for (int i = 2; i < n; ++i) {
        val = (val_2 + val_1) % 10;
        val_2 = val_1;
        val_1 = val;
    }

    return (val_2 + val_1) % 10;
}

int get_fibonacci_last_digit_naive(int n) {
    if (n <= 1)
        return n;

    int previous = 0;
    int current  = 1;

    for (int i = 0; i < n - 1; ++i) {
        int tmp_previous = previous;
        previous = current;
        current = tmp_previous + current;
    }

    return current % 10;
}

void test_solution() {
    int shouldBe9 = get_fibonacci_last_digit_fast(331);
    if (shouldBe9 != 9)
        std::cout << "Mod for F331 is wrong" << '\n';

    int shouldBe5 = get_fibonacci_last_digit_fast(327305);
    if (shouldBe5 != 5)
        std::cout << "Mod for F327305 is wrong" << '\n';
}

int get_input(){
    int n;
    std::cin >> n;
    return n;
}

int main() {
    // test_solution();
    std::cout << get_fibonacci_last_digit_naive(get_input()) << '\n';
}
