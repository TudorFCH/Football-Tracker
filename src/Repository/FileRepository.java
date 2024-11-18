package Repository;

import Model.Match;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public List<Match> getAllMatches() {
        List<Match> matches = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("matches.txt"))) {
            matches = (List<Match>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading matches: " + e.getMessage());
        }
        return matches;
    }

    // Save matches back to the file
    public void saveMatches(List<Match> matches) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("matches.txt"))) {
            oos.writeObject(matches);
        } catch (IOException e) {
            System.out.println("Error saving matches: " + e.getMessage());
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
