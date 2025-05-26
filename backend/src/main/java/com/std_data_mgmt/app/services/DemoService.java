package com.std_data_mgmt.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.std_data_mgmt.app.entities.DemoEntity;
import com.std_data_mgmt.app.repositories.DemoRepository;

@Service
public class DemoService {
    private final DemoRepository demoRepository;

    public DemoService(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    public List<DemoEntity> getAll() {
        return demoRepository.findAll();
    }

    public Optional<DemoEntity> getById(Long id) {
        return demoRepository.findById(id);
    }

    public DemoEntity create(DemoEntity entity) {
        return demoRepository.save(entity);
    }

    public Optional<DemoEntity> update(Long id, DemoEntity entity) {
        if (demoRepository.existsById(id)) {
            entity.setId(id);
            return Optional.of(demoRepository.save(entity));
        } else {
            return Optional.empty();
        }
    }

    public boolean delete(Long id) {
        if (demoRepository.existsById(id)) {
            demoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
