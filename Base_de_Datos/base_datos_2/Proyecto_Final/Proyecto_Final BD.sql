USE [StagingArea]
GO

SELECT [Delito]
      ,[SubDelito]
      ,[Fecha]
      ,[Hora]
      ,[Victima]
      ,[SubVictima]
      ,[Edad]
      ,[Genero]
      ,[Nacionalidad]
      ,[Provincia]
      ,[Canton]
      ,[Distrito]
  FROM Crimenes_OIJ_2023

GO

--Dimensiones 
--Fechas OIJ-DIM-Fecha
--Hora OIJ-DIM-Hora
--Edad OIJ-DIM-Edad
--Genero OIJ-DIM-Genero
--Provincia OIJ-DIM-Provincia
--Canton OIJ-DIM-Canton
--Nacionalidad OIJ-DIM-Nacionaidad
--Delito OIJ-DIM-Delito
--Victima OIJ-DIM-Victima
--SubDelito OIJ-DIM-SubDelito

-- Delito 
Select Distinct Delito
from Crimenes_OIJ_2023
Order By Delito;

--Subdelito 
SELECT DISTINCT SubDelito
FROM Crimenes_OIJ_2023
ORDER BY SubDelito;
--Creamos una nueva Tabla Subdelito para ajustar 'DISCUSION/RI&#209;A' THEN 'DISCUSION/RIÑA'
Alter Table [dbo].[Crimenes_OIJ_2023]
	Add SubDelito_New VarChar(200);

UPDATE Crimenes_OIJ_2023
	SET SubDelito_New =
		CASE
			WHEN SubDelito = 'DISCUSION/RI&#209;A' THEN 'DISCUSION/RIÑA'
			ELSE SubDelito
		END;

SELECT DISTINCT SubDelito_New
FROM Crimenes_OIJ_2023
ORDER BY SubDelito_New; 

-- SubVictima
SELECT DISTINCT SubVictima
FROM Crimenes_OIJ_2023 
ORDER BY SubVictima

--Fecha
--Verificamos las fechas sean validas / Fechas Invalidas = 0
SELECT *
FROM Crimenes_OIJ_2023
Where ISDATE(Fecha) = 0;

--Hora 
Select ROW_NUMBER() Over (Order By Hora) ID_Hora,*
alter DIM.Dbo.OIJ_Dim_Hora
from ( 
	Select Distinct Hora, 
		Descripcion_Hora = 
			Case 
				When Hora = '00:00:00 - 02:59:59'Then 'Noche'
				When Hora = '03:00:00 - 05:59:59'Then 'Noche'
				When Hora = '06:00:00 - 08:59:59'Then 'Mañana'
				When Hora = '09:00:00 - 11:59:59'Then 'Mañana'
				When Hora = '12:00:00 - 14:59:59'Then 'Tarde'
				When Hora = '15:00:00 - 17:59:59'Then 'Tarde'
				When Hora = '18:00:00 - 20:59:59'Then 'Noche'
				When Hora = '21:00:00 - 23:59:59'Then 'Noche'
			End
	from Crimenes_OIJ_2023
	)A;

--Fecha
	Select ROW_NUMBER() Over(Order By Fecha) ID_Fecha,*
Into dim.Dbo.OIJ_Dim_Fecha
From (
	Select Distinct Fecha, Year(Fecha) Anno, Month(Fecha) Numero_Mes
		,Nombre_Mes =
		Case
			When Month(Fecha) = 1 Then 'Enero'
			When Month(Fecha) = 2 Then 'Febrero'
			When Month(Fecha) = 3 Then 'Marzo'
			When Month(Fecha) = 4 Then 'Abril'
			When Month(Fecha) = 5 Then 'Mayo'
			When Month(Fecha) = 6 Then 'Junio'
			When Month(Fecha) = 7 Then 'Julio'
			When Month(Fecha) = 8 Then 'Agosto'
			When Month(Fecha) = 9 Then 'Setiembre'
			When Month(Fecha) = 10 Then 'Octubre'
			When Month(Fecha) = 11 Then 'Noviembre'
			When Month(Fecha) = 12 Then 'Diciembre'

		End
			, Day(Fecha) Numero_Dia
			,Nombre_Dia =
				Case
					When DatePart(DW, Fecha) = 1 Then 'Domingo'
					When DatePart (DW, Fecha) = 2 Then 'Lunes'
					When DatePart(DW, Fecha) = 3 Then 'Martes'
					When DatePart(DW, Fecha) = 4 Then 'Miercoles'
					When DatePart(DW, Fecha) = 5 Then 'Jueves'
					When DatePart (DW, Fecha) = 6 Then 'Viernes'
					When DatePart(DW, Fecha) = 7 Then 'Sabado'

		End
		from [Crimenes_OIJ_2023] 
		)A;

--Edad
select row_number() over(Order by EDAD) ID_Edad,*
Into DIM.Dbo.OIJ_Dim_Edad
from (
		select distinct Edad
		from [dbo].[Crimenes_OIJ_2023]
	)A;

--Genero
select row_number() over(Order by Genero) ID_Genero,*
Into DIM.Dbo.OIJ_Dim_Genero
from (
		select distinct Genero
		from [dbo].[Crimenes_OIJ_2023]
	)A;

--Provincia 
alter table [dbo].[Crimenes_OIJ_2023]
	add Provincia_New varchar(200);

Update [dbo].[Crimenes_OIJ_2023]
	Set Provincia_New =
		Case
			When Provincia = ' REPUBLICA DEL' Then 'DESCONOCIDO'
			When Provincia = ' ISLAS' Then 'DESCONOCIDO'
			When Provincia = ' REPUBLICA DEMOCRATICA DEL' Then 'DESCONOCIDO'
		Else Provincia
	End

select row_number() over(Order by Provincia) ID_Provincia,*
	Into DIM.Dbo.OIJ_Dim_Provincia
	from (
		select distinct Provincia_New Provincia
		from [dbo].[Crimenes_OIJ_2023]
	)A;

--Canton
alter table [dbo].[Crimenes_OIJ_2023]
	add Canton_New varchar(200);

Update [dbo].[Crimenes_OIJ_2023]
	Set Canton_New =
		Case
			When Canton = ' SARCH&#205;' Then 'SARCHI'
			When Canton = ' PUERTO JIM&#201;NEZ' Then 'PUERTO JIMENEZ'
			When Canton = ' CANAS' Then 'CAÑAS'
		Else Canton	
		End

select row_number() over(Order by Canton) ID_Canton,*
	Into DIM.Dbo.OIJ_Dim_Canton
	from (
		select distinct Canton_New Canton
		from [dbo].[Crimenes_OIJ_2023]
	)A;

--Nacionalidad
select row_number() over(Order by Nacionalidad) ID_Nacionalidad,*
Into DIM.Dbo.OIJ_Dim_Nacionalidad
from (
		select distinct Nacionalidad
		from [dbo].[Crimenes_OIJ_2023]
		order by Nacionalidad
	)A;

--Delito 
Select ROW_NUMBER() Over(Order By Delito) ID_Robo, Delito Descripcion_Delito
	Into DIM.Dbo.OIJ_Dim_Delito
		From (
			Select Distinct Delito
			From [dbo].[Crimenes_OIJ_2023]
		) A;

--SubDelito
select row_number() over(Order by SubDelito) ID_SubDelito, SubDelito
	Into DIM.Dbo.OIJ_Dim_SubDelito
	from (
		select distinct SubDelito_New SubDelito
		from [dbo].[Crimenes_OIJ_2023]
	)A;

--Victima
select row_number() over(Order by Victima) ID_Victima,*
Into DIM.Dbo.OIJ_Dim_Victima
from (
		select distinct Victima
		from [dbo].[Crimenes_OIJ_2023]
	)A;


select * from DIM.[Dbo].[OIJ_Delito_FAC];

--Dimensiones
Select Row_Number() Over (Order By Delito) ID_Delito, DD.ID_Robo, SD.ID_SubDelito, DF.ID_Fecha, DH.ID_Hora, DV.ID_Victima, 
											DE.ID_Edad, DG.ID_Genero, DN.ID_Nacionalidad, DP.ID_Provincia, DC.ID_Canton
INTO DIM.[dbo].[OIJ_DELITO_FAC] 
From [dbo].[Crimenes_OIJ_2023] OIJ
Join DIM.Dbo.OIJ_Dim_Delito DD On DD.Descripcion_Delito = OIJ.Delito
join DIM.Dbo.OIJ_Dim_SubDelito SD On SD.SubDelito = OIJ.Subdelito_new 
Join DIM.Dbo.OIJ_Dim_Fecha DF On DF.Fecha = OIJ.Fecha
Join DIM.Dbo.OIJ_Dim_Hora DH On DH.Hora = OIJ.Hora
Join DIM.Dbo.OIJ_Dim_Victima DV On DV.Victima = OIJ.Victima
Join DIM.Dbo.OIJ_Dim_Edad DE On DE. Edad = OIJ. Edad
Join DIM.Dbo.OIJ_Dim_Genero DG On DG.Genero = OIJ.Genero
Join DIM.Dbo.OIJ_Dim_Nacionalidad DN On DN.Nacionalidad = OIJ.Nacionalidad
Join DIM.Dbo.OIJ_Dim_Provincia DP On DP.Provincia = OIJ.Provincia
Join DIM.Dbo.OIJ_Dim_Canton DC On DC.Canton = OIJ.Canton;

SELECT *
FROM Crimenes_OIJ_2023