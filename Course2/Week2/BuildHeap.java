import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BuildHeap {
  private int[] data;
  private List<Swap> swaps;

  private FastScanner in;
  private PrintWriter out;

  public static void main(String[] args) throws IOException {
    new BuildHeap().solve();
  }

  public void solve() throws IOException {
    in = new FastScanner();
    out = new PrintWriter(new BufferedOutputStream(System.out));
    readData();
    generateSwaps();
    writeResponse();
    out.close();
  }

  private void readData() throws IOException {
    int n = in.nextInt();
    data = new int[n];
    for (int i = 0; i < n; ++i) {
      data[i] = in.nextInt();
    }
  }

  private void generateSwaps() {
    swaps = new ArrayList<Swap>();
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
  }

  private void writeResponse() {
    out.println(swaps.size());
    for (Swap swap : swaps) {
      out.println(swap.index1 + " " + swap.index2);
    }
  }

  static class Heap {
    int[] _data;

    Heap(int[] data) {
      this._data = data;
    }

    public void siftDown(int i, ArrayList<Swap> swaps) {
      // if we have no children, we can't sit down
      int l = Left(i), r = Right(i), s = size(), swapIndex = i;

      if (l <= s && !rule(swapIndex, l))
        swapIndex = l;
      
      if (r <= s && !rule(swapIndex, r))
        swapIndex = r;
      
      if (swapIndex != i){
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
