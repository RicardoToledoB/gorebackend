package com.sistemas.services;
import com.sistemas.dtos.StablishmentDTO;
import com.sistemas.dtos.StablishmentDTO;

import java.util.List;

public interface IStablishmentService {

    StablishmentDTO create(StablishmentDTO dto);
    StablishmentDTO update(Integer id, StablishmentDTO dto);
    StablishmentDTO getById(Integer id);
    List<StablishmentDTO> getAll();
    void delete(Integer id);
}
