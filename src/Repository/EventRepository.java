package Repository;

import Model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRepository implements IRepository<Event> {
    private Map<Integer, Event> eventStorage = new HashMap<>();
    private int currentEventID = 1;

    @Override
    public void create(Event event) {
        event.setEventID(currentEventID++);
        eventStorage.put(event.getEventID(), event);
    }

    @Override
    public Event read(int id) {
        return eventStorage.get(id);
    }

    @Override
    public void update(Event event) {
        if (eventStorage.containsKey(event.getEventID())) {
            eventStorage.put(event.getEventID(), event);
        }
    }

    @Override
    public void delete(int id) {
        eventStorage.remove(id);
    }

}
