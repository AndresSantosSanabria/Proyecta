package com.proyecta.api_gestion.dto.admin;

import jakarta.validation.constraints.Size;

/**
 * DTO for updating an existing Usuario.
 * All fields are optional — only non-null values will be applied (PATCH semantics).
 */
public record UsuarioUpdateDTO(

        @Size(max = 120)
        String nombre,

        /** Nuevo código de rol. Si es null, no se cambia el rol. */
        String rolCodigo,

        Boolean activo
) {}
