package repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<T> implements IRepository<T> {
    private Map<Integer, T> storage = new HashMap<>();
    private int currentId = 1;

    @Override
    public void create(T entity) {
        storage.put(currentId++, entity);
    }

    @Override
    public T read(int id) {
        return storage.get(id);
    }

    @Override
    public void update(T entity) {
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}