package com.sistemas.services;

import com.sistemas.dtos.RegisterHistoryDTO;

import java.util.List;

public interface IRegisterHistoryService {

    RegisterHistoryDTO create(RegisterHistoryDTO dto);
    RegisterHistoryDTO update(Integer id, RegisterHistoryDTO dto);
    RegisterHistoryDTO getById(Integer id);
    List<RegisterHistoryDTO> getAll();
    void delete(Integer id);

}
