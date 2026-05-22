package com.proyecta.api_gestion.security;

import com.proyecta.api_gestion.exception.UnauthorizedException;
import com.proyecta.api_gestion.model.Usuario;
import com.proyecta.api_gestion.repository.UsuarioRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class AuthenticatedUserService {

    private final UsuarioRepository usuarioRepository;

    public AuthenticatedUserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Usuario requireActiveUser(Jwt jwt) {
        String correo = extractCorreo(jwt);
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new UnauthorizedException(
                        "El usuario autenticado no esta registrado en el backend: " + correo));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new UnauthorizedException("El usuario autenticado esta inactivo: " + correo);
        }

        return usuario;
    }

    @Transactional
    public Usuario registerAccess(Jwt jwt) {
        Usuario usuario = requireActiveUser(jwt);
        usuario.setUltimoAcceso(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    private String extractCorreo(Jwt jwt) {
        String correo = jwt.getClaimAsString("email");
        if (correo == null || correo.isBlank()) {
            throw new UnauthorizedException("El token no contiene el correo electronico del usuario.");
        }
        return correo.trim();
    }
}
