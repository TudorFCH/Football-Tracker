package Service;

import Model.Match;
import Repository.IRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides business logic for managing matches with validations, sorting, and filtering.
 */
public class MatchService {
    private IRepository<Match> matchRepository;

    /**
     * Constructs a MatchService with the specified repository.
     *
     * @param matchRepository the repository for managing matches
     */
    public MatchService(IRepository<Match> matchRepository) {
        this.matchRepository = matchRepository;
    }

    /**
     * Adds a new match to the repository with validation.
     *
     * @param match the match to add
     * @throws IllegalArgumentException if validation fails
     */
    public void addMatch(Match match) {
        if (matchRepository.read(match.getMatchID()) != null) {
            throw new IllegalArgumentException("Match ID must be unique.");
        }

        if (match.getTeamId1() == match.getTeamId2()) {
            throw new IllegalArgumentException("A match cannot occur between the same team.");
        }

        matchRepository.create(match);
    }

    /**
     * Retrieves a match by its ID.
     *
     * @param id the unique ID of the match
     * @return the Match with the specified ID, or null if not found
     */
    public Match getMatch(int id) {
        return matchRepository.read(id);
    }

    /**
     * Sorts all matches by their ID.
     *
     * @return a list of matches sorted by their ID
     */
    public List<Match> sortMatchesByID() {
        List<Match> matches = new ArrayList<>();
        int id = 1;

        while (true) {
            Match match = matchRepository.read(id);
            if (match == null) {
                break;
            }
            matches.add(match);
            id++;
        }

        for (int i = 0; i < matches.size(); i++) {
            for (int j = 0; j < matches.size() - i - 1; j++) {
                if (matches.get(j).getMatchID() > matches.get(j + 1).getMatchID()) {
                    Collections.swap(matches, j, j + 1);
                }
            }
        }
        return matches;
    }

    /**
     * Filters matches that were played at the specified location.
     *
     * @param location the location to filter by
     * @return a list of matches played at the given location
     */
    public List<Match> filterMatchesByLocation(String location) {
        List<Match> filteredMatches = new ArrayList<>();
        int id = 1;

        // Iterate through all match IDs
        while (true) {
            Match match = matchRepository.read(id);
            if (match == null) {
                break; // Stop if no match exists for the ID
            }
            // Directly access the location attribute
            if (match.location.equalsIgnoreCase(location)) { // Assuming `location` is public or has a getter
                filteredMatches.add(match);
            }
            id++;
        }

        return filteredMatches;
    }

    /**
     * Deletes a match by its ID.
     *
     * @param id the unique ID of the match to delete
     * @throws IllegalArgumentException if the match ID does not exist
     */
    public void deleteMatch(int id) {
        if (matchRepository.read(id) == null) {
            throw new IllegalArgumentException("Match ID not found. Cannot delete.");
        }
        matchRepository.delete(id);
    }
}