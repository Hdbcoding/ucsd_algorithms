import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class BuildHeap {
  public static void main(String[] args) throws IOException {
    // runSolution();
    testSolution();
  }

  public static void runSolution() throws IOException {
    int[] data = readData();
    List<Swap> swaps = generateSwapsNaive(data);
    writeResponse(swaps);
  }

  public static void testSolution(){
    runTest(new int[]{5, 4, 3, 2, 1}, new int[]{3, 1, 4, 0, 1, 1, 3});
    runTest(new int[]{1, 2, 3, 4, 5}, new int[]{0});
  }

  public static void runTest(int[] data, int[] expected) {
    List<Swap> actualSwaps = generateSwapsFast(data);
    int[] actual = generateResponse(actualSwaps);
    String actualString = Arrays.toString(actual);
    String expectedString = Arrays.toString(expected);
    if (!actualString.equals(expectedString)) {
      System.out.println("Unexpected result for " + Arrays.toString(data) + ", expected: " + expectedString
          + ", but got: " + actualString);
    }
  }

  private static int[] generateResponse(List<Swap> actualSwaps) {
    int[] actual = new int[actualSwaps.size() * 2 + 1];
    actual[0] = actualSwaps.size();
    int i = 1;
    for (int j = 0; j < actualSwaps.size(); j++) {
      actual[i++] = actualSwaps.get(j).index1;
      actual[i++] = actualSwaps.get(j).index2;
    }
    return actual;
  }

  private static int[] readData() throws IOException {
    FastScanner in = new FastScanner();
    int n = in.nextInt();
    int[] data = new int[n];
    for (int i = 0; i < n; ++i) {
      data[i] = in.nextInt();
    }
    return data;
  }

  private static List<Swap> generateSwapsNaive(int[] data) {
    List<Swap> swaps = new ArrayList<Swap>();
    // The following naive implementation just sorts
    // the given sequence using selection sort algorithm
    // and saves the resulting sequence of swaps.
    // This turns the given array into a heap,
    // but in the worst case gives a quadratic number of swaps.
    //
    // TODO: replace by a more efficient implementation
    for (int i = 0; i < data.length; ++i) {
      for (int j = i + 1; j < data.length; ++j) {
        if (data[i] > data[j]) {
          swaps.add(new Swap(i, j));
          int tmp = data[i];
          data[i] = data[j];
          data[j] = tmp;
        }
      }
    }
    return swaps;
  }

  private static List<Swap> generateSwapsFast(int[] data){
    Heap heap = new Heap(data);
    List<Swap> swaps = heap.heapify();
    return swaps;
  }

  private static void writeResponse(List<Swap> swaps) {
    PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
    out.println(swaps.size());
    for (Swap swap : swaps) {
      out.println(swap.index1 + " " + swap.index2);
    }
    out.close();
  }

  static class Heap {
    int[] _data;

    private Heap(int[] data) {
      this._data = data;
    }

    public List<Swap> heapify(){
      List<Swap> swaps = new ArrayList<Swap>();
      for (int i = _data.length / 2; i >= 0; i--){
        siftDown(i, swaps);
      }
      return swaps;
    }

    public void siftDown(int i, List<Swap> swaps) {
      // if we have no children, we can't sit down
      int l = Left(i), r = Right(i), s = size(), swapIndex = i;

      if (l < s && !rule(swapIndex, l))
        swapIndex = l;

      if (r < s && !rule(swapIndex, r))
        swapIndex = r;

      if (swapIndex != i) {
        Swap(i, swapIndex);
        swaps.add(new Swap(i, swapIndex));
        siftDown(swapIndex, swaps);
      }
    }

    public int size() {
      if (_data == null)
        return 0;
      return _data.length;
    }

    int Left(int i) {
      return i + i + 1;
    }

    int Right(int i) {
      return i + i + 2;
    }

    void Swap(int i, int j) {
      int temp = _data[i];
      _data[i] = _data[j];
      _data[j] = temp;
    }

    // rule true => heap is good
    // in this case, we have a min heap
    boolean rule(int pIndex, int cIndex) {
      return _data[pIndex] <= _data[cIndex];
    }
  }

  static class Swap {
    int index1;
    int index2;

    public Swap(int index1, int index2) {
      this.index1 = index1;
      this.index2 = index2;
    }

    @Override
    public String toString(){
      return index1 + " " + index2;
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
