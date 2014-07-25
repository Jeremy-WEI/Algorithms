
public class Outcast {
 // constructor takes a WordNet object
    private WordNet wordNet;
    public Outcast(WordNet wordnet) {
        wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maximum = 0;
        String str = null;
        for (int i = 0, tmp = 0; i < nouns.length; i++, tmp = 0) {
            for (int j = 0; j < nouns.length; j++) {
                tmp += wordNet.distance(nouns[i], nouns[j]);
            }
            if (tmp > maximum) {
                maximum = tmp;
                str = nouns[i];
            }
        }
        return str;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            String[] nouns = In.readStrings(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
