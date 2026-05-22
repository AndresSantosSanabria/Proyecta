package com.proyecta.api_gestion.service.interfaces;

import com.proyecta.api_gestion.dto.admin.RolConfigDTO;

import java.util.List;

/**
 * ISP: Contract exclusively for RolConfig CRUD operations.
 */
public interface IRolConfigService {

    List<RolConfigDTO> listarTodos();

    List<RolConfigDTO> listarActivos();

    RolConfigDTO obtenerPorId(Long id);

    RolConfigDTO crear(RolConfigDTO dto);

    RolConfigDTO actualizar(Long id, RolConfigDTO dto);

    void eliminar(Long id);
}
