import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class JobQueue {
    public static void main(String[] args) throws IOException {
        runSolution();
        // testSolution();
    }

    static void runSolution() throws IOException {
        TestCase test = readData();
        AssignedJob[] handledJobs = assignJobsFast(test);
        writeResponse(handledJobs);
    }

    static void testSolution() {
        runTest(2, 
        new int[] { 
            1, 2, 3, 4, 5 
        }, 
        new int[] { 
            0, 0, 1, 0, 0, 1, 1, 2, 0, 4 
        });
        runTest(4, 
        new int[] { 
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1, 
            1, 1, 1, 1, 1 
        },
        new int[] { 
            0, 0, 1, 0, 2, 0, 3, 0, 0, 1, 
            1, 1, 2, 1, 3, 1, 0, 2, 1, 2, 
            2, 2, 3, 2, 0, 3, 1, 3, 2, 3, 
            3, 3, 0, 4, 1, 4, 2, 4, 3, 4 });
    }

    static void runTest(int numWorkers, int[] jobs, int[] expected) {
        TestCase test = new TestCase();
        test.numWorkers = numWorkers;
        test.jobs = jobs;
        AssignedJob[] assignedJobs = assignJobsFast(test);
        long[] actual = generateResponse(assignedJobs);
        String expectedString = Arrays.toString(expected);
        String actualString = Arrays.toString(actual);
        String jobsString = Arrays.toString(jobs);
        if (!actualString.equals(expectedString)){
            System.out.println("Unexpected result for " + numWorkers + " workers and jobs "
            + jobsString + ". Expected: " + expectedString + ", but got: " + actualString);
        }
    }

    static TestCase readData() throws IOException {
        FastScanner in = new FastScanner();
        TestCase test = new TestCase();
        test.numWorkers = in.nextInt();
        int m = in.nextInt();
        test.jobs = new int[m];
        for (int i = 0; i < m; ++i) {
            test.jobs[i] = in.nextInt();
        }
        return test;
    }

    static AssignedJob[] assignJobsNaive(TestCase test) {
        AssignedJob[] assignedJobs = new AssignedJob[test.jobs.length];
        long[] nextFreeTime = new long[test.numWorkers];
        for (int i = 0; i < test.jobs.length; i++) {
            int duration = test.jobs[i];
            int bestWorker = 0;
            for (int j = 0; j < test.numWorkers; ++j) {
                if (nextFreeTime[j] < nextFreeTime[bestWorker])
                    bestWorker = j;
            }
            assignedJobs[i] = new AssignedJob();
            assignedJobs[i].worker = bestWorker;
            assignedJobs[i].startTime = nextFreeTime[bestWorker];
            nextFreeTime[bestWorker] += duration;
        }
        return assignedJobs;
    }

    static AssignedJob[] assignJobsFast(TestCase test){
        WorkerHeap workers = new WorkerHeap(test.numWorkers);
        AssignedJob[] jobs = new AssignedJob[test.jobs.length];
        for (int i = 0; i < jobs.length; i++){
            int duration = test.jobs[i];
            AssignedJob job = new AssignedJob();
            jobs[i] = job;
            Worker worker = workers.getFreeWorker();
            job.startTime = worker.timeFree;
            job.worker = worker.index;
            worker.timeFree += duration;
            workers.add(worker);
        }
        return jobs;
    }

    static void writeResponse(AssignedJob[] assignedJobs) {
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        for (int i = 0; i < assignedJobs.length; ++i) {
            out.println(assignedJobs[i].worker + " " + assignedJobs[i].startTime);
        }
        out.close();
    }

    static long[] generateResponse(AssignedJob[] assignedJobs){
        long[] response = new long[assignedJobs.length * 2];
        int i = 0;
        for(int j = 0; j < assignedJobs.length; j++){
            response[i++] = assignedJobs[j].worker;
            response[i++] = assignedJobs[j].startTime;
        }
        return response;
    }

    static class TestCase {
        int numWorkers;
        int[] jobs;
    }

    static class AssignedJob {
        int worker;
        long startTime;
    }

    static class Worker {
        long timeFree;
        int index;

        public Worker(int i) {
            index = i;
        }
    }

    static class WorkerHeap {
        Worker[] _workers;
        int _numWorkers;

        WorkerHeap(int numWorkers) {
            _numWorkers = numWorkers;
            _workers = new Worker[numWorkers];
            for(int i = 0; i < numWorkers; i++){
                _workers[i] = new Worker(i);
            }
        }

        Worker getFreeWorker(){
            Worker w = _workers[0];
            _workers[0] = _workers[--_numWorkers];
            siftDown(0);
            return w;
        }

        void add(Worker w){
            _numWorkers++;
            _workers[_numWorkers - 1] = w;
            siftUp(_numWorkers - 1);
        }

        void siftDown(int i){
            int l = left(i), r = right(i), swapIndex = i;

            if (l < _numWorkers && !rule(swapIndex, l))
                swapIndex = l;
            
            if (r < _numWorkers && !rule(swapIndex, r))
                swapIndex = r;

            if (swapIndex != i){
                swap(i, swapIndex);
                siftDown(swapIndex);
            }
        }

        void siftUp(int i){
            int p = parent(i);

            if (!rule(p, i)){
                swap(p, i);
                siftUp(p);
            }
        }

        int parent(int i){
            return (i - 1) / 2;
        }

        int left(int i){
            return i + i + 1;
        }

        int right(int i){
            return i + i + 2;
        }

        void swap(int i, int j){
            Worker t = _workers[i];
            _workers[i] = _workers[j];
            _workers[j] = t;
        }

        // rule true => heap is good
        // a worker is higher in the heap if it is free first
        // for two workers free at the same time, smaller index is higher
        boolean rule(int pIndex, int cIndex){
            Worker p = _workers[pIndex];
            Worker c = _workers[cIndex];
            if (p.timeFree == c.timeFree) return p.index <= c.index;
            return p.timeFree < c.timeFree;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
