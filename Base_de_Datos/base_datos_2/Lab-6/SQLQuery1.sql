USE [StagingArea]

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
  FROM [dbo].[OJI-DatosAbiertos-Delitos2000al2024];

Select Top 10 *
From [dbo].[OJI-DatosAbiertos-Delitos2000al2024];
--Dimensiones
--Dimension Delito
Select ROW_NUMBER() Over (Order by Delito) ID_Robo, Delito Descripcion_Delito
Into BI.Dbo.OIJ_Dim_Delito
From (Select Distinct Delito
		from  [dbo].[OJI-DatosAbiertos-Delitos2000al2024])A;
Select * from  BI.Dbo.OIJ_Dim_Delito;

--Dimension Fecha
Select top 10 * 
from [dbo].[OJI-DatosAbiertos-Delitos2000al2024]
Where ISDATE(Fecha) = 0;

Select ROW_NUMBER() Over (Order by Fecha) ID_Fecha,*
Into BI.Dbo.OIJ_Dim_Fecha
	from (Select Distinct Fecha, Year(Fecha) Anno, MONTH(Fecha) Numero_Mes,
		Nombre_Mes = 
			Case
				When Month(Fecha) = 1 Then 'Enero'
				When Month(Fecha) = 2 Then 'Febrero'
				When Month(Fecha) = 3 Then 'Marzo'
				When Month(Fecha) = 4 Then 'Abril'
				When Month(Fecha) = 5 Then 'Mayo'
				When Month(Fecha) = 6 Then 'Junio'
				When Month(Fecha) = 7 Then 'Julio'
				When Month(Fecha) = 8 Then 'Agosto'
				When Month(Fecha) = 9 Then 'Septiembre'
				When Month(Fecha) = 10 Then 'Octubre'
				When Month(Fecha) = 11 Then 'Noviembre'
				When Month(Fecha) = 12 Then 'Diciembre'
			End,
		Day(Fecha) Numero_Dia, Nombre_Dia = 
			Case
				When DATEPART(DW,Fecha) = 1 Then 'Domingo'
				When DATEPART(DW,Fecha) = 2 Then 'Lunes'
				When DATEPART(DW,Fecha) = 3 Then 'Martes'
				When DATEPART(DW,Fecha) = 4 Then 'Miercoles'
				When DATEPART(DW,Fecha) = 5 Then 'Jueves'
				When DATEPART(DW,Fecha) = 6 Then 'Viernes'
				When DATEPART(DW,Fecha) = 7 Then 'Sabado'
			End,
			Semestre = 
			Case
				When MONTH(Fecha) In (1,2,3,4,5,6) Then 'Primer Semestre'
				When MONTH(Fecha) In (7,8,9,10,11,12) Then 'Segundo Semestre'
			End
	from  [dbo].[OJI-DatosAbiertos-Delitos2000al2024]) A;

Select * 
from BI.Dbo.OIJ_Dim_Fecha;

-- Dimension Hora
Select Distinct Hora
from  [dbo].[OJI-DatosAbiertos-Delitos2000al2024];

Select ROW_NUMBER() Over (Order By Hora) ID_Hora, *
Into BI.Dbo.OIJ_Dim_Hora
from ( Select Distinct Hora, 
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
	from  [dbo].[OJI-DatosAbiertos-Delitos2000al2024]
)A;
Select * 
from BI.Dbo.OIJ_Dim_Hora;

-- Dimension Vicitima
Select ROW_NUMBER() Over (Order by Victima) ID_Victima,*
Into BI.Dbo.OIJ_Dim_Victima
From (Select Distinct Victima
	from  [dbo].[OJI-DatosAbiertos-Delitos2000al2024])A;
Select * from BI.Dbo.OIJ_Dim_Victima;
-- Dimension Edad
Select ROW_NUMBER() Over (Order by Edad) ID_Edad,*
Into BI.Dbo.OIJ_Dim_Edad
From (Select Distinct Edad
	From  [dbo].[OJI-DatosAbiertos-Delitos2000al2024])A;
Select * From BI.Dbo.OIJ_Dim_Edad;

-- Dimension Genero
Select ROW_NUMBER() Over (Order by Genero) ID_Genero,*
Into BI.DBO.OIJ_Dim_Genero
From (Select Distinct Genero
	From  [dbo].[OJI-DatosAbiertos-Delitos2000al2024])A;
Select * From BI.DBO.OIJ_Dim_Genero;

--Dimension Nacionalidad
Select ROW_NUMBER() Over(Order by Nacionalidad) ID_Nacionalidad,*
Into BI.Dbo.OIJ_Dim_Nacionalidad
From (Select Distinct Nacionalidad
	From  [dbo].[OJI-DatosAbiertos-Delitos2000al2024])A;
Select * From BI.Dbo.OIJ_Dim_Nacionalidad;

--Dimension Provincia
SELECT Distinct Provincia
  FROM [dbo].[OJI-DatosAbiertos-Delitos2000al2024];

Alter Table [dbo].[OJI-DatosAbiertos-Delitos2000al2024]
	Add Provincia_New Varchar(500);

SELECT Distinct Provincia,Provincia_New
  FROM [dbo].[OJI-DatosAbiertos-Delitos2000al2024];

Update  [dbo].[OJI-DatosAbiertos-Delitos2000al2024]
	Set Provincia_New = 
		Case
			When Provincia = ' ISLAS' Then 'DESCONOCIDO'
			When Provincia = ' REPUBLICA DEMOCRATICA DEL' Then 'DESCONOCIDO'
			When Provincia = ' REPUBLICA DEL' Then 'DESCONOCIDO'
			Else Provincia
		End;
SELECT Distinct Provincia,Provincia_New
  FROM [dbo].[OJI-DatosAbiertos-Delitos2000al2024];

Select ROW_NUMBER() Over (Order by Provincia) ID_Provincia,*
Into BI.Dbo.OIJ_Dim_Provincia
From (Select Distinct Provincia_New Provincia
  From [dbo].[OJI-DatosAbiertos-Delitos2000al2024]) A;
Select * from BI.Dbo.OIJ_Dim_Provincia;

-- Fac Delito
Select ROW_NUMBER() Over (Order by Delito) ID_Delito,DD.ID_Robo, DF.ID_Fecha,DH.ID_Hora,DV.ID_Victima,
	DE.ID_Edad, DG.ID_Genero,DN.ID_Nacionalidad,DP.ID_Provincia
Into BI.Dbo.OIJ_Fac_Delito
From [dbo].[OJI-DatosAbiertos-Delitos2000al2024] OIJ
Join BI.Dbo.OIJ_Dim_Delito DD on DD.Descripcion_Delito = OIJ.Delito
Join BI.Dbo.OIJ_Dim_Fecha DF on DF.Fecha = OIJ.Fecha
Join Bi.Dbo.OIJ_Dim_Hora DH on DH.Hora = OIJ.Hora
Join Bi.Dbo.OIJ_Dim_Victima DV on DV.Victima = OIJ.Victima
Join Bi.Dbo.OIJ_Dim_Edad DE on DE.Edad  = OIJ.Edad
Join Bi.Dbo.OIJ_Dim_Genero DG on DG.Genero = OIJ.Genero
Join Bi.Dbo.OIJ_Dim_Nacionalidad DN on DN.Nacionalidad = OIJ.Nacionalidad
Join Bi.Dbo.OIJ_Dim_Provincia DP on DP.Provincia = OIJ.Provincia

Select ID_Delito,ID_Robo, ID_Fecha, ID_Hora,ID_Victima,ID_Edad,ID_Genero,ID_Nacionalidad,ID_Provincia
from BI.Dbo.OIJ_Fac_Delito;