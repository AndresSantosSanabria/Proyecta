package com.proyecta.api_gestion.dto.admin;

import java.time.LocalDateTime;

/**
 * DTO for returning Usuario data in API responses.
 * Never exposes sensitive fields like contrasenaHash.
 */
public record UsuarioResponseDTO(
        Integer id,
        String nombre,
        String correo,
        String rolCodigo,
        String rolNombre,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime ultimoAcceso
) {}
