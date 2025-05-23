package com.std_data_mgmt.app.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.std_data_mgmt.app.entity.DemoEntity;

import jakarta.annotation.PostConstruct;

@Repository
public class DemoRepository {
    private final Map<Long, DemoEntity> store = new HashMap<>();
    private long idCounter = 1;

    // Prepopulate using @PostConstruct method to ensure Spring finishes wiring
    @PostConstruct
    public void init() {
        save(new DemoEntity(null, "Dummy Data 1"));
        save(new DemoEntity(null, "Dummy Data 2"));
        save(new DemoEntity(null, "Dummy Data 3"));
    }

    public List<DemoEntity> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<DemoEntity> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public DemoEntity save(DemoEntity entity) {
        if (entity.getId() == null) {
            entity.setId(idCounter++);
        }
        store.put(entity.getId(), entity);
        return entity;
    }

    public void deleteById(Long id) {
        store.remove(id);
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }
}
