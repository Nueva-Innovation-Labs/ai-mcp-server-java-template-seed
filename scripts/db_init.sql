-- 1. Create a database named 'ai_database'
SELECT 'CREATE DATABASE ai_database'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ai_database')\gexec

-- Switch to the new database
\c ai_database

-- 2. Create the user 'user1234' with the specified password
DO
$do$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_catalog.pg_user WHERE usename = 'user1234'
   ) THEN
      CREATE USER user1234 WITH ENCRYPTED PASSWORD 'password1234';
   END IF;
END
$do$;

-- 3. Grant privileges to the user on the database
GRANT ALL PRIVILEGES ON DATABASE ai_database TO user1234;

-- 4. Create the 'contacts' table
CREATE TABLE IF NOT EXISTS contacts (
    contact_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20)
);

-- 5. Create the 'addresses' table
CREATE TABLE IF NOT EXISTS addresses (
    address_id SERIAL PRIMARY KEY,
    contact_id INTEGER REFERENCES contacts(contact_id),
    street_address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state_province VARCHAR(100),
    zip_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL
);

-- 6. Grant privileges on tables to the user
GRANT ALL ON contacts TO user1234;
GRANT ALL ON addresses TO user1234;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO user1234;