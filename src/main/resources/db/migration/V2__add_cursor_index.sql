-- Índice que se usa identificador en la paginación de tipo cursor que tienen las tareas.
CREATE INDEX IF NOT EXISTS idx_tasks_date_id ON tasks (date DESC, id DESC);