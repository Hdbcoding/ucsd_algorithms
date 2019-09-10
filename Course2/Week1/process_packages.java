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

class Response {
    public Response(boolean dropped, int start_time) {
        this.dropped = dropped;
        this.startTime = start_time;
    }

    public boolean dropped;
    public int startTime;

    public int getDisplayTime() {
        return dropped ? -1 : startTime;
    }
}

class Buffer {
    public Buffer(int size) {
        this.size = size;
        this.nextStartTime = 0;
        this.buffer = new LinkedList<Request>();
    }

    public Response process(Request request) {
        boolean success = false;
        if (buffer.isEmpty()) {
            success = true;
        } else {
            clearOldItems(request.arrivalTime);
            if (buffer.size() != size) {
                success = true;
            }
        }

        if (success) {
            request.startTime = nextStartTime;
            nextStartTime = request.startTime + request.processTime;
            buffer.add(request);
            return new Response(true, nextStartTime);
        } else {
            return new Response(false, -1);
        }
    }

    private void clearOldItems(int arrivalTime) {
        Request next = buffer.peek();
        while (!buffer.isEmpty() && (next.startTime + next.processTime < arrivalTime)) {
            buffer.poll();
            if (!buffer.isEmpty())
                next = buffer.peek();
        }
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

    private static ArrayList<Response> ProcessRequests(ArrayList<Request> requests, Buffer buffer) {
        ArrayList<Response> responses = new ArrayList<Response>();
        for (int i = 0; i < requests.size(); ++i) {
            responses.add(buffer.process(requests.get(i)));
        }
        return responses;
    }

    private static void PrintResponses(ArrayList<Response> responses) {
        for (int i = 0; i < responses.size(); ++i) {
            Response response = responses.get(i);
            System.out.println(response.getDisplayTime());
        }
    }

    public static void main(String[] args) throws IOException {
        runSolution();
    }

    private static void runSolution() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int buffer_max_size = scanner.nextInt();
        Buffer buffer = new Buffer(buffer_max_size);

        ArrayList<Request> requests = ReadQueries(scanner);
        scanner.close();
        ArrayList<Response> responses = ProcessRequests(requests, buffer);
        PrintResponses(responses);
    }

    private static void testSolution() {

    }

    private static void runTest(int bufferSize, ArrayList<Request> requests, int[] expected) {
        ArrayList<Response> responses = ProcessRequests(requests, new Buffer(bufferSize));
        int[] times = processResponses(responses);
        String exp_str = Arrays.toString(expected);
        String actual = Arrays.toString(times);
        if (exp_str != actual)
            System.out.println(
                    "Unexpected result for pattern of requests. Expected: " + exp_str + ", but got: " + actual);
    }

    private static int[] processResponses(ArrayList<Response> responses) {
        int[] times = new int[responses.size()];
        for (int i = 0; i < times.length; i++)
            times[i] = responses.get(i).getDisplayTime();
        return times;
    }
}
