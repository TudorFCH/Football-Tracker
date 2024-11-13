package Repository;

import Model.Event;
import Model.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FilePlayerRepository {
    private final String filename;
    private Map<Integer, Player> playerStorage = new HashMap<>();
    private int currentPlayerID = 1;

    public FilePlayerRepository(String filename) {
        this.filename = filename;
        loadFromFile();
    }

    public void addPlayer(Player player) {
        player.setPlayerID(currentPlayerID++);
        playerStorage.put(player.getPlayerID(), player);
        saveToFile();
    }

    public Player getPlayer(int playerID) {
        return playerStorage.get(playerID);
    }

    public void updatePlayer(Player player) {
        playerStorage.put(player.getPlayerID(), player);
        saveToFile();
    }

    public void deletePlayer(int playerID) {
        playerStorage.remove(playerID);
        saveToFile();
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Player player : playerStorage.values()) {
                writer.println(playerToFileString(player));
            }
        } catch (IOException e) {
            System.out.println("Error saving player data to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Player player = playerFromFileString(line);
                playerStorage.put(player.getPlayerID(), player);
                currentPlayerID = Math.max(currentPlayerID, player.getPlayerID() + 1);
            }
        } catch (IOException e) {
            System.out.println("Error loading player data from file: " + e.getMessage());
        }
    }

    private String playerToFileString(Player player) {
        return player.getPlayerID() + "," + player.getName() + "," + player.getTeamID() + ","
                + player.getGoals() + "," + player.getAssists() + "," + player.getYellowCards() + ","
                + player.getRedCards() + "," + player.getMinutesPlayed();
    }

    private Player playerFromFileString(String fileString) {
        try {
            String[] data = fileString.split(",");
            if (data.length != 8) {
                throw new IllegalArgumentException("Invalid player data format: " + fileString);
            }

            int playerID = Integer.parseInt(data[0]);
            String name = data[1];
            int teamID = Integer.parseInt(data[2]);
            int goals = Integer.parseInt(data[3]);
            int assists = Integer.parseInt(data[4]);
            int yellowCards = Integer.parseInt(data[5]);
            int redCards = Integer.parseInt(data[6]);
            int minutesPlayed = Integer.parseInt(data[7]);

            Player player = new Player(playerID, name, teamID);
            player.addGoal(goals);
            player.addAssist(assists);
            player.addYellowCard(yellowCards);
            player.addRedCard(redCards);
            player.addMinutesPlayed(minutesPlayed);

            return player;
        } catch (Exception e) {
            System.out.println("Error parsing player data: " + fileString + " - " + e.getMessage());
            return null; // Return null for invalid data
        }
    }
}