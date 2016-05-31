import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        byte[] radix = new byte[R];
        for (int i = 0; i < R; i++) {
            radix[i] = (byte) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            byte b = BinaryStdIn.readByte();
            for (int i = 0; i < R; i++) {
                if (b == radix[i]) {
                    BinaryStdOut.write((byte) i);
                    byte tmp = radix[i];
                    System.arraycopy(radix, 0, radix, 1, i);
                    radix[0] = tmp;
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        byte[] radix = new byte[R];
        for (int i = 0; i < R; i++) {
            radix[i] = (byte) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readByte() & 0xFF;
            BinaryStdOut.write(radix[i]);
            byte tmp = radix[i];
            System.arraycopy(radix, 0, radix, 1, i);
            radix[0] = tmp;
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}