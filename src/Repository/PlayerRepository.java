package Repository;

import Model.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerRepository {
    private Map<Integer, Player> playerStorage = new HashMap<>();
    private int currentPlayerID = 1;

    public void addPlayer(Player player) {
        player.setPlayerID(currentPlayerID++);
        playerStorage.put(player.getPlayerID(), player);
    }

    public Player getPlayer(int playerID) {
        return playerStorage.get(playerID);
    }

    public void updatePlayer(Player player) {
        playerStorage.put(player.getPlayerID(), player);
    }

    public void deletePlayer(int playerID) {
        playerStorage.remove(playerID);
    }
}