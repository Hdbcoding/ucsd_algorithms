import java.util.Arrays;
import java.util.Scanner;

class Dequeue {
    DequeueNode front;
    DequeueNode back;

    public boolean isEmpty() {
        return this.front == null || this.back == null;
    }

    public void addFront(int value) {
        DequeueNode node = new DequeueNode(value);
        node.back = this.front;
        this.front = node;
        if (this.back == null)
            this.back = node;
    }

    public void addBack(int value) {
        DequeueNode node = new DequeueNode(value);
        node.front = this.back;
        this.back = node;
        if (this.front == null)
            this.front = node;
    }

    public int peekFront() {
        if (this.front == null)
            throw new Error("set is empty");
        return this.front.value;
    }

    public int peekBack() {
        if (this.back == null)
            throw new Error("set is empty");
        return this.back.value;
    }

    public int popFront() {
        if (this.front == null)
            throw new Error("set is empty");
        int value = this.front.value;
        if (this.front == this.back)
            this.front = this.back = null;
        else
            this.front = this.front.back;
        return value;
    }

    public int popBack() {
        if (this.back == null)
            throw new Error("set is empty");
        int value = this.back.value;
        if (this.front == this.back)
            this.front = this.back = null;
        else
            this.back = this.back.front;
        return value;
    }

    class DequeueNode {
        public int value;
        public DequeueNode front;
        public DequeueNode back;

        DequeueNode(int _value) {
            this.value = _value;
        }
    }
}

public class max_sliding_window {
    static int[] max_sliding_window_naive(int[] A, int w) {
        int[] solutions = new int[A.length - w + 1];
        int k = 0;
        for (int i = 0; i < A.length - w; i++) {
            int max = A[i];
            for (int j = i + 1; j < i + w; j++)
                max = Math.max(max, A[j]);
            solutions[k++] = max;
        }
        return solutions;
    }

    static int[] max_sliding_window_fast(int[] A, int w) {
        int[] solutions = new int[A.length - w + 1];
        int k = 0;
        Dequeue dq = new Dequeue();
        for (int i = 0; i < w; i++) 
            addToDequeue(i, dq, A);
        solutions[k++] = A[dq.peekFront()];
        for (int i = w; i < A.length; i++){
            if (dq.peekFront() < (i - w + 1)) dq.popFront();
            addToDequeue(i, dq, A);
            solutions[k++] = A[dq.peekFront()];
        }

        return solutions;
    }

    static void addToDequeue(int index, Dequeue dq, int[] A) {
        if (!dq.isEmpty()) {
            int value = A[index];
            if (A[dq.peekBack()] > value)
                return;
            while (!dq.isEmpty() && A[dq.peekBack()] < value)
                dq.popBack();
        }
        dq.addBack(index);
    }

    public static void main(String[] args) {
        // runSolution();
        testSolution();
    }

    static void runSolution() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++)
            A[i] = scanner.nextInt();
        int w = scanner.nextInt();
        scanner.close();
        int[] solutions = max_sliding_window_naive(A, w);
        for (int s : solutions)
            System.out.println(s);
    }

    static void testSolution() {
        runTest(new int[]{2, 7, 3, 1, 5, 2, 6, 2}, 4, new int[]{7, 7, 5, 6, 6});
    }

    static void runTest(int[] A, int w, int[] expected) {
        int[] actual = max_sliding_window_fast(A, w);
        String actual_string = Arrays.toString(actual);
        String expected_string = Arrays.toString(expected);
        if (!actual_string.equals(expected_string))
            System.out.println("Expected " + expected_string + ", but got " + actual_string);
    }
}