import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class FractionalKnapsack {
    private static class Item implements Comparable<Item> {
        int value;
        int weight;

        Item(int _value, int _weight) {
            this.value = _value;
            this.weight = _weight;
        }

        double getFractionalValue() {
            return (double) this.value / this.weight;
        }

        @Override
        public int compareTo(Item o) {
            double fv = this.getFractionalValue();
            double ofv = o.getFractionalValue();

            //  sort in increasing fractional value
            if (fv > ofv)
                return -1;
            if (fv < ofv)
                return 1;
            return 0;
        }
    }

    private static List<Item> processItems(int[] values, int[] weights) {
        List<Item> items = new ArrayList<Item>(values.length);
        for (int i = 0; i < values.length; i++)
            items.add(new Item(values[i], weights[i]));
        items.sort(null);
        return items;
    }

    private static double getOptimalValue(int capacity, int[] values, int[] weights) {
        List<Item> items = processItems(values, weights);

        double value = 0;
        for (int i = 0; i < values.length; i++) {
            if (capacity == 0)
                return value;
            
            Item best = items.remove(0);
            int weight = Math.min(capacity, best.weight);
            capacity -= weight;
            value += best.getFractionalValue() * weight;
        }

        return value;
    }

    private static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int capacity = scanner.nextInt();
        int[] values = new int[n];
        int[] weights = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
            weights[i] = scanner.nextInt();
        }
        scanner.close();
        System.out.println(getOptimalValue(capacity, values, weights));
    }

    private static void testSolution() {
        runTest(50, new int[] { 60, 100, 120 }, new int[] { 20, 50, 30 }, 180);
        runTest(10, new int[] { 500 }, new int[] { 30 }, 166.6667);
    }
    
    private static void runTest(int capacity, int[] values, int[] weights, double expected) {
        double actual = getOptimalValue(capacity, values, weights);
        if (Math.abs(actual - expected) > 0.001)
            System.out.println("Knapsack test with expected value of " + expected + " returned " + actual);
    }

    public static void main(String args[]) {
        // testSolution();
        runSolution();
    }
} 
