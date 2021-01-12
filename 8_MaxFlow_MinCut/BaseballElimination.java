import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

// create a baseball division from given filename in format specified below
public class BaseballElimination {
    private int nbTeams;
    private ArrayList<String> teams;
    private int[] wins;
    private int[] losses;
    private int[] leftToPlay;
    private int[][] gamesPerTeam;
    private FlowNetwork fNetwork;

    public BaseballElimination(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }

        In fileIn = new In(filename);
        int lineNb = 0;
        while (fileIn.hasNextLine()) {
            String line = fileIn.readLine();
            //StdOut.println(line);
            if (lineNb == 0) {

                nbTeams = Integer.parseInt(line);
                wins = new int[nbTeams];
                losses = new int[nbTeams];
                leftToPlay = new int[nbTeams];
                gamesPerTeam = new int[nbTeams][nbTeams];
                teams = new ArrayList<String>();

            }
            else {
                line = line.trim();
                while (line.contains("  ")) {
                    line = line.replace("  ", " ");
                }
                String[] lineArray = line.split(" ");
                teams.add(lineArray[0]); // add team
                int index = teams.indexOf(lineArray[0]);
                wins[index] = Integer.parseInt(lineArray[1]);
                losses[index] = Integer.parseInt(lineArray[2]);
                leftToPlay[index] = Integer.parseInt(lineArray[3]);
                for (int u = 4; u < lineArray.length; u++) {
                    gamesPerTeam[index][u - 4] = Integer.parseInt(lineArray[u]);
                }
            }

            lineNb++;
        }

    }

    private void buildFlowNetwork(int chosenTeam) {

        /*
           -2-  (1) 1-2  -2-   (7) 1 -2-
           --   (2) 2-3  -2-   (8) 2 -2-
        (0)--   (3) 2-4  --    (9) 3     (11)end
           --   (4) 1-4  --    (10) 4
           --   (5) 1-3
           --   (6)
         */
        int offset = ((nbTeams - 1) * nbTeams) / 2;
        int[] nodeCapacity = new int[nbTeams];

        // should remove chosen team from flow

        fNetwork = new FlowNetwork(offset + nbTeams + 2);

        int gameNode = 1;
        for (int i = 0; i < gamesPerTeam.length; i++) {
            for (int y = 0; y < i; y++) {
                if (gamesPerTeam[i][y] > 0 && !(i == chosenTeam || y == chosenTeam)) {

                    FlowEdge originEdge = new FlowEdge(0, gameNode, gamesPerTeam[i][y]);
                    FlowEdge nodeGame1 = new FlowEdge(gameNode, offset + i,
                                                      Double.POSITIVE_INFINITY);
                    FlowEdge nodeGame2 = new FlowEdge(gameNode, offset + y,
                                                      Double.POSITIVE_INFINITY);
                    fNetwork.addEdge(originEdge);
                    fNetwork.addEdge(nodeGame1);
                    fNetwork.addEdge(nodeGame2);

                    nodeCapacity[i] += gamesPerTeam[i][y];
                    nodeCapacity[y] += gamesPerTeam[i][y];
                    gameNode++;
                }
            }

        }

        // add end node
        int endNode = offset + nbTeams + 1;
        for (int i = 0; i < nbTeams; i++) {
            if (i != chosenTeam) {
                FlowEdge endEdge;
                int capacity = Math.max(0, Math.min(
                        wins[chosenTeam] + leftToPlay[chosenTeam] - wins[i], nodeCapacity[i]));

                endEdge = new FlowEdge(i + offset, endNode, capacity);
                fNetwork.addEdge(endEdge);

            }
        }

    }


    // number of teams
    public int numberOfTeams() {
        return nbTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null || !(teams.contains(team))) {
            throw new IllegalArgumentException();
        }
        int index = teams.indexOf(team);
        return wins[index];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null || !(teams.contains(team))) {
            throw new IllegalArgumentException();
        }
        int index = teams.indexOf(team);
        return losses[index];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null || !(teams.contains(team))) {
            throw new IllegalArgumentException();
        }
        int index = teams.indexOf(team);
        return leftToPlay[index];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || !(teams.contains(team1))) {
            throw new IllegalArgumentException();
        }
        if (team2 == null || !(teams.contains(team2))) {
            throw new IllegalArgumentException();
        }
        int index1 = teams.indexOf(team1);
        int index2 = teams.indexOf(team2);

        return gamesPerTeam[index1][index2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {

        if (team == null || !(teams.contains(team))) {
            throw new IllegalArgumentException();
        }

        int indexTeam = teams.indexOf(team);
        int maxGain = 0;
        for (int l = 0; l < leftToPlay.length; l++) {
            maxGain = Math.max(maxGain, wins[l]);
        }
        if ((wins[indexTeam] + leftToPlay[indexTeam]) < maxGain) {
            return true;
        }

        buildFlowNetwork(indexTeam);

        FordFulkerson ffDigraph = new FordFulkerson(fNetwork, 0, fNetwork.V() - 1);


        for (FlowEdge fe : fNetwork.adj(0)) {
            if (!(fe.flow() == fe.capacity())) {
                return true;
            }
        }
        return false;

    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {

        if (team == null || !(teams.contains(team))) {
            throw new IllegalArgumentException();
        }
        if (!isEliminated(team)) {
            return null;
        }

        ArrayList<String> teamList = new ArrayList<String>();
        int indexTeam = teams.indexOf(team);
        buildFlowNetwork(indexTeam);
        FordFulkerson ffDigraph = new FordFulkerson(fNetwork, 0, fNetwork.V() - 1);
        int offset = ((nbTeams - 1) * nbTeams) / 2;

        for (int i = 0; i < nbTeams; i++) {

            if ((i != indexTeam) && ffDigraph.inCut(i + offset)) {
                teamList.add(teams.get(i));
            }
            else if ((wins[indexTeam] + leftToPlay[indexTeam]) < wins[i]) {
                teamList.add(teams.get(i));
            }

        }


        return teamList;
    }


    public static void main(String[] args) {

        String fileIn
                = "C:\\Users\\jonat\\Dropbox\\CS\\5-Core Theory\\Algorithms-Part II\\3_MaxFlow_MinCut\\baseball\\teams4b.txt";
        BaseballElimination division = new BaseballElimination(fileIn);

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

    }

}
