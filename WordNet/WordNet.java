
import java.util.HashMap;

public class WordNet {
    private HashMap<String, Bag<Integer>> map;
    private HashMap<Integer, String> anotherMap;
    private Digraph G;
    private SAP sap;
 // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        map = new HashMap<String, Bag<Integer>>();
        anotherMap = new HashMap<Integer, String>();
        In in = new In(synsets);
        while (!in.isEmpty()) {
           String[] str = in.readLine().split(",");
           int tmp = Integer.parseInt(str[0]);
           anotherMap.put(tmp, str[1]);
           String[] nouns = str[1].split(" ");
           for (String string : nouns) {
               if (!map.containsKey(string))
                   map.put(string, new Bag<Integer>());
               map.get(string).add(tmp);
           }
        }
        in.close();
        G = new Digraph(anotherMap.size());
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String[] words = in.readLine().split(",| ");
            int tmp = Integer.parseInt(words[0]);
            for (int i = 1; i < words.length; i++) {
                G.addEdge(tmp, Integer.parseInt(words[i]));
            }
        }
        in.close();
        sap = new SAP(G);
        Topological topo = new Topological(G.reverse());
        Iterable<Integer> iterator = topo.order();
        if (iterator == null) 
            throw new IllegalArgumentException();
        int start = iterator.iterator().next();
        BreadthFirstDirectedPaths bfdp = new BreadthFirstDirectedPaths(G.reverse(), start);
        for (int i = 0; i < anotherMap.size(); i++) {
            if (!bfdp.hasPathTo(i)) 
                throw new IllegalArgumentException();
            }
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB))
            return sap.length(map.get(nounA), map.get(nounB));
        else throw new IllegalArgumentException();
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB))
            return anotherMap.get(sap.ancestor(map.get(nounA), map.get(nounB)));
        else throw new IllegalArgumentException();
    }

    // for unit testing of this class
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }

}
