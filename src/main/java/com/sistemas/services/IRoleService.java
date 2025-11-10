package com.sistemas.services;

import com.sistemas.dtos.RoleDTO;

import java.util.List;

public interface IRoleService {

    RoleDTO create(RoleDTO dto);
    RoleDTO update(Integer id, RoleDTO dto);
    RoleDTO getById(Integer id);
    List<RoleDTO> getAll();
    void delete(Integer id);
}
