package com.sistemas.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemas.dtos.*;
import com.sistemas.entities.RegisterEntity;
import com.sistemas.entities.StablishmentEntity;
import com.sistemas.entities.UserEntity;
import com.sistemas.repositories.RegisterRepository;
import com.sistemas.repositories.UserRepository;
import com.sistemas.services.IRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegisterServiceImpl implements IRegisterService {

    @Autowired
    private RegisterRepository repository;

    @Autowired
    private RegisterHistoryServiceImpl historyService;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /* ============================================================
       🔥 GUARDAR HISTORIAL
       ============================================================ */
    private void saveHistory(RegisterDTO before,
                             RegisterDTO after,
                             String action) {

        UserDTO userDto = getAuthenticatedUserDTO();

        RegisterSnapshotDTO beforeSnap = toSnapshot(before);
        RegisterSnapshotDTO afterSnap = toSnapshot(after);

        Map<String, Map<String, Object>> diff = calculateDiff(beforeSnap, afterSnap);

        RegisterHistoryDTO historyDTO = RegisterHistoryDTO.builder()
                .register(after != null ? after : before)
                .user(userDto)
                .action(action)
                .jsonBefore(toJson(beforeSnap))
                .jsonAfter(toJson(afterSnap))
                .jsonDiff(toJson(diff))
                .build();

        historyService.create(historyDTO);
    }

    /* ============================================================
       🔥 USUARIO DESDE TOKEN (DTO)
       ============================================================ */
    private UserDTO getAuthenticatedUserDTO() {
        UserEntity entity = getAuthenticatedUserEntity();

        return UserDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .secondName(entity.getSecondName())
                .firstLastName(entity.getFirstLastName())
                .secondLastName(entity.getSecondLastName())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .rut(entity.getRut())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    /* ============================================================
       🔥 USUARIO DESDE TOKEN (ENTITY)
       ============================================================ */
    private UserEntity getAuthenticatedUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetails userDetails)) {
            throw new RuntimeException("No hay usuario autenticado");
        }

        String email = userDetails.getUsername(); // el token trae el email

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /* ============================================================
       🔥 CREATE
       ============================================================ */
    @Override
    public RegisterDTO create(RegisterDTO dto) {

        RegisterEntity entity = mapToEntity(dto);
        entity.setUser(getAuthenticatedUserEntity());

        RegisterDTO saved = mapToDTO(repository.save(entity));

        saveHistory(null, saved, "CREATE");

        return saved;
    }

    /* ============================================================
       🔥 UPDATE
       ============================================================ */
    @Override
    public RegisterDTO update(Integer id, RegisterDTO dto) {

        RegisterEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        RegisterDTO beforeDTO = mapToDTO(entity);

        entity.setN_inventary(dto.getN_inventary());
        entity.setDate_reception(dto.getDate_reception());
        entity.setN_memo(dto.getN_memo());
        entity.setProject(dto.getProject());
        entity.setFinancing(dto.getFinancing());
        entity.setN_acta_reception(dto.getN_acta_reception());
        entity.setDate_acta_reception(dto.getDate_acta_reception());
        entity.setPurchase_order(dto.getPurchase_order());
        entity.setAcquisition_value(dto.getAcquisition_value());
        entity.setProvider(dto.getProvider());
        entity.setRut_provider(dto.getRut_provider());
        entity.setN_fact(dto.getN_fact());
        entity.setDate_fact(dto.getDate_fact());
        entity.setAmount_fact(dto.getAmount_fact());
        entity.setN_dispatch_guide(dto.getN_dispatch_guide());
        entity.setDate_dispatch_guide(dto.getDate_dispatch_guide());
        entity.setAmount_dispatch_guide(dto.getAmount_dispatch_guide());
        entity.setDescription_property(dto.getDescription_property());
        entity.setBrand(dto.getBrand());
        entity.setModel(dto.getModel());
        entity.setN_serie(dto.getN_serie());
        entity.setN_res_info(dto.getN_res_info());
        entity.setDate_res_info(dto.getDate_res_info());
        entity.setStablishment(mapToStablishmentEntity(dto.getStablishment()));
        entity.setObservation_state(dto.getObservation_state());
        entity.setN_res_gore(dto.getN_res_gore());
        entity.setDate_res_gore(dto.getDate_res_gore());
        entity.setN_res_accept(dto.getN_res_accept());
        entity.setDate_res_accept(dto.getDate_res_accept());
        entity.setState(dto.getState());

        entity.setUser(getAuthenticatedUserEntity());

        RegisterEntity saved = repository.save(entity);
        RegisterDTO afterDTO = mapToDTO(saved);

        saveHistory(beforeDTO, afterDTO, "UPDATE");

        return afterDTO;
    }

    /* ============================================================
       🔥 DELETE (SOFT DELETE)
       ============================================================ */
    @Override
    public void delete(Integer id) {

        RegisterEntity entity = repository.findAnyById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        RegisterDTO before = mapToDTO(entity);

        repository.deleteById(id);

        saveHistory(before, null, "DELETE");
    }

    /* ============================================================
       🔥 RESTORE
       ============================================================ */
    @Override
    public void restore(Integer id) {

        RegisterEntity entity = repository.findAnyById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        RegisterDTO before = mapToDTO(entity);

        entity.setDeletedAt(null);

        RegisterDTO after = mapToDTO(repository.save(entity));

        saveHistory(before, after, "RESTORE");
    }

    /* ============================================================
       SNAPSHOT
       ============================================================ */
    private RegisterSnapshotDTO toSnapshot(RegisterDTO dto) {
        if (dto == null) return null;

        RegisterSnapshotDTO snap = new RegisterSnapshotDTO();

        snap.setN_inventary(dto.getN_inventary());
        snap.setDate_reception(dto.getDate_reception());
        snap.setN_memo(dto.getN_memo());
        snap.setProject(dto.getProject());
        snap.setFinancing(dto.getFinancing());
        snap.setN_acta_reception(dto.getN_acta_reception());
        snap.setDate_acta_reception(dto.getDate_acta_reception());
        snap.setPurchase_order(dto.getPurchase_order());
        snap.setAcquisition_value(dto.getAcquisition_value());
        snap.setProvider(dto.getProvider());
        snap.setRut_provider(dto.getRut_provider());
        snap.setN_fact(dto.getN_fact());
        snap.setDate_fact(dto.getDate_fact());
        snap.setAmount_fact(dto.getAmount_fact());
        snap.setN_dispatch_guide(dto.getN_dispatch_guide());
        snap.setDate_dispatch_guide(dto.getDate_dispatch_guide());
        snap.setAmount_dispatch_guide(dto.getAmount_dispatch_guide());
        snap.setDescription_property(dto.getDescription_property());
        snap.setBrand(dto.getBrand());
        snap.setModel(dto.getModel());
        snap.setN_serie(dto.getN_serie());
        snap.setN_res_info(dto.getN_res_info());
        snap.setDate_res_info(dto.getDate_res_info());
        snap.setObservation_state(dto.getObservation_state());
        snap.setN_res_gore(dto.getN_res_gore());
        snap.setDate_res_gore(dto.getDate_res_gore());
        snap.setN_res_accept(dto.getN_res_accept());
        snap.setDate_res_accept(dto.getDate_res_accept());
        snap.setState(dto.getState());

        return snap;
    }

    /* ============================================================
       🔥 DIFF
       ============================================================ */
    private Map<String, Map<String, Object>> calculateDiff(RegisterSnapshotDTO before,
                                                           RegisterSnapshotDTO after) {

        Map<String, Map<String, Object>> diff = new LinkedHashMap<>();

        try {
            var fields = RegisterSnapshotDTO.class.getDeclaredFields();

            for (var f : fields) {
                f.setAccessible(true);

                Object oldVal = before != null ? f.get(before) : null;
                Object newVal = after != null ? f.get(after) : null;

                if (!Objects.equals(oldVal, newVal)) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("old", oldVal);
                    entry.put("new", newVal);
                    diff.put(f.getName(), entry);
                }
            }

        } catch (Exception ignored) {}

        return diff;
    }

    /* ============================================================
       JSON seguro
       ============================================================ */
    private String toJson(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return "\"json_error\"";
        }
    }

    /* ============================================================
       MAPEO DTO / ENTITY
       ============================================================ */
    private RegisterDTO mapToDTO(RegisterEntity entity) {
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

    private RegisterEntity mapToEntity(RegisterDTO dto) {
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
                .rut(entity.getRut())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    /* ============================================================
       LISTADOS / PAGINADOS / REPORTES
       ============================================================ */
    @Override
    public RegisterDTO getById(Integer id) {
        RegisterEntity entity = repository.findAnyById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        return mapToDTO(entity);
    }

    @Override
    public List<RegisterDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegisterDTO> listAll() {
        return repository.findAllIncludingDeleted()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegisterDTO> listActive() {
        return repository.findAllActive()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegisterDTO> listDeleted() {
        return repository.findAllDeleted()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RegisterDTO> getAllPaginated(Pageable pageable) {
        return repository.findAllPaginated(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<RegisterDTO> getAllPaginated(String term, Pageable pageable) {
        return repository.findAllPaginatedFiltered(term, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<RegisterDTO> getActivePaginated(Pageable pageable) {
        return repository.findAllPaginated(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<RegisterDTO> getDeletedPaginated(String term, Pageable pageable) {
        return repository.findAllDeletedPaginatedFiltered(term, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public List<Map<String, Object>> countRegistersByStablishment() {
        return repository.countRegistersByStablishment()
                .stream()
                .map(obj -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("stablishment", obj[0]);
                    map.put("total", obj[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }
}
