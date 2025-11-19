package com.sistemas.services.impl;

import com.sistemas.dtos.RegisterDTO;
import com.sistemas.dtos.RegisterHistoryDTO;
import com.sistemas.dtos.StablishmentDTO;
import com.sistemas.dtos.UserDTO;
import com.sistemas.entities.RegisterEntity;
import com.sistemas.entities.RegisterHistoryEntity;
import com.sistemas.entities.StablishmentEntity;
import com.sistemas.entities.UserEntity;
import com.sistemas.repositories.RegisterHistoryRepository;
import com.sistemas.services.IRegisterHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterHistoryServiceImpl implements IRegisterHistoryService {

    @Autowired
    private RegisterHistoryRepository repository;

    /* ============================================================
       MAPEO DTO ↔ ENTITY
       ============================================================ */

    private RegisterHistoryDTO mapToDTO(RegisterHistoryEntity entity) {
        return RegisterHistoryDTO.builder()
                .id(entity.getId())
                .register(entity.getRegister() != null ? mapToRegisterDTO(entity.getRegister()) : null)
                .action(entity.getAction())
                .jsonBefore(entity.getJsonBefore())
                .jsonAfter(entity.getJsonAfter())
                .jsonDiff(entity.getJsonDiff())
                .user(entity.getUser() != null ? mapToUserDTO(entity.getUser()) : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private RegisterHistoryEntity mapToEntity(RegisterHistoryDTO dto) {
        return RegisterHistoryEntity.builder()
                .id(dto.getId())
                .register(mapToRegisterEntity(dto.getRegister()))
                .action(dto.getAction())
                .jsonBefore(dto.getJsonBefore())
                .jsonAfter(dto.getJsonAfter())
                .jsonDiff(dto.getJsonDiff())
                .user(mapToUserEntity(dto.getUser()))
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .deletedAt(dto.getDeletedAt())
                .build();
    }

    private RegisterDTO mapToRegisterDTO(RegisterEntity entity) {
        return RegisterDTO.builder()
                .id(entity.getId())
                .n_inventary(entity.getN_inventary())
                .date_reception(entity.getDate_reception())
                .n_memo(entity.getN_memo())
                .project(entity.getProject())
                .financing(entity.getFinancing())
                .n_acta_reception(entity.getN_acta_reception())
                .date_acta_reception(entity.getDate_acta_reception())
                .purchase_order(entity.getPurchase_order())
                .acquisition_value(entity.getAcquisition_value())
                .provider(entity.getProvider())
                .rut_provider(entity.getRut_provider())
                .n_fact(entity.getN_fact())
                .date_fact(entity.getDate_fact())
                .amount_fact(entity.getAmount_fact())
                .n_dispatch_guide(entity.getN_dispatch_guide())
                .date_dispatch_guide(entity.getDate_dispatch_guide())
                .amount_dispatch_guide(entity.getAmount_dispatch_guide())
                .description_property(entity.getDescription_property())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .n_serie(entity.getN_serie())
                .n_res_info(entity.getN_res_info())
                .date_res_info(entity.getDate_res_info())
                .stablishment(mapToStablishmentDTO(entity.getStablishment()))
                .observation_state(entity.getObservation_state())
                .n_res_gore(entity.getN_res_gore())
                .date_res_gore(entity.getDate_res_gore())
                .n_res_accept(entity.getN_res_accept())
                .date_res_accept(entity.getDate_res_accept())
                .state(entity.getState())
                .user(mapToUserDTO(entity.getUser()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private RegisterEntity mapToRegisterEntity(RegisterDTO dto) {
        return RegisterEntity.builder()
                .id(dto.getId())
                .n_inventary(dto.getN_inventary())
                .date_reception(dto.getDate_reception())
                .n_memo(dto.getN_memo())
                .project(dto.getProject())
                .financing(dto.getFinancing())
                .n_acta_reception(dto.getN_acta_reception())
                .date_acta_reception(dto.getDate_acta_reception())
                .purchase_order(dto.getPurchase_order())
                .acquisition_value(dto.getAcquisition_value())
                .provider(dto.getProvider())
                .rut_provider(dto.getRut_provider())
                .n_fact(dto.getN_fact())
                .date_fact(dto.getDate_fact())
                .amount_fact(dto.getAmount_fact())
                .n_dispatch_guide(dto.getN_dispatch_guide())
                .date_dispatch_guide(dto.getDate_dispatch_guide())
                .amount_dispatch_guide(dto.getAmount_dispatch_guide())
                .description_property(dto.getDescription_property())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .n_serie(dto.getN_serie())
                .n_res_info(dto.getN_res_info())
                .date_res_info(dto.getDate_res_info())
                .stablishment(mapToStablishmentEntity(dto.getStablishment()))
                .observation_state(dto.getObservation_state())
                .n_res_gore(dto.getN_res_gore())
                .date_res_gore(dto.getDate_res_gore())
                .n_res_accept(dto.getN_res_accept())
                .date_res_accept(dto.getDate_res_accept())
                .state(dto.getState())
                .user(mapToUserEntity(dto.getUser()))
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .deletedAt(dto.getDeletedAt())
                .build();
    }

    private StablishmentDTO mapToStablishmentDTO(StablishmentEntity entity) {
        return StablishmentDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private StablishmentEntity mapToStablishmentEntity(StablishmentDTO dto) {
        return StablishmentEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .deletedAt(dto.getDeletedAt())
                .build();
    }

    private UserDTO mapToUserDTO(UserEntity entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .secondName(entity.getSecondName())
                .firstLastName(entity.getFirstLastName())
                .secondLastName(entity.getSecondLastName())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .rut(entity.getRut())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private UserEntity mapToUserEntity(UserDTO dto) {
        return UserEntity.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .secondName(dto.getSecondName())
                .firstLastName(dto.getFirstLastName())
                .secondLastName(dto.getSecondLastName())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .rut(dto.getRut())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .deletedAt(dto.getDeletedAt())
                .build();
    }

    /* ============================================================
       CRUD SERVICIO
       ============================================================ */

    @Override
    public RegisterHistoryDTO create(RegisterHistoryDTO dto) {
        return mapToDTO(repository.save(mapToEntity(dto)));
    }

    @Override
    public RegisterHistoryDTO update(Integer id, RegisterHistoryDTO dto) {
        RegisterHistoryEntity entity = repository.findAnyById(id)
                .orElseThrow(() -> new RuntimeException("History not found"));

        entity.setRegister(mapToRegisterEntity(dto.getRegister()));
        entity.setUser(mapToUserEntity(dto.getUser()));
        entity.setAction(dto.getAction());
        entity.setJsonBefore(dto.getJsonBefore());
        entity.setJsonAfter(dto.getJsonAfter());
        entity.setJsonDiff(dto.getJsonDiff());

        return mapToDTO(repository.save(entity));
    }

    @Override
    public RegisterHistoryDTO getById(Integer id) {
        RegisterHistoryEntity entity = repository.findAnyById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        return mapToDTO(entity);
    }

    @Override
    public List<RegisterHistoryDTO> getAll() {
        return repository.findAll()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    /* ============================================================
       HISTORIAL POR REGISTER (INCLUYENDO ELIMINADOS)
       ============================================================ */


    public List<RegisterHistoryDTO> getByRegisterId(Integer id) {
        return repository.findAllByRegisterIdIncludingDeleted(id)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /* ============================================================
       LISTADOS ADMIN
       ============================================================ */

    public List<RegisterHistoryDTO> listAll() {
        return repository.findAllIncludingDeleted()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<RegisterHistoryDTO> listActive() {
        return repository.findAllActive()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<RegisterHistoryDTO> listDeleted() {
        return repository.findAllDeleted()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /* ============================================================
       RESTORE
       ============================================================ */


    public void restore(Integer id) {
        RegisterHistoryEntity entity = repository.findAnyById(id)
                .orElseThrow(() -> new RuntimeException("History not found"));
        entity.setDeletedAt(null);
        repository.save(entity);
    }

    /* ============================================================
       PAGINADOS
       ============================================================ */

    public Page<RegisterHistoryDTO> getAllPaginated(Pageable pageable) {
        return repository.findAllPaginated(pageable)
                .map(this::mapToDTO);
    }

    public Page<RegisterHistoryDTO> getAllPaginated(String term, Pageable pageable) {
        return repository.findAllPaginatedFiltered(term, pageable)
                .map(this::mapToDTO);
    }

    public Page<RegisterHistoryDTO> getDeletedPaginated(String term, Pageable pageable) {
        return repository.findAllDeletedPaginatedFiltered(term, pageable)
                .map(this::mapToDTO);
    }

}
