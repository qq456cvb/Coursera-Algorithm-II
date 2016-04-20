import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

/**
 * Created by qq456cvb on 4/16/16.
 */
public class BaseballElimination {
    private int[][] g; // games left
    private int[] w, l, r;
    private ArrayList<String> teamList;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename)
    {
        In file = new In(filename);
        int n = Integer.parseInt(file.readLine());
        g = new int[n][n];
        w = new int[n];
        l = new int[n];
        r = new int[n];
        teamList = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            String rawLine = file.readLine();
            String trimmed = rawLine.trim();
            String[] line = trimmed.split("\\s+");
            teamList.add(line[0]);
            w[i] = Integer.parseInt(line[1]);
            l[i] = Integer.parseInt(line[2]);
            r[i] = Integer.parseInt(line[3]);
            for (int j = 0; j < n; j++) {
                g[i][j] = Integer.parseInt(line[4+j]);
            }
        }
    }

    private void validateTeam(String team) {
        if (!teamList.contains(team))
            throw new java.lang.IllegalArgumentException();
    }

    public              int numberOfTeams()                        // number of teams
    {
        return teamList.size();
    }

    public Iterable<String> teams()                                // all teams
    {
        return teamList;
    }

    public              int wins(String team)                      // number of wins for given team
    {
        validateTeam(team);
        return w[teamList.indexOf(team)];
    }

    public              int losses(String team)                    // number of losses for given team
    {
        validateTeam(team);
        return l[teamList.indexOf(team)];
    }

    public              int remaining(String team)                 // number of remaining games for given team
    {
        validateTeam(team);
        return r[teamList.indexOf(team)];
    }

    public              int against(String team1, String team2)    // number of remaining games between team1 and team2
    {
        validateTeam(team1);
        validateTeam(team2);
        int idx1 = teamList.indexOf(team1);
        int idx2 = teamList.indexOf(team2);
        return g[idx1][idx2];
    }

    public          boolean isEliminated(String team)              // is given team eliminated?
    {
        validateTeam(team);
        ArrayList<String> minCut = (ArrayList<String>) certificateOfElimination(team);
        return !(minCut == null);
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team)
    {
        validateTeam(team);
        int teamIdx = teamList.indexOf(team);
        int max = 0, maxIdx = 0;
        for (int i = 0; i < w.length; i++) {
            if (w[i] > max) {
                max = w[i];
                maxIdx = i;
            }
        }

        if (w[teamIdx] + r[teamIdx] < max) { // simple test
            ArrayList<String> cut = new ArrayList<>();
            cut.add(teamList.get(maxIdx));
            return cut;
        }

        int n = numberOfTeams();
        int v = 1 + (n-1) * (n-2)/2 + n-1 + 1;
        int gameBaseV = 1, teamBaseV = gameBaseV + (n-1) * (n-2)/2;

        FlowNetwork network = new FlowNetwork(v);
        int idx = gameBaseV;
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                if (j == teamIdx || i == teamIdx)
                    continue;
                FlowEdge e1 = new FlowEdge(0, idx, g[i][j]);
                network.addEdge(e1);

                int netIdxI = teamBaseV + i, netIdxJ = teamBaseV + j;
                if (netIdxI - teamBaseV > teamIdx)
                    netIdxI--;
                if (netIdxJ - teamBaseV > teamIdx)
                    netIdxJ--;
                FlowEdge e2 = new FlowEdge(idx, netIdxI, Double.POSITIVE_INFINITY);
                FlowEdge e3 = new FlowEdge(idx, netIdxJ, Double.POSITIVE_INFINITY);
                network.addEdge(e2);
                network.addEdge(e3);
                idx++;
            }
        }

        idx = teamBaseV;
        for (int i = 0; i < n; i++) {
            if (i == teamIdx)
                continue;
            FlowEdge e = new FlowEdge(idx++, v - 1, w[teamIdx] + r[teamIdx] - w[i]);
            network.addEdge(e);
        }

        ArrayList<String> minCut = new ArrayList<>();
        FordFulkerson fordFulkerson = new FordFulkerson(network, 0, v-1);
        for (int i = 0; i < n; i++) {
            if (i == teamIdx)
                continue;
            int netIdx;
            if (i > teamIdx) {
                netIdx = teamBaseV+i-1;
            } else {
                netIdx = teamBaseV+i;
            }
            if (fordFulkerson.inCut(netIdx))
                minCut.add(teamList.get(i));
        }
        if (minCut.isEmpty())
            return null;
        return minCut;
    }
}
