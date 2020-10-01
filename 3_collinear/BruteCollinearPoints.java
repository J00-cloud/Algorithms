import java.util.Comparator;

public class BruteCollinearPoints {
    private Point[] pointsIn;

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        this.pointsIn = points;
    }

    public int numberOfSegments() {
        // the number of line segments
        LineSegment[] segments = this.segments();

        return segments.length;
    }

    private Point findMinPoint(Point a, Point b, Point c, Point d) {
        Point chosenA;
        Point chosenB;
        Point chosen;

        int compA = a.compareTo(b);
        int compB = c.compareTo(d);

        if (compA == 0 || compA == -1) {
            chosenA = a;
        }
        else {
            chosenA = b;
        }

        if (compB == 0 || compB == -1) {
            chosenB = c;
        }
        else {
            chosenB = d;
        }

        int finalComp = chosenA.compareTo(chosenB);

        if (finalComp == 0 || finalComp == -1) {
            chosen = chosenA;
        }
        else {
            chosen = chosenB;
        }

        return chosen;

    }

    private Point findMaxPoint(Point a, Point b, Point c, Point d) {
        Point chosenA;
        Point chosenB;
        Point chosen;

        int compA = a.compareTo(b);
        int compB = c.compareTo(d);

        if (compA == 0 || compA == -1) {
            chosenA = b;
        }
        else {
            chosenA = a;
        }

        if (compB == 0 || compB == -1) {
            chosenB = d;
        }
        else {
            chosenB = c;
        }

        int finalComp = chosenA.compareTo(chosenB);

        if (finalComp == 0 || finalComp == -1) {
            chosen = chosenB;
        }
        else {
            chosen = chosenA;
        }

        return chosen;

    }

    public LineSegment[] segments() {
        // not optimal at all
        //LineSegment[] lineSegments = new LineSegment[pointsIn.length];
        LineSegment[] lineSegments = new LineSegment[0];

        int segmentsFound = 0;
        // the line segments
        for (int i = 0; i < pointsIn.length;
             i++) {
            for (int j = i + 1; j < pointsIn.length;
                 j++) {
                for (int k = j + 1; k < pointsIn.length;
                     k++) {
                    for (int l = k + 1; l < pointsIn.length;
                         l++) {
                        Comparator<Point> comparator = pointsIn[i].slopeOrder();
                        //StdOut.println("Check index " + i + " with " + j + " and " + k + " and " + l);

                        int comp = comparator.compare(pointsIn[j], pointsIn[k]);
                        int comp2 = -1;
                        int comp3 = -1;

                        if (comp == 0) {
                            comp2 = comparator.compare(pointsIn[k], pointsIn[l]);
                        }
                        if (comp2 == 0) {
                            comp3 = comparator.compare(pointsIn[j], pointsIn[l]);
                        }

                        if (comp3 == 0) {
                            Point minPoint = findMinPoint(pointsIn[i], pointsIn[j], pointsIn[k],
                                                          pointsIn[l]);
                            Point maxPoint = findMaxPoint(pointsIn[i], pointsIn[j], pointsIn[k],
                                                          pointsIn[l]);

                            LineSegment lnSgmt = new LineSegment(minPoint, maxPoint);

                            LineSegment[] tempLineSegm = new LineSegment[lineSegments.length + 1];
                            for (int a = 0; a < lineSegments.length; a++) {
                                tempLineSegm[a] = lineSegments[a];
                            }
                            tempLineSegm[tempLineSegm.length - 1] = lnSgmt;

                            lineSegments = tempLineSegm;
                            segmentsFound++;
                        }


                    }

                }
            }
        }

        return lineSegments;
    }
}
