package com.proyecta.api_gestion.service.impl;

import com.proyecta.api_gestion.dto.admin.UsuarioCreateDTO;
import com.proyecta.api_gestion.dto.admin.UsuarioResponseDTO;
import com.proyecta.api_gestion.dto.admin.UsuarioUpdateDTO;
import com.proyecta.api_gestion.exception.ResourceNotFoundException;
import com.proyecta.api_gestion.model.Usuario;
import com.proyecta.api_gestion.model.config.RolConfig;
import com.proyecta.api_gestion.repository.UsuarioRepository;
import com.proyecta.api_gestion.repository.config.RolConfigRepository;
import com.proyecta.api_gestion.service.interfaces.IUsuarioAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SRP: Handles only administrative user management logic.
 * DIP: Depends on repository interfaces, not concrete implementations.
 * OCP: Open for extension (e.g., future Keycloak sync) via the IUsuarioAdminService contract.
 */
@Service
@Transactional
public class UsuarioAdminServiceImpl implements IUsuarioAdminService {

    private final UsuarioRepository usuarioRepository;
    private final RolConfigRepository rolConfigRepository;

    public UsuarioAdminServiceImpl(UsuarioRepository usuarioRepository,
                                   RolConfigRepository rolConfigRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolConfigRepository = rolConfigRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listar(String busqueda, Pageable pageable) {
        Page<Usuario> page;
        if (busqueda != null && !busqueda.isBlank()) {
            page = usuarioRepository.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                    busqueda, busqueda, pageable);
        } else {
            page = usuarioRepository.findAll(pageable);
        }
        return page.map(this::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Integer id) {
        return toResponseDTO(findOrThrow(id));
    }

    @Override
    public UsuarioResponseDTO crear(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByCorreoIgnoreCase(dto.correo())) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo: " + dto.correo());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setCorreo(dto.correo().toLowerCase());
        usuario.setActivo(dto.activo() != null ? dto.activo() : true);

        asignarRol(usuario, dto.rolCodigo());

        return toResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponseDTO actualizar(Integer id, UsuarioUpdateDTO dto) {
        Usuario usuario = findOrThrow(id);

        if (dto.nombre() != null && !dto.nombre().isBlank()) {
            usuario.setNombre(dto.nombre());
        }
        if (dto.rolCodigo() != null && !dto.rolCodigo().isBlank()) {
            asignarRol(usuario, dto.rolCodigo());
        }
        if (dto.activo() != null) {
            usuario.setActivo(dto.activo());
        }

        return toResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioResponseDTO cambiarEstado(Integer id, Boolean activo) {
        Usuario usuario = findOrThrow(id);
        usuario.setActivo(activo);
        return toResponseDTO(usuarioRepository.save(usuario));
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

    private Usuario findOrThrow(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    /**
     * Resolves the role from RolConfig first (granular config), then falls back
     * to the legacy Rol enum for backwards compatibility.
     */
    private void asignarRol(Usuario usuario, String rolCodigo) {
        rolConfigRepository.findByCodigo(rolCodigo.toUpperCase()).ifPresentOrElse(
                rolConfig -> {
                    usuario.setRolConfig(rolConfig);
                    usuario.setRol(null);
                },
                () -> {
                    // Fallback: try the legacy enum
                    try {
                        var rolEnum = com.proyecta.api_gestion.model.enums.Rol.valueOf(rolCodigo.toUpperCase());
                        usuario.setRol(rolEnum);
                        usuario.setRolConfig(null);
                    } catch (IllegalArgumentException ex) {
                        throw new IllegalArgumentException(
                                "Código de rol no reconocido: '" + rolCodigo + "'. " +
                                "Use un código de RolConfig existente o uno de: " +
                                java.util.Arrays.toString(com.proyecta.api_gestion.model.enums.Rol.values()));
                    }
                }
        );
    }

    private UsuarioResponseDTO toResponseDTO(Usuario u) {
        String rolCodigo = u.getRolCodigo();
        String rolNombre = null;
        if (u.getRolConfig() != null) {
            rolNombre = u.getRolConfig().getNombre();
        } else if (u.getRol() != null) {
            rolNombre = u.getRol().name();
        }
        return new UsuarioResponseDTO(
                u.getId(),
                u.getNombre(),
                u.getCorreo(),
                rolCodigo,
                rolNombre,
                u.getActivo(),
                u.getFechaCreacion(),
                u.getUltimoAcceso()
        );
    }
}
