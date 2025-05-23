package com.std_data_mgmt.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.std_data_mgmt.app.entity.DemoEntity;
import com.std_data_mgmt.app.service.DemoService;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping
    public List<DemoEntity> getAll() {
        return demoService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemoEntity> getById(@PathVariable Long id) {
        return demoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DemoEntity create(@RequestBody DemoEntity entity) {
        return demoService.create(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DemoEntity> update(@PathVariable Long id, @RequestBody DemoEntity entity) {
        return demoService.update(id, entity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return demoService.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
