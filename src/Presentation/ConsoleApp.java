package Presentation;

import Model.*;
import Repository.*;
import Service.MatchService;
import Service.PlayerService;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Football Score & Player Stats Tracker!");
        System.out.println("Select storage type:");
        System.out.println("1. In-Memory");
        System.out.println("2. File");
        System.out.println("3. Database");

        System.out.print("Your choice: ");
        int storageChoice = scanner.nextInt();
        scanner.nextLine();

        IRepository<Player> playerRepository;
        IRepository<Match> matchRepository;
        IRepository<Event> eventRepository;

        switch (storageChoice) {
            case 1:
                playerRepository = new InMemoryRepository<>();
                matchRepository = new InMemoryRepository<>();
                eventRepository = new InMemoryRepository<>();
                break;
            case 2:
                playerRepository = new FilePlayerRepository("players.txt");
                matchRepository = new FileRepository("matches.txt");
                eventRepository = new FileEventRepository("events.txt");
                break;
            case 3:
                playerRepository = new PlayerDatabaseRepository();
                matchRepository = new DatabaseMatchRepository();
                eventRepository = new EventDatabaseRepository();
                break;
            default:
                System.out.println("Invalid choice. Exiting.");
                scanner.close();
                return;
        }


        PlayerService playerService = new PlayerService(playerRepository);
        MatchService matchService = new MatchService(matchRepository);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Match");
            System.out.println("2. View Match");
            System.out.println("3. Add Player");
            System.out.println("4. View Player");
            System.out.println("5. Add Event to Match");
            System.out.println("6. Update Player Statistics");
            System.out.println("7. Delete Player");
            System.out.println("8. Compare Players (Better Goalscorer)");
            System.out.println("9. Sort Matches by ID");
            System.out.println("10. Sort Players by Goals");
            System.out.println("11. Filter Players by Goals");
            System.out.println("12. Filter Matches by Location");
            System.out.println("13. Best player in certain match");
            System.out.println("14. Exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        addMatch(scanner, matchService);
                        break;
                    case 2:
                        viewMatch(scanner, matchService);
                        break;
                    case 3:
                        addPlayer(scanner, playerService);
                        break;
                    case 4:
                        viewPlayer(scanner, playerService);
                        break;
                    case 5:
                        addEventToMatch(scanner, matchService, eventRepository);
                        break;
                    case 6:
                        updatePlayerStatistics(scanner, playerService);
                        break;
                    case 7:
                        deletePlayer(scanner, playerService);
                        break;
                    case 8:
                        comparePlayers(scanner, playerService);
                        break;
                    case 9:
                        sortMatchesByID(matchService);
                        break;
                    case 10:
                        sortPlayersByGoals(playerService);
                        break;
                    case 11:
                        filterPlayersByGoals(scanner, playerService);
                        break;
                    case 12:
                        filterMatchesByLocation(scanner, matchService);
                        break;
                    case 13:
                        bestPlayerInMatch(scanner, matchService, playerService);
                        break;
                    case 14:
                        System.out.println("Exiting the application. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    private static void addMatch(Scanner scanner, MatchService matchService) {
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
    }

    private static void viewMatch(Scanner scanner, MatchService matchService) {
        System.out.print("Enter Match ID to view: ");
        int viewMatchID = scanner.nextInt();
        Match matchToView = matchService.getMatch(viewMatchID);
        if (matchToView != null) {
            System.out.println("Match Details:");
            System.out.println(matchToView.getSummary());
        } else {
            System.out.println("Match not found.");
        }
    }

    private static void addPlayer(Scanner scanner, PlayerService playerService) {
        System.out.print("Enter Player Name: ");
        String newPlayerName = scanner.nextLine();
        System.out.print("Enter Team ID: ");
        int newTeamID = scanner.nextInt();

        Player newPlayer = new Player(0, newPlayerName, newTeamID);
        playerService.addPlayer(newPlayer);
        System.out.println("Player added successfully!");
    }

    private static void viewPlayer(Scanner scanner, PlayerService playerService) {
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
    }

    private static void addEventToMatch(Scanner scanner, MatchService matchService, IRepository<Event> eventRepository) {
        System.out.print("Enter Match ID to add event: ");
        int matchID = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Match match = matchService.getMatch(matchID);
        if (match == null) {
            System.out.println("Match not found.");
            return;
        }

        System.out.println("Choose Event Type: ");
        System.out.println("1. Goal Event");
        System.out.println("2. Card Event");
        System.out.println("3. Minutes Played Event");
        int eventChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Event event = null;
        switch (eventChoice) {
            case 1:
                event = createGoalEvent(scanner);
                break;
            case 2:
                event = createCardEvent(scanner);
                break;
            case 3:
                event = createMinutesPlayedEvent(scanner);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (event != null) {
            eventRepository.create(event); // Save event to repository
            matchService.addEventToMatch(matchID, event); // Add event to match
            System.out.println("Event added successfully.");
        }
    }


    private static GoalEvent createGoalEvent(Scanner scanner) {
        System.out.print("Enter Event ID: ");
        int eventID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Time (HH:MM): ");
        String time = scanner.nextLine();
        System.out.print("Enter Player ID: ");
        int playerID = scanner.nextInt();
        System.out.print("Was there an assist? (true/false): ");
        boolean isAssist = scanner.nextBoolean();
        return new GoalEvent(eventID, time, playerID, isAssist, 0); // Assuming matchID is handled later
    }

    private static CardEvent createCardEvent(Scanner scanner) {
        System.out.print("Enter Event ID: ");
        int eventID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Time (HH:MM): ");
        String time = scanner.nextLine();
        System.out.print("Enter Player ID: ");
        int playerID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Card Type (Yellow/Red): ");
        String cardType = scanner.nextLine();
        return new CardEvent(eventID, time, playerID, cardType, 0); // Assuming matchID is handled later
    }

    private static MinutesPlayedEvent createMinutesPlayedEvent(Scanner scanner) {
        System.out.print("Enter Event ID: ");
        int eventID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Time (HH:MM): ");
        String time = scanner.nextLine();
        System.out.print("Enter Player ID: ");
        int playerID = scanner.nextInt();
        System.out.print("Enter Minutes Played: ");
        int minutesPlayed = scanner.nextInt();
        return new MinutesPlayedEvent(eventID, time, playerID, minutesPlayed, 0); // Assuming matchID is handled later
    }

    private static void updatePlayerStatistics(Scanner scanner, PlayerService playerService) {
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
    }

    private static void deletePlayer(Scanner scanner, PlayerService playerService) {
        System.out.print("Enter Player ID to delete: ");
        int deletePlayerID = scanner.nextInt();
        playerService.deletePlayer(deletePlayerID);
        System.out.println("Player deleted successfully!");
    }

    private static void comparePlayers(Scanner scanner, PlayerService playerService) {
        System.out.print("Enter ID of the first player: ");
        int playerId1 = scanner.nextInt();
        System.out.print("Enter ID of the second player: ");
        int playerId2 = scanner.nextInt();

        try {
            playerService.betterGoalscorer(playerId1, playerId2);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void sortMatchesByID(MatchService matchService) {
        System.out.println("Matches sorted by ID:");
        List<Match> sortedMatches = matchService.sortMatchesByID();
        for (Match sortedMatch : sortedMatches) {
            System.out.println(sortedMatch.getSummary());
        }
    }

    private static void sortPlayersByGoals(PlayerService playerService) {
        System.out.println("Players sorted by goals:");
        List<Player> sortedPlayers = playerService.sortPlayersByGoals();
        for (Player sortedPlayer : sortedPlayers) {
            System.out.println("Name: " + sortedPlayer.getName() + ", Goals: " + sortedPlayer.getGoals());
        }
    }

    private static void filterPlayersByGoals(Scanner scanner, PlayerService playerService) {
        System.out.print("Enter the minimum number of goals: ");
        int minGoals = scanner.nextInt();
        System.out.println("Players with more than " + minGoals + " goals:");
        List<Player> filteredPlayers = playerService.filterPlayersByGoals(minGoals);
        for (Player filteredPlayer : filteredPlayers) {
            System.out.println("Name: " + filteredPlayer.getName() + ", Goals: " + filteredPlayer.getGoals());
        }
    }

    private static void filterMatchesByLocation(Scanner scanner, MatchService matchService) {
        System.out.print("Enter the location to filter by: ");
        String filterLocation = scanner.nextLine();
        System.out.println("Matches played at " + filterLocation + ":");
        List<Match> filteredMatches = matchService.filterMatchesByLocation(filterLocation);
        for (Match filteredMatch : filteredMatches) {
            System.out.println(filteredMatch.getSummary());
        }
    }

    private static void bestPlayerInMatch(Scanner scanner, MatchService matchService, PlayerService playerService) {
        System.out.println("Enter match ID:");
        int matchID = scanner.nextInt();
        try {
            Player bestPlayer = matchService.determineBestPlayer(matchID, playerService);
            System.out.printf("Best player of the match is %s (Player ID: %d)%n",
                    bestPlayer.getName(), bestPlayer.getPlayerID());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
