package com.proyecta.api_gestion.security;

import com.proyecta.api_gestion.model.Proyecto;
import com.proyecta.api_gestion.repository.ProyectoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("securityEvaluator")
public class SecurityEvaluator {

    private final ProyectoRepository proyectoRepository;

    public SecurityEvaluator(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    private boolean isTransversal(Authentication authentication) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String role = a.getAuthority();
                    return role.equals("ROLE_admin") || role.equals("ROLE_gestor_tic") || role.equals("ROLE_auditor") || role.equals("ROLE_consulta");
                });
    }

    private boolean isAdmin(Authentication authentication) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
    }
    
    private boolean isGestorTic(Authentication authentication) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_gestor_tic"));
    }

    private boolean isDirector(Authentication authentication) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_director_proyecto"));
    }

    public boolean canAccessProyecto(Authentication authentication, String proyectoId) {
        if (authentication == null) return false;
        if (isTransversal(authentication)) {
            return true;
        }

        if (isDirector(authentication)) {
            Optional<Proyecto> proyecto = proyectoRepository.findById(proyectoId);
            return proyecto.isPresent() && authentication.getName().equals(proyecto.get().getDirectorUsername());
        }

        return false;
    }

    public boolean canEditProyecto(Authentication authentication, String proyectoId) {
        if (authentication == null) return false;
        if (isAdmin(authentication)) {
            return true;
        }

        if (isDirector(authentication)) {
            Optional<Proyecto> proyecto = proyectoRepository.findById(proyectoId);
            return proyecto.isPresent() && authentication.getName().equals(proyecto.get().getDirectorUsername());
        }

        return false;
    }
    
    public boolean canApproveEntregable(Authentication authentication) {
        return isGestorTic(authentication);
    }
}
