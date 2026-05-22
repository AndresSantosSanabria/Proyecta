package com.proyecta.api_gestion.service.impl;

import com.proyecta.api_gestion.dto.admin.RolConfigDTO;
import com.proyecta.api_gestion.exception.ResourceNotFoundException;
import com.proyecta.api_gestion.model.config.RolConfig;
import com.proyecta.api_gestion.repository.config.RolConfigRepository;
import com.proyecta.api_gestion.service.interfaces.IRolConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SRP: Only handles RolConfig CRUD.
 * DIP: Depends on RolConfigRepository interface.
 */
@Service
@Transactional
public class RolConfigServiceImpl implements IRolConfigService {

    private final RolConfigRepository rolConfigRepository;

    public RolConfigServiceImpl(RolConfigRepository rolConfigRepository) {
        this.rolConfigRepository = rolConfigRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolConfigDTO> listarTodos() {
        return rolConfigRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolConfigDTO> listarActivos() {
        return rolConfigRepository.findByActivoTrueOrderByNivelAccesoDesc().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RolConfigDTO obtenerPorId(Long id) {
        return toDTO(findOrThrow(id));
    }

    @Override
    public RolConfigDTO crear(RolConfigDTO dto) {
        if (rolConfigRepository.findByCodigo(dto.codigo().toUpperCase()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un rol con el código: " + dto.codigo());
        }
        RolConfig entity = toEntity(dto);
        return toDTO(rolConfigRepository.save(entity));
    }

    @Override
    public RolConfigDTO actualizar(Long id, RolConfigDTO dto) {
        RolConfig entity = findOrThrow(id);
        entity.setNombre(dto.nombre());
        entity.setDescripcion(dto.descripcion());
        entity.setNivelAcceso(dto.nivelAcceso());
        if (dto.activo() != null) {
            entity.setActivo(dto.activo());
        }
        return toDTO(rolConfigRepository.save(entity));
    }

    @Override
    public void eliminar(Long id) {
        if (!rolConfigRepository.existsById(id)) {
            throw new ResourceNotFoundException("RolConfig no encontrado con id: " + id);
        }
        rolConfigRepository.deleteById(id);
    }

    // ─── Mappers ─────────────────────────────────────────────────────────────

    private RolConfig toEntity(RolConfigDTO dto) {
        RolConfig rc = new RolConfig();
        rc.setCodigo(dto.codigo().toUpperCase());
        rc.setNombre(dto.nombre());
        rc.setDescripcion(dto.descripcion());
        rc.setNivelAcceso(dto.nivelAcceso() != null ? dto.nivelAcceso() : 0);
        rc.setActivo(dto.activo() != null ? dto.activo() : true);
        return rc;
    }

    private RolConfigDTO toDTO(RolConfig rc) {
        return new RolConfigDTO(
                rc.getId(),
                rc.getCodigo(),
                rc.getNombre(),
                rc.getDescripcion(),
                rc.getNivelAcceso(),
                rc.getActivo()
        );
    }

    private RolConfig findOrThrow(Long id) {
        return rolConfigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RolConfig no encontrado con id: " + id));
    }
}
