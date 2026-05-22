package com.proyecta.api_gestion.controller;

import com.proyecta.api_gestion.controller.interfaces.IUsuarioController;
import com.proyecta.api_gestion.dto.common.ApiResponse;
import com.proyecta.api_gestion.dto.user.UsuarioDTO;
import com.proyecta.api_gestion.model.Usuario;
import com.proyecta.api_gestion.security.AuthenticatedUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
@PreAuthorize("hasRole('app_access')")
public class UsuarioController implements IUsuarioController {

    private final AuthenticatedUserService authenticatedUserService;

    public UsuarioController(AuthenticatedUserService authenticatedUserService) {
        this.authenticatedUserService = authenticatedUserService;
    }

    @Override
    public ResponseEntity<ApiResponse<UsuarioDTO>> getMe(@AuthenticationPrincipal Jwt jwt, Authentication authentication) {
        Usuario usuario = authenticatedUserService.registerAccess(jwt);
        String correo = usuario.getCorreo();
        String nombre = firstNonBlank(
                usuario.getNombre(),
                jwt.getClaimAsString("name"),
                correo);

        String rol = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .filter(authority -> !"ROLE_app_access".equals(authority))
                .map(authority -> authority.substring("ROLE_".length()))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining(", "));

        String dependencia = firstNonBlank(
                jwt.getClaimAsString("department"),
                jwt.getClaimAsString("organizational_unit"),
                correo,
                "No definida");

        UsuarioDTO user = new UsuarioDTO(nombre, correo, rol, dependencia);
        return ResponseEntity.ok(ApiResponse.success(user, "Perfil de usuario recuperado"));
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return "";
        }

        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }
}
