# Configuración del Sistema - API Gestion

## Estado: ✅ LISTO PARA EJECUTAR

El proyecto ha sido compilado y empaquetado exitosamente el **22/05/2026**.

---

## 📋 Requisitos Previos

### 1. **PostgreSQL**
- **Versión**: 12 o superior
- **Puerto**: 5432 (por defecto)
- **Credenciales predeterminadas**:
  - Usuario: `postgres`
  - Contraseña: `admin`

Crear la base de datos y esquema:
```sql
CREATE DATABASE proyecta_db;
CREATE SCHEMA proyecta_db AUTHORIZATION postgres;
```

### 2. **Java 25**
```bash
java -version
```

Debe retornar: `openjdk version "25" ...`

### 3. **Keycloak** (para autenticación)
- **Servidor**: `http://172.20.6.59:8080`
- **Realm**: `gob-cundinamarca-devqa`
- **Cliente**: `proyecta-web`

---

## 🚀 Cómo Ejecutar

### Opción 1: Ejecutar JAR compilado (Recomendado)
```powershell
cd "c:\Users\soporteportal\Pictures\Proyecta"
java -jar target/api-gestion-0.0.1-SNAPSHOT.jar
```

### Opción 2: Ejecutar con Maven
```powershell
cd "c:\Users\soporteportal\Pictures\Proyecta"
.\apache-maven-3.9.14\bin\mvn.cmd spring-boot:run
```

---

## ⚙️ Variables de Configuración

Editar: `src/main/resources/application.properties`

### Configuración de Base de Datos
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres?currentSchema=proyecta_db
spring.datasource.username=postgres
spring.datasource.password=admin
```

### Configuración de Keycloak (Variables de Entorno)
```bash
# Agregar al sistema o antes de ejecutar
$env:KEYCLOAK_ISSUER_URI = "http://172.20.6.59:8080/realms/gob-cundinamarca-devqa"
$env:KEYCLOAK_JWKS_URI = "http://172.20.6.59:8080/realms/gob-cundinamarca-devqa/protocol/openid-connect/certs"
$env:GOB_RESOURCE_CLIENT_IDS = "proyecta-web"
$env:GOB_CORS_ORIGINS = "http://localhost:5173,http://localhost:3000"
```

---

## 🌐 Acceso a la Aplicación

- **Backend API**: `http://localhost:8082`
- **API Documentation**: `http://localhost:8082/swagger-ui.html`
- **Ping Health**: `http://localhost:8082/actuator/health`

---

## 🔐 Seguridad

- **OAuth2 Resource Server**: Activo
- **CORS**: Configurado para `http://localhost:5173`
- **Rol requerido**: `app_access`

---

## 📖 Primeros Pasos Recomendados

1. **Verificar conexión a BD**:
   ```powershell
   psql -U postgres -h localhost -d postgres
   ```

2. **Verificar que Java 25 está disponible**:
   ```powershell
   java -version
   ```

3. **Iniciar Backend**:
   ```powershell
   java -jar target/api-gestion-0.0.1-SNAPSHOT.jar
   ```

4. **Verificar salud de la aplicación**:
   ```bash
   curl http://localhost:8082/actuator/health
   ```

---

## 📦 Artefactos Compilados

- **JAR Ejecutable**: `target/api-gestion-0.0.1-SNAPSHOT.jar`
- **Clases Compiladas**: `target/classes/`

---

## 🛠️ Compilar de Nuevo (Si hay cambios)

```powershell
cd "c:\Users\soporteportal\Pictures\Proyecta"
.\apache-maven-3.9.14\bin\mvn.cmd clean compile package -DskipTests
```

---

## 📝 Notas Importantes

- Las migraciones de BD están configuradas con `hibernate.ddl-auto=update`
- Los archivos se suben a: `uploads/documentos/`, `uploads/evidencias/`, `uploads/schedules/`
- Logs activos en: `INFO` (aplicación), `DEBUG` (seguridad)

---

**Última actualización**: 22/05/2026 - Sistema listo para desarrollo y pruebas.
