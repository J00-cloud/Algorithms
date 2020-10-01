/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class Permutation {

    public static void main(String[] args) {
        int count = Integer.parseInt(args[0]);

        RandomizedQueue<String> rQ = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {

            String str = StdIn.readString();

            rQ.enqueue(str);
        }


        for (int i = 0; i < count; i++) {

            StdOut.println(rQ.dequeue());
        }

    }
}
