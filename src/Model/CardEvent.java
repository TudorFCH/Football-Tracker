package Model;

public class CardEvent extends Event {
    private String cardType; // "Yellow" or "Red"

    public CardEvent(int eventID, String time, int playerID, String cardType) {
        super(eventID, time, String.valueOf(playerID));
        this.cardType = cardType;
        if ("Yellow".equalsIgnoreCase(cardType)) {
            this.yellowCards = 1;
        } else if ("Red".equalsIgnoreCase(cardType)) {
            this.redCards = 1;
        }
    }

    @Override
    public String getEventInfo() {
        return "Card Event - Player ID: " + playerID + ", Card Type: " + cardType + ", Time: " + time;
    }
}