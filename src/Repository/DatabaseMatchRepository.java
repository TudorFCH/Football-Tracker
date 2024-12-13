package Repository;

import Model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMatchRepository implements IRepository<Match> {

    @Override
    public void create(Match match) {
        String query = "INSERT INTO matches (team_id1, team_id2, date, location) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, match.getTeamId1());
            statement.setInt(2, match.getTeamId2());
            statement.setString(3, match.getDate());
            statement.setString(4, match.getLocation());
            statement.executeUpdate();

            // Retrieve the generated matchID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                match.setMatchID(generatedKeys.getInt(1)); // Set the matchID with the generated key
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Match read(int id) {
        String query = "SELECT * FROM matches WHERE match_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Match match = new Match(
                        resultSet.getInt("match_id"),
                        resultSet.getInt("team_id1"),
                        resultSet.getInt("team_id2"),
                        resultSet.getString("date"),
                        resultSet.getString("location")
                );

                // Fetch associated events
                match = readEventsForMatch(match);

                return match;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Match readEventsForMatch(Match match) {
        String query = "SELECT * FROM events WHERE match_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, match.getMatchID());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Event event = null;
                switch (resultSet.getString("event_type")) {
                    case "GoalEvent":
                        event = new GoalEvent(
                                resultSet.getInt("event_id"),
                                resultSet.getString("time"),
                                resultSet.getInt("player_id"),
                                resultSet.getBoolean("details"),
                                resultSet.getInt("match_id")
                        );
                        break;
                    case "CardEvent":
                        event = new CardEvent(
                                resultSet.getInt("event_id"),
                                resultSet.getString("time"),
                                resultSet.getInt("player_id"),
                                resultSet.getString("details"),
                                resultSet.getInt("match_id")
                        );
                        break;
                    case "MinutesPlayedEvent":
                        event = new MinutesPlayedEvent(
                                resultSet.getInt("event_id"),
                                resultSet.getString("time"),
                                resultSet.getInt("player_id"),
                                resultSet.getInt("details"),
                                resultSet.getInt("match_id")
                        );
                        break;
                }
                if (event != null) {
                    match.addEvent(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return match;
    }

    @Override
    public void update(Match match) {
        String query = "UPDATE matches SET team_id1 = ?, team_id2 = ?, date = ?, location = ? WHERE match_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, match.getTeamId1());
            statement.setInt(2, match.getTeamId2());
            statement.setString(3, match.getDate());
            statement.setString(4, match.getLocation());
            statement.setInt(5, match.getMatchID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM matches WHERE match_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();

            // Also delete associated events
            deleteEventsForMatch(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteEventsForMatch(int matchID) {
        String query = "DELETE FROM events WHERE match_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, matchID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
