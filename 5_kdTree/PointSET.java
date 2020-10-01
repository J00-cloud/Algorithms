import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Iterator;

// brute force implementation
public class PointSET {
    //Implement the following API by using a red–black BST
    // Use SET do not implement your own BST
    // will include turnLeft, turnRight,
    // SET already implements RB BST

    //Set<Point2D> nodeSet;
    private SET<Point2D> nodeSet;

    // construct an empty set of points
    public PointSET() {
        nodeSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return nodeSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return nodeSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // in time Lg(N)
        nodeSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        // in time Lg(N)
        return nodeSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : nodeSet) {
            StdDraw.point(p.x(), p.y());
        }

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        // in time N
        // probably complex task
        if (rect == null) {
            throw new java.lang.IllegalArgumentException();
        }
        ArrayList<Point2D> iterPoints = new ArrayList<Point2D>();

        for (Point2D p : nodeSet) {
            if (rect.contains(p)) {
                iterPoints.add(p);
            }
        }
        return iterPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // in time N
        // complex as well
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }
        Point2D nearest = null;
        double lowestDist = 10;

        for (Point2D p2 : nodeSet) {
            double distance = p2.distanceTo(p);
            if (distance < lowestDist) {
                lowestDist = distance;
                nearest = p2;
            }
        }
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // initialize the data structures from file
        String filename
                = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part I\\5_BST\\kdtree\\circle10.txt";
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        brute.draw();
        StdOut.println("size : " + brute.size());
        Point2D p = new Point2D(0.1, 0.1);
        StdDraw.point(p.x(), p.y());

        Point2D neareset = brute.nearest(p);
        StdOut.println("nearest : " + neareset);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point(neareset.x(), neareset.y());

        RectHV rectangle = new RectHV(0.1, 0.08, 1, 0.5);
        double xCenter = (rectangle.xmin() + rectangle.xmax()) / 2;
        double yCenter = (rectangle.ymin() + rectangle.ymax()) / 2;
        StdDraw.rectangle(xCenter, yCenter, rectangle.width() / 2, rectangle.height() / 2);

        Iterator<Point2D> iterPoints = brute.range(rectangle).iterator();
        StdOut.println("Points in rectangle :");
        for (Iterator<Point2D> it = iterPoints; it.hasNext(); ) {
            Point2D p2 = it.next();
            StdOut.println(p2);
        }

    }
}
