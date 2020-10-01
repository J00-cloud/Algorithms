import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] a;
    private int currentArrayPointer;
    private int arrayMax;

    // construct an empty randomized queue
    public RandomizedQueue() {
        //creates an item array list
        a = (Item[]) new Object[1];
        currentArrayPointer = 0;
        arrayMax = 1;
        a[currentArrayPointer] = null;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        // if len = 1 and last is null
        if (a[0] == null) {
            return true;
        }
        else {
            return false;
        }
    }

    // return the number of items on the randomized queue
    public int size() {
        // returns arrayLength
        return currentArrayPointer;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            IllegalArgumentException exc = new IllegalArgumentException();
            throw exc;
        }

        if (currentArrayPointer + 1 == arrayMax) {
            doubleArraySize();
        }

        a[currentArrayPointer] = item;
        //StdOut.println((Integer) a[0]);
        currentArrayPointer = currentArrayPointer + 1;

    }

    // remove and return a random item
    public Item dequeue() {
        if (currentArrayPointer == 0) {
            java.util.NoSuchElementException exc = new java.util.NoSuchElementException();
            throw exc;
        }
        int randNb = StdRandom.uniform(currentArrayPointer);
        // return front item

        //swap front and random
        Item temp = a[currentArrayPointer - 1];
        a[currentArrayPointer - 1] = a[randNb];
        a[randNb] = temp;


        if ((currentArrayPointer == (arrayMax / 4))) {
            divideArraySize();
        }

        Item frontItem = a[currentArrayPointer - 1];
        a[currentArrayPointer - 1] = null;

        if (currentArrayPointer > 0) {
            currentArrayPointer--;
        }

        return frontItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        // return front item
        if (currentArrayPointer == 0) {
            java.util.NoSuchElementException exc = new java.util.NoSuchElementException();
            throw exc;
        }
        int randNb = StdRandom.uniform(currentArrayPointer);
        Item output = a[randNb];
        return output;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {

        Item iterArr[] = a.clone();
        // return Iterator object

        Iterator<Item> iter = new Iterator<Item>() {
            int currentPointer = currentArrayPointer;

            public boolean hasNext() {
                // if array[pointer+1] is not null
                if (currentPointer != 0) {
                    return true;
                }
                return false;
            }

            public Item next() {

                if (hasNext()) {
                    Item popedItem = dequeueIter();

                    return popedItem;
                }
                else {
                    java.util.NoSuchElementException exc = new java.util.NoSuchElementException();
                    throw exc;

                }
            }

            public void remove() {
                UnsupportedOperationException exc = new UnsupportedOperationException();
                throw exc;
            }

            private Item dequeueIter() {
                if (currentPointer == 0) {
                    java.util.NoSuchElementException exc = new java.util.NoSuchElementException();
                    throw exc;
                }
                int randNb = StdRandom.uniform(currentPointer);

                //swap front and random
                Item temp = iterArr[currentPointer - 1];
                iterArr[currentPointer - 1] = iterArr[randNb];
                iterArr[randNb] = temp;

                // get front item
                Item frontItem = iterArr[currentPointer - 1];
                iterArr[currentPointer - 1] = null;

                if (currentPointer > 0) {
                    currentPointer--;
                }

                return frontItem;
            }


        };

        return iter;
    }

    private void doubleArraySize() {
        // doubles the array size
        Item[] b = (Item[]) new Object[arrayMax * 2];

        for (int i = 0; i < arrayMax; i++) {
            b[i] = a[i];
        }
        arrayMax = arrayMax * 2;
        a = b;
    }

    private void divideArraySize() {
        // divides by 2 the array size
        Item[] b = (Item[]) new Object[arrayMax / 2];

        for (int i = 0; i < arrayMax / 2; i++) {
            b[i] = a[i];
        }
        arrayMax = arrayMax / 2;
        a = b;
    }

    private void printArray() {
        Iterator<Item> iter = iterator();
        while (iter.hasNext()) {
            StdOut.print(iter.next());
            StdOut.print(",");
        }
        StdOut.println();
    }

    // unit testing (required)
    public static void main(String[] args) {
        /*
        RandomizedQueue<Integer> rQ = new RandomizedQueue();
        rQ.enqueue(000);
        rQ.enqueue(45);
        rQ.enqueue(376);
        rQ.enqueue(111);
        rQ.enqueue(192);
        rQ.printArray();
        StdOut.println("Samples");
        StdOut.println(rQ.sample());
        StdOut.println(rQ.sample());
        StdOut.println(rQ.sample());
        StdOut.println(rQ.sample());
        StdOut.println("/Samples");
        rQ.printArray();
        StdOut.println("Dequeue");
        StdOut.println(rQ.dequeue());
        StdOut.println(rQ.dequeue());
        StdOut.println(rQ.dequeue());
        rQ.printArray();
        StdOut.println("Stats");
        StdOut.println(rQ.size());

        // strings
        RandomizedQueue<String> rQ2 = new RandomizedQueue();
        rQ2.enqueue("Hey");
        rQ2.enqueue("boy");
        rQ2.enqueue("what");
        rQ2.enqueue("is");
        rQ2.enqueue("up");
        rQ2.printArray();
        StdOut.println("Samples");
        StdOut.println(rQ2.sample());
        StdOut.println(rQ2.sample());
        StdOut.println(rQ2.sample());
        StdOut.println(rQ2.sample());
        rQ2.printArray();
        StdOut.println("Dequeue");
        StdOut.println("Pointer :" + rQ2.currentArrayPointer);
        StdOut.println(rQ2.dequeue());
        StdOut.println("Pointer :" + rQ2.currentArrayPointer);

        StdOut.println(rQ2.dequeue());
        StdOut.println("Pointer :" + rQ2.currentArrayPointer);

        StdOut.println(rQ2.dequeue());
        StdOut.println("Pointer :" + rQ2.currentArrayPointer);

        StdOut.println(rQ2.dequeue());
        StdOut.println("Pointer :" + rQ2.currentArrayPointer);
        StdOut.println(rQ2.dequeue());
        StdOut.println("Pointer :" + rQ2.currentArrayPointer);
        StdOut.println(rQ2.dequeue());
        StdOut.println("Pointer :" + rQ2.currentArrayPointer);

        StdOut.println(rQ2.dequeue());
        rQ2.printArray();
        StdOut.println("Stats");
        StdOut.println(rQ2.size());
        */

    }

}

