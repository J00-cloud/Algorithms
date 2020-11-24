import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int width;
    private int height;

    private int[][] rbgPic;
    private double[][] energyMap;

    //private distTo;
    //private int[] edgeTo;
    private int finalNode;
    private boolean vertical = true;
    private boolean override = true;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {

        if (picture == null) {
            throw new IllegalArgumentException();
        }
        width = picture.width();
        height = picture.height();

        picToRBG(picture);
        updateEnergy();
        computeDistToEdgeTo(energyMap);

    }

    private void picToRBG(Picture picture) {
        rbgPic = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int i = 0; i < width; i++) {
                rbgPic[y][i] = picture.getRGB(i, y);
            }
        }
    }


    private void updateEnergy() {
        energyMap = new double[height][width]; // row col
        for (int y = 0; y < height; y++) {
            for (int i = 0; i < width; i++) {
                computeEnergy(i, y);
            }
        }

    }


    private int[] computeDistToEdgeTo(double[][] mapIn) {
        double[] distTo = new double[width * height];
        int[] edgeTo = new int[width * height];
        int index = 0;

        for (int i = 0; i < height; i++) {
            for (int y = 0; y < width; y++) {

                // get current node stats
                index = i * width + y;
                double currentVal = mapIn[i][y];

                if (i == 0) {
                    distTo[index] = currentVal;
                }
                else if (y > 0 && y < (width - 1)) {
                    double OlderLeft = distTo[index - width - 1];
                    double OlderMid = distTo[index - width];
                    double OlderRight = distTo[index - width + 1];

                    double min = Math.min(OlderLeft, OlderMid);
                    min = Math.min(min, OlderRight);

                    if (min == OlderLeft) {
                        edgeTo[index] = index - width - 1;
                    }
                    else if (min == OlderMid) {
                        edgeTo[index] = index - width;
                    }
                    else {
                        edgeTo[index] = index - width + 1;
                    }

                    distTo[index] = currentVal + min;
                }
                else {
                    distTo[index] = currentVal + distTo[index - width];
                    edgeTo[index] = index - width;
                }

            }

        }

        double minVal = -1;
        int minNode = -1;
        // get the highest in the last row
        for (int k = (width * (height - 1)); k < width * height; k++) {
            if (minVal == -1 || distTo[k] < minVal) {
                minVal = distTo[k];
                minNode = k;
            }
        }
        finalNode = minNode;

        return edgeTo;
    }

    /*
    private ArrayList<Integer> shortestPath() {
        int node = finalNode;
        ArrayList<Integer> shortestPath = new ArrayList<Integer>();
        shortestPath.add(node);

        while (node > width) {
            node = edgeTo[node];
            shortestPath.add(node);
        }

        return shortestPath;
    }

     */

    private void transposeEnergy() {
        int tempWidth = width;
        width = height;
        height = tempWidth;

        double[][] transposedEnergy = new double[height][width]; //row, col
        int[][] transposedRBG = new int[height][width]; //row, col


        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                transposedEnergy[r][c] = energyMap[c][r];
                transposedRBG[r][c] = rbgPic[c][r];
            }
        }

        vertical = !vertical;

        energyMap = transposedEnergy;
        rbgPic = transposedRBG;

    }

    // current picture
    public Picture picture() {

        if (!vertical) {
            transposeEnergy();
        }

        Picture newPic = new Picture(width, height);
        for (int c = 0; c < width; c++) {
            for (int r = 0; r < height; r++) {
                newPic.setRGB(c, r, rbgPic[r][c]);
            }
        }
        return newPic;
    }

    // width of current picture
    public int width() {
        if (!vertical && override == false) {
            transposeEnergy();
        }
        return width;
    }

    // height of current picture
    public int height() {
        if (!vertical && override == false) {
            transposeEnergy();
        }
        return height;
    }

    private void computeEnergy(int x, int y) { // col, row

        if (x > (width - 1) || y > (height - 1) || x < 0 || y < 0) {
            return;
        }
        if ((x == 0 || y == 0) || (x == (width - 1) || y == (height - 1))) {
            energyMap[y][x] = 1000.00;
            return;
        }
        int[] r = new int[4];
        int[] g = new int[4];
        int[] b = new int[4];
        double energy;

        for (int i = 0; i < 4; i++) {
            int shiftX = 0;
            int shiftY = 0;
            // should be 0, -1 or +1
            if (i > 1) {
                if (i % 2 == 0) {
                    shiftX = 1;
                }
                else {
                    shiftX = -1;
                }
            }
            if (i < 2) {
                if (i % 2 == 0) {
                    shiftY = 1;
                }
                else {
                    shiftY = -1;
                }
            }

            int rgb;
            rgb = rbgPic[y + shiftY][x + shiftX];

            r[i] = (rgb >> 16) & 0xFF;
            g[i] = (rgb >> 8) & 0xFF;
            b[i] = (rgb >> 0) & 0xFF;
        }

        energy = (r[0] - r[1]) * (r[0] - r[1]) + (g[0] - g[1]) * (g[0] - g[1]) + (b[0] - b[1]) * (
                b[0] - b[1]); // shift Y
        energy += (r[2] - r[3]) * (r[2] - r[3]) + (g[2] - g[3]) * (g[2] - g[3]) + (b[2] - b[3]) * (
                b[2] - b[3]); // shift X

        energyMap[y][x] = Math.sqrt(energy);
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!vertical && override == false) {
            transposeEnergy();
        }
        
        if (x > (width - 1) || y > (height - 1) || x < 0 || y < 0) {
            throw new IllegalArgumentException();
        }
        return energyMap[y][x];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        override = true;
        if (vertical) {
            transposeEnergy();
        }

        int[] horizontalSeam = findVerticalSeam();

        override = false;
        return horizontalSeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        // will always find the verticalSeam
        if (!vertical && override == false) {
            transposeEnergy();
        }

        int[] edgeTo = computeDistToEdgeTo(energyMap);

        int node = finalNode;
        int[] shortestPath2 = new int[height];
        shortestPath2[height - 1] = node - (width * (height - 1));
        int i = height - 2;
        while (node > width) {

            node = edgeTo[node];
            shortestPath2[i] = node - (width * i);
            i--;
        }
        return shortestPath2;

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

        if (vertical) {
            transposeEnergy();
        }

        if (seam == null || seam.length != height || width <= 1) {
            throw new IllegalArgumentException();
        }

        override = true;

        removeVerticalSeam(seam);

        override = false;

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

        if (!vertical && override == false) {
            transposeEnergy();
        }
        //StdOut.println("width = " + width);
        //StdOut.println("seam len = " + seam.length);
        if (seam == null || seam.length != height || width <= 1) {
            throw new IllegalArgumentException();
        }

        width--;

        double[][] newEnergyMap = new double[height][width];
        int[][] newRbgMap = new int[height][width];
        int prevSeam = seam[0];
        for (int i = 0; i < height; i++) {

            if (((prevSeam - seam[i]) * (prevSeam - seam[i])) > 1) {
                throw new IllegalArgumentException();
            }
            if (seam[i] > (width) || seam[i] < 0) { // not width-1, already one l304
                throw new IllegalArgumentException();
            }
            prevSeam = seam[i];
            System.arraycopy(energyMap[i], 0, newEnergyMap[i], 0, seam[i]);
            System.arraycopy(rbgPic[i], 0, newRbgMap[i], 0, seam[i]);
            int p2Start = seam[i];
            System.arraycopy(energyMap[i], p2Start + 1, newEnergyMap[i], p2Start,
                             width - p2Start);
            System.arraycopy(rbgPic[i], p2Start + 1, newRbgMap[i], p2Start,
                             width - p2Start);
        }

        energyMap = newEnergyMap;
        rbgPic = newRbgMap;

        // recompute energy
        for (int i = 0; i < height; i++) {
            computeEnergy(seam[i] - 1, i);
            computeEnergy(seam[i] + 1, i);
            computeEnergy(seam[i], i);
        }

    }

/*
    public void printEnergy() {
        for (int y = 0; y < height; y++) {
            for (int i = 0; i < width; i++) {
                StdOut.print(
                        "\t" + String.format("%.2f", energyMap[y][i]));
            }
            StdOut.println();
        }
    }

 */


    //  unit testing (optional)
    public static void main(String[] args) {
        String pathFile
                = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part II\\2_Shortest_Path\\seam\\3x7.png";

        Picture picIn = new Picture(pathFile);
        SeamCarver seamCarver = new SeamCarver(picIn);

        //seamCarver.printEnergy();
        //StdOut.println(seamCarver.energy(1, 2));

        int[] seam = { 1, 0, 0, 1, 1, 2, 1 };
        seamCarver.removeVerticalSeam(seam);
        int k = 0;

        /*
        while (k < 100) {
            int[] seam = seamCarver.findVerticalSeam();
            seamCarver.removeVerticalSeam(seam);
            k++;
        }
        k = 0;



        while (k < 100) {
            int[] seam = seamCarver.findHorizontalSeam();

            seamCarver.removeHorizontalSeam(seam);
            k++;

        }
*/

        Picture newPic = seamCarver.picture();
        newPic.save(pathFile.replace(".png", "-out.png"));

    }

}
