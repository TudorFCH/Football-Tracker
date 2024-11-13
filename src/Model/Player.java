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
    public int getGoals() { return goals; }
    public int getAssists() { return assists; }
    public int getYellowCards() { return yellowCards; }
    public int getRedCards() { return redCards; }
    public int getMinutesPlayed() { return minutesPlayed; }

    // Methods to increment statistics
    public void addGoal(int i) { this.goals+=i; }
    public void addAssist(int i) { this.assists+=i; }
    public void addYellowCard(int i) { this.yellowCards+=i; }
    public void addRedCard(int i) { this.redCards+=i; }
    public void addMinutesPlayed(int minutes) { minutesPlayed += minutes; }

    // Method to display statistics as a formatted string
    public String getStatistics() {
        return String.format("Goals: %d, Assists: %d, Yellow Cards: %d, Red Cards: %d, Minutes Played: %d",
                goals, assists, yellowCards, redCards, minutesPlayed);
    }

    // Additional getters for player information
    public int getPlayerID() { return playerID; }
    public String getName() { return name; }
    public int getTeamID() { return teamID; }
}