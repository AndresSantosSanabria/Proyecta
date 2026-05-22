package com.proyecta.api_gestion.controller.admin;

import com.proyecta.api_gestion.dto.admin.RolConfigDTO;
import com.proyecta.api_gestion.dto.common.ApiResponse;
import com.proyecta.api_gestion.service.interfaces.IRolConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SRP: This controller handles ONLY RolConfig CRUD.
 * All endpoints are restricted to the 'admin' role.
 */
@RestController
@RequestMapping("/api/v1/admin/roles")
@PreAuthorize("hasRole('admin')")
@Tag(name = "Administración — Roles", description = "Gestión de configuraciones de rol (solo Administradores)")
public class RolConfigController {

    private final IRolConfigService rolConfigService;

    public RolConfigController(IRolConfigService rolConfigService) {
        this.rolConfigService = rolConfigService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los roles")
    public ResponseEntity<ApiResponse<List<RolConfigDTO>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.success(rolConfigService.listarTodos(), "Roles listados"));
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar roles activos", description = "Retorna solo los roles con activo=true, ordenados por nivel de acceso descendente.")
    public ResponseEntity<ApiResponse<List<RolConfigDTO>>> listarActivos() {
        return ResponseEntity.ok(ApiResponse.success(rolConfigService.listarActivos(), "Roles activos listados"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rol por ID")
    public ResponseEntity<ApiResponse<RolConfigDTO>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(rolConfigService.obtenerPorId(id), "Rol encontrado"));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo rol", description = "Crea una nueva configuración de rol. El código debe ser único.")
    public ResponseEntity<ApiResponse<RolConfigDTO>> crear(@Valid @RequestBody RolConfigDTO dto) {
        RolConfigDTO created = rolConfigService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(created, "Rol creado exitosamente"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol")
    public ResponseEntity<ApiResponse<RolConfigDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RolConfigDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(rolConfigService.actualizar(id, dto), "Rol actualizado"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar rol")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rolConfigService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
