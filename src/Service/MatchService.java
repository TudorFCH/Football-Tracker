package Service;

import Model.Match;
import Repository.IRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides business logic for managing matches with validations and sorting.
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
        // Validate unique match ID
        if (matchRepository.read(match.getMatchID()) != null) {
            throw new IllegalArgumentException("Match ID must be unique.");
        }

        // Validate team IDs
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
     * Sorts all matches by their ID as a placeholder for date sorting.
     *
     * @return a list of matches sorted by their ID (as a replacement for date)
     */
    public List<Match> sortMatchesByID() {
        List<Match> matches = new ArrayList<>();
        int id = 1;

        // Iterate over the repository to collect all matches
        while (true) {
            Match match = matchRepository.read(id);
            if (match == null) {
                break; // No more matches
            }
            matches.add(match);
            id++;
        }

        // Manual sorting based on match ID (can replace with date sorting if implemented)
        for (int i = 0; i < matches.size(); i++) {
            for (int j = 0; j < matches.size() - i - 1; j++) {
                if (matches.get(j).getMatchID() > matches.get(j + 1).getMatchID()) {
                    Collections.swap(matches, j, j + 1); // Swap manually
                }
            }
        }
        return matches;
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