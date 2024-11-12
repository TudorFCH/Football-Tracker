package Repository;

import Model.Match;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileRepository implements IRepository<Match> {
    private final String filename;
    private Map<Integer, Match> storage = new HashMap<>();
    private int currentId = 1;

    public FileRepository(String filename) {
        this.filename = filename;
        loadFromFile();
    }

    @Override
    public void create(Match match) {
        match.setMatchID(currentId++);
        storage.put(match.getMatchID(), match);
        saveToFile();
    }

    @Override
    public Match read(int id) {
        return storage.get(id);
    }

    @Override
    public void update(Match match) {
        storage.put(match.getMatchID(), match);
        saveToFile();
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
        saveToFile();
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Match match : storage.values()) {
                writer.println(match.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Match match = Match.fromFileString(line);
                storage.put(match.getMatchID(), match);
                currentId = Math.max(currentId, match.getMatchID() + 1);
            }
        } catch (IOException e) {
            System.out.println("Error loading data from file: " + e.getMessage());
        }
    }
}