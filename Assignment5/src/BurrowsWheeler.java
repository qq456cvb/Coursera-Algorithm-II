import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode()
    {
        String s = BinaryStdIn.readString();
        int N = s.length();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        int start = 0;
        byte[] t = new byte[N];
        for (int i = 0; i < N; i++) {
            if (circularSuffixArray.index(i) == 0) {
                start = i;
                t[i] = (byte) s.charAt(N-1);
            } else {
                t[i] = (byte) s.charAt(circularSuffixArray.index(i)-1);
            }
        }
        BinaryStdOut.write(start);
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write(t[i]);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode()
    {
        int start = BinaryStdIn.readInt();
        String all = BinaryStdIn.readString();
        char[] t = all.toCharArray();
        int N = all.length();
        int[] next = new int[N];

        // radix sort
        int R = 256;   // extend ASCII alphabet size
        char[] aux = new char[N];

        // compute frequency counts
        int[] count = new int[R+1];
        for (int i = 0; i < N; i++)
            count[t[i] + 1]++;

        // compute cumulates
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];

        // move data and map the values
        for (int i = 0; i < N; i++) {
            // the sort is stable, guarantee the order
            next[count[t[i]]] = i;
            aux[count[t[i]]++] = t[i];
        }

        for (int i = 0; i < N; i++) {
            BinaryStdOut.write(aux[start]);
            start = next[start];
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args)
    {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}