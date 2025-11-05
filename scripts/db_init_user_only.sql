-- Create the user 'user1234' with the specified password
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

-- Grant privileges to the user on the existing database 'ai_database'
GRANT ALL PRIVILEGES ON DATABASE ai_database TO user1234;