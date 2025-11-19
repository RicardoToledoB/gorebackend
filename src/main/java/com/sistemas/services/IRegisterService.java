package com.sistemas.services;

import com.sistemas.dtos.RegisterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IRegisterService {

    // CRUD PRINCIPAL
    RegisterDTO create(RegisterDTO dto);
    RegisterDTO update(Integer id, RegisterDTO dto);
    RegisterDTO getById(Integer id);
    List<RegisterDTO> getAll();
    void delete(Integer id);

    // RESTORE DE REGISTROS SOFT-DELETED
    void restore(Integer id);

    // LISTADOS COMPLETOS
    List<RegisterDTO> listAll();     // incluye eliminados
    List<RegisterDTO> listActive();  // solo activos
    List<RegisterDTO> listDeleted(); // solo eliminados

    // PAGINADOS
    Page<RegisterDTO> getAllPaginated(Pageable pageable);
    Page<RegisterDTO> getAllPaginated(String term, Pageable pageable);
    Page<RegisterDTO> getActivePaginated(Pageable pageable);
    Page<RegisterDTO> getDeletedPaginated(String term, Pageable pageable);

    // REPORTES
    List<Map<String, Object>> countRegistersByStablishment();
}
