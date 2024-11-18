package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a football match.
 */
public class Match {
    private int matchID;
    private int teamId1;
    private int teamId2;
    private String date;
    public String location;
    private List<Event> events;

    /**
     * Constructs a Match with the specified details.
     *
     * @param matchID the unique ID of the match
     * @param teamId1 the ID of the first team
     * @param teamId2 the ID of the second team
     * @param date the date of the match
     * @param location the location of the match
     */
    public Match(int matchID, int teamId1, int teamId2, String date, String location) {
        this.matchID = matchID;
        this.teamId1 = teamId1;
        this.teamId2 = teamId2;
        this.date = date;
        this.location = location;
        this.events = new ArrayList<>();
    }

    public static Match fromFileString(String fileString) {
        // Split the string into its main components (matchID, teamId1, teamId2, date, location)
        String[] matchDetails = fileString.split(",", 5);

        // Parse the match details
        int matchID = Integer.parseInt(matchDetails[0]);
        int teamId1 = Integer.parseInt(matchDetails[1]);
        int teamId2 = Integer.parseInt(matchDetails[2]);
        String date = matchDetails[3];
        String location = matchDetails[4];

        // Create and return the Match object
        return new Match(matchID, teamId1, teamId2, date, location);
    }

    /**
     * Gets the unique ID of the match.
     *
     * @return the match ID
     */
    public int getMatchID() {
        return matchID;
    }

    /**
     * Gets the ID of the first team.
     *
     * @return the ID of the first team
     */
    public int getTeamId1() {
        return teamId1;
    }

    /**
     * Gets the ID of the second team.
     *
     * @return the ID of the second team
     */
    public int getTeamId2() {
        return teamId2;
    }

    /**
     * Adds an event to the match.
     *
     * @param event the event to add
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getEvents() {
        return events;
    }

    public String getLocation() {
        return location;
    }
    /**
     * Gets a summary of the match, including events.
     *
     * @return a string summarizing the match
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Match ID: ").append(matchID)
                .append(", Teams: ").append(teamId1).append(" vs ").append(teamId2)
                .append(", Date: ").append(date).append(", Location: ").append(location).append("\n");

        for (Event event : events) {
            summary.append(event.getEventInfo()).append("\n");
        }

        return summary.toString();
    }

    public void setMatchID(int i) {
        this.matchID = matchID;
    }

    public String toFileString() {
        StringBuilder fileString = new StringBuilder();
        fileString.append(matchID).append(",")
                .append(teamId1).append(",")
                .append(teamId2).append(",")
                .append(date).append(",")
                .append(location);

        // Append events in a simplified format
        for (Event event : events) {
            fileString.append("|").append(event.getEventInfo());
        }

        return fileString.toString();
    }

}

//package Model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Match {
//    private int matchID;
//    private int teamId1;
//    private int teamId2;
//    private String date;
//    private String location;
//    private List<Event> events; // List of events for this match
//
//    public Match(int matchID, int teamId1, int teamId2, String date, String location) {
//        this.matchID = matchID;
//        this.teamId1 = teamId1;
//        this.teamId2 = teamId2;
//        this.date = date;
//        this.location = location;
//        this.events = new ArrayList<>();
//    }
//
//    public int getMatchID() {
//        return matchID;
//    }
//
//    public int getTeamId1() {
//        return teamId1;
//    }
//
//    public int getTeamId2() {
//        return teamId2;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public List<Event> getEvents() {
//        return events;
//    }
//
//    public void addEvent(Event event) {
//        events.add(event);
//    }
//
//    public String getSummary() {
//        StringBuilder summary = new StringBuilder();
//        summary.append("Match ID: ").append(matchID)
//                .append(", Teams: ").append(teamId1).append(" vs ").append(teamId2)
//                .append(", Date: ").append(date).append(", Location: ").append(location).append("\n");
//
//        for (Event event : events) {
//            summary.append(event.getEventInfo()).append("\n");
//        }
//
//        return summary.toString();
//    }
//
//}