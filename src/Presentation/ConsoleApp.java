package Presentation;

import Model.*;
import Repository.*;
import Service.MatchService;
import Service.PlayerService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
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
        try {
            System.out.print("Enter Match ID: ");
            int newMatchID = getValidNonNegativeInt(scanner, "Match ID");

            System.out.print("Enter Team1 ID: ");
            int newTeamId1 = getValidNonNegativeInt(scanner, "Team 1 ID");

            System.out.print("Enter Team2 ID: ");
            int newTeamId2 = getValidNonNegativeInt(scanner, "Team 2 ID");

            if (newTeamId1 == newTeamId2) {
                System.out.println("Error: A match cannot be between the same team!");
                return;
            }

            scanner.nextLine(); // Consume newline
            System.out.print("Enter Date (YYYY-MM-DD): ");
            String newDate = scanner.nextLine();

            if (!isValidDate(newDate)) {
                System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
                return;
            }

            System.out.print("Enter Location: ");
            String newLocation = scanner.nextLine();

            if (!isValidPlaceName(newLocation)) {
                System.out.println("Error: Location can only contain letters!");
                return;
            }

            Match newMatch = new Match(newMatchID, newTeamId1, newTeamId2, newDate, newLocation);
            matchService.addMatch(newMatch);
            System.out.println("Match added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter numeric values for IDs.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    // Helper method to validate the date format
    private static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date); // This will throw an exception if the format is incorrect
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    private static void viewMatch(Scanner scanner, MatchService matchService) {
        try {
            System.out.print("Enter Match ID to view: ");
            int viewMatchID = getValidNonNegativeInt(scanner, "Match ID");

            Match matchToView = matchService.getMatch(viewMatchID);
            if (matchToView != null) {
                System.out.println("Match Details:");
                System.out.println(matchToView.getSummary());
            } else {
                System.out.println("Match not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter a valid positive number.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    private static void addPlayer(Scanner scanner, PlayerService playerService) {
        try {
            System.out.print("Enter Player Name: ");
            String newPlayerName = scanner.nextLine();

            if (!newPlayerName.matches("[a-zA-Z ]+")) {
                System.out.println("Error: Player name can only contain letters and spaces!");
                return;
            }

            System.out.print("Enter Team Name: ");
            String newTeamName = scanner.nextLine();

            if (!newTeamName.matches("[a-zA-Z ]+")) {
                System.out.println("Error: Team name can only contain letters and spaces!");
                return;
            }

            System.out.print("Enter Team ID: ");
            int newTeamID = getValidNonNegativeInt(scanner, "Team ID");

            Player newPlayer = new Player(0, newPlayerName, newTeamID);
            playerService.addPlayer(newPlayer);
            System.out.println("Player added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter the correct data types.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void viewPlayer(Scanner scanner, PlayerService playerService) {
        try {
            System.out.print("Enter Player ID to view: ");
            int viewPlayerID = getValidNonNegativeInt(scanner, "Player ID");

            Player playerToView = playerService.getPlayer(viewPlayerID);
            if (playerToView != null) {
                System.out.println("Player Details:");
                System.out.println("Name: " + playerToView.getName());
                System.out.println("Team ID: " + playerToView.getTeamID());
                System.out.println("Statistics: " + playerToView.getStatistics());
            } else {
                System.out.println("Player not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter a valid positive number.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void addEventToMatch(Scanner scanner, MatchService matchService, IRepository<Event> eventRepository) {
        System.out.print("Enter Match ID to add event: ");
        int matchID = getValidNonNegativeInt(scanner, "Match ID");
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
        try {
            System.out.print("Enter Event ID: ");
            int eventID = getValidNonNegativeInt(scanner, "Event ID");
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Time (HH:MM): ");
            String time = scanner.nextLine();
            if (!isValidMatchTime(time)) {
                System.out.println("Error: Time must be in the format HH:MM and within 00:00 to 120:00!");
                return null;
            }

            System.out.print("Enter Player ID: ");
            int playerID = scanner.nextInt();

            System.out.print("Was there an assist? (true/false): ");
            if (!scanner.hasNextBoolean()) {
                System.out.println("Error: Invalid input for assist. Please enter true or false.");
                scanner.nextLine(); // Consume invalid input
                return null;
            }
            boolean isAssist = scanner.nextBoolean();

            return new GoalEvent(eventID, time, playerID, isAssist, 0); // Assuming matchID is handled later
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter the correct data types.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
        return null; // Return null in case of an error
    }

    // Helper method to validate match time
    private static boolean isValidMatchTime(String time) {
        if (time.matches("^([0-9]{1,3}):[0-5]\\d$")) { // Match format HH:MM
            String[] parts = time.split(":");
            int minutes = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
            return minutes >= 0 && minutes <= 7200; // Time in minutes: 0 to 120:00
        }
        return false;
    }


    private static CardEvent createCardEvent(Scanner scanner) {
        try {
            System.out.print("Enter Event ID: ");
            int eventID = getValidNonNegativeInt(scanner, "Event ID");
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Time (HH:MM): ");
            String time = scanner.nextLine();
            if (!isValidMatchTime(time)) {
                System.out.println("Error: Time must be in the format HH:MM and within 00:00 to 120:00!");
                return null;
            }

            System.out.print("Enter Player ID: ");
            int playerID = getValidNonNegativeInt(scanner, "Player ID");
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Card Type (Yellow/Red): ");
            String cardType = scanner.nextLine();
            if (!cardType.equalsIgnoreCase("Yellow") && !cardType.equalsIgnoreCase("Red")) {
                System.out.println("Error: Card type must be 'Yellow' or 'Red'!");
                return null;
            }

            return new CardEvent(eventID, time, playerID, cardType, 0); // Assuming matchID is handled later
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter the correct data types.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
        return null; // Return null in case of an error
    }


    private static MinutesPlayedEvent createMinutesPlayedEvent(Scanner scanner) {
        try {
            System.out.print("Enter Event ID: ");
            int eventID = getValidNonNegativeInt(scanner, "Event ID");
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Time (HH:MM): ");
            String time = scanner.nextLine();
            if (!isValidMatchTime(time)) {
                System.out.println("Error: Time must be in the format HH:MM and within 00:00 to 120:00!");
                return null;
            }

            System.out.print("Enter Player ID: ");
            int playerID = getValidNonNegativeInt(scanner, "Player ID");

            System.out.print("Enter Minutes Played: ");
            int minutesPlayed = scanner.nextInt();
            if (minutesPlayed < 0 || minutesPlayed > 120) {
                System.out.println("Error: Minutes played must be between 0 and 120!");
                return null;
            }

            return new MinutesPlayedEvent(eventID, time, playerID, minutesPlayed, 0); // Assuming matchID is handled later
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter the correct data types.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
        return null; // Return null in case of an error
    }

    private static void updatePlayerStatistics(Scanner scanner, PlayerService playerService) {
        try {
            System.out.print("Enter Player ID to update: ");
            int updatePlayerID = getValidNonNegativeInt(scanner, "Player ID");
            Player playerToUpdate = playerService.getPlayer(updatePlayerID);

            if (playerToUpdate != null) {
                System.out.print("Enter New Goals: ");
                int goals = getValidNonNegativeInt(scanner, "Goals");

                System.out.print("Enter New Assists: ");
                int assists = getValidNonNegativeInt(scanner, "Assists");

                System.out.print("Enter New Yellow Cards: ");
                int yellowCards = getValidNonNegativeInt(scanner, "Yellow Cards");

                System.out.print("Enter New Red Cards: ");
                int redCards = getValidNonNegativeInt(scanner, "Red Cards");

                System.out.print("Enter New Minutes Played: ");
                int minutesPlayed = getValidNonNegativeInt(scanner, "Minutes Played");

                // Update the player's statistics
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
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter valid numbers.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Helper method to validate non-negative integer input
    private static int getValidNonNegativeInt(Scanner scanner, String fieldName) {
        while (true) {
            try {
                int value = scanner.nextInt();
                if (value < 0) {
                    System.out.println("Error: " + fieldName + " cannot be negative. Please enter a valid number.");
                } else {
                    return value;
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input for " + fieldName + ". Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }


    private static void deletePlayer(Scanner scanner, PlayerService playerService) {
        try {
            System.out.print("Enter Player ID to delete: ");
            int deletePlayerID = getValidNonNegativeInt(scanner, "Player ID");
            playerService.deletePlayer(deletePlayerID);
            System.out.println("Player deleted successfully!");
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter a valid positive number.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void comparePlayers(Scanner scanner, PlayerService playerService) {
        try {
            System.out.print("Enter ID of the first player: ");
            int playerId1 = getValidNonNegativeInt(scanner, "First player ID");

            System.out.print("Enter ID of the second player: ");
            int playerId2 = getValidNonNegativeInt(scanner, "Second player ID");

            try {
                playerService.betterGoalscorer(playerId1, playerId2);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter a valid positive number.");
            scanner.nextLine(); // Clear invalid input
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
        try {
            System.out.print("Enter the minimum number of goals: ");
            int minGoals = getValidNonNegativeInt(scanner, "Minimum goals");

            System.out.println("Players with more than " + minGoals + " goals:");
            List<Player> filteredPlayers = playerService.filterPlayersByGoals(minGoals);
            for (Player filteredPlayer : filteredPlayers) {
                System.out.println("Name: " + filteredPlayer.getName() + ", Goals: " + filteredPlayer.getGoals());
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter a valid positive number.");
            scanner.nextLine(); // Clear invalid input
        }
    }

    private static void filterMatchesByLocation(Scanner scanner, MatchService matchService) {
        try {
            System.out.print("Enter the location to filter by: ");
            String filterLocation = scanner.nextLine();

            if (!isValidPlaceName(filterLocation)) {
                System.out.println("Error: Location must contain only letters.");
                return;
            }

            System.out.println("Matches played at " + filterLocation + ":");
            List<Match> filteredMatches = matchService.filterMatchesByLocation(filterLocation);
            for (Match filteredMatch : filteredMatches) {
                System.out.println(filteredMatch.getSummary());
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Helper method to validate place name (letters only)
    private static boolean isValidPlaceName(String placeName) {
        return placeName.matches("^[a-zA-Z]+$");
    }


    private static void bestPlayerInMatch(Scanner scanner, MatchService matchService, PlayerService playerService) {
        try {
            System.out.println("Enter match ID:");
            int matchID = getValidNonNegativeInt(scanner, "Match ID");

            try {
                Player bestPlayer = matchService.determineBestPlayer(matchID, playerService);
                System.out.printf("Best player of the match is %s (Player ID: %d)%n",
                        bestPlayer.getName(), bestPlayer.getPlayerID());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter a valid positive number.");
            scanner.nextLine(); // Clear invalid input
        }
    }
}
