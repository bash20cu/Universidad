-- 1) Crear una BD llamada Laboratorio02 con una tabla llamada bitacora
CREATE DATABASE Laboratorio02;
GO
USE Laboratorio02;
GO
CREATE TABLE bitacora (id INT IDENTITY(1,1) PRIMARY KEY, Fecha DATETIME);
-- 2) Iniciar una transacci�n
BEGIN TRANSACTION;
-- 3) Ingresar 20 registros
INSERT INTO bitacora (Fecha) VALUES (GETDATE());
GO 20
-- 4) Desplegar los datos de la tabla bit�cora
SELECT ID,Fecha FROM bitacora;
-- 5) Reversar la transacci�n
ROLLBACK;
-- 6) Desplegar los datos de la tabla bit�cora
SELECT ID,Fecha FROM bitacora;
-- 7) Iniciar una transacci�n
BEGIN TRANSACTION;
-- 8) Ingresar 10 registros
INSERT INTO bitacora (Fecha) VALUES (GETDATE());
GO 10
-- 9) Desplegar los datos de la tabla bit�cora
SELECT ID,Fecha FROM bitacora;
-- 10) Eliminar 25 registros de la tabla bit�cora
DELETE TOP(25) FROM bitacora;
-- 11) Desplegar los datos de la tabla bit�cora
SELECT ID,Fecha FROM bitacora;
-- 12) Hacer una lectura sucia de los datos
BEGIN TRANSACTION;
UPDATE bitacora SET Fecha = GETDATE() WHERE id = 1;
-- No confirmar la transacci�n para simular una lectura sucia
-- COMMIT;
-- 13) Confirmar la transacci�n
COMMIT;
-- 14) Desplegar los datos de la tabla bit�cora
SELECT ID,Fecha FROM bitacora;


