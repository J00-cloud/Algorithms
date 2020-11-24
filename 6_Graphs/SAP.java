import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private Digraph _G;
    private int commonAncestor;
    private int minLength;
    //  private int[] closestVertices = new int[2];
    // private BreadthFirstDirectedPaths bfs;
    // constructor takes a digraph (not necessarily a DAG)

    public SAP(Digraph G) {
        Digraph dgCop = new Digraph(G);
        _G = dgCop;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // Gets all vertices from v, and then using BFS, all vertices from v's children an so on
        // BFS
        // does not take direction into account
        // same alg as ancestor
        updateBFSval(v, w);

        return minLength;

    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    // directed path from v to common ancestor x
    public int ancestor(int v, int w) {
        // get vertices from v and then w until a common one is found

        updateBFSval(v, w);

        return commonAncestor;

    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        if (v == null || w == null) {
            throw new java.lang.IllegalArgumentException();
        }

        int minVal = -1;

        try {
            for (Integer sv : v) {
                for (Integer sw : w) {
                    updateBFSval(sv, sw);
                    if (minVal == -1 || minLength < minVal) {
                        minVal = minLength;
                    }
                }
            }
        }
        catch (java.lang.NullPointerException e) {
            throw new java.lang.IllegalArgumentException();
        }

        return minVal;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

        if (v == null || w == null) {
            throw new java.lang.IllegalArgumentException();
        }

        int minVal = -1;
        int minAncestor = -1;

        try {
            for (Integer sv : v) {
                for (Integer sw : w) {
                    updateBFSval(sv, sw);
                    if (minVal == -1 || minLength < minVal) {
                        minVal = minLength;
                        minAncestor = commonAncestor;
                    }
                }
            }

        }
        catch (java.lang.NullPointerException e) {
            throw new java.lang.IllegalArgumentException();
        }

        return minAncestor;
    }


    private void updateBFSval(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(_G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(_G, w);

        minLength = -1;
        commonAncestor = -1;

        for (int i = 0; i < _G.V(); i++) {

            int a = bfsV.distTo(i);
            int b = bfsW.distTo(i);

            if (a != Integer.MAX_VALUE && b != Integer.MAX_VALUE) {
                int length = a + b;
                if (minLength == -1 || minLength > length) {
                    minLength = length;
                    commonAncestor = i;

                }
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {

        In fileIn = new In(
                "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part II\\1_Graphs\\wordnet\\digraph1.txt");
        Digraph G = new Digraph(fileIn);
        //Graph Gb = new Graph(fileIn);

        SAP newSAP = new SAP(G);

        StdOut.println("Final Distance : " + newSAP.length(8, 0));

        StdOut.println("Common Ancestor : " + newSAP.ancestor(8, 0));

    }
}
