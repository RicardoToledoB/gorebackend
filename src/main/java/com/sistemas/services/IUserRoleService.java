package com.sistemas.services;

import com.sistemas.dtos.UserRoleDTO;

import java.util.List;

public interface IUserRoleService {

    UserRoleDTO create(UserRoleDTO dto);

    UserRoleDTO update(Integer id, UserRoleDTO dto);

    UserRoleDTO getById(Integer id);

    List<UserRoleDTO> getAll();

    void delete(Integer id);
}
