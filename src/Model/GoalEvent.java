package Model;

public class GoalEvent extends Event {
    private boolean isAssist;

    public GoalEvent(int eventID, String time, int playerID, boolean isAssist, int matchID) {
        super(eventID, "GoalEvent", time, playerID, matchID);
        this.isAssist = isAssist;
        this.goals = 1; // Increment goal count for this event
        if (isAssist) {
            this.assists = 1; // Increment assist count if applicable
        }
    }

    // Getter for isAssist
    public boolean getIsAssist() {
        return isAssist;
    }

    @Override
    public String getEventInfo() {
        return "Goal Event - Player ID: " + playerID + ", Assist: " + isAssist + ", Time: " + time;
    }
}