
public class SAP {
 // constructor takes a digraph (not necessarily a DAG)
    private Digraph G;
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 || v >= G.V() || w >= G.V())
            throw new IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(G, w);
        int shortestDist = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                shortestDist = (shortestDist <= bfdp1.distTo(i) + bfdp2.distTo(i)) ? shortestDist : bfdp1.distTo(i) + bfdp2.distTo(i);
            }
        }
        return (shortestDist == Integer.MAX_VALUE) ? -1: shortestDist;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v >= G.V() || w >= G.V())
            throw new IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(G, w);
        int shortestDist = Integer.MAX_VALUE;
        int shortestDistIndex = -1;
        for (int i = 0; i < G.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                if (shortestDist > bfdp1.distTo(i) + bfdp2.distTo(i)) {
                    shortestDist = bfdp1.distTo(i) + bfdp2.distTo(i);
                    shortestDistIndex = i;
                }
            }
        }
        return shortestDistIndex;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        for (Integer vTmp : v) {
            if (vTmp < 0 || vTmp >= G.V())
                throw new IndexOutOfBoundsException();
        }
        for (Integer wTmp : w) {
            if (wTmp < 0 || wTmp >= G.V())
                throw new IndexOutOfBoundsException();
        }
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(G, w);
        int shortestDist = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                shortestDist = (shortestDist <= bfdp1.distTo(i) + bfdp2.distTo(i)) ? shortestDist: bfdp1.distTo(i) + bfdp2.distTo(i);
            }
        }
        return (shortestDist == Integer.MAX_VALUE) ? -1 : shortestDist;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        for (Integer vTmp : v) {
            if (vTmp < 0 || vTmp >= G.V())
                throw new IndexOutOfBoundsException();
        }
        for (Integer wTmp : w) {
            if (wTmp < 0 || wTmp >= G.V())
                throw new IndexOutOfBoundsException();
        }
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(G, w);
        int shortestDist = Integer.MAX_VALUE;
        int shortestDistIndex = -1;
        for (int i = 0; i < G.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                if (shortestDist > bfdp1.distTo(i) + bfdp2.distTo(i)) {
                    shortestDist = bfdp1.distTo(i) + bfdp2.distTo(i);
                    shortestDistIndex = i;
                }
            }
        }
        return shortestDistIndex;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
