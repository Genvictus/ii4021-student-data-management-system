CREATE TABLE "user" (
    user_id     VARCHAR(20) PRIMARY KEY,
    email       VARCHAR(255) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    full_name   VARCHAR(255) NOT NULL,
    role        VARCHAR(50) NOT NULL CHECK (role IN ('STUDENT', 'SUPERVISOR', 'HOD')),
    public_key  TEXT
);
