package Service;

import Model.Player;
import Repository.IRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides business logic for managing players with validations and sorting.
 */
public class PlayerService {
    private IRepository<Player> playerRepository;

    /**
     * Constructs a PlayerService with the specified repository.
     *
     * @param playerRepository the repository for managing players
     */
    public PlayerService(IRepository<Player> playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Adds a new player to the repository with validation.
     *
     * @param player the player to add
     * @throws IllegalArgumentException if a duplicate player name is detected within the same team
     */
    public void addPlayer(Player player) {
        for (int id = 1; id <= Integer.MAX_VALUE; id++) {
            Player existingPlayer = playerRepository.read(id);
            if (existingPlayer == null) {
                break;
            }
            if (existingPlayer.getTeamID() == player.getTeamID() &&
                    existingPlayer.getName().equalsIgnoreCase(player.getName())) {
                throw new IllegalArgumentException("Duplicate player name in the same team is not allowed.");
            }
        }

        playerRepository.create(player);
    }

    /**
     * Retrieves a player by their ID.
     *
     * @param id the unique ID of the player
     * @return the Player with the specified ID, or null if not found
     */
    public Player getPlayer(int id) {
        return playerRepository.read(id);
    }

    /**
     * Updates a player's information with validation.
     *
     * @param player the player to update
     * @throws IllegalArgumentException if invalid statistics are detected
     */
    public void updatePlayer(Player player) {
        if (player.getGoals() < 0 || player.getAssists() < 0 ||
                player.getYellowCards() < 0 || player.getRedCards() < 0 ||
                player.getMinutesPlayed() < 0) {
            throw new IllegalArgumentException("Player statistics cannot be negative.");
        }

        playerRepository.update(player);
    }

    /**
     * Deletes a player from the repository by their ID.
     *
     * @param id the unique ID of the player to delete
     */
    public void deletePlayer(int id) {
        playerRepository.delete(id);
    }

    /**
     * Compares two players based on their goals per 90 minutes played.
     * Displays the better goalscorer and their goals per 90 minutes ratio.
     *
     * @param playerId1 the ID of the first player
     * @param playerId2 the ID of the second player
     * @throws IllegalArgumentException if either player does not exist
     */
    public void betterGoalscorer(int playerId1, int playerId2) {
        Player player1 = playerRepository.read(playerId1);
        Player player2 = playerRepository.read(playerId2);

        if (player1 == null) {
            throw new IllegalArgumentException("Player with ID " + playerId1 + " does not exist.");
        }
        if (player2 == null) {
            throw new IllegalArgumentException("Player with ID " + playerId2 + " does not exist.");
        }

        double goalsPer90Player1 = player1.getMinutesPlayed() > 0
                ? (double) player1.getGoals() / player1.getMinutesPlayed() * 90
                : 0;
        double goalsPer90Player2 = player2.getMinutesPlayed() > 0
                ? (double) player2.getGoals() / player2.getMinutesPlayed() * 90
                : 0;

        System.out.printf("%s (Player ID: %d) - Goals per 90 minutes: %.2f%n",
                player1.getName(), player1.getPlayerID(), goalsPer90Player1);
        System.out.printf("%s (Player ID: %d) - Goals per 90 minutes: %.2f%n",
                player2.getName(), player2.getPlayerID(), goalsPer90Player2);

        if (goalsPer90Player1 > goalsPer90Player2) {
            System.out.printf("%s is the better goalscorer.%n", player1.getName());
        } else if (goalsPer90Player2 > goalsPer90Player1) {
            System.out.printf("%s is the better goalscorer.%n", player2.getName());
        } else {
            System.out.println("Both players have the same goals per 90 minutes.");
        }
    }

    /**
     * Sorts all players by their number of goals in descending order.
     *
     * @return a list of players sorted by goals
     */
    public List<Player> sortPlayersByGoals() {
        List<Player> players = new ArrayList<>();
        int id = 1;

        // Iterate through all possible player IDs
        while (true) {
            Player player = playerRepository.read(id);
            if (player == null) {
                break; // Stop if no player exists for the ID
            }
            players.add(player);
            id++;
        }

        // Manual sorting by goals in descending order
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < players.size() - i - 1; j++) {
                if (players.get(j).getGoals() < players.get(j + 1).getGoals()) {
                    Collections.swap(players, j, j + 1);
                }
            }
        }
        return players;
    }
}