USE UIA;

exec sp_columns Padron_Electoral;

/* 1.	Seleccionar a todas las personas que viven en el código electoral 10302023  */
Select PE.Cedula ID,PE.Nombre,PE.Apellido1 Primer_Apellido,PE.Apellido2 Segundo_Apellido, PE.Cod_Elec
From Padron_Electoral PE
Where PE.Cod_Elec = 10302023; 

Select COUNT( PE.Cedula)
from Padron_Electoral PE
where PE.Cod_Elec = 10302023;


/*
2.	Seleccionar la fecha de caducidad mayor y menor, en la misma instrucción, para ello debe investigar 
el concepto de min y max. La fecha menor se debe llamar valor_min, la fecha mayor se debe llamar valor_max 
*/
Select  MIN(PE.Fecha_Vencimiento) valor_min, MAX(PE.Fecha_Vencimiento) valor_max
From Padron_Electoral PE;

/*
3.	Seleccionar el nombre más extenso y el menos extenso. Usar la función LEN. 

Usando consultas anidadas
*/
SELECT
    (SELECT TOP 1 PE.Nombre FROM Padron_Electoral PE ORDER BY LEN(PE.Nombre) DESC, PE.Nombre) Nombre_Mas_Extenso,
    (SELECT TOP 1 PE.Nombre FROM Padron_Electoral PE ORDER BY LEN(PE.Nombre) ASC, PE.Nombre) Nombre_Menos_Extenso;


/* 4.	Seleccionar a todas las personas cuyo nombre contenga la palabra “RAMBO”. */
Select  PE.Cedula ID,PE.Nombre,PE.Apellido1 Primer_Apellido,PE.Apellido2 Segundo_Apellido
FROM Padron_Electoral PE
WHERE PE.Nombre LIKE '%RAMBO%';


/*
5.	Seleccionar a todas las personas cuyo nombre contenga la palabra “RAMBO” y
además el apellido1 y el apellido2 empiece con una vocal y termine con una vocal.

[AEIOU] => Arreglos de vocales.
*/
Select  PE.Cedula ID,PE.Nombre,PE.Apellido1 Primer_Apellido,PE.Apellido2 Segundo_Apellido
From Padron_Electoral PE
Where PE.Nombre LIKE '%RAMBO%'
And (
	(PE.Apellido1 like '[AEIOU]%[AEIOU]' and PE.Apellido2 like '[AEIOU]%[AEIOU]') Or
	(PE.Apellido2 like '[AEIOU]%[AEIOU]' and PE.Apellido1 like '[AEIOU]%[AEIOU]')
)

/* 
6.	Contabilizar a todas las personas donde el nombre empiece con “KOBE BRY” 
*/
Select Count (PE.Nombre) Total
From Padron_Electoral PE
Where PE.Nombre Like 'KOBE BRY%';

/*
7.	Seleccionar a todas las personas donde el nombre empiece con “KOBE BRY” y 
el primer apellido no empiece con una vocal. 
*/
Select  PE.Cedula ID,PE.Nombre,PE.Apellido1 Primer_Apellido,PE.Apellido2 Segundo_Apellido
From Padron_Electoral PE
Where PE.Nombre LIKE 'KOBE BRY%'
And PE.Apellido1 not like '[AEIOU]%[AEIOU]';


/*
8.	Seleccionar los 15 primeros apellidos (apellido1) más comunes y 
el total de ocurrencias por cada apellido. 
*/
Select Top 15 PE.Apellido1 Apellidos, Count (Apellido1) Total
From Padron_Electoral PE
Group by PE.Apellido1
Order By Total DESC;

/*
9.	Seleccionar los 10 códigos electorales donde existen mayor 
población inscrita para votar. 
*/

Select Top 10
    PE.Cod_Elec  Codigo_Electoral,
    Count(PE.nombre) Poblacion_Total
From Padron_Electoral PE
GROUP BY Cod_Elec
ORDER BY Poblacion_Total DESC;

/*
10.	Determinar todas las personas que en el nombre contenga JUAN y 
el apellido1 sea SANTAMARIA y el apellido2 sea RODRIGUEZ, 
sin importar si tildan (Santamaría) o Rodríguez.

*/

Select  PE.Cedula ID,PE.Nombre,PE.Apellido1 Primer_Apellido,PE.Apellido2 Segundo_Apellido
From Padron_Electoral PE
Where PE.Nombre like '%JUAN%' 
	AND PE.Apellido1 like 'SANTAMAR_A%'   
	AND PE.Apellido2 like 'RODR_GUEZ';