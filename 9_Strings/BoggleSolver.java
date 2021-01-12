import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BoggleSolver {
    private Node firstNode;
    private Bag<Node> closedNodes = new Bag<Node>();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        firstNode = new Node(dictionary[0].charAt(0));
        for (String word : dictionary) {
            firstNode.addWord(word);
        }
    }

    private class Node {
        private char _charVal;
        Node leftNode;
        Node rightNode;
        Node centerNode;
        boolean endWord = false;
        boolean closedPath = false;

        public Node(char charVal) {
            _charVal = charVal;
        }

        public Iterable<Node> subNodes() {
            ArrayList<Node> subNodes = new ArrayList<Node>();
            if (!(leftNode == null)) {
                subNodes.add(leftNode);
            }
            if (!(rightNode == null)) {
                subNodes.add(rightNode);
            }
            if (!(centerNode == null)) {
                subNodes.add(centerNode);
            }
            return subNodes;
        }


        public Node getEndNode(String word) {
            char firstChar = word.charAt(0);
            if (this == null) {
                return null;
            }
            if (word.length() == 1 && _charVal == firstChar) {
                return this;
            }

            if (!(centerNode == null)) {
                if (_charVal == firstChar) {
                    Node chosenNode = centerNode.getEndNode(word.substring(1));
                    if (!(chosenNode == null)) {
                        return chosenNode;
                    }
                }
            }
            if (firstChar < _charVal) {
                if (leftNode == null) {
                    return null;
                }
                Node chosenNode = leftNode.getEndNode(word); // right sense ?
                if (!(chosenNode == null)) {
                    return chosenNode;
                }
            }
            else if (firstChar > _charVal) {
                if (rightNode == null) {
                    return null;
                }
                Node chosenNode = rightNode.getEndNode(word);
                if (!(chosenNode == null)) {
                    return chosenNode;
                }
            }

            return null;
        }


        private void addWord(String word) {
            char currentChar = word.charAt(0);

            if (_charVal == currentChar) {

                if (word.length() > 1) {
                    if (centerNode == null) {
                        centerNode = new Node(word.charAt(1)); // adding twice the same character
                    }
                    centerNode.addWord(word.substring(1));
                }
                else {
                    endWord = true;
                }

            }
            if (currentChar < _charVal) {
                if (leftNode == null) {
                    leftNode = new Node(currentChar);
                }
                leftNode.addWord(word);

            }
            if (currentChar > _charVal) {
                if (rightNode == null) {
                    rightNode = new Node(currentChar);
                }
                rightNode.addWord(word);

            }

        }

    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        ArrayList<String> validWords = new ArrayList<String>();

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                bogglePath bgP = new bogglePath(board, i, j);
                String nextString;

                while ((nextString = bgP.getNext()) != null) {
                    nextString = nextString.replace("Q", "QU");
                    //StdOut.println(nextString);
                    if (!isPath(nextString)) {
                        bgP.goBack();
                    }
                    else if (isWord(nextString) && !validWords.contains(nextString)
                            && nextString.length() > 2) {
                        validWords
                                .add(nextString); // issue : All potential paths are potential solutions...
                        setAsClosedPath(nextString, board.cols() * board.rows());
                    }
                }
            }
        }
        openBackNodes();

        return validWords;

    }

    private void openBackNodes() {
        for (Node n : closedNodes) {
            n.closedPath = false;
        }
    }

    // make a tree for the boggles
    private void setAsClosedPath(String word, int boardMaxSize) {
        word = word.toUpperCase();
        Node startNodeTrie = firstNode;
        Node endNode = startNodeTrie.getEndNode(word);

        if (endNode.rightNode == null || endNode.rightNode.closedPath == true) {
            if (endNode.leftNode == null || endNode.leftNode.closedPath == true) {
                if (endNode.centerNode == null || endNode.centerNode.closedPath == true) {

                    endNode.closedPath = true;
                    closedNodes.add(endNode);
                    String subWord = word.substring(0, word.length() - 1);
                    setAsClosedPath(subWord, boardMaxSize);
                    return;
                }
            }
        }

        if (word.replace("QU", "Q").length() >= (boardMaxSize)) {

            endNode.closedPath = true;
            closedNodes.add(endNode);
            String subWord = word.substring(0, word.length() - 1);
            setAsClosedPath(subWord, boardMaxSize);
            return;
        }

        return;

    }


    private boolean isWord(String word) {
        // option : return node instead
        word = word.toUpperCase();
        Node startNodeTrie = firstNode;
        Node endNode = startNodeTrie.getEndNode(word);
        if (endNode == null) {
            return false;
        }
        else if (endNode.closedPath == true) {
            return false;
        }
        else if (endNode.endWord) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isPath(String word) {
        Node startNodeTrie = firstNode;
        Node endNode = startNodeTrie.getEndNode(word);
        if (endNode == null) {
            return false;
        }
        else if (endNode.closedPath == true) {
            return false;
        }
        else {
            return true;
        }
    }


    private void printTree() { // BFS
        Queue<Node> nodeList = new Queue<Node>();
        nodeList.enqueue(firstNode);
        StdOut.println(firstNode._charVal);

        while (!nodeList.isEmpty()) {
            nodeList = getSubNodes(nodeList);
            StdOut.println();
        }
    }

    private Queue<Node> getSubNodes(Queue<Node> nodes) {
        Queue<Node> subNodes = new Queue<Node>();

        while (!nodes.isEmpty()) {
            Node currentNode = nodes.dequeue();


            for (Node n : currentNode.subNodes()) {
                subNodes.enqueue(n);
                StdOut.print("_" + n._charVal);
                if (n.endWord) {
                    StdOut.print("(end)");
                }
            }

        }

        return subNodes;
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!isWord(word)) {
            return 0;
        }
        int len = word.length();
        if (len < 3) {
            return 0;
        }
        else if (len < 5) {
            return 1;
        }
        else if (len < 6) {
            return 2;
        }
        else if (len < 7) {
            return 3;
        }
        else if (len < 8) {
            return 5;
        }
        else {
            return 11;
        }
    }


    private class bogglePath {
        boolean[][] donePath;
        int[] position = new int[2];
        int depth;
        String newString = "";
        BoggleBoard _bgg;
        ArrayList<Integer> direction = new ArrayList<Integer>();
        ArrayList<int[]> positionList = new ArrayList<int[]>();

        public bogglePath(BoggleBoard bgg, int startR, int startC) {
            _bgg = bgg;
            depth = 0;
            direction.add(0);
            newString = newString + bgg.getLetter(startR, startC);
            position[0] = startR;
            position[1] = startC;
            positionList.add(depth, position);
            donePath = new boolean[_bgg.rows()][_bgg.cols()];
            donePath[startR][startC] = true;
        }

        public String getNext() {
            position = positionList.get(depth);

            while (depth >= 0) {
                int dir = direction.get(depth);
                int[] newPosition = isvalid(dir);

                if (newPosition != null) {
                    depth++;
                    position = newPosition;
                    newString = newString + _bgg.getLetter(position[0], position[1]);

                    donePath[position[0]][position[1]] = true;
                    direction.add(depth, 0);
                    positionList.add(depth, position);
                    return newString;
                }
                else if (dir >= 8) {
                    if (depth <= 0) {
                        depth--;
                        break;
                    }
                    goBack();
                }
                else {
                    dir++;
                    direction.add(depth, dir);
                }

            }
            return null;
        }

        public int[] isvalid(int direction) {
            int deltaR = 0;
            int deltaC = 0;
            switch (direction) {
                case 0:
                    deltaR = -1;
                    break;
                case 1:
                    deltaR = -1;
                    deltaC = -1;
                    break;
                case 2:
                    deltaC = -1;
                    break;
                case 3:
                    deltaR = 1;
                    deltaC = -1;
                    break;
                case 4:
                    deltaR = 1;
                    break;
                case 5:
                    deltaR = 1;
                    deltaC = 1;
                    break;
                case 6:
                    deltaC = 1;
                    break;
                case 7:
                    deltaR = -1;
                    deltaC = 1;
            }
            int newR = position[0] + deltaR;
            int newC = position[1] + deltaC;
            if (newR < 0 || newC < 0) {
                return null;
            }
            if (newR >= _bgg.rows() || newC >= _bgg.cols()) {
                return null;
            }
            if (donePath[newR][newC] == true) {
                return null;
            }
            int[] out = { newR, newC };
            return out;

        }

        public void goBack() {
            if (depth > 0) {
                donePath[position[0]][position[1]] = false; // erase current path
            }
            depth = depth - 1; // go back
            position = positionList.get(depth);

            newString = newString.substring(0, newString.length() - 1);
            direction.add(depth, direction.get(depth) + 1); // change direction
        }

    }


    public static void main(String[] args) {

        String dictionaryFile
                = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part II\\4_Strings\\boggle\\dictionary-algs4.txt";
        //String dictionaryFile
        //       = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part II\\4_Strings\\boggle\\dictionary-16q.txt";


        In in = new In(dictionaryFile);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);

        //solver.printTree();
        int i = 0;
        //String boardFile
        //        = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part II\\4_Strings\\boggle\\board-16q.txt";
        long date = System.currentTimeMillis() / 1000;
        System.out.println(date);
        while (i < 1) {
            BoggleBoard board = new BoggleBoard();
            StdOut.println(board);
            int score = 0;
            for (String word : solver.getAllValidWords(board)) {
                StdOut.println(word);
                score += solver.scoreOf(word);
            }
            StdOut.println("Score = " + score);
            i++;
        }
        date = System.currentTimeMillis() / 1000;
        System.out.println(date);
    }

}





/*


 */

/*
    public static void main(String[] args) {


     */
