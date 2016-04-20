import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by qq456cvb on 4/20/16.
 */
public class BoggleSolver
{
    private class TrieAlphabet
    {
        private static final int R = 26;        // alphabet

        private TrieAlphabet.Node root;      // root of trie
        private int N;          // number of keys in trie

        // R-way trie node
        private class Node {
            private TrieAlphabet.Node[] next = new TrieAlphabet.Node[R];
            private boolean isString;
        }

        /**
         * Initializes an empty set of strings.
         */
        public TrieAlphabet() {
        }

        /**
         * Does the set contain the given key?
         * @param key the key
         * @return <tt>true</tt> if the set contains <tt>key</tt> and
         *     <tt>false</tt> otherwise
         * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
         */
        public boolean contains(String key) {
            TrieAlphabet.Node x = get(root, key, 0);
            if (x == null) return false;
            return x.isString;
        }

        public boolean containsPrefix(String key) {
            TrieAlphabet.Node x = get(root, key, 0);
            return !(x == null);
        }

        private TrieAlphabet.Node get(TrieAlphabet.Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            int c = key.charAt(d) - 65;
            return get(x.next[c], key, d+1);
        }

        /**
         * Adds the key to the set if it is not already present.
         * @param key the key to add
         * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
         */
        public void add(String key) {
            root = add(root, key, 0);
        }

        private TrieAlphabet.Node add(TrieAlphabet.Node x, String key, int d) {
            if (x == null) x = new TrieAlphabet.Node();
            if (d == key.length()) {
                if (!x.isString) N++;
                x.isString = true;
            }
            else {
                int c = key.charAt(d) - 65;
                x.next[c] = add(x.next[c], key, d+1);
            }
            return x;
        }

        public int size() {
            return N;
        }

        public boolean isEmpty() {
            return size() == 0;
        }
    }

    private TrieAlphabet trieSET = new TrieAlphabet();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        for (String word : dictionary) {
            trieSET.add(word);
        }
    }

    private void findWord(StringBuilder prefix, BoggleBoard board, int x, int y, boolean[][] visited, Queue<String> results)
    {
        String str = prefix.toString();
        if (trieSET.containsPrefix(str)) {
            if (str.length() > 2) {
                if (trieSET.contains(str))
                    results.enqueue(str);
            }
            for (int i = Math.max(x - 1, 0); i <= Math.min(x + 1, board.rows()-1); i++) {
                for (int j = Math.max(y - 1, 0); j <= Math.min(y + 1, board.cols()-1); j++) {
                    if (i == x && j == y) continue;
                    if (!visited[i][j]) {
                        if (board.getLetter(i, j) == 'Q') {
                            prefix.append("QU");
                        } else {
                            prefix.append(board.getLetter(i, j));
                        }
                        visited[i][j] = true;
                        findWord(prefix, board, i, j, visited, results);
                        if (board.getLetter(i, j) == 'Q') {
                            prefix.deleteCharAt(prefix.length() - 1);
                        }
                        prefix.deleteCharAt(prefix.length() - 1);
                        visited[i][j] = false;
                    }
                }
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        HashSet<String> results = new HashSet<>();
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                Queue<String> subResults = new Queue<>();
                StringBuilder string = new StringBuilder();
                if (board.getLetter(i, j) == 'Q') {
                    string.append("QU");
                } else {
                    string.append(board.getLetter(i, j));
                }
                for (boolean[] entry : visited) {
                    Arrays.fill(entry, false);
                }
                visited[i][j] = true;
                findWord(string, board, i, j, visited, subResults);
                for (String str : subResults) {
                    results.add(str);
                }
            }
        }
        return results;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        if (trieSET.contains(word)) {
            switch (word.length()) {
                case 0: case 1: case 2:
                    return 0;
                case 3: case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 11;
            }
        } else {
            return 0;
        }
    }
}
