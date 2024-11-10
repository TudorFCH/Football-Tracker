package Presentation;

import Controller.MatchController;
import Model.Match;
import Repository.InMemoryRepository;
import Service.MatchService;

import java.util.Scanner;

public class ConsoleApp {
    public static void main(String[] args) {
        MatchController matchController = new MatchController(new MatchService(new InMemoryRepository<>()));

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Football Score & Player Stats Tracker");

        while (true) {
            System.out.println("1. Add Match");
            System.out.println("2. View Match");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            if (choice == 1) {
                System.out.println("Enter Match ID, Team1 ID, Team2 ID, Date, Location:");
                int matchID = scanner.nextInt();
                int teamId1 = scanner.nextInt();
                int teamId2 = scanner.nextInt();
                String date = scanner.next();
                String location = scanner.next();

                Match match = new Match(matchID, teamId1, teamId2, date, location);
                matchController.addMatch(match);
                System.out.println("Match added successfully!");
            } else if (choice == 2) {
                System.out.println("Enter Match ID:");
                int matchID = scanner.nextInt();
                Match match = matchController.getMatch(matchID);
                if (match != null) {
                    System.out.println("Match Summary: " + match.getSummary());
                } else {
                    System.out.println("Match not found.");
                }
            } else if (choice == 3) {
                System.out.println("Exiting application...");
                break;
            }
        }
        scanner.close();
    }
}