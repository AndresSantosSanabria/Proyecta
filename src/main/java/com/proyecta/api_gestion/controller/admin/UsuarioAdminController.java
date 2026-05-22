package com.proyecta.api_gestion.controller.admin;

import com.proyecta.api_gestion.dto.admin.UsuarioCreateDTO;
import com.proyecta.api_gestion.dto.admin.UsuarioResponseDTO;
import com.proyecta.api_gestion.dto.admin.UsuarioUpdateDTO;
import com.proyecta.api_gestion.dto.common.ApiResponse;
import com.proyecta.api_gestion.service.interfaces.IUsuarioAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * SRP: This controller handles ONLY administrative user operations.
 * All endpoints are restricted to the 'admin' role.
 */
@RestController
@RequestMapping("/api/v1/admin/usuarios")
@PreAuthorize("hasRole('admin')")
@Tag(name = "Administración — Usuarios", description = "Gestión granular de usuarios (solo Administradores)")
public class UsuarioAdminController {

    private final IUsuarioAdminService usuarioAdminService;

    public UsuarioAdminController(IUsuarioAdminService usuarioAdminService) {
        this.usuarioAdminService = usuarioAdminService;
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Lista todos los usuarios con paginación y búsqueda opcional por nombre o correo.")
    public ResponseEntity<ApiResponse<Page<UsuarioResponseDTO>>> listar(
            @RequestParam(required = false) String busqueda,
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable) {
        Page<UsuarioResponseDTO> page = usuarioAdminService.listar(busqueda, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Usuarios listados correctamente"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(usuarioAdminService.obtenerPorId(id), "Usuario encontrado"));
    }

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario asignándole un rol de la tabla RolConfig o del enum Rol.")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> crear(@Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioResponseDTO created = usuarioAdminService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(created, "Usuario creado exitosamente"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza nombre, rol o estado activo del usuario.")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioUpdateDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(usuarioAdminService.actualizar(id, dto), "Usuario actualizado"));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Activar o desactivar usuario")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam Boolean activo) {
        return ResponseEntity.ok(ApiResponse.success(
                usuarioAdminService.cambiarEstado(id, activo),
                activo ? "Usuario activado" : "Usuario desactivado"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioAdminService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
