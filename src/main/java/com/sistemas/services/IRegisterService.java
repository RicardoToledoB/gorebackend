package com.sistemas.services;

import com.sistemas.dtos.RegisterDTO;

import java.util.List;

public interface IRegisterService {

    RegisterDTO create(RegisterDTO dto);
    RegisterDTO update(Integer id, RegisterDTO dto);
    RegisterDTO getById(Integer id);
    List<RegisterDTO> getAll();
    void delete(Integer id);

}
