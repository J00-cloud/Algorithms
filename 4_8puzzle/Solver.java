import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Iterator;

public class Solver {

    private searchNode foundNode;
    private int _moves = 0;
    private Boolean solvable = false;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) {
            IllegalArgumentException ex = new IllegalArgumentException();
            throw ex;
        }
        MinPQ<searchNode> priorityQueue;
        MinPQ<searchNode> t_priorityQueue;

        ArrayList<Board> doneBoards = new ArrayList<Board>();
        ArrayList<Board> t_doneBoards = new ArrayList<Board>();

        Boolean solutionFound = false;
        searchNode firstNode = new searchNode(initial, null, 0);

        Board t_initial = initial.twin();
        searchNode t_firstNode = new searchNode(t_initial, null, 0);

        doneBoards.add(initial);
        t_doneBoards.add(t_initial);

        priorityQueue = new MinPQ<searchNode>();
        t_priorityQueue = new MinPQ<searchNode>();

        priorityQueue.insert(firstNode);
        t_priorityQueue.insert(t_firstNode);

        if (initial.isGoal()) { // if twin is goal ?

            foundNode = firstNode;
            solutionFound = true;
            solvable = true;
        }

        int i = 0;
        int enque = 0;
        int deque = 0;
        while (!solutionFound) {
            //StdOut.println("step " + i + "size : " + priorityQueue.size());

            searchNode currentNode = priorityQueue.delMin();


            for (Board board : currentNode.nodeBoard.neighbors()) {
                if (currentNode.moves == 0 || !board.equals(currentNode.previousNode.nodeBoard)) {

                    searchNode newNode = new searchNode(board, currentNode, currentNode.moves + 1);

                    priorityQueue.insert(newNode);
                    enque++;
                    if (board.isGoal()) {
                        priorityQueue.insert(newNode);
                        foundNode = newNode;
                        solutionFound = true;
                        solvable = true;
                        break;
                    }

                }

            }
            //printPQ(priorityQueue);

            deque++;

            if (priorityQueue.size() == 0) {
                _moves = -1;
                return;
            }

            searchNode t_currentNode = t_priorityQueue.delMin();

            for (Board t_board : t_currentNode.nodeBoard.neighbors()) {
                if (t_currentNode.moves == 0 || !t_board
                        .equals(t_currentNode.previousNode.nodeBoard)) {

                    searchNode t_newNode = new searchNode(t_board, t_currentNode,
                                                          t_currentNode.moves + 1);

                    t_priorityQueue.insert(t_newNode);
                    t_doneBoards.add(t_board);

                    if (t_board.isGoal()) {
                        foundNode = t_newNode;
                        solutionFound = true;
                        solvable = false;
                    }

                }


            }

            i++;
        }

        // compute moves
        if (solutionFound == true) {
            computeMoves(foundNode);
        }
        //StdOut.println("enq : " + enque + ", deque " + deque);

    }

    private void computeMoves(searchNode usedNode) {
        int moveNode = -1;
        searchNode currentNode = usedNode;
        int actualMoves = 0;
        moveNode = currentNode.moves;

        while (moveNode != 0) {
            currentNode = currentNode.previousNode;
            moveNode = currentNode.moves;
            actualMoves++;
        }
        _moves = actualMoves;
    }


    private static class searchNode implements Comparable<searchNode> {

        public int priority;
        public searchNode previousNode;
        public Board nodeBoard;
        public int moves;
        public int manhattan;

        public searchNode(Board inBoard, searchNode inPreviousBoard, int inMoves) {
            nodeBoard = inBoard;
            previousNode = inPreviousBoard;
            moves = inMoves;
            manhattan = nodeBoard.manhattan();
            priority = manhattan + moves;

        }

        public int compareTo(searchNode o) {

            if (o.priority != priority) {
                return priority - o.priority;
            }
            else {
                return manhattan - o.manhattan;
            }

        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable) {
            return -1;
        }
        return _moves;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    private static void printPQ(MinPQ<searchNode> pq) {

        for (Iterator<searchNode> iter2 = pq.iterator(); iter2.hasNext(); ) {
            searchNode element = iter2.next();
            StdOut.println(
                    "Priority :" + element.priority + "(moves :" + element.moves + ", manhattan :"
                            + element.nodeBoard.manhattan() + ")");
            StdOut.print(element.nodeBoard.toString());
        }
        return;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        // add pointer to previous node
        if (moves() == -1) {
            return null;
        }
        if (!solvable) {
            return null;
        }
        Stack<Board> iterStack = new Stack<Board>();
        int moveNode = -1;
        searchNode currentNode = foundNode;
        iterStack.push(foundNode.nodeBoard);
        moveNode = currentNode.moves;

        while (moveNode != 0) {
            currentNode = currentNode.previousNode;
            iterStack.push(currentNode.nodeBoard);
            moveNode = currentNode.moves;
        }
        return iterStack;
    }


    // test client (see below)
    public static void main(String[] args) {

        // priority = estimated min number of moves

        In in = new In(
                "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part I\\4_PriorityQueues\\8puzzle\\puzzle3x3-15.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);

        Solver solverB1 = new Solver(initial);

        StdOut.println("Solution : " + solverB1.moves());

        Iterable<Board> boardSol = solverB1.solution();

        for (Board solvBoard : boardSol) {
            StdOut.println(solvBoard.toString());
        }

    }

}
