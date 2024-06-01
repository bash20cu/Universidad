use pcine;
-- Rellenando Catalogos
-- Rellenar la tabla IdiomaCatalogo
INSERT INTO pCine.IdiomaCatalogo (Descripcion, Activo)
VALUES  ('Español', 1), ('Inglés', 1), ('Francés', 1);
SELECT `idiomacatalogo`.`idcatalogo_tipo_pelicula`, `idiomacatalogo`.`Descripcion`, `idiomacatalogo`.`Activo`
FROM `pcine`.`idiomacatalogo`;

-- Rellenar la tabla CategoriaCatalogo
INSERT INTO pCine.CategoriaCatalogo (Descripcion, Activo)
VALUES ('Acción', 1),('Comedia', 1),('Drama', 1);
SELECT `categoriacatalogo`.`idcatalogo_categoria`, `categoriacatalogo`.`Descripcion`, `categoriacatalogo`.`Activo`
FROM `pcine`.`categoriacatalogo`;

-- Rellenar la tabla CatalogoGenero
INSERT INTO pCine.CatalogoGenero (Descripcion, Activo, Fecha)
VALUES ('Masculino', 1, NOW()),('Femenino', 1, NOW());
SELECT `catalogogenero`.`idCatalogoGenero`, `catalogogenero`.`Descripcion`, `catalogogenero`.`Activo`,  `catalogogenero`.`Fecha`
FROM `pcine`.`catalogogenero`;

-- Rellenar la tabla CatalogoTelefono
INSERT INTO pCine.CatalogoTelefono (Descripcion, Activo, Fecha)
VALUES ('Móvil', 1, NOW()),('Casa', 1, NOW()),('Trabajo', 1, NOW());
SELECT `catalogotelefono`.`idCatalogoTelefono`, `catalogotelefono`.`Descripcion`, `catalogotelefono`.`Activo`, `catalogotelefono`.`Fecha`
FROM `pcine`.`catalogotelefono`;

-- Rellenar la tabla CatalogoCorreoElectronico
INSERT INTO pCine.CatalogoCorreoElectronico (Descripcion, Activo, Fecha)
VALUES  ('Personal', 1, NOW()), ('Trabajo', 1, NOW()), ('Otro', 1, NOW());
SELECT `catalogocorreoelectronico`.`idCatalogoCorreoElectronico`,`catalogocorreoelectronico`.`Descripcion`, `catalogocorreoelectronico`.`Activo`,
    `catalogocorreoelectronico`.`Fecha`
FROM `pcine`.`catalogocorreoelectronico`;

-- Rellenar la tabla CatalogoDescuento
INSERT INTO `pCine`.`CatalogoDescuento` (`Descripcion`, `Activo`) VALUES
('Descuento de Estreno', 1),('Descuento Estudiante', 1);
SELECT `catalogodescuento`.`idCatalogoDescuento`, `catalogodescuento`.`Descripcion`, `catalogodescuento`.`Activo`
FROM `pcine`.`catalogodescuento`;

-- Rellenar la tabla CatalogoAsientos
INSERT INTO pCine.CatalogoAsientos (Descripcion, Activo, Fecha)
VALUES ('Asiento VIP', 1, NOW()), ('Asiento Regular', 1, NOW()), ('Asiento Preferencial', 1, NOW());
SELECT `catalogoasientos`.`idCatalogoAsientos`, `catalogoasientos`.`Descripcion`, `catalogoasientos`.`Activo`,
    `catalogoasientos`.`Fecha`
FROM `pcine`.`catalogoasientos`;

-- Rellenar la tabla CatalogoTipoPago
INSERT INTO pCine.CatalogoTipoPago (Descripcion, Activo)
VALUES ('Efectivo', 1),('Tarjeta de Crédito', 1), ('Tarjeta de Débito', 1);
SELECT `catalogotipopago`.`idCatalogoTipoPago`, `catalogotipopago`.`Descripcion`, `catalogotipopago`.`Activo`
FROM `pcine`.`catalogotipopago`;

-- Insertando 9 personas 
INSERT INTO pCine.Persona (Nombre, Apellido1, Apellido2, Fecha_Nacimiento, Fecha_Introducido, CatalogoGenero)
VALUES ('Juan', 'Gómez', 'Pérez', '1990-01-15', NOW(), 1), ('María', 'López', 'Martínez', '1985-05-20', NOW(), 2),
    ('Carlos', 'Fernández', 'Ruiz', '1995-09-10', NOW(), 1), ('Ana', 'Martínez', 'Gómez', '1988-03-25', NOW(), 2),
    ('Pedro', 'González', 'Ramírez', '1992-11-12', NOW(), 1), ('Laura', 'Rodríguez', 'Sánchez', '1983-07-08', NOW(), 2),
    ('Miguel', 'Díaz', 'Hernández', '1998-04-18', NOW(), 1), ('Isabel', 'Vega', 'López', '1993-06-30', NOW(), 2),
    ('Javier', 'Serrano', 'Gutiérrez', '1980-09-05', NOW(), 1);

SELECT   persona.idPersona,  persona.Nombre,  persona.Apellido1,  persona.Apellido2,  persona.Fecha_Nacimiento,  persona.Fecha_Introducido, 
    catalogogenero.Descripcion as Genero
FROM pcine.persona
JOIN pcine.CatalogoGenero ON persona.CatalogoGenero = CatalogoGenero.idCatalogoGenero;

-- Insertar teléfonos para las 9 personas
INSERT INTO pCine.Telefono (Numero_Telefono, Activo, CatalogoTelefono, Persona_idPersona, FechaIngreso)
VALUES
    ('123456789', 1, 1, 1, NOW()), ('987654321', 1, 2, 1, NOW()), ('555555555', 1, 3, 1, NOW()), -- Juan
    ('111111111', 1, 1, 2, NOW()), ('222222222', 1, 2, 2, NOW()), ('333333333', 1, 3, 2, NOW()), -- María
    ('444444444', 1, 1, 3, NOW()), ('555666666', 1, 2, 3, NOW()), ('666666666', 1, 3, 3, NOW()), -- Carlos
    ('777777777', 1, 1, 4, NOW()), ('888888888', 1, 2, 4, NOW()), ('999999999', 1, 3, 4, NOW()), -- Ana
    ('111222333', 1, 1, 5, NOW()), ('222333444', 1, 2, 5, NOW()), ('333444555', 1, 3, 5, NOW()), -- Pedro
    ('444555666', 1, 1, 6, NOW()), ('555666777', 1, 2, 6, NOW()), ('666777888', 1, 3, 6, NOW()), -- Laura
    ('777888999', 1, 1, 7, NOW()), ('888999000', 1, 2, 7, NOW()), ('999000111', 1, 3, 7, NOW()), -- Miguel
    ('111224333', 1, 1, 8, NOW()), ('222335444', 1, 2, 8, NOW()), ('333844555', 1, 3, 8, NOW()), -- Isabel
    ('444575666', 1, 1, 9, NOW()), ('559666777', 1, 2, 9, NOW()), ('666774888', 1, 3, 9, NOW()); -- Javier  

-- Seleccionar información de personas con su género y teléfono
SELECT  persona.idPersona,  persona.Nombre, persona.Apellido1, persona.Apellido2,  catalogogenero.Descripcion as Genero, 
    telefono.Numero_Telefono,  catalogotelefono.Descripcion as Descripcion_Telefono
FROM    pcine.persona
JOIN    pcine.CatalogoGenero ON persona.CatalogoGenero = CatalogoGenero.idCatalogoGenero
JOIN    pcine.Telefono ON persona.idPersona = Telefono.Persona_idPersona
JOIN    pcine.CatalogoTelefono ON Telefono.CatalogoTelefono = CatalogoTelefono.idCatalogoTelefono;


-- Insertar correos electrónicos para la personas
-- Insertar correos electrónicos para las 9 personas
INSERT INTO pcine.CorreoElectronico (CorreoElectronico, Descripcion, Activo, Fecha, CatalogoCorreoElectronico, Persona_idPersona)
VALUES ('correo1@dominio.com', 'Correo Personal', 1, NOW(), 1, 1), ('correo2@dominio.com', 'Correo Trabajo', 1, NOW(), 2, 1), ('correo3@dominio.com', 'Correo Otro', 1, NOW(), 3, 1), -- Juan
    ('correo4@dominio.com', 'Correo Personal', 1, NOW(), 1, 2), ('correo5@dominio.com', 'Correo Trabajo', 1, NOW(), 2, 2), ('correo6@dominio.com', 'Correo Otro', 1, NOW(), 3, 2), -- María
    ('correo7@dominio.com', 'Correo Personal', 1, NOW(), 1, 3), ('correo8@dominio.com', 'Correo Trabajo', 1, NOW(), 2, 3), ('correo9@dominio.com', 'Correo Otro', 1, NOW(), 3, 3), -- Carlos
    ('correo10@dominio.com', 'Correo Personal', 1, NOW(), 1, 4), ('correo11@dominio.com', 'Correo Trabajo', 1, NOW(), 2, 4), ('correo12@dominio.com', 'Correo Otro', 1, NOW(), 3, 4), -- Ana
    ('correo13@dominio.com', 'Correo Personal', 1, NOW(), 1, 5), ('correo14@dominio.com', 'Correo Trabajo', 1, NOW(), 2, 5), ('correo15@dominio.com', 'Correo Otro', 1, NOW(), 3, 5), -- Pedro
    ('correo16@dominio.com', 'Correo Personal', 1, NOW(), 1, 6), ('correo17@dominio.com', 'Correo Trabajo', 1, NOW(), 2, 6), ('correo18@dominio.com', 'Correo Otro', 1, NOW(), 3, 6), -- Laura
    ('correo19@dominio.com', 'Correo Personal', 1, NOW(), 1, 7), ('correo20@dominio.com', 'Correo Trabajo', 1, NOW(), 2, 7), ('correo21@dominio.com', 'Correo Otro', 1, NOW(), 3, 7), -- Miguel
    ('correo22@dominio.com', 'Correo Personal', 1, NOW(), 1, 8), ('correo23@dominio.com', 'Correo Trabajo', 1, NOW(), 2, 8), ('correo24@dominio.com', 'Correo Otro', 1, NOW(), 3, 8), -- Isabel
    ('correo25@dominio.com', 'Correo Personal', 1, NOW(), 1, 9), ('correo26@dominio.com', 'Correo Trabajo', 1, NOW(), 2, 9), ('correo27@dominio.com', 'Correo Otro', 1, NOW(), 3, 9); -- Javier

SELECT  persona.idPersona,  persona.Nombre, persona.Apellido1, persona.Apellido2,  catalogogenero.Descripcion as Genero, 
    telefono.Numero_Telefono,  catalogotelefono.Descripcion as Descripcion_Telefono, correoelectronico.CorreoElectronico,
    catalogocorreoelectronico.Descripcion as Descripcion_CorreoElectronico
FROM    pcine.persona
JOIN    pcine.CatalogoGenero ON persona.CatalogoGenero = CatalogoGenero.idCatalogoGenero
JOIN    pcine.Telefono ON persona.idPersona = Telefono.Persona_idPersona
JOIN    pcine.CatalogoTelefono ON Telefono.CatalogoTelefono = CatalogoTelefono.idCatalogoTelefono
JOIN    pcine.CorreoElectronico ON persona.idPersona = CorreoElectronico.Persona_idPersona
JOIN    pcine.CatalogoCorreoElectronico ON CorreoElectronico.CatalogoCorreoElectronico = CatalogoCorreoElectronico.idCatalogoCorreoElectronico;
-- Seleccionar información de personas agrupadas por su id, con género, teléfono y correo electrónico
SELECT persona.idPersona, persona.Nombre,  persona.Apellido1,  persona.Apellido2,  catalogogenero.Descripcion as Genero,
    (SELECT telefono.Numero_Telefono FROM pcine.Telefono WHERE persona.idPersona = Telefono.Persona_idPersona ORDER BY telefono.Numero_Telefono LIMIT 1) as Primer_Telefono,
    (SELECT correoelectronico.CorreoElectronico FROM pcine.CorreoElectronico WHERE persona.idPersona = CorreoElectronico.Persona_idPersona ORDER BY correoelectronico.CorreoElectronico LIMIT 1) as Primer_CorreoElectronico
FROM pcine.persona
JOIN pcine.CatalogoGenero ON persona.CatalogoGenero = CatalogoGenero.idCatalogoGenero;


-- Insertar Colaboradores (Administrador Persona 1)
INSERT INTO `pCine`.`Colaborador` (`idColaborador`, `Colaborador_idColaborador`, `Persona_idPersona`)
VALUES (1, 1, 1), (2, 1, 2), (3, 1, 3), (4, 1, 4);
-- Seleccionar información de colaboradores
SELECT    colaborador.idColaborador,  colaborador.Colaborador_idColaborador Supervisor,
    persona.Nombre nombre, persona.Apellido1 apellido, persona.Apellido2 apellido_2, catalogogenero.Descripcion as Genero
FROM    pcine.Colaborador colaborador
JOIN    pcine.Persona persona ON colaborador.Persona_idPersona = persona.idPersona
JOIN    pcine.CatalogoGenero ON persona.CatalogoGenero = CatalogoGenero.idCatalogoGenero;

-- Insertar horarios
INSERT INTO `pCine`.`Horarios` (`Dia`, `HoraInicio`, `HoraFinal`) VALUES
('2023-11-13', '09:00:00', '17:00:00'),('2023-11-14', '09:00:00', '17:00:00'),('2023-11-15', '09:00:00', '17:00:00'),
('2023-11-16', '09:00:00', '17:00:00'),('2023-11-17', '09:00:00', '17:00:00');
SELECT `horarios`.`idHorario`, `horarios`.`Dia`, `horarios`.`HoraInicio`, `horarios`.`HoraFinal`
FROM `pcine`.`horarios`;
-- Asignar horarios a colaboradores
INSERT INTO `pCine`.`HorariosColaborador` (`Horarios`, `Colaborador`) VALUES
(1, 1),(2, 1), (3, 1), (4, 1), (5, 1), -- Juan
(1, 3), (2, 3),(3, 3), (4, 3), (5, 3), -- Carlos
(1, 2),(2, 2),(3, 2),(4, 2),(5, 2), -- María
(1, 4),(2, 4),(3, 4),(4, 4),(5, 4); -- Ana
-- Seleccionar horarios de colaboradores
SELECT colaborador.idColaborador,persona.Nombre,persona.Apellido1, horarios.Dia,
    horarios.HoraInicio,horarios.HoraFinal
FROM  pcine.Colaborador colaborador
JOIN  pcine.Persona persona ON colaborador.Persona_idPersona = persona.idPersona
JOIN  pcine.HorariosColaborador horariosColab ON colaborador.idColaborador = horariosColab.Colaborador
JOIN  pcine.Horarios horarios ON horariosColab.Horarios = horarios.idHorario;

-- Insertar Clientes
INSERT INTO `pCine`.`Cliente` (`idCliente`, `Persona_idPersona`, `Frecuente`, `Fecha_Ingreso`)
VALUES (1, 5, 0, CURRENT_TIMESTAMP), (2, 6, 0, CURRENT_TIMESTAMP), (3, 7, 0, CURRENT_TIMESTAMP), (4, 8, 0, CURRENT_TIMESTAMP), (5, 9, 0, CURRENT_TIMESTAMP);
-- Seleccionar información del cliente con primer teléfono y primer correo electrónico
SELECT persona.idPersona,persona.Nombre,persona.Apellido1,cliente.Frecuente cliente_frecuente, pcine.cliente.idCliente
FROM   pcine.Cliente
JOIN   pcine.Persona ON Cliente.Persona_idPersona = persona.idPersona;

----------------------------------------
-- Insertar películas
INSERT INTO `pCine`.`pelicula` (`idpelicula`, `Titulo`, `Descripcion`, `Fecha`, `Activo`, `IdiomaCatalogo`, `CategoriaCatalogo`, `duracion`) 
VALUES (1, 'Pelicula 1', 'Descripción 1', '2023-11-11 18:00:00', 1, 1, 1, 2.5),(2, 'Pelicula 2', 'Descripción 2', '2023-11-12 20:00:00', 1, 2, 2, 2.0),
(3, 'Pelicula 3', 'Descripción 3', '2023-11-13 15:30:00', 1, 3, 1, 1.8);
SELECT   p.`idpelicula`,p.`Titulo`,p.`Descripcion`,p.`Fecha`,ic.`Descripcion` Idioma, cc.Descripcion Categoria, p.`duracion`
FROM `pCine`.`pelicula` p
JOIN `pCine`.`IdiomaCatalogo` ic ON p.`IdiomaCatalogo` = ic.`idcatalogo_tipo_pelicula`
JOIN `pCine`.`CategoriaCatalogo` cc ON p.`CategoriaCatalogo` = cc.`idcatalogo_categoria`;

-- Insertar horarios
INSERT INTO `pCine`.`Horarios` (`Dia`, `HoraInicio`, `HoraFinal`)
VALUES('2023-11-11', '18:00:00', '20:30:00'),('2023-11-12', '20:00:00', '22:00:00'),('2023-11-13', '15:30:00', '17:30:00');
SELECT `horarios`.`idHorario`, `horarios`.`Dia`,`horarios`.`HoraInicio`,`horarios`.`HoraFinal`FROM `pcine`.`horarios`;

-- Insertar salas
INSERT INTO `pCine`.`Sala` (`idSala`, `Ubicacion`, `Capacidad`, `Cantidad_Asientos`)
VALUES(1, 'Sala 1', 100, 100),(2, 'Sala 2', 80, 80),(3, 'Sala 3', 120, 120);
SELECT `sala`.`idSala`, `sala`.`Ubicacion`, `sala`.`Capacidad`, `sala`.`Cantidad_Asientos`
FROM `pcine`.`sala`;

-- Insertar horarios de películas en salas
INSERT INTO `pCine`.`HorarioSalaPelicula` (`Horarios_idHorario`, `Sala_idSala`, `pelicula_idpelicula`)
VALUES(6, 1, 1),(7, 2, 2),(8, 3, 3);

SELECT  s.`Ubicacion` `UbicacionSala`,  p.`Titulo` `TituloPelicula`, h.`Dia`  `FechaHorario`, h.`HoraInicio` `Hora de Inicio`, h.`HoraFinal` `Hora final`,
	TIMEDIFF(h.`HoraFinal`, h.`HoraInicio`) `Duracion Proyeccion` -- Calculo de diferencia de horas (Hfinal - Hinicio)
FROM `pCine`.`HorarioSalaPelicula` hsp
JOIN `pCine`.`Sala` s ON hsp.`Sala_idSala` = s.`idSala`
JOIN `pCine`.`pelicula` p ON hsp.`pelicula_idpelicula` = p.`idpelicula`
JOIN `pCine`.`Horarios` h ON hsp.`Horarios_idHorario` = h.`idHorario`;

-- Precios
-- Insertar registros en la tabla `Descuento`
INSERT INTO `pCine`.`Descuento` (`idDescuento`,`Procentaje`,`Activo`,`CatalogoDescuento`,`Fecha_Inicio`,`Fecha_Final`) VALUES
(1,10.00, 1, 1, '2023-01-01', '2023-12-31'),(2,5.00, 1, 2, '2023-01-01', '2023-12-31'),(3,5.00, 1, 2, '2023-01-01', '2023-12-31');
SELECT `descuento`.`idDescuento`, `descuento`.`Procentaje`, `descuento`.`Activo`, `descuento`.`CatalogoDescuento`, `descuento`.`Fecha_Inicio`,
    `descuento`.`Fecha_Final`
FROM `pcine`.`descuento`;

-- Insertar registros en la tabla `Precio`
INSERT INTO `pCine`.`Precio` (`Descuento_idDescuento`, `Precio_Final`) VALUES
(1, 90.00),(2, 80.00);
SELECT `precio`.`idPrecios`, `precio`.`Descuento_idDescuento`, `precio`.`Precio_Final`
FROM `pcine`.`precio`;

-- Insertar los asientos de cada sala
INSERT INTO `pcine`.`asiento`(`idAsiento`,`CatalogoAsientos`,`Sala_idSala`,`Activo`,`Precio_idPrecios`)
VALUES
('A1', 1, 1, 1, 1),('A2', 1, 1, 1, 2),('B1', 2, 1, 1, 2), ('B2', 2, 1, 1, 2),('C1', 3, 1, 1, 2), ('C2', 3, 1, 1, 1);
SELECT a.`idAsiento`, ca.`Descripcion` `Descripcion`, s.`Ubicacion` `Sala`
FROM `pCine`.`Asiento` a
JOIN `pCine`.`CatalogoAsientos` ca ON a.`CatalogoAsientos` = ca.`idCatalogoAsientos`
JOIN `pCine`.`Sala` s ON a.`Sala_idSala` = s.`idSala`;

-- Seleccionar la información del asiento con el precio calculado
SELECT  a.idAsiento,ca.Descripcion AS CatalogoAsientos,s.Ubicacion AS SalaUbicacion,
  -- Calcular el precio con descuento
    (pr.Precio_Final - (pr.Precio_Final * d.Procentaje / 100)) AS PrecioConDescuento
FROM   pCine.Asiento a
    JOIN pCine.CatalogoAsientos ca ON a.CatalogoAsientos = ca.idCatalogoAsientos
    JOIN pCine.Sala s ON a.Sala_idSala = s.idSala
    JOIN pCine.Precio pr ON a.CatalogoAsientos = pr.Descuento_idDescuento
    JOIN pCine.Descuento d ON pr.Descuento_idDescuento = d.idDescuento;


--  descuentos
INSERT INTO `pCine`.`Descuento` (`Procentaje`, `Activo`, `CatalogoDescuento`, `Fecha_Inicio`, `Fecha_Final`)
VALUES   (10.00, 1, 1, '2023-11-11 00:00:00', '2023-12-31 23:59:59'),  (15.00, 1, 1, '2023-11-11 00:00:00', '2023-12-31 23:59:59'),
    (20.00, 1, 1, '2023-11-11 00:00:00', '2023-12-31 23:59:59');
SELECT `descuento`.`idDescuento`, `descuento`.`Procentaje`,  `descuento`.`Activo`,    `descuento`.`CatalogoDescuento`,
    `descuento`.`Fecha_Inicio`,   `descuento`.`Fecha_Final`
FROM `pcine`.`descuento`;


-- Facturas 
-- Factura para Clientes
INSERT INTO `pCine`.`Factura` (`Fecha`, `Anulado`, `Total`, `Cliente_idCleinte`, `Colaborador_idColaborador`)
VALUES  ('2023-11-11 12:00:00', 0, 10.00, 3, 2),  ('2023-11-11 12:00:00', 0, 15.00, 4, 2),('2023-11-11 12:00:00', 0, 20.00, 5, 3);
SELECT `factura`.`idFactura`, `factura`.`Fecha`,`factura`.`Anulado`, `factura`.`Total`, `factura`.`Cliente_idCleinte`, `factura`.`Colaborador_idColaborador`
FROM `pcine`.`factura`;

-- Insertar tres líneas por cada factura (ejemplo)
INSERT INTO pCine.FacturaDetalle (Precio, Subtotal, Factura_idFactura, Asiento_idAsiento, Descuento_idDescuento)
VALUES  -- Línea 1: Asiento 1 sin descuento
    ((SELECT Precio_Final FROM pCine.Precio WHERE idPrecios = 1) * 1, (SELECT Precio_Final FROM pCine.Precio WHERE idPrecios = 1) * 1, 1, 'A1', 1),    
    -- Línea 2: Asiento 2 sin descuento
    ((SELECT Precio_Final FROM pCine.Precio WHERE idPrecios = 1) * 1, (SELECT Precio_Final FROM pCine.Precio WHERE idPrecios = 1) * 1, 1, 'A2', 1),
    -- Línea 3: Descuento de cliente frecuente
    (0.00, -((SELECT Procentaje FROM pCine.Descuento WHERE idDescuento = 2) / 100) * ((SELECT Precio_Final FROM pCine.Precio WHERE idPrecios = 1) * 2), 1, 'A1', 2);

SELECT `facturadetalle`.`idFacturaDetalle`,`facturadetalle`.`Precio`,`facturadetalle`.`Subtotal`,
    `facturadetalle`.`Factura_idFactura`,`facturadetalle`.`Asiento_idAsiento`, `facturadetalle`.`Descuento_idDescuento`
FROM `pcine`.`facturadetalle`;

-- Actualizar el campo Total en Factura para la factura con idFactura = 1
UPDATE `pCine`.`Factura` AS F
SET F.Total = (SELECT SUM(FD.Subtotal) FROM `pCine`.`FacturaDetalle` AS FD WHERE FD.Factura_idFactura = 1),
    F.Fecha = NOW()
WHERE F.idFactura = 1;
SELECT `factura`.`idFactura`, `factura`.`Fecha`,`factura`.`Anulado`, `factura`.`Total`, `factura`.`Cliente_idCleinte`, `factura`.`Colaborador_idColaborador`
FROM `pcine`.`factura`;
-- Metodo de pago    
INSERT INTO `pcine`.`facturametodopago`
(`Monto`,`Factura_idFactura`,`CatalogoTipoPago`)
VALUES (50.00, 1, 1);

-- Seleccionar información combinada de factura y método de pago
SELECT F.idFactura, F.Fecha, Per.Nombre Nombre_Cliente, F.Total, CTP.Descripcion TipoPago
FROM `pcine`.`factura` F
JOIN `pcine`.`facturametodopago` FP ON F.idFactura = FP.Factura_idFactura
JOIN `pcine`.catalogotipopago CTP ON FP.CatalogoTipoPago = CTP.idCatalogoTipoPago
JOIN `pcine`.cliente CL ON F.Cliente_idCleinte = CL.idCliente
JOIN `pcine`.persona Per ON CL.Persona_idPersona = Per.idPersona
WHERE F.idFactura = 1;  

INSERT INTO `pcine`.`compras` 
(`Fecha`, `Monto`, `Cliente_idCleinte`, `Descuento_idDescuento`) 
VALUES 
((SELECT Fecha FROM `pcine`.`Factura` WHERE idFactura = 1), (SELECT Total FROM `pcine`.`Factura` WHERE idFactura = 1),  3,  1),
((SELECT Fecha FROM `pcine`.`Factura` WHERE idFactura = 2),  (SELECT Total FROM `pcine`.`Factura` WHERE idFactura = 2),  4,  1),
((SELECT Fecha FROM `pcine`.`Factura` WHERE idFactura = 3),  (SELECT Total FROM `pcine`.`Factura` WHERE idFactura = 3), 5,  1);
SELECT `compras`.`idCompras`, `compras`.`Fecha`, `compras`.`Monto`, `compras`.`Cliente_idCleinte`, `compras`.`Descuento_idDescuento`
FROM `pcine`.`compras`;


/*
Escribir una instrucción DQL que muestre un reporte por cliente, fecha y cantidad 
de compras realizadas, así como el total facturado para esa fecha, para esto debe utilizar un 
DQL con al menos dos Joins.
*/

SELECT
    Per.Nombre  Cliente,
    Cmp.Fecha  Fecha,
    COUNT(CL.idCliente) CantidadCompras,
    SUM(Cmp.Monto) TotalFacturado
FROM
    `pcine`.compras  Cmp
JOIN `pcine`.cliente CL ON Cmp.Cliente_idCleinte = CL.idCliente
JOIN `pcine`.persona Per ON CL.Persona_idPersona = Per.idPersona
GROUP BY
    Per.Nombre, Cmp.Fecha;





    


    











