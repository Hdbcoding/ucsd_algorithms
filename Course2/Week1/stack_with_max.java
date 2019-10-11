import java.util.*;
import java.io.*;

// alternative solution, instead of Stack<Integer> _stack, Stack<Integer> _max
// could do ArrayList<MaxRecord> _stack treated as stack
// MaxRecord { int value, int lastMaxIndex }
// max() { int index = _stack.peek().lastMaxIndex; return _stack[lastMaxIndex]; }
// something like that
// push(int value) { 
//    Record r = new maxRecord(value, stack.peek().lastMaxIndex);
//    if (value > max()) r.lastMaxIndex = stack.length;
//    stack.push(r);
// }
// nothing special to do on pop
class MaxStack {
    Stack<Integer> _stack;
    Stack<Integer> _max;

    public MaxStack() {
        _stack = new Stack<Integer>();
        _max = new Stack<Integer>();
    }

    public void push(int value) {
        _stack.push(value);
        if (_max.isEmpty() || value >= max())
            _max.push(value);
    }

    public Integer pop() {
        if (_stack.peek().equals(max()))
            _max.pop();
        return _stack.pop();
    }

    public Integer max() {
        return _max.peek();
    }
}

class FastScanner {
    StringTokenizer tok = new StringTokenizer("");
    BufferedReader in;

    FastScanner() {
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    String next() throws IOException {
        while (!tok.hasMoreElements())
            tok = new StringTokenizer(in.readLine());
        return tok.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(next());
    }
}

public class stack_with_max {
    static public void main(String[] args) throws IOException {
        runSolution();
        // testSolution();
    }

    static void runSolution() throws IOException {
        FastScanner scanner = new FastScanner();
        int queries = scanner.nextInt();
        MaxStack stack = new MaxStack();

        for (int qi = 0; qi < queries; ++qi) {
            String operation = scanner.next();
            if ("push".equals(operation)) {
                stack.push(scanner.nextInt());
            } else if ("pop".equals(operation)) {
                stack.pop();
            } else if ("max".equals(operation)) {
                System.out.println(stack.max());
            }
        }
    }

    static void testSolution() {
        runTest1();
        runTest2();
        runTest3();
        runTest4();
        runTest5();
    }

    static void runTest1() {
        Integer[] actual = new Integer[2];
        MaxStack stack = new MaxStack();
        stack.push(2);
        stack.push(1);
        actual[0] = stack.max();
        stack.pop();
        actual[1] = stack.max();
        checkResult(new Integer[] { 2, 2 }, actual);
    }

    static void runTest2() {
        Integer[] actual = new Integer[2];
        MaxStack stack = new MaxStack();
        stack.push(1);
        stack.push(2);
        actual[0] = stack.max();
        stack.pop();
        actual[1] = stack.max();
        checkResult(new Integer[] { 2, 1 }, actual);
    }

    static void runTest3() {
        Integer[] actual = new Integer[4];
        MaxStack stack = new MaxStack();
        stack.push(2);
        stack.push(3);
        stack.push(9);
        stack.push(7);
        stack.push(2);
        actual[0] = stack.max();
        actual[1] = stack.max();
        actual[2] = stack.max();
        stack.pop();
        actual[3] = stack.max();
        checkResult(new Integer[] { 9, 9, 9, 9 }, actual);
    }

    static void runTest4() {
        Integer[] actual = new Integer[2];
        MaxStack stack = new MaxStack();
        stack.push(7);
        stack.push(1);
        stack.push(7);
        actual[0] = stack.max();
        stack.pop();
        actual[1] = stack.max();
        checkResult(new Integer[] { 7, 7 }, actual);
    }

    static void runTest5() {
        Integer[] actual = new Integer[19];
        MaxStack stack = new MaxStack();
        stack.push(15560);
        actual[0] = stack.max();
        stack.push(25104);
        actual[1] = stack.max();
        stack.push(93136);
        actual[2] = stack.max();
        stack.push(19637);
        actual[3] = stack.max();
        stack.push(54647);
        actual[4] = stack.max();
        stack.push(13521);
        actual[5] = stack.max();
        stack.push(57160);
        actual[6] = stack.max();
        stack.push(8087);
        actual[7] = stack.max();
        stack.push(48207);
        actual[8] = stack.max();
        stack.push(53519);
        actual[9] = stack.max();
        stack.pop();
        actual[10] = stack.max();
        stack.pop();
        actual[11] = stack.max();
        stack.pop();
        actual[12] = stack.max();
        stack.pop();
        actual[13] = stack.max();
        stack.pop();
        actual[14] = stack.max();
        stack.pop();
        actual[15] = stack.max();
        stack.pop();
        actual[16] = stack.max();
        stack.pop();
        actual[17] = stack.max();
        stack.pop();
        actual[18] = stack.max();
        checkResult(new Integer[] { 15560, 25104, 93136, 93136, 93136, 93136, 93136, 93136, 93136, 93136, 93136, 93136,
                93136, 93136, 93136, 93136, 93136, 25104, 15560 }, actual);

    }

    static void checkResult(Integer[] expected, Integer[] actual) {
        String exp_str = Arrays.toString(expected);
        String actual_str = Arrays.toString(actual);

        if (!exp_str.equals(actual_str))
            System.out.println("Unexpected result for maxes. Expected: " + exp_str + ", but got: " + actual_str);
    }
}
