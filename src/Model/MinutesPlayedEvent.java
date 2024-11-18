package Model;

public class MinutesPlayedEvent extends Event {
    public MinutesPlayedEvent(int eventID, String time, int playerID, int minutesPlayed, int matchID) {
        super(eventID, "MinutesPlayedEvent", time, playerID, matchID);
        this.minutesPlayed = minutesPlayed;
    }

    @Override
    public String getEventInfo() {
        return "Minutes Played - Player ID: " + playerID + ", Minutes: " + minutesPlayed;
    }
}
