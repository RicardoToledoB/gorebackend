package com.sistemas.services;
import com.sistemas.dtos.StablishmentDTO;
import java.util.List;

public interface IStablishmentService {

     List<StablishmentDTO> list();
     StablishmentDTO save(StablishmentDTO dto);
     StablishmentDTO findById(Integer id);
     void deleteById(Integer id);
     StablishmentDTO update(Integer id, StablishmentDTO dto);
}
