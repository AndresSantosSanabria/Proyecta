-- Migration V8: Fix Usuario Rol Constraint
-- Date: 2026-05-22
-- Description: Ensure usuario_rol_check allows all valid roles

-- Drop existing constraint if it exists
ALTER TABLE proyecta_db.usuario DROP CONSTRAINT IF EXISTS usuario_rol_check;

-- Add correct constraint with all valid roles
ALTER TABLE proyecta_db.usuario ADD CONSTRAINT usuario_rol_check
    CHECK (rol IN ('ADMINISTRADOR', 'GESTOR_PROYECTOS_TI', 'GESTOR_PROYECTOS', 'ANALISTA_PROYECTOS', 'SUPER_ADMIN'));

-- Add rol_config_id column if it doesn't exist
ALTER TABLE proyecta_db.usuario ADD COLUMN IF NOT EXISTS rol_config_id INTEGER REFERENCES proyecta_db.rol_config(rol_id) ON DELETE SET NULL;
