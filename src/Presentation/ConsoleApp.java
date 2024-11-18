package Presentation;

import Model.Match;
import Model.Player;
import Repository.FilePlayerRepository;
import Repository.FileRepository;
import Service.MatchService;
import Service.PlayerService;

import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Football Score & Player Stats Tracker application.
 */
public class ConsoleApp {
    public static void main(String[] args) {
        // Initialize repositories and services
        // Initialize file repositories
        FilePlayerRepository playerRepository = new FilePlayerRepository("players.txt");
        FileRepository matchRepository = new FileRepository("matches.txt");

        // Initialize the services with the appropriate repositories
        PlayerService playerService = new PlayerService(playerRepository);
        MatchService matchService = new MatchService(matchRepository);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Football Score & Player Stats Tracker!");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Match");
            System.out.println("2. View Match");
            System.out.println("3. Add Player");
            System.out.println("4. View Player");
            System.out.println("5. Update Player Statistics");
            System.out.println("6. Delete Player");
            System.out.println("7. Compare Players (Better Goalscorer)");
            System.out.println("8. Sort Matches by ID");
            System.out.println("9. Sort Players by Goals");
            System.out.println("10. Filter Players by Goals");
            System.out.println("11. Filter Matches by Location");
            System.out.println("12. Exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1: // Add Match
                        System.out.print("Enter Match ID: ");
                        int newMatchID = scanner.nextInt();
                        System.out.print("Enter Team1 ID: ");
                        int newTeamId1 = scanner.nextInt();
                        System.out.print("Enter Team2 ID: ");
                        int newTeamId2 = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter Date (YYYY-MM-DD): ");
                        String newDate = scanner.nextLine();
                        System.out.print("Enter Location: ");
                        String newLocation = scanner.nextLine();

                        Match newMatch = new Match(newMatchID, newTeamId1, newTeamId2, newDate, newLocation);
                        matchService.addMatch(newMatch);
                        System.out.println("Match added successfully!");
                        break;

                    case 2: // View Match
                        System.out.print("Enter Match ID to view: ");
                        int viewMatchID = scanner.nextInt();
                        Match matchToView = matchService.getMatch(viewMatchID);
                        if (matchToView != null) {
                            System.out.println("Match Details:");
                            System.out.println(matchToView.getSummary());
                        } else {
                            System.out.println("Match not found.");
                        }
                        break;

                    case 3: // Add Player
                        System.out.print("Enter Player Name: ");
                        String newPlayerName = scanner.nextLine();
                        System.out.print("Enter Team ID: ");
                        int newTeamID = scanner.nextInt();

                        Player newPlayer = new Player(0, newPlayerName, newTeamID);
                        playerService.addPlayer(newPlayer);
                        System.out.println("Player added successfully!");
                        break;

                    case 4: // View Player
                        System.out.print("Enter Player ID to view: ");
                        int viewPlayerID = scanner.nextInt();
                        Player playerToView = playerService.getPlayer(viewPlayerID);
                        if (playerToView != null) {
                            System.out.println("Player Details:");
                            System.out.println("Name: " + playerToView.getName());
                            System.out.println("Team ID: " + playerToView.getTeamID());
                            System.out.println("Statistics: " + playerToView.getStatistics());
                        } else {
                            System.out.println("Player not found.");
                        }
                        break;

                    case 5: // Update Player Statistics
                        System.out.print("Enter Player ID to update: ");
                        int updatePlayerID = scanner.nextInt();
                        Player playerToUpdate = playerService.getPlayer(updatePlayerID);
                        if (playerToUpdate != null) {
                            System.out.print("Enter New Goals: ");
                            int goals = scanner.nextInt();
                            System.out.print("Enter New Assists: ");
                            int assists = scanner.nextInt();
                            System.out.print("Enter New Yellow Cards: ");
                            int yellowCards = scanner.nextInt();
                            System.out.print("Enter New Red Cards: ");
                            int redCards = scanner.nextInt();
                            System.out.print("Enter New Minutes Played: ");
                            int minutesPlayed = scanner.nextInt();

                            playerToUpdate.addGoal(goals - playerToUpdate.getGoals());
                            playerToUpdate.addAssist(assists - playerToUpdate.getAssists());
                            playerToUpdate.addYellowCard(yellowCards - playerToUpdate.getYellowCards());
                            playerToUpdate.addRedCard(redCards - playerToUpdate.getRedCards());
                            playerToUpdate.addMinutesPlayed(minutesPlayed - playerToUpdate.getMinutesPlayed());

                            playerService.updatePlayer(playerToUpdate);
                            System.out.println("Player statistics updated successfully!");
                        } else {
                            System.out.println("Player not found.");
                        }
                        break;

                    case 6: // Delete Player
                        System.out.print("Enter Player ID to delete: ");
                        int deletePlayerID = scanner.nextInt();
                        playerService.deletePlayer(deletePlayerID);
                        System.out.println("Player deleted successfully!");
                        break;

                    case 7: // Compare Players
                        System.out.print("Enter ID of the first player: ");
                        int playerId1 = scanner.nextInt();
                        System.out.print("Enter ID of the second player: ");
                        int playerId2 = scanner.nextInt();

                        try {
                            playerService.betterGoalscorer(playerId1, playerId2);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 8: // Sort Matches by ID
                        System.out.println("Matches sorted by ID:");
                        List<Match> sortedMatches = matchService.sortMatchesByID();
                        for (Match sortedMatch : sortedMatches) {
                            System.out.println(sortedMatch.getSummary());
                        }
                        break;

                    case 9: // Sort Players by Goals
                        System.out.println("Players sorted by goals:");
                        List<Player> sortedPlayers = playerService.sortPlayersByGoals();
                        for (Player sortedPlayer : sortedPlayers) {
                            System.out.println("Name: " + sortedPlayer.getName() + ", Goals: " + sortedPlayer.getGoals());
                        }
                        break;

                    case 10: // Filter Players by Goals
                        System.out.print("Enter the minimum number of goals: ");
                        int minGoals = scanner.nextInt();
                        System.out.println("Players with more than " + minGoals + " goals:");
                        List<Player> filteredPlayers = playerService.filterPlayersByGoals(minGoals);
                        for (Player filteredPlayer : filteredPlayers) {
                            System.out.println("Name: " + filteredPlayer.getName() + ", Goals: " + filteredPlayer.getGoals());
                        }
                        break;

                    case 11: // Filter Matches by Location
                        System.out.print("Enter the location to filter by: ");
                        String filterLocation = scanner.nextLine();
                        System.out.println("Matches played at " + filterLocation + ":");
                        List<Match> filteredMatches = matchService.filterMatchesByLocation(filterLocation);
                        for (Match filteredMatch : filteredMatches) {
                            System.out.println(filteredMatch.getSummary());
                        }
                        break;

                    case 12: // Exit
                        System.out.println("Exiting the application. Goodbye!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }
}