/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] alphaStr = new char[256];

        for (int i = 0; i < 256; i++) {
            alphaStr[i] = (char) i;
        }
        //StdOut.println(1);
        while (!BinaryStdIn.isEmpty()) {
            //  StdOut.println(2);
            char currChar = BinaryStdIn.readChar();
            //StdOut.println(3);
            int index = findIndex(alphaStr, currChar);
            BinaryStdOut.write((char) index);
            if (alphaStr[0] != currChar) {
                System.arraycopy(alphaStr, 0, alphaStr, 1, index);
                alphaStr[0] = currChar;
            }
        }
        //StdOut.println(4);
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    private static int findIndex(char[] array, char value) {
        for (int i = 0; i < 256; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        ArrayList<Character> outInt = new ArrayList<Character>();
        for (int i = 0; i < 256; i++) {
            outInt.add((char) i);
        }

        while (!BinaryStdIn.isEmpty()) {
            char h = BinaryStdIn.readChar();
            char val = outInt.remove(h);
            BinaryStdOut.write(val);
            outInt.add(0, val);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();

    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {

        // java-algs4 MoveToFront - abra.txt
        if (args[0].contains("-")) {
            //StdOut.println(0);
            encode();
        }
        else if (args[0].contains("+")) {
            decode();
        }

    }

}
