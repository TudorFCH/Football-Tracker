package Service;

import Model.Match;
import Repository.IRepository;

public class MatchService {
    private IRepository<Match> matchRepository;

    public MatchService(IRepository<Match> matchRepository) {
        this.matchRepository = matchRepository;
    }

    public void addMatch(Match match) {
        matchRepository.create(match);
    }

    public Match getMatch(int id) {
        return matchRepository.read(id);
    }
}