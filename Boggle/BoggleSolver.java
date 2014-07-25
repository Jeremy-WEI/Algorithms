
import java.util.HashSet;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private TrieST<Integer> dictionary;
    private HashSet<String> allValidWords;
    private static class TrieST<Value> {
        private static final int R = 26;        // 26 Alphabet
        private Node root;      // root of trie
        private int N;
        public TrieST() {
        }
        // R-way trie node
        private static class Node {
            private Object val;
            private Node[] next = new Node[R];
        }
        public boolean isEmpty() {
            return N == 0;
        }
        public Value get(String key) {
            Node x = get(root, key, 0);
            if (x == null) return null;
            return (Value) x.val;
        }
        public boolean contains(String key) {
            return get(key) != null;
        }
        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c - 'A'], key, d+1);
        }
        public void put(String key, Value val) {
            root = put(root, key, val, 0);
        }
        private Node put(Node x, String key, Value val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (x.val == null) N++;
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[c - 'A'] = put(x.next[c - 'A'], key, val, d+1);
            return x;
        }
        public boolean isPath(String key) {
            Node x = root;
            int d = 0;
            while (d < key.length()) {
                if (x.next[key.charAt(d) - 'A'] == null) 
                    return false;
                x = x.next[key.charAt(d) - 'A'];
                d++;
            }
            return true;
        }
    }
    private static class Direction {
        private int[] direction = new int[2];
        public Direction(int x, int y) {
            direction[0] = x;
            direction[1] = y;
        }
    }
    private int point(String word) {
        switch (word.length()) {
        case 0 :
        case 1 :
        case 2 : return 0;
        case 3 :
        case 4 : return 1; 
        case 5 : return 2; 
        case 6 : return 3; 
        case 7 : return 5; 
        default : return 11;
        }
    }
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new TrieST<Integer>();
        for (String word : dictionary)
            if (word.length() > 2)
                this.dictionary.put(word, point(word));
    }
    private boolean isInBound(int m, int n, int M, int N) {
        return m >= 0 && n >= 0 && m < M && n < N; 
    }
    private Queue<Direction> getAllValidAdjacentDice(boolean[][] dice, int m, int n) {
        Queue<Direction> queue = new Queue<Direction>();
        int M = dice.length;
        int N = dice[0].length;
        if (isInBound(m - 1, n - 1, M, N) && !dice[m - 1][n - 1]) queue.enqueue(new Direction(m - 1, n - 1));
        if (isInBound(m - 1, n, M, N) && !dice[m - 1][n]) queue.enqueue(new Direction(m - 1, n));
        if (isInBound(m - 1, n + 1, M, N) && !dice[m - 1][n + 1]) queue.enqueue(new Direction(m - 1, n + 1));
        if (isInBound(m, n - 1, M, N) && !dice[m][n - 1]) queue.enqueue(new Direction(m, n - 1));
        if (isInBound(m, n + 1, M, N) && !dice[m][n + 1]) queue.enqueue(new Direction(m, n + 1));
        if (isInBound(m + 1, n - 1, M, N) && !dice[m +1][n - 1]) queue.enqueue(new Direction(m + 1, n - 1));
        if (isInBound(m + 1, n, M, N) && !dice[m +1][n]) queue.enqueue(new Direction(m + 1, n));
        if (isInBound(m + 1, n + 1, M, N) && !dice[m +1][n + 1]) queue.enqueue(new Direction(m + 1, n + 1));
        //StdOut.println(queue.size());
        return queue;
    }
    private void dfs(BoggleBoard board, boolean[][] dice, int m, int n, String str) {
        char c = board.getLetter(m, n);
        if (c == 'Q')
            str += "QU";
        else
            str += c;
        if  (!dictionary.isPath(str))
            return;
        dice[m][n] = true;
        if (dictionary.contains(str)) allValidWords.add(str);
        Queue<Direction> adjacentDice = getAllValidAdjacentDice(dice, m, n);
        while (!adjacentDice.isEmpty()) {
            Direction direction = adjacentDice.dequeue();
            dfs(board, dice, direction.direction[0], direction.direction[1], str);
        }
        dice[m][n] = false;
    }
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        allValidWords = new HashSet<String>();
        if (dictionary.isEmpty()) return allValidWords;
        int M = board.rows(), N = board.cols();
        boolean[][] dice = new boolean[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                dice[i][j] = false;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                dfs(board, dice, i, j, "");
            }
        }
        return allValidWords;
    }
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
            if (dictionary.contains(word))
                return point(word);
            else 
                return 0;
    }
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        StdOut.println(board);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}