import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Request {
    public Request(int arrival_time, int process_time) {
        this.arrivalTime = arrival_time;
        this.processTime = process_time;
        this.startTime = -1;
    }

    public int arrivalTime;
    public int processTime;
    public int startTime;
}

class Buffer {
    public Buffer(int size) {
        this.size = size;
        this.nextStartTime = 0;
        this.buffer = new LinkedList<Request>();
    }

    public void process(Request request) {
        clearOldItems(request.arrivalTime);

        if (!isFull()) {
            request.startTime = Math.max(nextStartTime, request.arrivalTime);
            nextStartTime = request.startTime + request.processTime;
            buffer.add(request);
        }
    }

    private void clearOldItems(int arrivalTime) {
        Request next = buffer.peek();
        while (next != null && (next.startTime + next.processTime <= arrivalTime)) {
            buffer.poll();
            next = buffer.peek();
        }
    }

    private boolean isFull(){
        return buffer.size() >= size;
    }

    private int size;
    private int nextStartTime;
    private Queue<Request> buffer;
}

class process_packages {
    private static ArrayList<Request> ReadQueries(Scanner scanner) throws IOException {
        int requests_count = scanner.nextInt();
        ArrayList<Request> requests = new ArrayList<Request>();
        for (int i = 0; i < requests_count; ++i) {
            int arrival_time = scanner.nextInt();
            int process_time = scanner.nextInt();
            requests.add(new Request(arrival_time, process_time));
        }
        return requests;
    }

    private static void ProcessRequests(ArrayList<Request> requests, Buffer buffer) {
        for (int i = 0; i < requests.size(); ++i) {
            buffer.process(requests.get(i));
        }
    }

    private static void PrintResults(ArrayList<Request> requests) {
        for (int i = 0; i < requests.size(); ++i) {
            Request request = requests.get(i);
            System.out.println(request.startTime);
        }
    }

    public static void main(String[] args) throws IOException {
        runSolution();
        // testSolution();
    }

    private static void runSolution() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int buffer_max_size = scanner.nextInt();
        Buffer buffer = new Buffer(buffer_max_size);

        ArrayList<Request> requests = ReadQueries(scanner);
        scanner.close();
        ProcessRequests(requests, buffer);
        PrintResults(requests);
    }

    private static void testSolution() {
        ArrayList<Request> requests = new ArrayList<Request>();
        runTest(0, requests, new int[0]);
        requests.add(new Request(0, 0));
        runTest(1, requests, new int[]{0});
        requests.clear();
        requests.add(new Request(0, 1));
        requests.add(new Request(0, 1));
        runTest(1, requests, new int[]{0, -1});
        requests.clear();
        requests.add(new Request(0, 1));
        requests.add(new Request(1, 1));
        runTest(1, requests, new int[]{0, 1});
    }

    private static void runTest(int bufferSize, ArrayList<Request> requests, int[] expected) {
        ProcessRequests(requests, new Buffer(bufferSize));
        int[] times = processResults(requests);
        String exp_str = Arrays.toString(expected);
        String actual = Arrays.toString(times);
        if (!exp_str.equals(actual))
            System.out.println(
                    "Unexpected result for pattern of requests. Expected: " + exp_str + ", but got: " + actual);
    }

    private static int[] processResults(ArrayList<Request> results) {
        int[] times = new int[results.size()];
        for (int i = 0; i < times.length; i++)
            times[i] = results.get(i).startTime;
        return times;
    }
}
