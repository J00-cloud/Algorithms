import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;

    private int count = 0;

    private class Node {
        Item item;
        Node next = null;
        Node prev = null;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the deque
    public int size() {
        // returns arrayLength
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            IllegalArgumentException exc = new IllegalArgumentException();
            throw exc;
        }
        // goes to arrayLength position
        // if array Length = array max,
        //      create a new array with size max = 2x current size
        //      re copy all the array
        if (isEmpty()) {
            first = new Node();
            first.item = item;
            first.next = null;
            first.prev = null;
            last = first;
        }
        else {
            Node oldfirst = first;
            first = new Node();
            oldfirst.next = first;
            first.item = item;
            first.next = null;
            first.prev = oldfirst;
        }

        count++;
    }


    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            IllegalArgumentException exc = new IllegalArgumentException();
            throw exc;
        }

        Node prevLast = last;
        last = new Node();
        last.item = item;
        last.next = prevLast;
        last.prev = null;
        if (isEmpty()) first = last;

        if (prevLast != null) {
            prevLast.prev = last;
        }
        count++;

    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (count == 0) {
            java.util.NoSuchElementException exc = new java.util.NoSuchElementException();
            throw exc;
        }

        Item item = first.item;
        first = first.prev;

        if (first != null) {
            first.next = null;
        }
        count--;

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (count == 0) {
            java.util.NoSuchElementException exc = new java.util.NoSuchElementException();
            throw exc;
        }

        Item item = last.item;
        Node prevNode = last.next;
        last = null;

        last = prevNode;
        if (last != null) {
            last.prev = null;
        }
        count--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {

        // return Iterator object
        Iterator<Item> iter = new Iterator<Item>() {
            Node current = first;

            public boolean hasNext() {
                // if array[pointer+1] is not null
                return current != null;
            }

            public Item next() {
                if (hasNext()) {
                    Item item = current.item;
                    current = current.prev;
                    return item;
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
        };

        return iter;
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
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.removeLast();
        deque.removeLast();
        deque.addFirst(5);
        StdOut.println(deque.removeLast());


         */
        Deque<Integer> DqNode = new Deque<Integer>();

        StdOut.println("Add First");
        DqNode.addFirst(12);
        DqNode.addFirst(13);
        DqNode.addFirst(131);
        DqNode.addFirst(132);
        DqNode.addFirst(14);
        StdOut.println(DqNode.size());
        DqNode.printArray();
        StdOut.println("Remove Last");
        StdOut.println(DqNode.removeLast());
        StdOut.println(DqNode.removeLast());
        StdOut.println(DqNode.removeLast());
        StdOut.println(DqNode.removeLast());
        StdOut.println(DqNode.removeLast());

        StdOut.println(DqNode.isEmpty());

        DqNode.printArray();
        StdOut.println("Add First");
        DqNode.addFirst(12);
        DqNode.addFirst(13);
        DqNode.addFirst(131);
        DqNode.addFirst(132);
        DqNode.addFirst(14);
        StdOut.println(DqNode.size());
        DqNode.printArray();
        StdOut.println("Remove First");
        StdOut.println(DqNode.removeFirst());
        StdOut.println(DqNode.removeFirst());
        StdOut.println(DqNode.removeFirst());
        StdOut.println(DqNode.removeFirst());
        StdOut.println(DqNode.removeFirst());
        StdOut.println(DqNode.removeFirst());
        StdOut.println(DqNode.removeFirst());

        Deque<Integer> DqNode2 = new Deque<Integer>();

        DqNode2.printArray();
        StdOut.println("Add Last");
        DqNode2.addLast(12);
        DqNode2.addLast(13);
        DqNode2.addLast(131);
        DqNode2.addLast(132);
        DqNode2.addLast(14);
        //StdOut.println(DqNode2.size());
        DqNode2.printArray();

        StdOut.println("Remove First");
        StdOut.println(DqNode2.removeFirst());
        StdOut.println(DqNode2.removeFirst());
        StdOut.println(DqNode2.removeFirst());
        StdOut.println(DqNode2.removeFirst());
        StdOut.println(DqNode2.removeFirst());
        StdOut.println(DqNode2.removeFirst());
        StdOut.println(DqNode2.removeFirst());


        DqNode2.printArray();
        StdOut.println("Add Last");
        DqNode2.addLast(12);
        DqNode2.addLast(13);
        DqNode2.addLast(131);
        DqNode2.addLast(132);
        DqNode2.addLast(14);
        StdOut.println(DqNode2.size());
        DqNode2.printArray();
        StdOut.println("Remove last");
        StdOut.println(DqNode2.removeLast());
        StdOut.println(DqNode2.removeLast());
        StdOut.println(DqNode2.removeLast());
        StdOut.println(DqNode2.removeLast());
        StdOut.println(DqNode2.removeLast());
        StdOut.println(DqNode2.removeLast());
        StdOut.println(DqNode2.removeLast());

    }

}
