import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class WordNet {
    private Digraph digHyper;
    private synsetStruct synsets;
    private SAP _sap;
    private int roots = 0;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        buildDigHyper(hypernyms);
        buildSynsetStruct(synsets);
        _sap = new SAP(digHyper);
        if (roots > 1) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    private void buildDigHyper(String hypernyms) {
        In hypIn = new In(hypernyms);
        String[] fileIn = hypIn.readAllLines();
        int fileLen = fileIn.length;
        digHyper = new Digraph(fileLen);

        for (String newLine : fileIn) {

            String[] lineVals = newLine.split(",");
            if (lineVals.length > 1) {
                for (int i = 1; i < lineVals.length; i++) {
                    digHyper.addEdge(Integer.parseInt(lineVals[0]), Integer.parseInt(lineVals[i]));
                }
            }
            else {
                roots++;
            }

        }
    }

    private void buildSynsetStruct(String synsetFile) {
        In synIn = new In(synsetFile);
        synsets = new synsetStruct();

        while (synIn.hasNextLine()) {
            String newLine = synIn.readLine();
            int id = Integer.parseInt(newLine.split(",")[0]);
            String synonyms = newLine.split(",")[1];

            synsets.addEnt0(synonyms, id);

            String[] synList = synonyms.split(" ");
            int synListLen = synList.length;
            for (int i = 0; i < synListLen; i++) {
                synsets.addEntry(synList[i], id);
            }
        }
    }

    private class synsetStruct {

        public HashMap<Integer, String> nounsTitles;
        public HashMap<String, ArrayList<Integer>> synsNbs;

        public synsetStruct() {
            nounsTitles = new HashMap<Integer, String>();
            synsNbs = new HashMap<String, ArrayList<Integer>>();
        }

        public void addEnt0(String noun, int id) {
            nounsTitles.put(id, noun);

        }

        public void addEntry(String noun, int id) {

            if (synsNbs.containsKey(noun)) {
                synsNbs.get(noun).add(id);
            }
            else {
                ArrayList<Integer> ints = new ArrayList<Integer>();
                ints.add(id);
                synsNbs.put(noun, ints);
            }
        }

        public Set<Integer> getNouns() {
            return nounsTitles.keySet();
        }

        public String getNounFromID(int id) {
            return nounsTitles.get(id);
        }

        public boolean isNoun(String noun) {
            boolean resp = synsNbs.containsKey(noun);
            return resp;
        }

        public Iterable<String> printNouns() {
            return synsNbs.keySet();
        }

        public ArrayList<Integer> getSyns(String noun) {
            return synsNbs.get(noun);
        }
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {

        /*
        ArrayList<String> vals = new ArrayList<String>();
        Set<Integer> valsInt = synsets.getNouns();

        for (int inInt : valsInt) {
            vals.add(synsets.getNounFromID(inInt));
        }
        return vals;

         */
        return synsets.printNouns();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        // is noun part of hashtable
        if (word == null) {
            throw new java.lang.IllegalArgumentException();
        }
        return synsets.isNoun(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        // all the instances where nounA is found
        // let's not care about synonyms for now

        if (!isNoun(nounA) || !isNoun(nounB) || nounB == null || nounA == null) {
            throw new java.lang.IllegalArgumentException();
        }

        Iterable<Integer> interA = synsets.getSyns(nounA);
        Iterable<Integer> interB = synsets.getSyns(nounB);


        int distance = _sap.length(interA, interB);

        return distance;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        //
        if (!isNoun(nounA) || !isNoun(nounB) || nounB == null || nounA == null) {
            throw new java.lang.IllegalArgumentException();
        }

        Iterable<Integer> interA = synsets.getSyns(nounA);
        Iterable<Integer> interB = synsets.getSyns(nounB);

        //SAP sap = new SAP(digHyper);

        int ancestor = _sap.ancestor(interA, interB);

        String strAncestor = synsets.getNounFromID(ancestor);

        return strAncestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets
                = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part II\\1_Graphs\\wordnet\\synsets.txt";
        String hypernyms
                = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part II\\1_Graphs\\wordnet\\hypernyms.txt";

        WordNet wordNet = new WordNet(synsets, hypernyms);

        String nounChosen1 = "macromolecule";
        String nounChosen2 = "immune_gamma_globulin";
        StdOut.println("is noun " + nounChosen1 + " :" + wordNet.isNoun(nounChosen1));
        StdOut.println("Distance " + nounChosen1 + " and " + nounChosen2 + " :" + wordNet
                .distance(nounChosen1, nounChosen2));
        StdOut.println("Common ancestor :" + wordNet
                .sap(nounChosen1, nounChosen2));

    }
}
