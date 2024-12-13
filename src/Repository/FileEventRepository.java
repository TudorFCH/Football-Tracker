package Repository;

import Model.Event;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileEventRepository implements IRepository<Event> {
    private final String filename;
    private final List<Event> events = new ArrayList<>();

    public FileEventRepository(String filename) {
        this.filename = filename;
        loadFromFile();
    }

    @Override
    public void create(Event event) {
        events.add(event);
        saveToFile();
    }

    @Override
    public Event read(int id) {
        return events.stream().filter(e -> e.getEventID() == id).findFirst().orElse(null);
    }

    @Override
    public void update(Event event) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventID() == event.getEventID()) {
                events.set(i, event);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        events.removeIf(e -> e.getEventID() == id);
        saveToFile();
    }


    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(events);
        } catch (IOException e) {
            System.err.println("Error saving events to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<Event> loadedEvents = (List<Event>) ois.readObject();
            events.addAll(loadedEvents);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading events from file: " + e.getMessage());
        }
    }
}
