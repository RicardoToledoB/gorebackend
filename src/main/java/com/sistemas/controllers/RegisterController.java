package com.sistemas.controllers;

import com.sistemas.dtos.RegisterDTO;
import com.sistemas.services.impl.RegisterServiceImpl;
import com.sistemas.services.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/registers")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN','ADMINISTRATIVO')")
public class RegisterController {
    @Autowired
    private RegisterServiceImpl service;

    @PostMapping
    public ResponseEntity<RegisterDTO> create(@RequestBody RegisterDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegisterDTO> update(@PathVariable Integer id, @RequestBody RegisterDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/getAllPaginated")
    public ResponseEntity<Page<RegisterDTO>> getAllPaginated(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        // Si no se envía "search", el parámetro será null, y el repository lo ignora.
        return ResponseEntity.ok(service.getAllPaginated(search, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/all")
    public ResponseEntity<List<RegisterDTO>> getAll() {
        return ResponseEntity.ok(service.listAll());
    }



    /* SOFT DELETE */
    @GetMapping
    public ResponseEntity<List<RegisterDTO>> listActive() {
        return ResponseEntity.ok(service.listActive());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<RegisterDTO>> listDeleted() {
        return ResponseEntity.ok(service.listDeleted());
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        service.restore(id);
        return ResponseEntity.noContent().build();
    }
}
