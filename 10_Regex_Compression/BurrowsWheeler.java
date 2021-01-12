/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        StringBuilder s = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            s.append(BinaryStdIn.readChar());
        }

        BinaryStdIn.close();
        CircularSuffixArray csa = new CircularSuffixArray(s.toString());

        StringBuilder sOut = new StringBuilder();
        int startIndex = -1;
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                startIndex = i;
                BinaryStdOut.write(startIndex);
            }
            char charVal = s.charAt((csa.index(i) + (csa.length() - 1)) % csa.length());
            sOut.append(charVal);
        }

        BinaryStdOut.write(sOut.toString());
        BinaryStdOut.close();

    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int firstIndex = BinaryStdIn.readInt();
        StringBuilder s = new StringBuilder();

        while (!BinaryStdIn.isEmpty()) {
            s.append(BinaryStdIn.readChar());
        }
        BinaryStdIn.close();

        char[] t = s.toString().toCharArray();
        char[] sortedT = Arrays.copyOf(t, t.length);
        int[] next = new int[t.length];
        boolean[] done = new boolean[t.length];

        sortedT = quickSort(sortedT, 0, sortedT.length - 1);
        char lastChar = 0;
        int lastStart = 0;
        // use key index for strings instead of loop
        for (int i = 0; i < t.length; i++) {
            char curChar = sortedT[i];
            int start = 0;
            if (curChar == lastChar) {
                start = lastStart;
            }
            for (int y = start; y < t.length; y++) {
                if (curChar == t[y] && done[y] != true) {
                    next[i] = y;
                    t[y] = 0;
                    done[y] = true;
                    lastStart = y;
                    break;
                }
            }
            lastChar = curChar;
        }

        int l = 0;
        BinaryStdOut.write(sortedT[firstIndex]);
        int nextLine = firstIndex;
        while (l < t.length - 1) {
            nextLine = next[nextLine];
            BinaryStdOut.write(sortedT[nextLine]);
            l++;
        }
        BinaryStdOut.close();

    }

    private static char[] quickSort(char[] arrIn, int lo, int hi) {
        if (lo >= hi) {
            return arrIn;
        }
        else {
            int lt = lo;
            int gt = hi;
            int eq = lo + 1;
            char compChar = arrIn[lo];

            while (eq <= gt) {
                char currentChar = arrIn[eq];
                if (compChar < currentChar) {
                    char temp = arrIn[gt];
                    arrIn[gt] = arrIn[eq];
                    arrIn[eq] = temp;
                    gt--;
                }
                else if (compChar > currentChar) {
                    char temp = arrIn[lt];
                    arrIn[lt] = arrIn[eq];
                    arrIn[eq] = temp;
                    lt++;
                    eq++;
                }
                else {
                    eq++;
                }
            }

            // if there are same chars (many eq), do recursive sort on those

            quickSort(arrIn, lo, (lt - 1));
            quickSort(arrIn, (gt + 1), hi);

        }
        return arrIn;
    }


    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {

        if (args[0].contains("-")) {
            transform();
        }
        else if (args[0].contains("+")) {
            inverseTransform();
        }

    }

}
