package com.sistemas.services.impl;

import com.sistemas.dtos.RoleDTO;
import com.sistemas.dtos.UserDTO;
import com.sistemas.dtos.UserRoleDTO;
import com.sistemas.entities.RoleEntity;
import com.sistemas.entities.UserEntity;
import com.sistemas.entities.UserRoleEntity;
import com.sistemas.repositories.RoleRepository;
import com.sistemas.repositories.UserRepository;
import com.sistemas.repositories.UserRoleRepository;
import com.sistemas.services.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements IUserRoleService {

    @Autowired
    private UserRoleRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    /* -------------------- MAPPERS -------------------- */

    private UserRoleDTO mapToDTO(UserRoleEntity entity) {
        return UserRoleDTO.builder()
                .id(entity.getId())
                .user(mapToUserDTO(entity.getUser()))
                .role(mapToRoleDTO(entity.getRole()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private UserRoleEntity mapToEntity(UserRoleDTO dto) {
        return UserRoleEntity.builder()
                .id(dto.getId())
                .user(mapToUserEntity(dto.getUser()))
                .role(mapToRoleEntity(dto.getRole()))
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

    private RoleDTO mapToRoleDTO(RoleEntity entity) {
        return RoleDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private RoleEntity mapToRoleEntity(RoleDTO dto) {
        return RoleEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .deletedAt(dto.getDeletedAt())
                .build();
    }

    /* -------------------- CRUD -------------------- */

    public UserRoleDTO create(UserRoleDTO dto) {
        UserRoleEntity entity = repository.save(mapToEntity(dto));
        return mapToDTO(entity);
    }

    @Override
    public UserRoleDTO update(Integer id, UserRoleDTO dto) {
        UserRoleEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        entity.setUser(mapToUserEntity(dto.getUser()));
        entity.setRole(mapToRoleEntity(dto.getRole()));

        return mapToDTO(repository.save(entity));
    }

    @Override
    public UserRoleDTO getById(Integer id) {
        return mapToDTO(
                repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Task not found"))
        );
    }

    @Override
    public List<UserRoleDTO> getAll() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Page<UserRoleDTO> getAllPaginated(Pageable pageable) {
        return repository.findAllPaginated(pageable)
                .map(this::mapToDTO);
    }

    public Page<UserRoleDTO> getAllPaginated(Integer id, Pageable pageable) {
        return repository.search(id, pageable).map(this::mapToDTO);
    }

    /* -------------------- LISTS -------------------- */

    public List<UserRoleDTO> listAll() {
        return repository.findAllIncludingDeleted().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<UserRoleDTO> listActive() {
        return repository.findAllActive().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<UserRoleDTO> listDeleted() {
        return repository.findAllDeleted().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void restore(Integer id) {
        UserRoleEntity entity = repository.findAnyById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        entity.setDeletedAt(null);
        repository.save(entity);
    }

    /* -------------------- CUSTOM METHODS (Assign / Remove / Get Roles) -------------------- */

    public void assignRole(Integer userId, Integer roleId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        boolean exists = repository.findAllActive().stream()
                .anyMatch(ur -> ur.getUser().getId().equals(userId) &&
                        ur.getRole().getId().equals(roleId));

        if (exists) return;

        UserRoleEntity ur = UserRoleEntity.builder()
                .user(user)
                .role(role)
                .build();

        repository.save(ur);
    }

    public void removeRole(Integer userId, Integer roleId) {
        repository.findAllActive().stream()
                .filter(ur -> ur.getUser().getId().equals(userId) &&
                        ur.getRole().getId().equals(roleId))
                .findFirst()
                .ifPresent(ur -> {
                    ur.setDeletedAt(LocalDateTime.now());
                    repository.save(ur);
                });
    }

    /** VERSIÓN FINAL CORRECTA **/
    public List<RoleDTO> getRolesByUser(Integer userId) {
        return repository.findRolesByUserId(userId).stream()
                .map(this::mapToRoleDTO)
                .collect(Collectors.toList());
    }

}
