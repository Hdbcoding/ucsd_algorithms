import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;

public class MergingTables {
    static int maxTableSize = -1;

    public static void main(String[] args) {
        runSolution();
        // testSolution();
    }

    static void testSolution() {
        runTest(new int[] { 1, 1, 1, 1, 1 }, new int[] { 3, 5, 2, 4, 1, 4, 5, 4, 5, 3 }, new int[] { 2, 2, 3, 5, 5 });
        runTest(new int[] { 10, 0, 5, 0, 3, 3 }, new int[] { 6, 6, 6, 5, 5, 4, 4, 3 }, new int[] { 10, 10, 10, 11 });
    }

    static void runTest(int[] initialTableSizes, int[] operations, int[] expectedSizes) {
        maxTableSize = -1;
        Table[] tables = new Table[initialTableSizes.length];
        for (int i = 0; i < initialTableSizes.length; i++) {
            tables[i] = new Table(initialTableSizes[i]);
            maxTableSize = Math.max(maxTableSize, initialTableSizes[i]);
        }
        int[] actualSizes = new int[operations.length / 2];
        for (int i = 0; i < operations.length; i += 2) {
            int destination = operations[i] - 1;
            int source = operations[i + 1] - 1;
            merge(tables[destination], tables[source]);
            actualSizes[i / 2] = maxTableSize;
        }
        String initialString = Arrays.toString(initialTableSizes);
        String opsString = Arrays.toString(operations);
        String expectedString = Arrays.toString(expectedSizes);
        String actualString = Arrays.toString(actualSizes);

        if (!expectedString.equals(actualString)) {
            System.out.println("Unexpected result for tables " + initialString + " and operations " + opsString
                    + ". Expected: " + expectedString + ", but got: " + actualString);
        }
    }

    static void runSolution() {
        InputReader reader = new InputReader(System.in);
        int numTables = reader.nextInt();
        int numOperations = reader.nextInt();
        Table[] tables = loadTables(numTables, reader);

        OutputWriter writer = new OutputWriter(System.out);
        loadAndRunOperations(numOperations, tables, reader, writer);
        writer.writer.flush();
    }

    static Table[] loadTables(int numTables, InputReader reader) {
        Table[] tables = new Table[numTables];
        for (int i = 0; i < numTables; i++) {
            int numberOfRows = reader.nextInt();
            tables[i] = new Table(numberOfRows);
            maxTableSize = Math.max(maxTableSize, numberOfRows);
        }
        return tables;
    }

    private static void loadAndRunOperations(int numOperations, Table[] tables, InputReader reader,
            OutputWriter writer) {
        for (int i = 0; i < numOperations; i++) {
            int destination = reader.nextInt() - 1;
            int source = reader.nextInt() - 1;
            merge(tables[destination], tables[source]);
            writer.printf("%d\n", maxTableSize);
        }
    }

    static void merge(Table destination, Table source) {
        Table realDestination = destination.getParent();
        Table realSource = source.getParent();
        if (realDestination == realSource) {
            return;
        }

        // merge two components here
        // use rank heuristic
        if (realDestination.rank > realSource.rank)
            realSource.setParent(realDestination);
        else {
            realDestination.setParent(realSource);
            if (realDestination.rank == realSource.rank)
                realDestination.rank++;
        }
    }

    static class Table {
        Table parent;
        int rank;
        int numberOfRows;

        Table(int numberOfRows) {
            this.numberOfRows = numberOfRows;
            rank = 0;
            parent = this;
        }

        Table getParent() {
            // find super parent and compress path
            if (parent != this) {
                parent = parent.getParent();
            }
            return parent;
        }

        void setParent(Table newParent) {
            parent = newParent;
            parent.numberOfRows += numberOfRows;
            // update maximumNumberOfRows
            maxTableSize = Math.max(maxTableSize, parent.numberOfRows);
            numberOfRows = 0;
        }
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class OutputWriter {
        public PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        public void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}
