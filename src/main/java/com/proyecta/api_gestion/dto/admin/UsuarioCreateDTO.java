package com.proyecta.api_gestion.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new Usuario.
 * SRP: Only holds the data contract for user creation.
 */
public record UsuarioCreateDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 120)
        String nombre,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener un formato válido")
        @Size(max = 200)
        String correo,

        /**
         * Código del rol local (ej: ADMINISTRADOR, GESTOR_PROYECTOS_TI, DIRECTOR_PROYECTO).
         * Debe coincidir con un RolConfig.codigo existente o con el enum Rol.
         */
        @NotBlank(message = "El código de rol es obligatorio")
        String rolCodigo,

        Boolean activo
) {}
