use [];

SELECT PE.Cedula cedula, PE.Nombre nombre, PE.Apellido1 apellido_1 , PE.Apellido1 apellido_2
FROM Padron_Electoral PE
WHERE PE.Nombre = 'MIGUEL';

CREATE INDEX ix_nombre ON Padron_Electoral(nombre);
CREATE INDEX ix_apellido1 ON Padron_Electoral(apellido1);
CREATE INDEX ix_apellido2 ON Padron_Electoral(apellido2);
DROP INDEX ix_nombre ON Padron_Electoral;
DROP INDEX ix_apellido1 ON Padron_Electoral;
DROP INDEX ix_apellido2 ON Padron_Electoral;

CREATE INDEX ix_nombrecompleto ON Padron_Electoral(nombre) INCLUDE (apellido1,apellido2);