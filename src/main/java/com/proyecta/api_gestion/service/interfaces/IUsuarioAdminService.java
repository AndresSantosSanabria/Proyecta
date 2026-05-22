package com.proyecta.api_gestion.service.interfaces;

import com.proyecta.api_gestion.dto.admin.UsuarioCreateDTO;
import com.proyecta.api_gestion.dto.admin.UsuarioResponseDTO;
import com.proyecta.api_gestion.dto.admin.UsuarioUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * ISP (Interface Segregation Principle): This interface defines ONLY
 * the administrative operations over users. The read-only profile
 * endpoint lives in a separate controller/service.
 */
public interface IUsuarioAdminService {

    Page<UsuarioResponseDTO> listar(String busqueda, Pageable pageable);

    UsuarioResponseDTO obtenerPorId(Integer id);

    UsuarioResponseDTO crear(UsuarioCreateDTO dto);

    UsuarioResponseDTO actualizar(Integer id, UsuarioUpdateDTO dto);

    void eliminar(Integer id);

    UsuarioResponseDTO cambiarEstado(Integer id, Boolean activo);
}
