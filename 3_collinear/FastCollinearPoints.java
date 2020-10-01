import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private Point[] pointsIn;
    private Point[] sortedPoints;
    private double[] anglesArray;
    private int lengthLS = 0;
    private Point[][] pointArray;
    private LineSegment[] sortedLineSegments;

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points

        //order points
        // find duplicates

        this.pointsIn = points;
        anglesArray = new double[pointsIn.length];
        pointArray = new Point[1][2];
    }

    public int numberOfSegments() {
        // the number of line segments
        LineSegment[] segments = this
                .segments(); // not sure this is very fast if it has to redo the segment each time

        return lengthLS;
    }


    public LineSegment[] segments() {
        // angles = [0, 0.3, 2, 0.3, 1,1], pointP with q, r, s ,t ...

        LineSegment[] lineSegments = new LineSegment[1];
        int n = pointsIn.length;

        for (int p = 0; p < n; p++) {

            // compute angles
            computeAngles(p, n);

            // Sorts sortedPoints by angles
            ThreeWayQuickSort2();

            // make segments from angles
            lineSegments = buildSegmentsFromAngles(lineSegments, p, n);

        }

        // remove nulls (only if prev val is diff)
        lineSegments = removeNullInArray(lineSegments);

        return lineSegments;
        // the line segments
    }

    private void computeAngles(int from, int to) {
        Point pointP = pointsIn[from];
        for (int q = 0; q < to; q++) {
            anglesArray[q] = pointP.slopeTo(pointsIn[q]);
        }
    }

    private LineSegment[] addToLSArray(LineSegment[] lsArrayIn, LineSegment lsIn) {

        lsArrayIn[lsArrayIn.length - 1] = lsIn;
        LineSegment[] newLS = new LineSegment[lsArrayIn.length * 2];


        for (int a = 0; a < lsArrayIn.length; a++) {
            newLS[a] = lsArrayIn[a];
        }

        return newLS;
    }

    private Point[][] addPointCoupleToArray(Point[][] pointArrayIn, Point[] pointsIn) {

        pointArrayIn[pointArrayIn.length - 1] = pointsIn;
        Point[][] newPA = new Point[pointArrayIn.length * 2][];

        for (int a = 0; a < pointArrayIn.length; a++) {
            newPA[a] = pointArrayIn[a];
        }

        return newPA;
    }

    private LineSegment[] removeNullInArray(LineSegment[] lsIn) { // N

        sortedLineSegments = lsIn;


        lsIn = orderPointArrayAndLS(lsIn);


        LineSegment[] newLSArray = new LineSegment[lengthLS];
        int p = 0;
        String prevVal = "";

        Point[] prevVal2 = new Point[2];

        for (int i = 0; i < lengthLS; i++) {
            /*
            if (!(prevVal.equals(lsIn[i].toString()))) {
                newLSArray[p] = lsIn[i];
                p++;
            }
             */

            if ((pointArray[i][0] == prevVal2[0]) &&
                    (pointArray[i][1] == prevVal2[1])) {
            }
            else {
                newLSArray[p] = lsIn[i];
                p++;
            }
            prevVal2[0] = pointArray[i][0];
            prevVal2[1] = pointArray[i][1];

            //prevVal = lsIn[i].toString();
        }

        LineSegment[] finalSegment = new LineSegment[p];

        for (int i = 0; i < p; i++) {
            finalSegment[i] = newLSArray[i];
        }
        lengthLS = p;
        return finalSegment;
    }

    private LineSegment[] orderPointArrayAndLS(LineSegment[] lsIn) {

        ThreeWayPartitionPointArray(0, lengthLS - 1);

        return lsIn;

    }


    private LineSegment[] buildSegmentsFromAngles(LineSegment[] lineSegments, int p, int n) {
        // Build segment list
        // Loop for all the angles
        for (int i = 1; i < n; i++) {
            // add case if end of array
            Point firstPoint = pointsIn[p];
            Point lastPoint = pointsIn[p];
            int count = 1;

            while ((anglesArray[i] == anglesArray[i - 1])) {
                if (sortedPoints[i].compareTo(firstPoint) < 0) {
                    firstPoint = sortedPoints[i];
                }
                if (sortedPoints[i].compareTo(lastPoint) > 0) {
                    lastPoint = sortedPoints[i];
                }
                if (count == 1) {
                    if (sortedPoints[i - 1].compareTo(firstPoint) < 0) {
                        firstPoint = sortedPoints[i - 1];
                    }
                    if (sortedPoints[i - 1].compareTo(lastPoint) > 0) {
                        lastPoint = sortedPoints[i - 1];
                    }

                }
                count++;
                i++;

                if (i == n) {
                    break;
                }

            }

            if (count >= 3) {

                LineSegment ls = new LineSegment(firstPoint, lastPoint);
                if ((lengthLS + 1) >= lineSegments.length) {
                    lineSegments = addToLSArray(lineSegments, ls);
                    Point[] pointsIn = { firstPoint, lastPoint };
                    pointArray = addPointCoupleToArray(pointArray, pointsIn);

                }
                else {
                    lineSegments[lengthLS] = ls;
                    Point[] pointsIn = { firstPoint, lastPoint };
                    pointArray[lengthLS] = pointsIn;
                }
                lengthLS++;

            }

        }

        // sort linesegments using another array with endPoint + Slope

        return lineSegments;
    }


    private void ThreeWayQuickSort2() {
        sortedPoints = pointsIn.clone();
        ThreeWayPartition(0, anglesArray.length - 1);
    }


    private void ThreeWayPartition(int low, int high) {

        if (low >= high) {
            return;
        }
        double v = anglesArray[low];
        int i = low;
        int gt = high; // index after which all entries are greater than v
        int lt = low; // index before which all entries are lower than v
        while (i <= gt) {
            if (anglesArray[i] == v) {
                i++;
            }
            else if (anglesArray[i] < v) {

                exch(anglesArray, i, lt);
                exch(sortedPoints, i, lt);

                lt++; // we're sure before lt is < v
                i++;
            }
            else if (anglesArray[i] > v) {

                exch(anglesArray, i, gt);
                exch(sortedPoints, i, gt);

                gt--; // we're now sure that this val is above v
                // NO i++ !
            }
        }
        ThreeWayPartition(low, lt - 1);
        ThreeWayPartition(gt + 1, high);

    }

    private void ThreeWayPartitionPointArray(int low, int high) {

        if (low >= high) {
            return;
        }
        Point d = pointArray[low][0]; // order by first point (origin)
        Point f = pointArray[low][1]; // order by first point (origin)
        int i = low;
        int gt = high; // index after which all entries are greater than v
        int lt = low; // index before which all entries are lower than v

        while (i <= gt) {
            //StdOut.println(gt);
            if (pointArray[i][0].compareTo(d) == 0 && pointArray[i][1].compareTo(f) == 0) {
                i++;
            }
            else if (pointArray[i][0].compareTo(d) == 0 && pointArray[i][1].compareTo(d) < 0) {
                exch(pointArray, i, lt);
                exch(sortedLineSegments, i, lt);

                lt++; // we're sure before lt is < v
                i++;
            }
            else if (pointArray[i][0].compareTo(d) == 0 && pointArray[i][1].compareTo(d) > 0) {
                exch(pointArray, i, gt);
                exch(sortedLineSegments, i, gt);

                gt--; // we're now sure that this val is above v
            }
            else if (pointArray[i][0].compareTo(d) < 0) {

                exch(pointArray, i, lt);
                exch(sortedLineSegments, i, lt);

                lt++; // we're sure before lt is < v
                i++;
            }
            else if (pointArray[i][0].compareTo(d) > 0) {

                exch(pointArray, i, gt);
                exch(sortedLineSegments, i, gt);

                gt--; // we're now sure that this val is above v
                // NO i++ !
            }
        }
        ThreeWayPartitionPointArray(low, lt - 1);
        ThreeWayPartitionPointArray(gt + 1, high);

    }


    private void exch(double[] arrayIn, int i, int j) {
        double temp = arrayIn[j];
        arrayIn[j] = arrayIn[i];
        arrayIn[i] = temp;
    }

    private void exch(Point[] arrayIn, int i, int j) {
        Point temp = arrayIn[j];
        arrayIn[j] = arrayIn[i];
        arrayIn[i] = temp;
    }

    private void exch(Point[][] arrayIn, int i, int j) {
        Point[] temp = arrayIn[j];
        arrayIn[j] = arrayIn[i];
        arrayIn[i] = temp;
    }

    private void exch(LineSegment[] arrayIn, int i, int j) {
        LineSegment temp = arrayIn[j];
        arrayIn[j] = arrayIn[i];
        arrayIn[i] = temp;
    }

    private static void printArray(double[] array) {
        for (int i = 0; i < array.length; i++) {
            StdOut.print(array[i]);
            StdOut.print(",");
        }
        StdOut.println();
    }

    private static void printArray(Point[] array) {
        for (int i = 0; i < array.length; i++) {
            StdOut.print(array[i].toString());
            StdOut.print(",");
        }
        StdOut.println();
    }


    public static void main(String[] args) {
        /* YOUR CODE HERE */
        /*
        Point[] arrayPoints = new Point[] { };

        FastCollinearPoints fcp2 = new FastCollinearPoints(arrayPoints);
        double[] unsortedArray2 = new double[] {
                1.0, 0.35, 0.35, 0.2, 0.2, 5.6, 2.4, 5.6, 5.6, 2.4, 0.35, 5.6
        };
        fcp2.anglesArray = unsortedArray2;

        printArray(fcp2.anglesArray);
        fcp2.ThreeWayQuickSort2();
        printArray(fcp2.anglesArray);

        String file
                = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part I\\3_Sorts\\Collinear\\input400.txt";
        In in = new In(file);

        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        FastCollinearPoints fcp3 = new FastCollinearPoints(points);
        printArray(points);

        fcp3.computeAngles(0, points.length - 1);
        printArray(fcp3.anglesArray);
        fcp3.ThreeWayQuickSort2();
        printArray(fcp3.anglesArray);




        LineSegment[] ls = fcp3.segments();
        StdOut.println(ls.length);
        for (int i = 0; i < ls.length; i++) {
            StdOut.println(ls[i].toString());
        }


         */
    }

}
