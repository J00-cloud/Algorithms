import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {

    private final char[][] _tiles;
    //private char[][] _solvedTiles; // optim : delete
    private int n;
    private int[] emptyPosition = new int[2];
    private int cachedManhattan = 0;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {

        n = tiles[0].length;
        int solvedVal = 1;
        _tiles = new char[n][n];
        //_solvedTiles = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int y = 0; y < n; y++) {
                _tiles[i][y] = (char) tiles[i][y];

                if (solvedVal == (n * n)) {
                    //_solvedTiles[i][y] = 0;
                }
                else {
                    //_solvedTiles[i][y] = (char) solvedVal;
                }

                if (tiles[i][y] == 0) {
                    emptyPosition[0] = i;
                    emptyPosition[1] = y;
                }
                solvedVal++;
            }
        }
        computeManhattan();


    }

    private void computeManhattan() {
        int distance = 0;
        int added = 0;
        for (int i = 0; i < n; i++) {//row
            for (int y = 0; y < n; y++) {//col
                if (_tiles[i][y] != 0) {
                    int r = (_tiles[i][y] - 1) / n;
                    int c = _tiles[i][y] - (r * n) - 1;
                    added = Math.max(i, r) - Math.min(i, r) + Math.max(y, c) - Math.min(y, c);
                    distance += added;

                    //StdOut.println(
                    //        _tiles[i][y] + " = row :" + r + ", col :" + c + "dist : " + added);
                }

            }
        }
        cachedManhattan = distance;
    }

    // string representation of this board
    public String toString() {
        String outStr = n + "\n";

        for (int i = 0; i < n; i++) {
            for (int y = 0; y < n; y++) {
                outStr = outStr + (int) _tiles[i][y] + " ";
            }
            outStr = outStr + "\n";
        }

        return outStr;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        // what it should be
        // is it equal  ?
        int hammingDist = 0;
        int nodeCompare = 1;
        for (int i = 0; i < n; i++) {
            for (int y = 0; y < n; y++) {
                if (nodeCompare == (n * n)) {
                    nodeCompare = 0;
                }
                if (_tiles[i][y] != 0) {
                    if (_tiles[i][y] != nodeCompare) {
                        hammingDist++;
                    }
                }
                nodeCompare++;
            }
        }
        return hammingDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // while diff != 0
        //      if not on the same line : change line
        //      if on the same line : count diff
        return cachedManhattan;

    }

    // is same line : if divisors of the 2 ints are the same

    // is this board the goal board?
    public boolean isGoal() {

        if (cachedManhattan == 0) {
            return true;
        }
        else {
            return false;
        }

    }

    // does this board equal y?
    public boolean equals(Object y) {

        if (y == null) {
            return false;
        }
        else if (y == this) {
            return true;
        }
        else if (y.getClass().equals(Board.class)) {
            final Board yb = (Board) y;
            if (yb.dimension() != n) {
                return false;
            }
            for (int i = 0; i < n; i++) {
                for (int z = 0; z < n; z++) {
                    if (yb._tiles[i][z] != this._tiles[i][z]) {
                        return false;
                    }
                }
            }
            return true;

        }
        else {
            return false;
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        // you can return an arrayList, stack or queue
        ArrayList<Board> iterBoard = new ArrayList<>();

        int[][] oTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int y = 0; y < n; y++) {
                oTiles[i][y] = _tiles[i][y];
            }
        }

        if (emptyPosition[0] > 0) {
            int[][] newTiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int y = 0; y < n; y++) {
                    newTiles[i][y] = oTiles[i][y];
                }
            }
            int switchA = oTiles[emptyPosition[0] - 1][emptyPosition[1]];
            int switchB = oTiles[emptyPosition[0]][emptyPosition[1]];

            newTiles[emptyPosition[0]][emptyPosition[1]] = switchA;
            newTiles[emptyPosition[0] - 1][emptyPosition[1]] = switchB;

            Board b1 = new Board(newTiles);
            iterBoard.add(b1);
        }
        if (emptyPosition[0] < (n - 1)) {
            int[][] newTiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int y = 0; y < n; y++) {
                    newTiles[i][y] = oTiles[i][y];
                }
            }
            int switchA = oTiles[emptyPosition[0] + 1][emptyPosition[1]];
            int switchB = oTiles[emptyPosition[0]][emptyPosition[1]];

            newTiles[emptyPosition[0]][emptyPosition[1]] = switchA;
            newTiles[emptyPosition[0] + 1][emptyPosition[1]] = switchB;

            Board b2 = new Board(newTiles);
            iterBoard.add(b2);
        }
        if (emptyPosition[1] > 0) {
            int[][] newTiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int y = 0; y < n; y++) {
                    newTiles[i][y] = oTiles[i][y];
                }
            }
            int switchA = oTiles[emptyPosition[0]][emptyPosition[1] - 1];
            int switchB = oTiles[emptyPosition[0]][emptyPosition[1]];

            newTiles[emptyPosition[0]][emptyPosition[1]] = switchA;
            newTiles[emptyPosition[0]][emptyPosition[1] - 1] = switchB;

            Board b3 = new Board(newTiles);
            iterBoard.add(b3);
        }
        if (emptyPosition[1] < (n - 1)) {
            int[][] newTiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int y = 0; y < n; y++) {
                    newTiles[i][y] = oTiles[i][y];
                }
            }
            int switchA = oTiles[emptyPosition[0]][emptyPosition[1] + 1];
            int switchB = oTiles[emptyPosition[0]][emptyPosition[1]];

            newTiles[emptyPosition[0]][emptyPosition[1]] = switchA;
            newTiles[emptyPosition[0]][emptyPosition[1] + 1] = switchB;

            Board b4 = new Board(newTiles);
            iterBoard.add(b4);
        }
        return iterBoard;
    }

    // a board that is obtained by exchanging any pair of tiles
    // a switch in tiles would allow to check if solvable
    public Board twin() {
        int prevI = -1;
        int prevY = -1;
        int switchVal = -1;
        int step = 0;
        int[][] twinTiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int y = 0; y < n; y++) {
                if (step >= 2) {
                    twinTiles[i][y] = _tiles[i][y];
                }
                else if (prevI == -1 && _tiles[i][y] != 0) {
                    prevI = i;
                    prevY = y;
                    switchVal = _tiles[i][y];
                    step++;
                }
                else if (prevI != -1 && _tiles[i][y] != 0) {
                    int tempVal = _tiles[i][y];
                    twinTiles[i][y] = switchVal;
                    twinTiles[prevI][prevY] = tempVal;
                    step++;
                }
            }
        }

        Board twinBoard = new Board(twinTiles);

        return twinBoard;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(
                "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part I\\4_PriorityQueues\\8puzzle\\puzzle04.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);

        StdOut.println(initial.toString());

        StdOut.println("Manhattan = " + initial.manhattan());
        StdOut.println("Hamming = " + initial.hamming());

        for (Board b : initial.neighbors()) {
            StdOut.println(b.toString());
        }

        StdOut.println(initial.toString());
        StdOut.println(initial.twin().toString());

        StdOut.println(initial.equals(initial));
        StdOut.println(initial.equals(initial.twin()));

    }


}
