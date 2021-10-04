--;;
DROP DATABASE IF EXISTS framework;

--;;
CREATE DATABASE framework;

--;;
GRANT ALL PRIVILEGES ON DATABASE framework TO postgres;

--;;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
