package Test;

import Model.*;
import Repository.*;
import Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {

    private PlayerService playerService;
    private MatchService matchService;
    private InMemoryRepository<Player> playerRepository;
    private InMemoryRepository<Match> matchRepository;

    @BeforeEach
    public void setup() {
        playerRepository = new InMemoryRepository<>();
        matchRepository = new InMemoryRepository<>();
        playerService = new PlayerService(playerRepository);
        matchService = new MatchService(matchRepository);
    }

    @Test
    public void testAddAndRetrievePlayer() {
        Player player = new Player(1, "John Doe", 1);
        playerService.addPlayer(player);
        Player retrievedPlayer = playerService.getPlayer(player.getPlayerID());
        assertNotNull(retrievedPlayer);
        assertEquals("John Doe", retrievedPlayer.getName());
    }

    @Test
    public void testAddMatchAndRetrieve() {
        Match match = new Match(1, 1, 2, "2024-11-10", "Stadium");
        matchService.addMatch(match);
        Match retrievedMatch = matchService.getMatch(match.getMatchID());
        assertNotNull(retrievedMatch);
        assertEquals("Stadium", retrievedMatch.getLocation());
    }

    @Test
    public void testDetermineBestPlayer() {
        Player player1 = new Player(1, "Player One", 1);
        Player player2 = new Player(2, "Player Two", 1);
        playerService.addPlayer(player1);
        playerService.addPlayer(player2);

        Match match = new Match(1, 1, 2, "2024-11-10", "Stadium");
        matchService.addMatch(match);

        GoalEvent goalEvent = new GoalEvent(1, "10:00", 1, true, 1);
        match.addEvent(goalEvent);

        Player bestPlayer = matchService.determineBestPlayer(match.getMatchID(), playerService);
        assertNotNull(bestPlayer);
        assertEquals(player1.getPlayerID(), bestPlayer.getPlayerID());
    }

    @Test
    public void testFilterMatchesByLocation() {
        Match match1 = new Match(1, 1, 2, "2024-11-10", "Stadium");
        Match match2 = new Match(2, 1, 2, "2024-11-11", "Arena");
        matchService.addMatch(match1);
        matchService.addMatch(match2);

        List<Match> matches = matchService.filterMatchesByLocation("Stadium");
        assertEquals(1, matches.size());
        assertEquals("Stadium", matches.get(0).getLocation());
    }

    @Test
    public void testInvalidMatchAddition() {
        Match match = new Match(1, 1, 1, "2024-11-10", "Stadium");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matchService.addMatch(match);
        });
        assertEquals("A match cannot occur between the same team.", exception.getMessage());
    }

    @Test
    public void testSortPlayersByGoals() {
        Player player1 = new Player(1, "Player One", 1);
        Player player2 = new Player(2, "Player Two", 1);
        player1.addGoal(3);
        player2.addGoal(5);

        playerService.addPlayer(player1);
        playerService.addPlayer(player2);

        List<Player> sortedPlayers = playerService.sortPlayersByGoals();
        assertEquals(2, sortedPlayers.size());
        assertEquals(player2.getPlayerID(), sortedPlayers.get(0).getPlayerID());
    }
}