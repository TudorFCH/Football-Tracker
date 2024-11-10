package Controller;

import Model.Match;
import Service.MatchService;

public class MatchController {
    private MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    public void addMatch(Match match) {
        matchService.addMatch(match);
    }

    public Match getMatch(int id) {
        return matchService.getMatch(id);
    }
}