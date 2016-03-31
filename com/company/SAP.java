package com.company;


import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by qq456cvb on 3/30/16.
 */
public class SAP {

    private Digraph G;
    private boolean[][] marked;
    private int[][] distTo;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.NullPointerException();
        }
        this.G = new Digraph(G);
        marked = new boolean[2][G.V()];
        distTo = new int[2][G.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v == w)
            return 0;

        Queue<Integer> q = new Queue<Integer>();
        Arrays.fill(marked[0], false);
        Arrays.fill(marked[1], false);
        Arrays.fill(distTo[0], 0);
        Arrays.fill(distTo[1], 0);

        marked[0][v] = true;
        distTo[0][v] = 0;
        q.enqueue(v);
        while (!q.isEmpty()) {
            int top = q.dequeue();
            for (int linked : G.adj(top)) {
                if (!marked[0][linked]) {
                    distTo[0][linked] = distTo[0][top] + 1;
                    marked[0][linked] = true;
                    q.enqueue(linked);
                }
            }
        }

        int minDisp = -1;
        marked[1][w] = true;
        distTo[1][w] = 0;
        q.enqueue(w);
        if (marked[0][w]) { // found
            minDisp = distTo[0][w];
        }

        while (!q.isEmpty()) {
            int top = q.dequeue();
            for (int linked : G.adj(top)) {
                if (marked[0][linked]) { // found
                    if (minDisp == -1 || distTo[0][linked] + distTo[1][top] + 1 < minDisp) {
                        minDisp = distTo[0][linked] + distTo[1][top] + 1;
                    }
                }
                if (!marked[1][linked]) {
                    distTo[1][linked] = distTo[1][top] + 1;
                    marked[1][linked] = true;
                    q.enqueue(linked);
                }
            }
        }
        return minDisp;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v == w)
            return v;

        Queue<Integer> q = new Queue<>();
        Arrays.fill(marked[0], false);
        Arrays.fill(marked[1], false);
        Arrays.fill(distTo[0], 0);
        Arrays.fill(distTo[1], 0);

        marked[0][v] = true;
        distTo[0][v] = 0;
        q.enqueue(v);
        while (!q.isEmpty()) {
            int top = q.dequeue();
            for (int linked : G.adj(top)) {
                if (!marked[0][linked]) {
                    distTo[0][linked] = distTo[0][top] + 1;
                    marked[0][linked] = true;
                    q.enqueue(linked);
                }
            }
        }


        int minDisp = -1, minAncestor = -1;
        marked[1][w] = true;
        distTo[1][w] = 0;
        q.enqueue(w);
        if (marked[0][w]) { // found
            minDisp = distTo[0][w];
            minAncestor = w;
        }

        while (!q.isEmpty()) {
            int top = q.dequeue();
            for (int linked : G.adj(top)) {
                if (marked[0][linked]) { // found
                    if (minDisp == -1 || distTo[0][linked] + distTo[1][top] + 1 < minDisp) {
                        minDisp = distTo[0][linked] + distTo[1][top] + 1;
                        minAncestor = linked;
                    }
                }
                if (!marked[1][linked]) {
                    distTo[1][linked] = distTo[1][top] + 1;
                    marked[1][linked] = true;
                    q.enqueue(linked);
                }
            }
        }
        return  minAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.NullPointerException();
        }

        Queue<Integer> q = new Queue<>();
        Arrays.fill(marked[0], false);
        Arrays.fill(marked[1], false);
        Arrays.fill(distTo[0], 0);
        Arrays.fill(distTo[1], 0);

        for (int s : v) {
            marked[0][s] = true;
            distTo[0][s] = 0;
            q.enqueue(s);
        }
        for (int s : w) {
            if (marked[0][s]) {
                return 0;
            }
        }

        while (!q.isEmpty()) {
            int top = q.dequeue();
            for (int linked : G.adj(top)) {
                if (!marked[0][linked]) {
                    distTo[0][linked] = distTo[0][top] + 1;
                    marked[0][linked] = true;
                    q.enqueue(linked);
                }
            }
        }

        int minDisp = -1;
        for (int s : w) {
            marked[1][s] = true;
            distTo[1][s] = 0;
            q.enqueue(s);
            if (marked[0][s]) { // found
                if (minDisp == -1 || distTo[0][s] < minDisp) {
                    minDisp = distTo[0][s];
                }
            }
        }

        while (!q.isEmpty()) {
            int top = q.dequeue();
            for (int linked : G.adj(top)) {
                if (marked[0][linked]) { // found
                    if (minDisp == -1 || distTo[0][linked] + distTo[1][top] + 1 < minDisp) {
                        minDisp = distTo[0][linked] + distTo[1][top] + 1;
                    }
                }
                if (!marked[1][linked]) {
                    distTo[1][linked] = distTo[1][top] + 1;
                    marked[1][linked] = true;
                    q.enqueue(linked);
                }
            }
        }

        return  minDisp;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.NullPointerException();
        }

        Queue<Integer> q = new Queue<Integer>();
        Arrays.fill(marked[0], false);
        Arrays.fill(marked[1], false);
        Arrays.fill(distTo[0], 0);
        Arrays.fill(distTo[1], 0);

        for (int s : v) {
            marked[0][s] = true;
            distTo[0][s] = 0;
            q.enqueue(s);
        }
        for (int s : w) {
            if (marked[0][s]) {
                return s;
            }
        }

        while (!q.isEmpty()) {
            int top = q.dequeue();
            for (int linked : G.adj(top)) {
                if (!marked[0][linked]) {
                    distTo[0][linked] = distTo[0][top] + 1;
                    marked[0][linked] = true;
                    q.enqueue(linked);
                }
            }
        }

        int minDisp = -1, minAncestor = -1;
        for (int s : w) {
            marked[1][s] = true;
            distTo[1][s] = 0;
            q.enqueue(s);
            if (marked[0][s]) { // found
                if (minDisp == -1 || distTo[0][s] < minDisp) {
                    minDisp = distTo[0][s];
                    minAncestor = s;
                }
            }
        }

        while (!q.isEmpty()) {
            int top = q.dequeue();
            for (int linked : G.adj(top)) {
                if (marked[0][linked]) { // found
                    if (minDisp == -1 || distTo[0][linked] + distTo[1][top] + 1 < minDisp) {
                        minDisp = distTo[0][linked] + distTo[1][top] + 1;
                        minAncestor = linked;
                    }
                }
                if (!marked[1][linked]) {
                    distTo[1][linked] = distTo[1][top] + 1;
                    marked[1][linked] = true;
                    q.enqueue(linked);
                }
            }
        }
        return minAncestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        int v = Integer.parseInt(args[1]);
        int w = Integer.parseInt(args[2]);

        SAP sap = new SAP(G);
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        if (length != -1) {
            StdOut.printf("%d to %d: length(%d), ancestor(%d) ", v, w, length, ancestor);
        } else {
            StdOut.printf("%d to %d (-):  NO SAP\n", v, w);
        }
    }
}
