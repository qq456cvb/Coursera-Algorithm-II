package com.company;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


/**
 * Created by qq456cvb on 3/31/16.
 */
public class Outcast {

    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordNet) {
        if (wordNet == null) {
            throw new java.lang.NullPointerException();
        }
        this.wordNet = wordNet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDist = -1;
        String out = null;
        for (int i = 0; i < nouns.length; i++) {
            int distanceToAll = 0;
            String noun = nouns[i];
            for (int j = 0; j < nouns.length; j++) {
                if (j == i)
                    continue;
                String nounTo = nouns[j];
                int dist = wordNet.distance(noun, nounTo);
                if (dist >= 0) {
                    distanceToAll += dist;
                } else {
                    StdOut.println("Warning: unrelated nouns found: " + noun + "and " + nounTo + "\n");
                }
            }
            if (distanceToAll > maxDist) {
                maxDist = distanceToAll;
                out = noun;
            }
        }
        return out;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
