import java.util.*;

public class CoveringSegments {

    private static ArrayList<Integer> optimalPoints(Segment[] s) {
        Arrays.sort(s);
        Stack<Segment> segments = new Stack<Segment>();
        segments.addAll(Arrays.asList(s));
        ArrayList<Integer> points = new ArrayList<Integer>();
        while (!segments.isEmpty()) {
            Segment last = segments.pop();
            int start = last.start;
            while (!segments.isEmpty() && segments.peek().end >= start) {
                start = Math.max(segments.pop().start, start);
            }
            points.add(start);
        }
        
        return points;
    }

    private static class Segment implements Comparable<Segment> {
        int start, end;

        Segment(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(Segment o) {
            if (o == null)
                return 0;
            if (start < o.start)
                return -1;
            if (start > o.start)
                return 1;
            return o.end - end;
        }
    }

    private static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        for (int i = 0; i < n; i++) {
            int start, end;
            start = scanner.nextInt();
            end = scanner.nextInt();
            segments[i] = new Segment(start, end);
        }
        scanner.close();
        ArrayList<Integer> points = optimalPoints(segments);
        System.out.println(points.size());
        for (int point : points) {
            System.out.print(point + " ");
        }
    }

    private static void testSolution() {
        runTest(new Segment[] {}, 0);
        runTest(new Segment[] {new Segment(0, 0)}, 1);
        runTest(new Segment[] {new Segment(1, 3), new Segment(2, 5), new Segment(3, 6)}, 1);
        runTest(new Segment[] {new Segment(1, 3), new Segment(2, 5), new Segment(5, 6), new Segment(4, 7)}, 2);
    }
    
    private static void runTest(Segment[] s, int expectedSize) {
        ArrayList<Integer> points = optimalPoints(s);
        if (points.size() != expectedSize)
            System.out.println("Incorrect value for num covering points, expected " + expectedSize + ", but got " + points.size());
    }

    public static void main(String[] args) {
        // testSolution();
        runSolution();
    }
}
 
