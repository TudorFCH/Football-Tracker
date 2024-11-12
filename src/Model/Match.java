package Model;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private int matchID;
    private int teamId1;
    private int teamId2;
    private String date;
    private String location;
    private List<Model.Event> events;

    public Match(int matchID, int teamId1, int teamId2, String date, String location) {
        this.matchID = matchID;
        this.teamId1 = teamId1;
        this.teamId2 = teamId2;
        this.date = date;
        this.location = location;
        this.events = new ArrayList<>(); // Initialize events to avoid null issues
    }

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public void addEvent(Model.Event event) {
        events.add(event);
    }

    public String getSummary() {
        return "Match ID: " + matchID + ", Teams: " + teamId1 + " vs " + teamId2 + ", Date: " + date + ", Location: " + location;
    }

    public String toFileString() {
        return matchID + "," + teamId1 + "," + teamId2 + "," + date + "," + location;
    }

    public static Match fromFileString(String fileString) {
        String[] data = fileString.split(",");
        int matchID = Integer.parseInt(data[0]);
        int teamId1 = Integer.parseInt(data[1]);
        int teamId2 = Integer.parseInt(data[2]);
        String date = data[3];
        String location = data[4];
        return new Match(matchID, teamId1, teamId2, date, location);
    }
}
