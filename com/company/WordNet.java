package com.company;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by qq456cvb on 3/30/16.
 */
public class WordNet {

    private HashMap<String, HashSet<Integer>> nounToIds;
    private HashMap<Integer, String> idToNouns;
    private HashSet<String> allWords;
    private Digraph net;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new java.lang.NullPointerException();
        }
        idToNouns = new HashMap<>();
        nounToIds = new HashMap<>();
        allWords = new HashSet<>();
        In synsetsIn = new In(synsets);

        int nounNum = 0;
        while (synsetsIn.hasNextLine()) {
            String ln = synsetsIn.readLine();
            String[] words = ln.split(",");
            int id = Integer.parseInt(words[0]);

            idToNouns.put(id, words[1]);
            String[] nouns = words[1].split(" ");
            for (int i = 0; i < nouns.length; i++) {
                if (!nounToIds.containsKey(nouns[i])) {
                    HashSet<Integer> set = new HashSet<>();
                    set.add(id);
                    nounToIds.put(nouns[i], set);
                } else {
                    nounToIds.get(nouns[i]).add(id);
                }
                allWords.add(nouns[i]);
            }
            nounNum++;
        }

        // build net
        net = new Digraph(nounNum);
        In hypernymsIn = new In(hypernyms);
        while (hypernymsIn.hasNextLine()) {
            String ln = hypernymsIn.readLine();
            String[] words = ln.split(",");

            int n = Integer.parseInt(words[0]);
            for (int i = 1; i < words.length; i++) {
                int v = Integer.parseInt(words[i]);
                net.addEdge(n, v);
            }
        }
        validDAG(); // check if it is valid
        sap = new SAP(net);
    }

    private void validDAG() {
        // check if net has exactly one root
        int rootNum = 0;
        for (int i = 0; i < net.V(); i++) {
            if (net.indegree(i) > 0 && net.outdegree(i) == 0) {
                rootNum++;
            }
        }
        if (rootNum != 1) {
            throw new java.lang.IllegalArgumentException("The graph has more than one root or no root!");
        }

        // check cycle
        boolean[] visited = new boolean[net.V()];
        for (int i = 0; i < net.V(); i++) {
            if (checkCycleRec(net, i, visited))
                throw new java.lang.IllegalArgumentException("The graph has cycle!");
        }
    }

    private boolean checkCycleRec(Digraph g, int v, boolean[] visited) {
        if (!visited[v]) {
            visited[v] = true;
            boolean found = false;
            for (int linked : g.adj(v)) {
                found = checkCycleRec(g, linked, visited) || found;
            }
            visited[v] = false;
            return found;
        } else {
            return true;
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return allWords;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new java.lang.NullPointerException();
        }
        return allWords.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new java.lang.NullPointerException();
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }
        Iterable<Integer> v = nounToIds.get(nounA), w = nounToIds.get(nounB);

        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new java.lang.NullPointerException();
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }
        Iterable<Integer> v = nounToIds.get(nounA), w = nounToIds.get(nounB);

        int ancestor = sap.ancestor(v, w);
        return idToNouns.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);

        String word1 = "zebra", word2 = "horse";
        StdOut.printf("%s and %s's sap is %s with distance %d\n",
          word1, word2, wordNet.sap(word1, word2), wordNet.distance(word1, word2));
    }
}
