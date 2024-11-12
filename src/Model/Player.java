package Model;

public class Player {
    private int playerID;
    private String name;
    private int teamID;
    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;
    private int minutesPlayed;

    public Player(int playerID, String name, int teamID) {
        this.playerID = playerID;
        this.name = name;
        this.teamID = teamID;
        this.goals = 0;
        this.assists = 0;
        this.yellowCards = 0;
        this.redCards = 0;
        this.minutesPlayed = 0;
    }

    // Setter for playerID
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    // Getters for statistics
    public int getPlayerID() { return playerID; }
    public String getName() { return name; }
    public int getTeamID() { return teamID; }

    public void addGoal() { goals++; }
    public void addAssist() { assists++; }
    public void addYellowCard() { yellowCards++; }
    public void addRedCard() { redCards++; }
    public void addMinutesPlayed(int minutes) { minutesPlayed += minutes; }

    public String getStatistics() {
        return String.format("Goals: %d, Assists: %d, Yellow Cards: %d, Red Cards: %d, Minutes Played: %d",
                goals, assists, yellowCards, redCards, minutesPlayed);
    }
}