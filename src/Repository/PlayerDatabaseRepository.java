package Repository;

import Model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDatabaseRepository implements IRepository<Player> {

    // Method to create a new Player entry
    @Override
    public void create(Player player) {
        String query = "INSERT INTO players (name, team_id, goals, assists, yellow_cards, red_cards, minutes_played) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getName());
            statement.setInt(2, player.getTeamID());
            statement.setInt(3, player.getGoals());
            statement.setInt(4, player.getAssists());
            statement.setInt(5, player.getYellowCards());
            statement.setInt(6, player.getRedCards());
            statement.setInt(7, player.getMinutesPlayed());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to read a Player entry by ID
    @Override
    public Player read(int id) {
        String query = "SELECT * FROM players WHERE player_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Player(
                        resultSet.getInt("player_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("team_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no player is found
    }

    // Method to update an existing Player entry
    @Override
    public void update(Player player) {
        String query = "UPDATE players SET name = ?, team_id = ?, goals = ?, assists = ?, yellow_cards = ?, red_cards = ?, minutes_played = ? WHERE player_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getName());
            statement.setInt(2, player.getTeamID());
            statement.setInt(3, player.getGoals());
            statement.setInt(4, player.getAssists());
            statement.setInt(5, player.getYellowCards());
            statement.setInt(6, player.getRedCards());
            statement.setInt(7, player.getMinutesPlayed());
            statement.setInt(8, player.getPlayerID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a Player entry by ID
    @Override
    public void delete(int id) {
        String query = "DELETE FROM players WHERE player_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/footballdb", "root", "!parola1234");
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
