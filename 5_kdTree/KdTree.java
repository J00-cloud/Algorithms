import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

// brute force implementation
public class KdTree {
    private Boolean Hor = true;
    private Boolean Ver = false;
    private int count;
    private int countRect;
    private ArrayList<Point2D> iterOut;
    private Node startNode;
    private int size = 0;
    private Node bestNode;
    private double bestDistance;
    private int nodesSearched = 0;

    private static class Node {
        public Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left or bottom subtree
        private Node rt;        // the right or top subtree

        public Node(Point2D _p) {
            p = _p;
        }
    }


    // construct an empty set of points


    // is the set empty?
    public boolean isEmpty() {
        if (startNode == null) {
            return true;
        }
        else {
            return false;
        }
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }

        if (startNode == null) {
            startNode = new Node(p);
            size++;
            return;
        }
        Node currentNode = startNode;

        int counter = 0;
        while (currentNode != null) {

            if ((currentNode.p.x() == p.x()) && (currentNode.p.y() == p.y())) {
                return;
            }
            //StdOut.println("x :" + currentNode.p.x() + " y : " + currentNode.p.y());
            if (counter % 2 == 0) {
                if (currentNode.p.x() > p.x()) { // what if equal ?
                    if (currentNode.lb == null) {
                        currentNode.lb = new Node(p);
                        size++;
                        return;
                    }
                    currentNode = currentNode.lb;
                }
                else if (currentNode.p.x() <= p.x()) {
                    if (currentNode.rt == null) {
                        currentNode.rt = new Node(p);
                        size++;
                        return;
                    }
                    currentNode = currentNode.rt;
                }
            }
            else {
                if (currentNode.p.y() > p.y()) {
                    if (currentNode.lb == null) {
                        currentNode.lb = new Node(p);
                        size++;
                        return;
                    }
                    currentNode = currentNode.lb;
                }
                else if (currentNode.p.y() <= p.y()) {
                    if (currentNode.rt == null) {
                        currentNode.rt = new Node(p);
                        size++;
                        return;
                    }
                    currentNode = currentNode.rt;
                }
            }
            counter++;
        }

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }

        if (startNode == null) {
            return false;
        }
        Node currentNode = startNode;

        int counter = 0;
        while (currentNode != null) {

            if ((currentNode.p.x() == p.x()) && (currentNode.p.y() == p.y())) {
                return true;
            }
            if (counter % 2 == 0) {
                if (currentNode.p.x() > p.x()) { // what if equal ?
                    if (currentNode.lb == null) {
                        return false;
                    }
                    currentNode = currentNode.lb;
                }
                else if (currentNode.p.x() <= p.x()) {
                    if (currentNode.rt == null) {
                        return false;
                    }
                    currentNode = currentNode.rt;
                }
            }
            else {
                if (currentNode.p.y() > p.y()) {
                    if (currentNode.lb == null) {
                        return false;
                    }
                    currentNode = currentNode.lb;
                }
                else if (currentNode.p.y() <= p.y()) {
                    if (currentNode.rt == null) {
                        return false;
                    }
                    currentNode = currentNode.rt;
                }
            }
            counter++;
        }

        return false;
    }

    // draw all points to standard draw
    public void draw() {
        drawNode(startNode, Ver, 0, 1);
    }


    private void drawNode(Node currentNode, Boolean direction, double min, double max) {

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(currentNode.p.x(), currentNode.p.y());
        count++;

        if (direction == Ver) {
            StdDraw.setPenRadius(0.001);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(currentNode.p.x(), min, currentNode.p.x(), max);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.001);
            StdDraw.line(min, currentNode.p.y(), max, currentNode.p.y());
        }
        double stopPoint;
        if (direction == Hor) {
            stopPoint = currentNode.p.y();
        }
        else {
            stopPoint = currentNode.p.x();
        }
        if (currentNode.lb != null) {

            drawNode(currentNode.lb, !direction, 0, stopPoint);
        }
        if (currentNode.rt != null) {
            drawNode(currentNode.rt, !direction, stopPoint, 1);
        }

    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new java.lang.IllegalArgumentException();
        }
        iterOut = new ArrayList<Point2D>();
        rectSearch(rect, startNode, Ver);
        //StdOut.println("Rectangle count : " + countRect);
        return iterOut;
    }

    private void rectSearch(RectHV rect, Node n, Boolean direction) {
        // if n is null, return
        if (n == null) {
            return;
        }
        countRect++;

        // if node in rectangle, add node
        //      search both left and right
        if (isNodeInRectangle(n, rect)) {
            iterOut.add(n.p);
            rectSearch(rect, n.lb, !direction);
            rectSearch(rect, n.rt, !direction);
        }

        // if current node is vertical split
        //      if rectangle is strictly to the left
        //          search left
        //      if rectangle is strictly to the right
        //          search right
        //      if point within rectangle Xs
        //          search both
        else if (direction == Ver) {
            if (isNodeStrictlyLeft(n, rect)) {
                rectSearch(rect, n.lb, !direction);
            }
            else if (isNodeStrictlyRight(n, rect)) {
                rectSearch(rect, n.rt, !direction);
            }
            else {
                rectSearch(rect, n.lb, !direction);
                rectSearch(rect, n.rt, !direction);
            }

        }

        // if current node is horizontal split
        //      if rectangle is strictly above
        //          search above
        //      if rectangle is strictly below
        //          search below
        //      if point within rectangle Ys
        //          search both
        else if (direction == Hor) {
            if (isNodeStrictlyUp(n, rect)) {
                rectSearch(rect, n.rt, !direction);
            }
            else if (isNodeStrictlyDown(n, rect)) {
                rectSearch(rect, n.lb, !direction);
            }
            else {
                rectSearch(rect, n.lb, !direction);
                rectSearch(rect, n.rt, !direction);
            }

        }

    }

    private Boolean isNodeInRectangle(Node n, RectHV rect) {
        if ((n.p.x() >= rect.xmin()) && (n.p.x() <= rect.xmax())) {
            if ((n.p.y() >= rect.ymin()) && (n.p.y() <= rect.ymax())) {
                return true;
            }
        }
        return false;
    }

    private Boolean isNodeStrictlyLeft(Node n, RectHV rect) {
        if (rect.xmax() < n.p.x()) {
            return true;
        }
        return false;
    }

    private Boolean isNodeStrictlyRight(Node n, RectHV rect) {
        if (rect.xmin() > n.p.x()) {
            return true;
        }
        return false;
    }

    private Boolean isNodeStrictlyUp(Node n, RectHV rect) {
        if (rect.ymin() > n.p.y()) {
            return true;
        }
        return false;
    }

    private Boolean isNodeStrictlyDown(Node n, RectHV rect) {
        if (rect.ymax() < n.p.y()) {
            return true;
        }
        return false;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if (startNode == null) {
            return null;
        }

        Point2D finalPoint;
        Node currentNode = startNode;
        bestNode = currentNode;
        bestDistance = currentNode.p.distanceTo((p));

        searchNode(p, currentNode, Ver);

        finalPoint = bestNode.p;
        return finalPoint;
    }


    private void searchNode(Point2D searchedPoint, Node currentNode, Boolean direction) {
        if (currentNode == null) {
            return;
        }
        nodesSearched++;

        double distance = searchedPoint.distanceTo(currentNode.p);
        //StdDraw.setPenColor(StdDraw.ORANGE);
        //StdDraw.setPenRadius(0.01);
        //StdDraw.point(currentNode.p.x(), currentNode.p.y());

        if (distance < bestDistance) {
            bestDistance = distance;
            bestNode = currentNode;

        }
        Boolean isSearchedPointLB = false;
        Boolean isSearchedPointRT = false;

        if (direction == Ver && (currentNode.p.x() > searchedPoint.x())) {
            isSearchedPointLB = true;
        }
        else if (direction == Hor && (currentNode.p.y() > searchedPoint.y())) {
            isSearchedPointLB = true;
        }
        else {
            isSearchedPointRT = true;
        }
        //StdOut.println("isSearchedPointLB" + isSearchedPointLB);
        //StdOut.println("isSearchedPointRT" + isSearchedPointRT);

        double deltaX = Math.abs(currentNode.p.x() - searchedPoint.x());
        double deltaY = Math.abs(currentNode.p.y() - searchedPoint.y());

        if (isSearchedPointLB) {
            // if searched point is left and next node is go left
            //      search left
            searchNode(searchedPoint, currentNode.lb, !direction);

            if (currentNode.rt != null) {
                // if searched point is left
                //      if deltaX is less than best distance
                //          search right
                if (direction == Ver) {
                    if (deltaX <= bestDistance) {
                        searchNode(searchedPoint, currentNode.rt, !direction);
                    }
                }
                else {
                    if (deltaY <= bestDistance) {
                        searchNode(searchedPoint, currentNode.rt, !direction);
                    }
                }
            }

        }
        if (isSearchedPointRT) {
            // if searched point is right and next node is go right
            //      search right
            searchNode(searchedPoint, currentNode.rt, !direction);

            if (currentNode.lb != null) {
                // if searched point is right and next node is go left
                //      search right if deltaX is less that best distance
                if (direction == Ver) {
                    if (deltaX <= bestDistance) {
                        searchNode(searchedPoint, currentNode.lb, !direction);
                    }
                }
                else {
                    if (deltaY <= bestDistance) {
                        searchNode(searchedPoint, currentNode.lb, !direction);
                    }
                }

            }
        }


    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        String filename
                = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part I\\5_BST\\kdtree\\input10.txt";
        In in = new In(filename);
        KdTree kdTree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
        }

        kdTree.draw();

        StdOut.println("size : " + kdTree.size());
        /*
        Point2D containPoint = new Point2D(0.706, 0.971);
        StdOut.println("contains : " + kdTree.contains(containPoint));

         */

        Point2D p = new Point2D(0.1, 0.8);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(p.x(), p.y());

        Point2D neareset = kdTree.nearest(p);
        StdOut.println("nearest is " + neareset + " (" + kdTree.nodesSearched + ")");

        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(kdTree.bestNode.p.x(), kdTree.bestNode.p.y());


        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.005);
        RectHV searchRect = new RectHV(0.1, 0.1, 0.5, 0.5);
        StdDraw.rectangle(((searchRect.xmax() + searchRect.xmin()) / 2),
                          ((searchRect.ymax() + searchRect.ymin()) / 2), searchRect.width() / 2,
                          searchRect.height() / 2);

        Iterable<Point2D> pointsInRect = kdTree.range(searchRect);
        for (Point2D point : pointsInRect) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(point.x(), point.y());
            StdOut.println(point);
        }


    }
}
