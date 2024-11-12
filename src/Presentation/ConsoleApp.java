package Presentation;

import Controller.MatchController;
import Model.*;
import Repository.FileRepository;
import Repository.PlayerRepository;
import Service.MatchService;

import java.util.Scanner;

public class ConsoleApp {
    public static void main(String[] args) {
        MatchController matchController = new MatchController(new MatchService(new FileRepository("matches.txt")));
        PlayerRepository playerRepository = new PlayerRepository();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Football Score & Player Stats Tracker");

        while (true) {
            System.out.println("1. Add Match");
            System.out.println("2. View Match");
            System.out.println("3. Add Event to Match");
            System.out.println("4. Add Player");
            System.out.println("5. View Player Statistics");
            System.out.println("6. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice == 1) {
                // Adding a new match
                System.out.println("Enter Team1 ID, Team2 ID, Date, Location:");
                int teamId1 = scanner.nextInt();
                int teamId2 = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                String date = scanner.nextLine();
                String location = scanner.nextLine();

                Match match = new Match(0, teamId1, teamId2, date, location);
                matchController.addMatch(match);
                System.out.println("Match added successfully and saved to file!");
            } else if (choice == 2) {
                // Viewing a match
                System.out.println("Enter Match ID:");
                int matchID = scanner.nextInt();
                Match match = matchController.getMatch(matchID);
                if (match != null) {
                    System.out.println("Match Summary:\n" + match.getSummary());
                } else {
                    System.out.println("Match not found.");
                }
            } else if (choice == 3) {
                // Adding an event to an existing match
                System.out.println("Enter Match ID to add event:");
                int matchID = scanner.nextInt();
                Match match = matchController.getMatch(matchID);
                if (match != null) {
                    System.out.println("Select Event Type:\n1. Goal\n2. Card\n3. Minutes Played");
                    int eventType = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    System.out.println("Enter Player ID:");
                    int playerID = scanner.nextInt();
                    Player player = playerRepository.getPlayer(playerID);
                    if (player == null) {
                        System.out.println("Player not found.");
                        continue;
                    }
                    System.out.println("Enter Time:");
                    String time = scanner.nextLine();

                    Event event = null;
                    if (eventType == 1) {
                        System.out.println("Is this an assist? (true/false):");
                        boolean isAssist = scanner.nextBoolean();
                        event = new GoalEvent(0, time, playerID, isAssist);
                        player.addGoal();
                        if (isAssist) player.addAssist();
                    } else if (eventType == 2) {
                        System.out.println("Enter Card Type (Yellow/Red):");
                        String cardType = scanner.nextLine();
                        event = new CardEvent(0, time, playerID, cardType);
                        if (cardType.equalsIgnoreCase("Yellow")) {
                            player.addYellowCard();
                        } else if (cardType.equalsIgnoreCase("Red")) {
                            player.addRedCard();
                        }
                    } else if (eventType == 3) {
                        System.out.println("Enter Minutes Played:");
                        int minutesPlayed = scanner.nextInt();
                        event = new MinutesPlayedEvent(0, time, playerID, minutesPlayed);
                        player.addMinutesPlayed(minutesPlayed);
                    }

                    if (event != null) {
                        match.addEvent(event);
                        playerRepository.updatePlayer(player); // Save updated player stats to repository
                        System.out.println("Event added successfully, and player statistics updated!");
                    }
                } else {
                    System.out.println("Match not found.");
                }
            } else if (choice == 4) {
                // Adding a player
                System.out.println("Enter Player Name:");
                String playerName = scanner.nextLine();
                System.out.println("Enter Team ID:");
                int teamID = scanner.nextInt();
                Player player = new Player(0, playerName, teamID);
                playerRepository.addPlayer(player);
                System.out.println("Player added successfully!");
            } else if (choice == 5) {
                // Viewing player statistics
                System.out.println("Enter Player ID:");
                int playerID = scanner.nextInt();
                Player player = playerRepository.getPlayer(playerID);
                if (player != null) {
                    System.out.println("Player: " + player.getName() + "\nStatistics: " + player.getStatistics());
                } else {
                    System.out.println("Player not found.");
                }
            } else if (choice == 6) {
                System.out.println("Exiting application...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}