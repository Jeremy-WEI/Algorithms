
import java.util.ArrayList;

public class BaseballElimination {
    private int numberOfTeams;
    private ArrayList<String> teams;
    private int[] wins, losses, remaining;
    private int[][] against;
    private Bag<String>[] certificateOfElimination;
    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        In in = new In(filename);
        numberOfTeams = in.readInt();
        teams = new ArrayList<String>();
        wins = new int[numberOfTeams];
        losses = new int[numberOfTeams];
        remaining = new int[numberOfTeams];
        against = new int[numberOfTeams][numberOfTeams];
        certificateOfElimination = (Bag<String>[]) new Bag[numberOfTeams];
        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(in.readString());
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < numberOfTeams; j++) {
                against[i][j] = in.readInt();
            }
        }
    }
    public int numberOfTeams() {
         // number of teams
        return numberOfTeams;
    }
    public Iterable<String> teams() {
        // all teams
        return teams;
    }
    public int wins(String team) {
        // number of wins for given team
        int index = teams.indexOf(team);
        if (index == -1) 
            throw new IllegalArgumentException();
        else
            return wins[teams.indexOf(team)];
    }
    public int losses(String team) {
        // number of losses for given team
        int index = teams.indexOf(team);
        if (index == -1) 
            throw new IllegalArgumentException();
        else
            return losses[teams.indexOf(team)];
    }
    public int remaining(String team) {
        // number of remaining games for given team
        int index = teams.indexOf(team);
        if (index == -1) 
            throw new IllegalArgumentException();
        else
            return remaining[teams.indexOf(team)];
    }
    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        int index1 = teams.indexOf(team1);
        int index2 = teams.indexOf(team2);
        if (index1 == -1 || index2 == -1) 
            throw new IllegalArgumentException();
        else
            return against[teams.indexOf(team1)][teams.indexOf(team2)];
    }
    public boolean isEliminated(String team) {
        // is given team eliminated?
        int index = teams.indexOf(team);
        if (index == -1) 
            throw new IllegalArgumentException();
        certificateOfElimination[index] = new Bag<String>();
        if (numberOfTeams == 1) 
            return false;
        int totalWins = wins[index] + remaining[index];
        certificateOfElimination[index] = new Bag<String>();
        for (int i = 0; i < numberOfTeams; i++) {
            if (totalWins < wins[i]) 
                certificateOfElimination[index].add(teams.get(i));
        }
        if (!certificateOfElimination[index].isEmpty()) return true;
        int num = (numberOfTeams * numberOfTeams - numberOfTeams + 4) / 2;
        int sum = 0;
        FlowNetwork flowNetwork = new FlowNetwork(num);
        for (int i = 0, k = 1, offset1 = 0; i < numberOfTeams; i++, offset1++) {
            if (i == index) {
                offset1--;
                continue;
            }
            for (int j = i + 1, offset2 = 1; j < numberOfTeams; j++, offset2++) {
                if (j == index) {
                    offset2--;
                    continue;
                }
                flowNetwork.addEdge(new FlowEdge(0, k, against[i][j]));
                flowNetwork.addEdge(new FlowEdge(k, num - numberOfTeams + offset1, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(k, num - numberOfTeams + offset1 + offset2, Double.POSITIVE_INFINITY));
                sum += against[i][j];
                k++;
            }
        }
        for (int i = 0, offset = 0; i < numberOfTeams; i++, offset++) {
            if (i == index) {
                offset--;
                continue;
            }
            else {
                flowNetwork.addEdge(new FlowEdge(num - numberOfTeams + offset, num - 1, totalWins - wins[i]));
            }
        }
        FordFulkerson FF = new  FordFulkerson(flowNetwork, 0, num - 1);
        for (int i = 0, offset = 0; i < numberOfTeams; i++, offset++) {
            if (i == index) {
                offset--;
                continue;
            }
            else {
                 if (FF.inCut(num - numberOfTeams + offset)) certificateOfElimination[index].add(teams.get(i));
            }
        }
        if (FF.value() < sum) return true;
        else return false;
    }
    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        int index = teams.indexOf(team);
        if (index == -1) 
            throw new IllegalArgumentException();
        if (certificateOfElimination[index] == null) 
            isEliminated(team);
        return certificateOfElimination[index].isEmpty() ? null : certificateOfElimination[index];
    }
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}