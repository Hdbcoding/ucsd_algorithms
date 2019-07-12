import java.util.*;
import java.io.*;
import java.lang.*;

public class MaxPairwiseProduct {
    // fast version
    static long getMaxPairwiseProduct(int[] numbers) {
        long max1 = 0;
        long max2 = 0;
        for (int i = 0; i < numbers.length; i++) {
            int num = numbers[i];
            if (num >= max1) {
                max2 = max1;
                max1 = num;
            } else if (num >= max2) {
                max2 = num;
            }
        }

        return max1 * max2;
    }

    // naive solution
    // static int getMaxPairwiseProduct(int[] numbers) {
    //     int max_product = 0;
    //     int n = numbers.length;

    //     for (int first = 0; first < n; ++first) {
    //         for (int second = first + 1; second < n; ++second) {
    //             max_product = Math.max(max_product, numbers[first] * numbers[second]);
    //         }
    //     }

    //     return max_product;
    // }

    // stress test runner
    // public static void main(String[] args) {
    //     Random rng = new Random();

    //     while(true){
    //         int n = rng.nextInt(1000);
    //         int[] numbers = new int[n];
    //         for (int i = 0; i < n; i++){
    //             numbers[i] = rng.nextInt(1000);
    //             System.out.print(numbers[i] + " ");
    //         }
    //         System.out.println();
    //         int naive = getMaxPairwiseProduct(numbers);
    //         int fast = getMaxPairwiseProductFast(numbers);
    //         if (naive == fast) System.out.println("same result");
    //         else {
    //             System.out.println("different results: " + naive + ", " + fast);
    //             break;
    //         }
    //     }
    // }

    // double check big values
    // public static void main(String[] args) {
    //     int[] numbers = new int[2];
    //     numbers[0] = 100000;
    //     numbers[1] = 90000;
    //     System.out.println(getMaxPairwiseProduct(numbers));
    // }

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = scanner.nextInt();
        }
        System.out.println(getMaxPairwiseProduct(numbers));
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

}