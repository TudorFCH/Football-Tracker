package Model;

/**
 * Represents a goal event in a match.
 */
public class GoalEvent extends Event {
    private int matchId; // ID-ul meciului
    private int playerId; // ID-ul jucătorului
    private int assists; // Numărul de pase decisive

    /**
     * Constructs a GoalEvent with the specified details.
     *
     * @param eventID  the unique ID of the event
     * @param matchId  the ID of the match where the event occurred
     * @param playerId the ID of the player involved in the event
     * @param assists  the number of assists made by the player
     */
    public GoalEvent(int eventID, int matchId, int playerId, int assists) {
        super(eventID, "Goal", "time_placeholder", playerId); // "time" poate fi adaptat
        this.matchId = matchId;
        this.playerId = playerId;
        this.assists = assists;
    }

    /**
     * Gets the match ID associated with this goal event.
     *
     * @return the match ID
     */
    @Override
    public int getMatchID() {
        return matchId;
    }

    /**
     * Gets the player ID associated with this goal event.
     *
     * @return the player ID
     */
    @Override
    public int getPlayerID() {
        return playerId;
    }

    /**
     * Gets the number of assists in this goal event.
     *
     * @return the number of assists
     */
    public int getAssists() {
        return assists;
    }

    @Override
    public String getEventInfo() {
        return String.format("Goal Event - Match ID: %d, Player ID: %d, Assists: %d", matchId, playerId, assists);
    }
}