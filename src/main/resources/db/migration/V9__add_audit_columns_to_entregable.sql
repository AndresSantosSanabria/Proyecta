ALTER TABLE proyecta_db.entregable
    ADD COLUMN IF NOT EXISTS creado_por VARCHAR(100),
    ADD COLUMN IF NOT EXISTS modificado_por VARCHAR(100);
