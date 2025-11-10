package com.sistemas.controllers;
import com.sistemas.dtos.StablishmentDTO;
import com.sistemas.services.impl.StablishmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stablishments")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN','ADMINISTRATIVO')")
public class StablishmentController {


    @Autowired
    private StablishmentServiceImpl service;

    @PostMapping
    public ResponseEntity<StablishmentDTO> create(@RequestBody StablishmentDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<StablishmentDTO>> getAll() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StablishmentDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StablishmentDTO> update(@PathVariable Integer id, @RequestBody StablishmentDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
