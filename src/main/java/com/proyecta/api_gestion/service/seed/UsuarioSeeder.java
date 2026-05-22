package com.proyecta.api_gestion.service.seed;

import com.proyecta.api_gestion.model.Usuario;
import com.proyecta.api_gestion.model.enums.Rol;
import com.proyecta.api_gestion.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Seeder especializado para la entidad Usuario.
 *
 * Responsabilidad: Solo crear/actualizar usuarios semilla (SRP).
 */
@Service
public class UsuarioSeeder {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioSeeder.class);
    private final UsuarioRepository usuarioRepository;

    public UsuarioSeeder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void seedUsuarios() {
        logger.info("Cargando usuarios semilla...");

        asegurarUsuario(
                "fabio.santos@cundinamarca.gov.co",
                "Fabio Andres Santos Sanabria",
                Rol.ADMINISTRADOR,
                true
        );

        crearUsuarioSiNoExiste(
                "admin@proyecta.com",
                "Administrador Proyecta",
                Rol.ADMINISTRADOR
        );

        crearUsuarioSiNoExiste(
                "gestor@proyecta.com",
                "Gestor de Proyectos",
                Rol.GESTOR_PROYECTOS
        );

        crearUsuarioSiNoExiste(
                "analista@proyecta.com",
                "Analista de Proyectos",
                Rol.ANALISTA_PROYECTOS
        );

        logger.info("Usuarios semilla cargados");
    }

    private void crearUsuarioSiNoExiste(String correo, String nombre, Rol rol) {
        if (usuarioRepository.findByCorreoIgnoreCase(correo).isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setCorreo(correo.toLowerCase());
            usuario.setRol(rol);
            usuario.setActivo(true);
            usuarioRepository.save(usuario);
            logger.info("Usuario creado: {} [{}]", correo, rol.name());
        } else {
            logger.debug("Usuario ya existe, omitiendo: {}", correo);
        }
    }

    private void asegurarUsuario(String correo, String nombre, Rol rol, boolean activo) {
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo)
                .orElseGet(Usuario::new);

        usuario.setNombre(nombre);
        usuario.setCorreo(correo.toLowerCase());
        usuario.setRol(rol);
        usuario.setRolConfig(null);
        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
        logger.info("Usuario asegurado: {} [{}]", correo, rol.name());
    }
}
