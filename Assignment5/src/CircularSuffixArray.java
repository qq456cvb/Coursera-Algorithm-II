import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

    private final char[] text;
    private final int[] index;   // index[i] = j means text.substring(j) is ith largest suffix
    private final int N;         // number of characters in text


    public CircularSuffixArray(String s)  // circular suffix array of s
    {
        if (s == null) {
            throw new java.lang.NullPointerException();
        }

        // use 3-way radix sort
        N = s.length();
        this.text = s.toCharArray();
        this.index = new int[N];
        for (int i = 0; i < N; i++)
            index[i] = i;

        sort(0, N-1, 0);
    }

    private char getChar(int i) {
        int idx = i;
        while (idx > N-1) {
            idx -= N;
        }
        return text[idx];
    }

    private void sort(int lo, int hi, int d) {

        // cutoff to insertion sort for small sub arrays
        if (hi <= lo || d >= N) {
            return;
        }

        int lt = lo, gt = hi;
        char v = getChar(index[lo] + d);
        int i = lo + 1;
        while (i <= gt) {
            char t = getChar(index[i] + d);
            if      (((int) t) < ((int) v)) exch(lt++, i++);
            else if (((int) t) > ((int) v)) exch(i, gt--);
            else            i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(lo, lt-1, d);
        sort(lt, gt, d+1);
        sort(gt+1, hi, d);
    }

    // exchange index[i] and index[j]
    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }

    public int length()                   // length of s
    {
        return this.N;
    }

    public int index(int i)               // returns index of ith sorted suffix
    {
        if (i < 0 || i >= N) {
            throw new IndexOutOfBoundsException();
        }
        return this.index[i];
    }

    public static void main(String[] args)// unit testing of the methods (optional)
    {
        char[] chars = {0x6b, 0x4e, 0x6a, 0x21, 0x5b, 0x64, 0x00, 0x00, 0x68, 0x40};
        String s = new String(chars);
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            StdOut.printf("%d\n", circularSuffixArray.index(i));
        }
    }
}