package com.sistemas.services.impl;

import com.sistemas.dtos.RegisterDTO;
import com.sistemas.dtos.RoleDTO;
import com.sistemas.dtos.StablishmentDTO;
import com.sistemas.dtos.UserDTO;
import com.sistemas.entities.RegisterEntity;
import com.sistemas.entities.StablishmentEntity;
import com.sistemas.entities.UserEntity;
import com.sistemas.repositories.RegisterRepository;
import com.sistemas.services.IRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterServiceImpl implements IRegisterService {


    @Autowired
    private RegisterRepository repository;


    private RegisterDTO mapToDTO(RegisterEntity entity) {
        return RegisterDTO.builder()
                .id(entity.getId())
                .n_inventary(entity.getN_inventary())
                .date_reception(entity.getDate_reception())
                .n_memo(entity.getN_memo())
                .project(entity.getProject())
                .financing(entity.getFinancing())//financiamiento
                .n_acta_reception(entity.getN_acta_reception()) //numero de acta de recepcion
                .date_acta_reception(entity.getDate_acta_reception())//fecha de acta de recepcion
                .purchase_order(entity.getPurchase_order()) //orden de compra
                .acquisition_value(entity.getAcquisition_value())//Valor de adquisicion
                .provider(entity.getProvider()) //proveedor
                .rut_provider(entity.getRut_provider()) // rut del proveedor
                .n_fact(entity.getN_fact())
                .date_fact(entity.getDate_fact())
                .amount_fact(entity.getAmount_fact())
                .n_dispatch_guide(entity.getN_dispatch_guide())
                .date_dispatch_guide(entity.getDate_dispatch_guide())
                .amount_dispatch_guide(entity.getAmount_dispatch_guide())
                .description_property(entity.getDescription_property()) // descripcion del bien
                .brand(entity.getBrand()) // marca
                .model(entity.getModel())
                .n_serie(entity.getN_serie())
                .n_res_info(entity.getN_res_info()) // numero de resolucion informativa
                .date_res_info(entity.getDate_res_info())
                .stablishment(mapToStablishmentDTO(entity.getStablishment()))
                .observation_state(entity.getObservation_state())
                .n_res_gore(entity.getN_res_gore()) //Numero de resolucion del gore
                .date_res_gore(entity.getDate_res_gore())
                .n_res_accept(entity.getN_res_accept())//N° RES. ACEPTACION Y TRANSFERENCIA DE DOMINIO
                .date_res_accept(entity.getDate_res_accept())//FECHA RES. ACEPTACION Y TRANSFERENCIA DE DOMINIO
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
                .financing(dto.getFinancing())//financiamiento
                .n_acta_reception(dto.getN_acta_reception()) //numero de acta de recepcion
                .date_acta_reception(dto.getDate_acta_reception())//fecha de acta de recepcion
                .purchase_order(dto.getPurchase_order()) //orden de compra
                .acquisition_value(dto.getAcquisition_value())//Valor de adquisicion
                .provider(dto.getProvider()) //proveedor
                .rut_provider(dto.getRut_provider()) // rut del proveedor
                .n_fact(dto.getN_fact())
                .date_fact(dto.getDate_fact())
                .amount_fact(dto.getAmount_fact())
                .n_dispatch_guide(dto.getN_dispatch_guide())
                .date_dispatch_guide(dto.getDate_dispatch_guide())
                .amount_dispatch_guide(dto.getAmount_dispatch_guide())
                .description_property(dto.getDescription_property()) // descripcion del bien
                .brand(dto.getBrand()) // marca
                .model(dto.getModel())
                .n_serie(dto.getN_serie())
                .n_res_info(dto.getN_res_info()) // numero de resolucion informativa
                .date_res_info(dto.getDate_res_info())
                .stablishment(mapToStablishmentEntity(dto.getStablishment()))
                .observation_state(dto.getObservation_state())
                .n_res_gore(dto.getN_res_gore()) //Numero de resolucion del gore
                .date_res_gore(dto.getDate_res_gore())
                .n_res_accept(dto.getN_res_accept())//N° RES. ACEPTACION Y TRANSFERENCIA DE DOMINIO
                .date_res_accept(dto.getDate_res_accept())//FECHA RES. ACEPTACION Y TRANSFERENCIA DE DOMINIO
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

    public RegisterDTO create(RegisterDTO dto) {
        RegisterEntity entity = repository.save(mapToEntity(dto));
        return mapToDTO(entity);
    }

    @Override
    public RegisterDTO update(Integer id, RegisterDTO dto) {
        RegisterEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
                entity.setN_inventary(dto.getN_inventary());
                entity.setDate_reception(dto.getDate_reception());
                entity.setN_memo(dto.getN_memo());
                entity.setProject(dto.getProject());
                entity.setFinancing(dto.getFinancing());//financiamiento
                entity.setN_acta_reception(dto.getN_acta_reception()); //numero de acta de recepcion
                entity.setDate_acta_reception(dto.getDate_acta_reception());//fecha de acta de recepcion
                entity.setPurchase_order(dto.getPurchase_order()); //orden de compra
                entity.setAcquisition_value(dto.getAcquisition_value());//Valor de adquisicion
                entity.setProvider(dto.getProvider()); //proveedor
                entity.setRut_provider(dto.getRut_provider()); // rut del proveedor
                entity.setN_fact(dto.getN_fact());
                entity.setDate_fact(dto.getDate_fact());
                entity.setAmount_fact(dto.getAmount_fact());
                entity.setN_dispatch_guide(dto.getN_dispatch_guide());
                entity.setDate_dispatch_guide(dto.getDate_dispatch_guide());
                entity.setAmount_dispatch_guide(dto.getAmount_dispatch_guide());
                entity.setDescription_property(dto.getDescription_property()); // descripcion del bien
                entity.setBrand(dto.getBrand()); // marca
                entity.setModel(dto.getModel());
                entity.setN_serie(dto.getN_serie());
                entity.setN_res_info(dto.getN_res_info()); // numero de resolucion informativa
                entity.setDate_res_info(dto.getDate_res_info());
                entity.setStablishment(mapToStablishmentEntity(dto.getStablishment()));
                entity.setObservation_state(dto.getObservation_state());
                entity.setN_res_gore(dto.getN_res_gore()); //Numero de resolucion del gore
                entity.setDate_res_gore(dto.getDate_res_gore());
                entity.setN_res_accept(dto.getN_res_accept());//N° RES. ACEPTACION Y TRANSFERENCIA DE DOMINIO
                entity.setDate_res_accept(dto.getDate_res_accept());//FECHA RES. ACEPTACION Y TRANSFERENCIA DE DOMINIO
                entity.setState(dto.getState());
                entity.setUser(mapToUserEntity(dto.getUser()));

        return mapToDTO(repository.save(entity));
    }

    @Override
    public RegisterDTO getById(Integer id) {
        RegisterEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return mapToDTO(entity);
    }

    @Override
    public List<RegisterDTO> getAll() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }



    /*Listar communas activas*/
    public List<RegisterDTO> listAll() {
        return repository.findAllIncludingDeleted().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    public List<RegisterDTO> listActive() {
        return repository.findAllActive().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }



    public List<RegisterDTO> listDeleted() {
        return repository.findAllDeleted().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void restore(Integer id) {
        RegisterEntity entity = repository.findAnyById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        entity.setDeletedAt(null);
        repository.save(entity);
    }

    public Page<RegisterDTO> getAllPaginated(Pageable pageable) {
        return repository.findAllPaginated(pageable)
                .map(this::mapToDTO);
    }

    public Page<RegisterDTO> getAllPaginated(String term, Pageable pageable) {
        return repository.findAllPaginatedFiltered(term, pageable)
                .map(this::mapToDTO);
    }
}
