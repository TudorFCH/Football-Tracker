package Repository;

import Model.CardEvent;
import Model.Event;
import Model.GoalEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDatabaseRepository implements IRepository<Event> {

    // Method to create a new Event entry
    @Override
    public void create(Event event) {
        String query = "INSERT INTO events (match_id, player_id, event_type, time, details) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, event.getMatchID());
            statement.setInt(2, event.getPlayerID());
            statement.setString(3, event.getType());
            statement.setString(4, event.getTime());

            if (event instanceof GoalEvent) {
                GoalEvent goalEvent = (GoalEvent) event;
                statement.setString(5, goalEvent.getIsAssist() ? "Assisted: true" : "Assisted: false");
            } else if (event instanceof CardEvent) {
                CardEvent cardEvent = (CardEvent) event;
                statement.setString(5, "Card: " + cardEvent.getCardType());
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to read an Event entry by ID
    @Override
    public Event read(int id) {
        String query = "SELECT * FROM events WHERE event_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String eventType = resultSet.getString("event_type");
                if ("GoalEvent".equals(eventType)) {
                    return new GoalEvent(
                            resultSet.getInt("event_id"),
                            resultSet.getString("time"),
                            resultSet.getInt("player_id"),
                            "Assisted: true".equals(resultSet.getString("details")),
                            resultSet.getInt("match_id")
                    );
                } else if ("CardEvent".equals(eventType)) {
                    return new CardEvent(
                            resultSet.getInt("event_id"),
                            resultSet.getString("time"),
                            resultSet.getInt("player_id"),
                            resultSet.getString("details").split(": ")[1],
                            resultSet.getInt("match_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no event is found
    }

    // Method to update an existing Event entry
    @Override
    public void update(Event event) {
        String query = "UPDATE events SET match_id = ?, player_id = ?, event_type = ?, time = ?, details = ? WHERE event_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, event.getMatchID());
            statement.setInt(2, event.getPlayerID());
            statement.setString(3, event.getType());
            statement.setString(4, event.getTime());

            if (event instanceof GoalEvent) {
                GoalEvent goalEvent = (GoalEvent) event;
                statement.setString(5, goalEvent.getIsAssist() ? "Assisted: true" : "Assisted: false");
            } else if (event instanceof CardEvent) {
                CardEvent cardEvent = (CardEvent) event;
                statement.setString(5, "Card: " + cardEvent.getCardType());
            }

            statement.setInt(6, event.getEventID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete an Event entry by ID
    @Override
    public void delete(int id) {
        String query = "DELETE FROM events WHERE event_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Additional Method to get all Events for a particular match or player can be added as needed
}
