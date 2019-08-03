import java.util.*;

class EditDistance {
  public static int editDistance(String s, String t) {
    int[][] distances = setupBaseCases(s, t);
    findSolutions(distances, s, t);
    return distances[s.length()][t.length()];
  }

  private static int[][] setupBaseCases(String s, String t) {
    int[][] distances = new int[s.length() + 1][t.length() + 1];

    for (int i = 1; i <= s.length(); i++) {
      distances[i][0] = i;
    }

    for (int i = 1; i <= t.length(); i++) {
      distances[0][i] = i;
    }

    return distances;
  }

  private static void findSolutions(int[][] distances, String s, String t) {
    for (int i = 1; i <= s.length(); i++) {
      char si = s.charAt(i - 1);
      for (int j = 1; j <= t.length(); j++) {
        char tj = t.charAt(j - 1);
        int i1j1 = distances[i - 1][j - 1] + (si == tj ? 0 : 1);
        int i1j0 = distances[i - 1][j] + 1;
        int i0j1 = distances[i][j - 1] + 1;
        distances[i][j] = Math.min(i1j1, Math.min(i1j0, i0j1));
      }
    }
  }

  static void runSolution() {
    Scanner scanner = new Scanner(System.in);
    String s = scanner.next();
    String t = scanner.next();
    scanner.close();
    System.out.println(editDistance(s, t));
  }

  static void testSolution() {
    runTest("ab", "ab", 0);
    runTest("short", "ports", 3);
    runTest("ports", "short", 3);
    runTest("editing", "distance", 5);
    runTest("distance", "editing", 5);
    runTest("bread", "really", 4);
    runTest("really", "bread", 4);
  }
  
  static void runTest(String s, String t, int expected) {
    int actual = editDistance(s, t);
    if (actual != expected)
      System.out
          .println("Wrong edit distance for " + s + " and " + t + ", expected: " + expected + ", actual: " + actual);
  }

  public static void main(String args[]) {
    // testSolution();
    runSolution();
  }

}
