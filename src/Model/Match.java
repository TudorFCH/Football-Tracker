package Model;

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
    }

    public void addEvent(Model.Event event) {
        events.add(event);
    }

    public String getSummary() {
        return "Summary";
    }
}