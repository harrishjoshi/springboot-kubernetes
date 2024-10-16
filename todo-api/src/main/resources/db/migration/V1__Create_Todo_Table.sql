-- Create a sequence for the 'id' field
CREATE SEQUENCE todo_id_seq
    START WITH 1
    INCREMENT BY 50;

-- Create the 'todo' table
CREATE TABLE todo
(
    id          BIGINT      DEFAULT nextval('todo_id_seq') PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    status      VARCHAR(50) DEFAULT 'PENDING',
    description TEXT,
    created_at  TIMESTAMP   DEFAULT now(),
    updated_at  TIMESTAMP,

    -- Unique constraint for title
    CONSTRAINT idx_todo_title UNIQUE (title)
);

-- Add an index on the 'title' column
CREATE INDEX idx_title ON todo (title);