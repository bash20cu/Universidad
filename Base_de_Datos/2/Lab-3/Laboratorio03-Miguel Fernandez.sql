
1)	Hacer un respaldo completo llamado Lab02Full.bak 
BACKUP DATABASE [Laboratorio02] TO  DISK = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\Backup\Laboratorio02.bak',  
DISK = N'D:\BD2\Lab02Full.bak' WITH NOFORMAT, NOINIT,  NAME = N'Laboratorio02-Full Database Backup', 
SKIP, NOREWIND, NOUNLOAD,  STATS = 10
GO
2)	Ingresar un nuevo registro  
Insert Into bitacora (Fecha) Values(GetDate());
Select ID,Fecha from bitacora;

3)	Hacer un respaldo diferencial llamado Lab02Dif.bak
BACKUP DATABASE [Laboratorio02] TO  DISK = N'D:\BD2\Lab02Dif.bak' WITH  DIFFERENTIAL , 
NOFORMAT, NOINIT,  NAME = N'Laboratorio02-Full Database Backup', SKIP, NOREWIND, NOUNLOAD,  STATS = 10
GO

4)	Ingresar un nuevo registro  
Insert Into bitacora (Fecha) Values(GetDate());
Select ID,Fecha from bitacora;
5)	Hacer un respaldo de bit�cora llamado Lab02log.trn
BACKUP LOG [Laboratorio02] TO  DISK = N'D:\BD2\Lab02log.trn' WITH NOFORMAT, 
NOINIT,  NAME = N'Laboratorio02-Full Database Backup', SKIP, NOREWIND, NOUNLOAD,  STATS = 10
GO

6)	Ingresar un nuevo registro
Insert Into bitacora (Fecha) Values(GetDate());
Select ID,Fecha from bitacora;
7)	Restaurar los respaldos en la BD con el nombre Lab02Respaldos 
USE [master]
RESTORE DATABASE [Lab02Respaldos] FROM  DISK = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\Backup\Laboratorio02.bak', 
DISK = N'D:\BD2\Lab02Full.bak' WITH  FILE = 1,  MOVE N'Laboratorio02' 
TO N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\Lab02Respaldos.mdf',  MOVE N'Laboratorio02_log' 
TO N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\Lab02Respaldos_log.ldf',  NORECOVERY,  NOUNLOAD,  STATS = 5
RESTORE DATABASE [Lab02Respaldos] FROM  DISK = N'D:\BD2\Lab02Dif.bak' WITH  FILE = 1,  NORECOVERY,  NOUNLOAD,  STATS = 5
RESTORE LOG [Lab02Respaldos] FROM  DISK = N'D:\BD2\Lab02log.trn' WITH  FILE = 1,  NOUNLOAD,  STATS = 5

GO
8)	Desplegar los datos de la tabla bit�cora
use Lab02Respaldos;
Select ID,Fecha from bitacora;
9)	�Explicar qu� sucedi� con el registro del punto 6? 10) �Qu� tipo de respaldo har�a para incluirlo? 
