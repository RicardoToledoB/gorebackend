package com.sistemas.services;

import com.sistemas.dtos.UserDTO;

import java.util.List;

public interface IUserService {

    UserDTO create(UserDTO dto);
    UserDTO update(Integer id, UserDTO dto);
    UserDTO getById(Integer id);
    List<UserDTO> getAll();
    void delete(Integer id);
    UserDTO getByEmail(String email);
}
