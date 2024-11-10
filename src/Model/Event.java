package Model;

public abstract class Event {
    protected int eventID;
    protected String type;
    protected String time;

    public Event(int eventID, String type, String time) {
        this.eventID = eventID;
        this.type = type;
        this.time = time;
    }

    public abstract String getEventInfo();
}