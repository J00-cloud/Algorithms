public class Percolation {

    private int[] grid;
    private int[] openGrid;
    private final int _n;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            IllegalArgumentException exc = new IllegalArgumentException();
            throw exc;
        }
        _n = n;
        grid = new int[n * n + 2];
        openGrid = new int[n * n + 2];

        // setup grid and openGrid
        for (int i = 1; i <= (n * n + 1); i++) {
            grid[i] = i;
            if (i < (n * n + 1)) {
                openGrid[i] = 1;
            }
        }

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if ((row <= 0) || (col <= 0) || (row > _n) || (col > _n)) {
            IllegalArgumentException exc = new IllegalArgumentException();
            throw exc;
        }
        int index = rowColToIndex(row, col);

        // set opengrid as open
        openGrid[index] = 0;

        // if isOpen left, union left, index
        if (((index % _n) != 1) && (openGrid[index - 1] == 0)) { //6%5 = 1
            union(index, index - 1);
            //System.out.println("join left !" + index + "%" + index % _n);
        }

        // if isOpen right, union right, index
        if (((index % _n) != 0) && (openGrid[index + 1] == 0)) { //10%5 = 0
            union(index, index + 1);
            //System.out.println("join right !" + index);
        }

        // if isOpen up, union up, index
        if ((index > _n) && (openGrid[index - _n] == 0)) {
            union(index, index - _n);
        }
        if (index <= (_n)) {
            union(index, 0);
        }

        // if isOpen down, union down, index
        if ((index <= (_n * (_n - 1))) && (openGrid[index + _n] == 0)) {
            union(index, index + _n);
        }

        if (index > (_n * (_n - 1))) {
            union(index, (_n * _n + 1));
        }

    }

    // is the site (row, col) open? (0)
    public boolean isOpen(int row, int col) {
        if ((row <= 0) || (col <= 0) || (row > _n) || (col > _n)) {
            IllegalArgumentException exc = new IllegalArgumentException();
            throw exc;
        }

        int index = rowColToIndex(row, col);

        if (openGrid[index] == 0) {
            return true;
        }
        else {
            return false;
        }

    }


    // is the site (row, col) full? is it fullOpe
    public boolean isFull(int row, int col) {
        if ((row <= 0) || (col <= 0) || (row > _n) || (col > _n)) {
            IllegalArgumentException exc = new IllegalArgumentException();
            throw exc;
        }

        int index = rowColToIndex(row, col);

        if (root(index) == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int openSites = _n * _n;
        for (int i = 1; i <= (_n * _n); i++) { // from 1 to 25
            openSites = openSites - openGrid[i];
        }
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // is there a number above X in the list of full
        int rootOfEndNode = root(grid[_n * _n + 1]);
        if (rootOfEndNode == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    private int rowColToIndex(int row, int col) {
        int index = ((row - 1) * _n) + col;
        return (index);
    }

    private void union(int valA, int valB) {
        int rootA = root(valA);
        int rootB = root(valB);

        if (rootA < rootB) {
            grid[rootB] = rootA;
        }
        else {
            grid[rootA] = rootB;
        }

    }

    private int root(int index) {
        int firstIndex = index;
        int indexValue = grid[index];

        while (indexValue != index) {
            index = indexValue;
            indexValue = grid[index];
        }
        grid[firstIndex] = index; // ln
        return index;
    }


    // test client (optional)
    public static void main(String[] args) {
        /*
        System.out.println("");
        System.out.println("new Test");

        Percolation perc = new Percolation(5);

        perc.open(1, 1);
        perc.open(2, 3);
        perc.open(3, 2);
        perc.open(3, 3);

        perc.open(1, 3);
        for (int i = 0; i < (25); i++) {
            System.out.println(
                    " grid index : " + i + " root " + perc.root(i) + " closed " + perc.openGrid[i]);
        }

        System.out.println("Root of r3c2 (i=12) : " + perc.root(12));
        System.out.println("is full r3c2? : " + perc.isFull(3, 2));
        System.out.println("percolates? : " + perc.percolates());

        perc.open(5, 5);
        perc.open(4, 5);
        perc.open(3, 5);
        perc.open(2, 5);

        System.out.println("Root of r5c5 (i=25) : " + perc.root(25));
        System.out.println("is full r3c2? : " + perc.isFull(5, 5));
        System.out.println("percolates? : " + perc.percolates());

        perc.open(3, 4);

        System.out.println("Root of r5c5 (i=25) : " + perc.root(25));
        System.out.println("is full r3c2? : " + perc.isFull(5, 5));
        System.out.println("percolates? : " + perc.percolates());
        */
        Percolation perc2 = new Percolation(6);
        perc2.open(1, 6);
        perc2.open(2, 6);
        perc2.open(2, 1);
        for (int i = 0; i < (37); i++) {
            System.out.println(
                    " grid index : " + i + " root " + perc2.root(i) + " closed "
                            + perc2.openGrid[i]);
        }

    }

}
