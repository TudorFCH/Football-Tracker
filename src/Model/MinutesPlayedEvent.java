package Model;

public class MinutesPlayedEvent extends Event {
    public MinutesPlayedEvent(int eventID, String time, int playerID, int minutesPlayed) {
        super(eventID, time, String.valueOf(playerID));
        this.minutesPlayed = minutesPlayed;
    }

    @Override
    public String getEventInfo() {
        return "Minutes Played - Player ID: " + playerID + ", Minutes: " + minutesPlayed;
    }
}