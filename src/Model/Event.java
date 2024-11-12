package Model;

public abstract class Event {
    protected int eventID;
    protected String type;
    protected String time;
    protected int playerID;

    protected int goals;
    protected int assists;
    protected int yellowCards;
    protected int redCards;
    protected int minutesPlayed;

    public Event(int eventID, String type, String time) {
        this.eventID = eventID;
        this.type = type;
        this.time = time;
        this.playerID= playerID;
        this.goals = 0;
        this.assists = 0;
        this.yellowCards = 0;
        this.redCards = 0;
        this.minutesPlayed = 0;
    }

    public abstract String getEventInfo();

    public void addGoal() {goals++;}
    public void addAssist() {assists++;}
    public void addYellowCard() {yellowCards++;}
    public void addRedCard() {redCards++;}
    public void setMinutesPlayed(int minutes) { this.minutesPlayed = minutes; }

    public int getGoals() { return goals; }
    public int getAssists() { return assists; }
    public int getYellowCards() { return yellowCards; }
    public int getRedCards() { return redCards; }
    public int getMinutesPlayed() { return minutesPlayed; }
}