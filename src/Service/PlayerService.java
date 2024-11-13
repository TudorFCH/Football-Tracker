package Service;

import Model.Player;
import Repository.FilePlayerRepository;

/**
 * Provides business logic for managing players with validations.
 */
public class PlayerService {
    private FilePlayerRepository playerRepository;

    /**
     * Constructs a PlayerService with the specified repository.
     *
     * @param playerRepository the repository for managing players
     */
    public PlayerService(FilePlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Adds a new player to the repository with validation.
     *
     * @param player the player to add
     * @throws IllegalArgumentException if a duplicate player name is detected within the same team
     */
    public void addPlayer(Player player) {
        // Validate unique player name within the same team
        int id = 1;
        while (true) {
            Player existingPlayer = playerRepository.getPlayer(id);
            if (existingPlayer == null) {
                break; // Stop if no more players exist
            }
            if (existingPlayer.getTeamID() == player.getTeamID() &&
                    existingPlayer.getName().equalsIgnoreCase(player.getName())) {
                throw new IllegalArgumentException("Duplicate player name in the same team is not allowed.");
            }
            id++;
        }

        playerRepository.addPlayer(player);
    }

    /**
     * Retrieves a player by their ID.
     *
     * @param id the unique ID of the player
     * @return the Player with the specified ID, or null if not found
     */
    public Player getPlayer(int id) {
        return playerRepository.getPlayer(id);
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

        playerRepository.updatePlayer(player);
    }

    /**
     * Deletes a player from the repository by their ID.
     *
     * @param id the unique ID of the player to delete
     */
    public void deletePlayer(int id) {
        playerRepository.deletePlayer(id);
    }
}