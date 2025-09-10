CREATE DATABASE universidaddb;
GO
USE universidaddb;
CREATE LOGIN u_dev WITH PASSWORD = 'DevUser!23';
CREATE USER u_dev FOR LOGIN u_dev;
ALTER ROLE db_owner ADD MEMBER u_dev;
GO