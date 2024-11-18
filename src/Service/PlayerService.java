package Service;

import Model.Player;
import Model.Match;
import Model.GoalEvent;
import Repository.IRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides business logic for managing players with validations, sorting, filtering, and comparisons.
 */
public class PlayerService {
    private IRepository<Player> playerRepository;
    private IRepository<Match> matchRepository;

    /**
     * Constructs a PlayerService with the specified repositories.
     *
     * @param playerRepository the repository for managing players
     * @param matchRepository  the repository for managing matches
     */
    public PlayerService(IRepository<Player> playerRepository, IRepository<Match> matchRepository) {
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
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
     * Filters players who have scored more goals than the specified number.
     *
     * @param minGoals the minimum number of goals
     * @return a list of players who have scored more than the given number of goals
     */
    public List<Player> filterPlayersByGoals(int minGoals) {
        List<Player> filteredPlayers = new ArrayList<>();
        int id = 1;

        while (true) {
            Player player = playerRepository.read(id);
            if (player == null) {
                break;
            }
            if (player.getGoals() > minGoals) {
                filteredPlayers.add(player);
            }
            id++;
        }

        return filteredPlayers;
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
        // Retrieve the players from the repository
        Player player1 = playerRepository.read(playerId1);
        Player player2 = playerRepository.read(playerId2);

        // Validate that both players exist
        if (player1 == null) {
            throw new IllegalArgumentException("Player with ID " + playerId1 + " does not exist.");
        }
        if (player2 == null) {
            throw new IllegalArgumentException("Player with ID " + playerId2 + " does not exist.");
        }

        // Calculate goals per 90 minutes for each player
        double goalsPer90Player1 = player1.getMinutesPlayed() > 0
                ? (double) player1.getGoals() / player1.getMinutesPlayed() * 90
                : 0;
        double goalsPer90Player2 = player2.getMinutesPlayed() > 0
                ? (double) player2.getGoals() / player2.getMinutesPlayed() * 90
                : 0;

        // Display the comparison results
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
     * Calculates the performance of a player in a specific match.
     *
     * @param playerId the ID of the player
     * @param matchId  the ID of the match
     * @param events   the list of events associated with the match
     * @return a string summarizing the player's performance in the match
     * @throws IllegalArgumentException if the player or match does not exist
     */
    public String calculatePlayerPerformance(int playerId, int matchId, List<GoalEvent> events) {
        Player player = playerRepository.read(playerId);
        if (player == null) {
            throw new IllegalArgumentException("Player with ID " + playerId + " does not exist.");
        }

        Match match = matchRepository.read(matchId);
        if (match == null) {
            throw new IllegalArgumentException("Match with ID " + matchId + " does not exist.");
        }

        // Calculate goals and assists by the player in this match
        int goals = 0;
        int assists = 0;

        for (GoalEvent event : events) {
            if (event != null && event.getMatchID() == matchId && event.getPlayerID() == playerId) {
                goals++;
                assists += event.getAssists();
            }
        }

        return String.format(
                "Player %s (ID: %d) in Match %d:\nGoals: %d\nAssists: %d",
                player.getName(), playerId, matchId, goals, assists
        );
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
     * Sorts all players by their number of goals in descending order.
     *
     * @return a list of players sorted by goals
     */
    public List<Player> sortPlayersByGoals() {
        List<Player> players = new ArrayList<>();
        int id = 1;

        while (true) {
            Player player = playerRepository.read(id);
            if (player == null) {
                break;
            }
            players.add(player);
            id++;
        }

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