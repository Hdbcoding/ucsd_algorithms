import java.util.*;

public class LargestNumber {
    private static String largestNumber(String[] a) {
        String result = "";
        Arrays.sort(a, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                boolean o1Longer = o1.length() > o2.length();
                int length = o1Longer ? o2.length() : o1.length();
                for (int i = 0; i < length; i++) {
                    if (o1.charAt(i) > o2.charAt(i))
                        return 1;
                    if (o1.charAt(i) < o2.charAt(i))
                        return -1;
                }
                // "1" should be ">" than "10"
                return 0;
            }
        });
        for (int i = a.length - 1; i >= 0; i--) {
            result += a[i];
        }
        return result;
    }

    private static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String[] a = new String[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.next();
        }
        scanner.close();
        System.out.println(largestNumber(a));
    }

    private static void testSolution() {
        runTest(new String[] { "21", "22" }, "2221");
        runTest(new String[] { "21", "2" }, "221");
        runTest(new String[] { "9", "4", "6", "1", "9" }, "99641");
        runTest(new String[] { "23", "39", "92" }, "923923");
        runTest(new String[] { "99", "901", "909" }, "99909901");
        runTest(new String[] { "2", "8", "2", "3", "6", "4", "1", "1", "10", "6", "3", "3", "6", "1", "3", "8", "4",
                "6", "1", "10", "8", "4", "10", "4", "1", "3", "2", "3", "2", "6", "1", "5", "2", "9", "8", "5", "10",
                "8", "7", "9", "6", "4", "2", "6", "3", "8", "8", "9", "8", "2", "9", "10", "3", "10", "7", "5", "7",
                "1", "7", "5", "1", "4", "7", "6", "1", "10", "5", "4", "8", "4", "2", "7", "8", "1", "1", "7", "4",
                "1", "1", "9", "8", "6", "5", "9", "9", "3", "7", "6", "3", "10", "8", "10", "7", "2", "5", "1", "1",
                "9", "9", "5" },
                "9999999998888888888887777777776666666666555555554444444443333333333222222222111111111111111101010101010101010");
    }

    private static void runTest(String[] a, String expected) {
        String actual = largestNumber(a);
        if (!actual.equals(expected))
            System.out.println("Incorrect value for largestNumber, expected " + expected + ", but got " + actual);
    }

    public static void main(String[] args) {
        testSolution();
        // runSolution();
    }
}
