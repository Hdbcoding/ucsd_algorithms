import java.util.Scanner;

public class CarFueling {
    static int computeMinRefills(int dist, int tank, int[] stops) {
        int numStops = 0;
        int tankRemaining = tank;
        int lastPosition = 0;
        for (int i = 0; i < stops.length; i++) {
            int distance = stops[i] - lastPosition;
            if (distance > tank)
                return -1;
            if (distance > tankRemaining) {
                ++numStops;
                tankRemaining = tank;
            }
            tankRemaining -= distance;
            lastPosition = stops[i];
        }

        int lastDistance = dist - lastPosition;
        if (lastDistance > tank)
            return -1;
        if (lastDistance > tankRemaining)
            ++numStops;

        return numStops;
    }

    private static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int dist = scanner.nextInt();
        int tank = scanner.nextInt();
        int n = scanner.nextInt();
        int stops[] = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = scanner.nextInt();
        }
        scanner.close();
        System.out.println(computeMinRefills(dist, tank, stops));
    }

    private static void testSolution() {
        runTest(950, 400, new int[] { 200, 375, 550, 750 }, 2);
        runTest(1650, 400, new int[] { 400, 800, 1200, 1600 }, 4);
        runTest(1600, 400, new int[] { 400, 800, 1200, 1600 }, 3);
        runTest(10, 3, new int[] { 1, 2, 5, 9 }, -1);
        runTest(200, 250, new int[] { 100, 150 }, 0);
    }
    
    private static void runTest(int dist, int tank, int[] stops, int expected) {
        int actual = computeMinRefills(dist, tank, stops);
        if (actual != expected)
            System.out.println("Incorrect value for computeMinRefills, expected " + expected + ", but got " + actual);
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }
}
