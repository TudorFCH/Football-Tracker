package Model;

public class CardEvent extends Event {
    private String cardType; // "Yellow" or "Red"

    public CardEvent(int eventID, String time, int playerID, String cardType, int matchID) {
        super(eventID, "CardEvent", time, playerID, matchID);
        this.cardType = cardType;
        if ("Yellow".equalsIgnoreCase(cardType)) {
            this.yellowCards = 1;
        } else if ("Red".equalsIgnoreCase(cardType)) {
            this.redCards = 1;
        }
    }

    // Getter for cardType
    public String getCardType() {
        return cardType;
    }

    @Override
    public String getEventInfo() {
        return "Card Event - Player ID: " + playerID + ", Card Type: " + cardType + ", Time: " + time;
    }
}
