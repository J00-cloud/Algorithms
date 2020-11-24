import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet _wordnet;

    public Outcast(WordNet wordnet) {
        // constructor takes a WordNet object
        _wordnet = wordnet;
    }

    public String outcast(String[] nouns) {

        int[] scores = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int y = i + 1; y < nouns.length; y++) {
                int distance = _wordnet.distance(nouns[i], nouns[y]);
                //StdOut.println("distance " + nouns[i] + " to " + nouns[y] + " = " + distance);
                scores[i] = scores[i] + distance;
                scores[y] = scores[y] + distance;
            }
        }
        int maxDist = -1;
        String maxWord = "";
        for (int s = 0; s < scores.length; s++) {

            if (scores[s] > maxDist) {
                maxDist = scores[s];
                maxWord = nouns[s];
            }
        }

        return maxWord;
    }   // given an array of WordNet nouns, return an outcast

    public static void main(String[] args) {
        String synsets
                = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part II\\1_Graphs\\wordnet\\synsets.txt";
        String hypernyms
                = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part II\\1_Graphs\\wordnet\\hypernyms.txt";

        WordNet wordnet = new WordNet(synsets, hypernyms);
        Outcast outcast = new Outcast(wordnet);
        //In in = new In(args[t]);
        In in = new In(
                "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part II\\1_Graphs\\wordnet\\outcast8.txt");
        String[] nouns = in.readAllStrings();
        StdOut.println("Outcast : " + outcast.outcast(nouns));
    }
}
