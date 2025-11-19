package com.sistemas.controllers;

import com.sistemas.dtos.RegisterHistoryDTO;
import com.sistemas.services.impl.RegisterHistoryServiceImpl;
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
@RequestMapping("/api/v1/registers_histories")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN','ADMINISTRATIVO')")
public class RegisterHistoryController {

    @Autowired
    private RegisterHistoryServiceImpl service;

    /* ============================================================
       CRUD
       ============================================================ */

    @PostMapping
    public ResponseEntity<RegisterHistoryDTO> create(@RequestBody RegisterHistoryDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegisterHistoryDTO> update(@PathVariable Integer id,
                                                     @RequestBody RegisterHistoryDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterHistoryDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /* ============================================================
       LISTADOS
       ============================================================ */

    @GetMapping("/all")
    public ResponseEntity<List<RegisterHistoryDTO>> getAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<RegisterHistoryDTO>> listActive() {
        return ResponseEntity.ok(service.listActive());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<RegisterHistoryDTO>> listDeleted() {
        return ResponseEntity.ok(service.listDeleted());
    }

    /* ============================================================
       RESTORE
       ============================================================ */

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        service.restore(id);
        return ResponseEntity.noContent().build();
    }

    /* ============================================================
       HISTORIAL POR REGISTER_ID
       ============================================================ */

    @GetMapping("/by-register/{registerId}")
    public ResponseEntity<List<RegisterHistoryDTO>> getByRegister(@PathVariable Integer registerId) {
        return ResponseEntity.ok(service.getByRegisterId(registerId));
    }

    /* ============================================================
       PAGINADOS
       ============================================================ */

    @GetMapping("/paginated")
    public ResponseEntity<Page<RegisterHistoryDTO>> getAllPaginated(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.getAllPaginated(search, pageable));
    }

    @GetMapping("/deleted-paginated")
    public ResponseEntity<Page<RegisterHistoryDTO>> getDeletedPaginated(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.getDeletedPaginated(search, pageable));
    }


}
