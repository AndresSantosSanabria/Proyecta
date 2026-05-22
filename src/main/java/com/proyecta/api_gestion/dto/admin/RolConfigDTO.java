package com.proyecta.api_gestion.dto.admin;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating and updating RolConfig records.
 * Used for both input (create/update) and output (response).
 */
public record RolConfigDTO(
        Long id,

        @NotBlank(message = "El código del rol es obligatorio")
        @Size(max = 30, message = "El código no puede superar 30 caracteres")
        String codigo,

        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(max = 100)
        String nombre,

        @Size(max = 300)
        String descripcion,

        @NotNull(message = "El nivel de acceso es obligatorio")
        @Min(value = 0, message = "El nivel de acceso mínimo es 0")
        @Max(value = 100, message = "El nivel de acceso máximo es 100")
        Integer nivelAcceso,

        Boolean activo
) {}
