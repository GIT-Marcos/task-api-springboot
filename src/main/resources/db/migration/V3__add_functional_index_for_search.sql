-- Índice funcional para optimizar las búsquedas de descripciones duplicadas en las tareas.
CREATE INDEX IF NOT EXISTS idx_task_description_lower ON tasks (lower (description));