ALTER TABLE proyecta_db.proyecto
    ADD COLUMN IF NOT EXISTS creado_por VARCHAR(100),
    ADD COLUMN IF NOT EXISTS modificado_por VARCHAR(100);
