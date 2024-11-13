package Service;

import Model.Match;
import Repository.IRepository;

/**
 * Provides business logic for managing matches with validations.
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
}