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
  FROM [dbo].[crimenes OIJ 2023]

GO



-- Dimensiones 
-- Delito 
Select Distinct Delito
from [dbo].[crimenes OIJ 2023]
Order By Delito;

--Subdelito 
SELECT DISTINCT SubDelito
FROM [dbo].[crimenes OIJ 2023] 
ORDER BY SubDelito;
--Creamos una nueva Tabla Subdelito para ajustar 'DISCUSION/RI&#209;A' THEN 'DISCUSION/RIÑA'
Alter Table [dbo].[crimenes OIJ 2023]
	Add SubDelito_New VarChar(200);

UPDATE [dbo].[crimenes OIJ 2023]
	SET SubDelito_New =
		CASE
			WHEN SubDelito = 'DISCUSION/RI&#209;A' THEN 'DISCUSION/RIÑA'
			ELSE SubDelito
		END;

SELECT DISTINCT SubDelito_New
FROM [dbo].[crimenes OIJ 2023] 
ORDER BY SubDelito_New;

--Fecha
--Verificamos las fechas sean validas / Fechas Invalidas = 0
SELECT *
FROM [dbo].[crimenes OIJ 2023]
Where ISDATE(Fecha) = 0;

SELECT *
FROM [dbo].[crimenes OIJ 2023];

-- Victima
Select ROW_NUMBER() Over (Order By Hora) ID_Hora, *
--Into BI.Dbo.OIJ_Dim_Hora
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
	from [dbo].[crimenes OIJ 2023]
)A;

-- SubVictima
SELECT DISTINCT SubVictima
FROM [dbo].[crimenes OIJ 2023] 
ORDER BY SubVictima


