package Repository;

import Model.Event;
import Model.GoalEvent;
import Model.CardEvent;
import Model.MinutesPlayedEvent;

import java.io.*;
import java.util.*;

public class EventRepository {

    private String filePath;

    public EventRepository(String filePath) {
        this.filePath = filePath;
    }

    // Helper method to parse an event from a line in the file
    private Event parseEvent(String line) {
        String[] parts = line.split(",");
        if (parts.length > 0) {
            int eventID = Integer.parseInt(parts[0]);
            String type = parts[1];
            String time = parts[2];
            int playerID = Integer.parseInt(parts[3]);
            int matchID = Integer.parseInt(parts[4]);

            switch (type) {
                case "GoalEvent":
                    boolean isAssist = Boolean.parseBoolean(parts[5]);
                    return new GoalEvent(eventID, time, playerID, isAssist, matchID);
                case "CardEvent":
                    String cardType = parts[5];
                    return new CardEvent(eventID, time, playerID, cardType, matchID);
                case "MinutesPlayedEvent":
                    int minutesPlayed = Integer.parseInt(parts[5]);
                    return new MinutesPlayedEvent(eventID, time, playerID, minutesPlayed, matchID);
                default:
                    return null;
            }
        }
        return null;
    }

    // Get all events for a match from the file
    public List<Event> getEventsForMatch(int matchID) {
        List<Event> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Event event = parseEvent(line);
                if (event != null && event.getMatchID() == matchID) {
                    events.add(event);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading events: " + e.getMessage());
        }
        return events;
    }

    // Save a new list of events to the file (overwrites the existing file)
    public void saveEvents(List<Event> events) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Event event : events) {
                String line = event.eventID + "," + event.type + "," + event.time + "," + event.playerID + "," + event.getMatchID();
                if (event instanceof GoalEvent) {
                    line += "," + ((GoalEvent) event).getIsAssist();  // Use getIsAssist() here
                } else if (event instanceof CardEvent) {
                    line += "," + ((CardEvent) event).getCardType();
                } else if (event instanceof MinutesPlayedEvent) {
                    line += "," + ((MinutesPlayedEvent) event).getMinutesPlayed();
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving events: " + e.getMessage());
        }
    }
}
