package com.proyecta.api_gestion.security;

import com.proyecta.api_gestion.model.Usuario;
import com.proyecta.api_gestion.repository.UsuarioRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseRoleJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final OAuth2Error INVALID_TOKEN_ERROR = new OAuth2Error(
            "invalid_token",
            "The authenticated user does not exist, is inactive, or the token does not contain a valid email.",
            null);

    private final UsuarioRepository usuarioRepository;

    public DatabaseRoleJwtConverter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        String email = jwt.getClaimAsString("email");
        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException(INVALID_TOKEN_ERROR);
        }

        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(email.trim())
                .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                .orElseThrow(() -> new OAuth2AuthenticationException(INVALID_TOKEN_ERROR));

        String rolCodigo = usuario.getRolCodigo();
        String roleName = mapRolLocalToKeycloakRole(rolCodigo);
        if (roleName == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error(
                    "invalid_token",
                    "The authenticated user does not have a valid role assigned in the backend.",
                    null));
        }

        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        authorities.add(new SimpleGrantedAuthority("ROLE_app_access"));
        return authorities;
    }

    private String mapRolLocalToKeycloakRole(String rolLocal) {
        if (rolLocal == null) {
            return null;
        }
        return switch (rolLocal.toUpperCase()) {
            case "ADMINISTRADOR" -> "admin";
            case "GESTOR_PROYECTOS_TI" -> "gestor_tic";
            case "GESTOR_PROYECTOS", "DIRECTOR_PROYECTO" -> "director_proyecto";
            case "ANALISTA_PROYECTOS", "CONSULTA" -> "consulta";
            case "AUDITOR" -> "auditor";
            default -> rolLocal.toLowerCase();
        };
    }
}
