package com.sistemas.services.impl;
import com.sistemas.dtos.StablishmentDTO;
import com.sistemas.entities.StablishmentEntity;
import com.sistemas.repositories.StablishmentRepository;
import com.sistemas.services.IStablishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StablishmentServiceImpl implements IStablishmentService {

    @Autowired
    private StablishmentRepository repository;


    private StablishmentDTO mapToDTO(StablishmentEntity entity) {
        return StablishmentDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    private StablishmentEntity mapToEntity(StablishmentDTO dto) {
        return StablishmentEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .deletedAt(dto.getDeletedAt())
                .build();
    }

    @Override
    public List<StablishmentDTO> list() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StablishmentDTO save(StablishmentDTO dto) {
        StablishmentEntity entity = repository.save(mapToEntity(dto));
        return mapToDTO(entity);
    }

    @Override
    public StablishmentDTO update(Integer id, StablishmentDTO dto) {
        StablishmentEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        entity.setName(dto.getName());
        return mapToDTO(repository.save(entity));
    }

    @Override
    public StablishmentDTO findById(Integer id) {
        StablishmentEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return mapToDTO(entity);
    }

    @Override
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }
}
