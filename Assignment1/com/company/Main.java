package com.company;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> v = new ArrayList<>();
        v.add(50377);
        v.add(64782);
        ArrayList<Integer> w = new ArrayList<>();
        w.add(8578);
        w.add(70303);

        In in = new In("digraph-wordnet.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        WordNet net = new WordNet("synsets.txt", "hypernyms.txt");

        long startTime = System.nanoTime();
        for (int i = 0; i < 100; i++) {
//            net.isNoun("worm");
//            int length = sap.length(v, w);
//            net.sap("worm", "bird");
//            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length %d with ancestor %s\n", net.distance("worm", "bird"), net.sap("worm", "bird"));
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000/100;  //divide by 1000000 to get milliseconds.

        StdOut.printf("average time is %d ms\n", duration);
        SAP.main(args);
    }
}
