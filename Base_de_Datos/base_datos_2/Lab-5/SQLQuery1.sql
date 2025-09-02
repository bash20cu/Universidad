Create Database BI;
Create database StagingArea;
Use StagingArea;

//EDA
Select Top 10 ID,Fecha_Defuncion,Provincia,Canton 
From [dbo].[Data_Defunciones202310];
--Total de Registros 1160302
Select Count (ID) Totales 
From [dbo].[Data_Defunciones202310];

--Total con fechas 
--correctas 1136676
Select Count (ID) Total 
From [dbo].[Data_Defunciones202310]
Where ISDATE(Fecha_Defuncion) = 1;

--incorrectas 23626
Select Count (ID) Total 
From [dbo].[Data_Defunciones202310]
Where ISDATE(Fecha_Defuncion) = 0;

Select Distinct Fecha_Defuncion
From [dbo].[Data_Defunciones202310]
Where ISDATE(Fecha_Defuncion) = 0;
-- Total con Provincias
Select Distinct Provincia
From [dbo].[Data_Defunciones202310]
Where Len(Provincia) > 1;

Select Provincia, Count(ID) Total
From [dbo].[Data_Defunciones202310]
Where Len(Provincia) > 1
Group By Provincia
Order by Total Desc;

-- Total con Provincias y Cantón
Select Provincia, Canton, Count(ID) Total
From [dbo].[Data_Defunciones202310]
Where Len(Provincia) > 1 And Len(Canton) > 1
Group By Provincia, Canton
Order by Total Desc;

-- 2.	Hacer todo el proceso de limpieza, documentando cada paso y todas las decisiones que se tomaron. 
--Script para generar Cubo de Defunciones
-- Respaldo
Select ID,Fecha_Defuncion,Provincia,Canton
Into [dbo].[Data_Defuncionesv1]
From [dbo].[Data_Defunciones202310];

-- Fechas Incorrectas
Select ID,Fecha_Defuncion,Provincia,Canton
From [dbo].[Data_Defuncionesv1]
Where ISDATE(Fecha_Defuncion) = 0;

Select ID,Fecha_Defuncion,Provincia,Canton
Into [dbo].[Data_Defunciones_FechasMalasv1]
From [dbo].[Data_Defuncionesv1]
Where ISDATE(Fecha_Defuncion) = 0;

Delete
From [dbo].[Data_Defuncionesv1]
Where ISDATE(Fecha_Defuncion) = 0;

Select Count(ID)
From [dbo].[Data_Defuncionesv1];

Select Count(ID)
From [dbo].[Data_Defuncionesv1]
Where ISDATE(Fecha_Defuncion) = 1;

-- Nulos en descripcion de Provincia
Select Provincia, Canton, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Where Len(Provincia) > 1 And Len(Canton) > 1
Group By Provincia, Canton
Order by Total Desc;

Select Provincia,Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Group By Provincia
Order by Total Desc;

Alter Table [dbo].[Data_Defuncionesv1]
	Add NProvincia VarChar(50);

Update [dbo].[Data_Defuncionesv1]
Set NProvincia = 'NO REGISTRADO'
Where Provincia Is Null Or Provincia = 'NULL' Or Provincia = '';

Update [dbo].[Data_Defuncionesv1]
Set NProvincia = 'SAN JOSE'
Where Provincia In ('SAN JOSE','SAN JOSÉ');

Update [dbo].[Data_Defuncionesv1]
Set NProvincia = 'ALAJUELA'
Where Provincia In ('ALAJUELA');

Update [dbo].[Data_Defuncionesv1]
Set NProvincia = 'CARTAGO'
Where Provincia In ('CARTAGO');

Update [dbo].[Data_Defuncionesv1]
Set NProvincia = 'HEREDIA'
Where Provincia In ('HEREDIA');

Update [dbo].[Data_Defuncionesv1]
Set NProvincia = 'PUNTARENAS'
Where Provincia In ('PUNTARENAS');

Update [dbo].[Data_Defuncionesv1]
Set NProvincia = 'GUANACASTE'
Where Provincia In ('GUANACASTE');

Update [dbo].[Data_Defuncionesv1]
Set NProvincia = 'LIMON'
Where Provincia In ('LIMON','LIMÓN');

Update [dbo].[Data_Defuncionesv1]
Set NProvincia = 'CONSULADO'
Where Provincia In ('CONSULADO');

Select NProvincia, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Group By NProvincia
Order By Total Desc;

Select Top 100 * 
From [dbo].[Data_Defuncionesv1];

-- Nulos en descripcion de Canton
Alter Table [dbo].[Data_Defuncionesv1]
	Add NCanton VarChar(50);

Select NProvincia,NCanton, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Group By NProvincia, NCanton
Order By NProvincia Desc;

Select Provincia,Canton, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Group By Provincia, Canton
Order By Total Desc;

Update [dbo].[Data_Defuncionesv1]
Set NCanton = 'NO REGISTRADO'
Where Canton Is Null Or Canton = 'NULL' Or Canton = '';

Update [dbo].[Data_Defuncionesv1]
Set NCanton = Canton
Where NProvincia = 'SAN JOSE' And NCanton Is NULL;

Update [dbo].[Data_Defuncionesv1]
Set NCanton = Canton
Where NProvincia = 'ALAJUELA' And NCanton Is NULL;

Update [dbo].[Data_Defuncionesv1]
Set NCanton = Canton
Where NProvincia = 'CARTAGO' And NCanton Is NULL;

Update [dbo].[Data_Defuncionesv1]
Set NCanton = Canton
Where NProvincia = 'HEREDIA' And NCanton Is NULL;

Update [dbo].[Data_Defuncionesv1]
Set NCanton = Canton
Where NProvincia = 'GUANACASTE' And Canton != 'CAÃ‘AS' And NCanton Is NULL;

Update [dbo].[Data_Defuncionesv1]
Set NCanton = 'CAÑAS'
Where NProvincia = 'GUANACASTE' And Canton = 'CAÃ‘AS' And NCanton Is NULL;

Update [dbo].[Data_Defuncionesv1]
Set NCanton = Canton
Where NProvincia = 'LIMON' And NCanton Is NULL;

Update [dbo].[Data_Defuncionesv1]
Set NCanton = Canton
Where NProvincia = 'CONSULADO' And Canton != 'ESPAÃ‘A' And NCanton Is NULL;

Update [dbo].[Data_Defuncionesv1]
Set NCanton = 'ESPAÑA'
Where NProvincia = 'CONSULADO' And Canton = 'ESPAÃ‘A' And NCanton Is NULL;

Update [dbo].[Data_Defuncionesv1]
Set NCanton = Canton
Where NProvincia = 'PUNTARENAS' And NCanton Is NULL;


Select Provincia,Canton, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Where Provincia = 'SAN JOSE'
Group By Provincia, Canton
Order By Canton Desc;

Select Provincia,Canton, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Where Provincia = 'ALAJUELA'
Group By Provincia, Canton
Order By Canton Desc;

Select Provincia,Canton, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Where Provincia = 'CARTAGO'
Group By Provincia, Canton
Order By Canton Desc;

Select Provincia,Canton, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Where Provincia = 'HEREDIA'
Group By Provincia, Canton
Order By Canton Desc;

Select Provincia,Canton, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Where Provincia = 'GUANACASTE'
Group By Provincia, Canton
Order By Canton Desc;

Select Provincia,Canton, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Where Provincia = 'LIMON'
Group By Provincia, Canton
Order By Canton Desc;

Select Provincia,Canton, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Where Provincia = 'CONSULADO'
Group By Provincia, Canton
Order By Canton Desc;

Select Provincia,Canton, Count(ID) Total
From [dbo].[Data_Defuncionesv1]
Where Provincia = 'PUNTARENAS'
Group By Provincia, Canton
Order By Canton Desc;

Select ID,Fecha_Defuncion, NProvincia Provincia, NCanton Canton 
Into [dbo].[Data_DefuncionesFinalv1]
From [dbo].[Data_Defuncionesv1];

-- 3.	Construir las tablas de dimensiones necesarias para poder responder las 3 preguntas planteadas. 
Select * 
From [dbo].[Data_DefuncionesFinalv1];
-- fecha 
-- Tabla  BI.Dbo.Defunciones_Dim_Fecha
Select ROW_NUMBER() Over (Order By Fecha_Defuncion Asc) ID_Fecha, Fecha_Defuncion
	,Day(Fecha_Defuncion) Numero_Dia, Month(Fecha_Defuncion) Numero_Mes,Year(Fecha_Defuncion) Anno,
	Nombre_Dia = 
		Case
			When DATEPART(DW,Fecha_Defuncion) = 1 Then 'Domingo'
			When DATEPART(DW,Fecha_Defuncion) = 2 Then 'Lunes'
			When DATEPART(DW,Fecha_Defuncion) = 3 Then 'Martes'
			When DATEPART(DW,Fecha_Defuncion) = 4 Then 'Miercoles'
			When DATEPART(DW,Fecha_Defuncion) = 5 Then 'Jueves'
			When DATEPART(DW,Fecha_Defuncion) = 6 Then 'Viernes'
			When DATEPART(DW,Fecha_Defuncion) = 7 Then 'Sabado'
		End
		,Nombre_Mes = 
		Case
			When Month(Fecha_Defuncion) = 1 Then 'Enero'
			When Month(Fecha_Defuncion) = 2 Then 'Febrero'
			When Month(Fecha_Defuncion) = 3 Then 'Marzo'
			When Month(Fecha_Defuncion) = 4 Then 'Abril'
			When Month(Fecha_Defuncion) = 5 Then 'Mayo'
			When Month(Fecha_Defuncion) = 6 Then 'Junio'
			When Month(Fecha_Defuncion) = 7 Then 'Julio'
			When Month(Fecha_Defuncion) = 8 Then 'Agosto'
			When Month(Fecha_Defuncion) = 9 Then 'Septiembre'
			When Month(Fecha_Defuncion) = 10 Then 'Octubre'
			When Month(Fecha_Defuncion) = 11 Then 'Noviembre'
			When Month(Fecha_Defuncion) = 12 Then 'Diciembre'
		End
		Into BI.Dbo.Defunciones_Dim_Fecha
From (  Select Distinct Fecha_Defuncion From [dbo].[Data_DefuncionesFinalv1]) T1;

Select * 
From BI.Dbo.Defunciones_Dim_Fecha;

-- Provincia y Canton
-- Tabla BI.Dbo.Defunciones_Dim_ProvinciaCanton
Select ROW_NUMBER() Over (Order by Provincia, Canton) ID_PC,Provincia,Canton
Into BI.Dbo.Defunciones_Dim_ProvinciaCanton
From (Select Distinct Provincia,Canton 	From [dbo].[Data_DefuncionesFinalv1]) A;


Select *
From BI.Dbo.Defunciones_Dim_ProvinciaCanton;

-- Tabla Fac 
-- BI.Dbo.Defunciones_Fac_Defunciones
Select DDF.ID,DF.ID_Fecha,DPC.ID_PC
Into BI.Dbo.Defunciones_Fac_Defunciones
From [dbo].[Data_DefuncionesFinalv1] DDF
Join BI.dbo.Defunciones_Dim_Fecha DF On DF.Fecha_Defuncion = DDF.Fecha_Defuncion
Join BI.Dbo.Defunciones_Dim_ProvinciaCanton DPC On DPC.Provincia = DDF.Provincia And DPC.Canton = DDF.Canton;

Select *
From BI.Dbo.Defunciones_Fac_Defunciones;

