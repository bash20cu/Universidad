use UIALab07;
-- Insertando permisos en el catalogo
-- Insertar permisos relacionados con el menú  en catalogo_permisos
INSERT INTO catalogo_permisos (idcatalogo_permisos, descripcion, activo) 
VALUES (1, 'Administrar', 1), (2, 'Moderar', 1),(3, 'Usuario', 1), (4, 'Ayuda', 1);
SELECT `catalogo_permisos`.`idcatalogo_permisos`, `catalogo_permisos`.`descripcion`, `catalogo_permisos`.`activo`
FROM `uialab07`.`catalogo_permisos`;

-- Insertar opciones de nivel del menú
-- Insertar opciones de nivel 1 como permisos en la tabla "permisos"
INSERT INTO permisos (idPermisos, descripcion, activo, catalogo_permisos_idcatalogo_permisos) 
VALUES (1, 'crear usuario', 1, 1), (2, 'modificar usuario', 1, 1),(3, 'eiminar usuario', 1, 1);
-- Insertar opciones de nivel 2 como permisos en la tabla "permisos"
INSERT INTO permisos (idPermisos, descripcion, activo, catalogo_permisos_idcatalogo_permisos) 
VALUES (4, 'moderar publicacion', 1, 2), (5, 'editar publicacion', 1, 2), (6, 'confirmar publicacion', 1, 2);
-- Insertar opciones de nivel 3 como permisos en la tabla "permisos"
INSERT INTO permisos (idPermisos, descripcion, activo, catalogo_permisos_idcatalogo_permisos) 
VALUES (7, 'crear publicacion', 1, 3), (8, 'cancelar publicacion', 1, 3), (9, 'estadisticas de usuario', 1, 3);

SELECT `permisos`.`idPermisos`,`permisos`.`descripcion`,`permisos`.`activo`, `permisos`.`catalogo_permisos_idcatalogo_permisos`
FROM `uialab07`.`permisos`;
-- Revisando la tabla y las relaciones
SELECT p.idPermisos, p.descripcion AS Permiso, c.descripcion AS Opcion_Catalogo
FROM permisos AS p
INNER JOIN catalogo_permisos AS c ON p.catalogo_permisos_idcatalogo_permisos = c.idcatalogo_permisos;

-- Insertando Usuarios
INSERT INTO `uialab07`.`usuario`
(`idusuario`,`usuario`,`contrasena`,`activo`,`fecha_creado`,`usuario_idusuario`)
VALUES
(1,'admin','supercoolcontraseña',1,now(),1), -- usuario administrador
(2,'moderador', 'contrasena_moderador', 1, NOW(), 1), -- usuario moderador
(3,'usuario', 'contrasena_usuario', 1, NOW(), 2);  -- usuario 
SELECT `usuario`.`idusuario`, `usuario`.`usuario`, `usuario`.`contrasena`, `usuario`.`activo`, `usuario`.`fecha_creado`, `usuario`.`usuario_idusuario`
FROM `uialab07`.`usuario`;

-- Insertando los permisos a los usuarios
-- Asignar todos los permisos al administrador
INSERT INTO usuario_permiso (usuario_idusuario, permisos_idPermisos)
SELECT 1, idPermisos
FROM permisos;
-- Asignar permisos de nivel 2 a los moderadores
INSERT INTO usuario_permiso (usuario_idusuario, permisos_idPermisos)
SELECT u.idusuario, p.idPermisos
FROM usuario u
INNER JOIN permisos p ON u.usuario = 'moderador' 
AND  p.catalogo_permisos_idcatalogo_permisos = 2;

INSERT INTO usuario_permiso (usuario_idusuario, permisos_idPermisos)
SELECT u.idusuario, p.idPermisos
FROM usuario u
INNER JOIN permisos p ON u.usuario = 'moderador' 
AND  p.catalogo_permisos_idcatalogo_permisos = 3; 

-- Asignar permisos de nivel 3 a los usuarios
INSERT INTO usuario_permiso (usuario_idusuario, permisos_idPermisos)
SELECT u.idusuario, p.idPermisos
FROM usuario u
INNER JOIN permisos p ON u.usuario = 'usuario' AND p.catalogo_permisos_idcatalogo_permisos = 3; 

SELECT `usuario_permiso`.`usuario_idusuario`,`usuario_permiso`.`permisos_idPermisos`
FROM `uialab07`.`usuario_permiso`
ORDER BY  `usuario_permiso`.`usuario_idusuario` ASC;

-- verificando los permisos
SELECT u.idusuario, u.usuario, p.descripcion Nivel_Opcion ,  c.descripcion Descripcion_Permiso
FROM usuario u
LEFT JOIN usuario_permiso up ON u.idusuario = up.usuario_idusuario
LEFT JOIN permisos p ON up.permisos_idPermisos = p.idPermisos
LEFT JOIN catalogo_permisos c ON p.catalogo_permisos_idcatalogo_permisos = c.idcatalogo_permisos;
-- Join con 1 usuario, Moderador
SELECT u.idusuario, u.usuario, p.descripcion Nivel_Opcion ,  c.descripcion Descripcion_Permiso
FROM usuario u
LEFT JOIN usuario_permiso up ON u.idusuario = up.usuario_idusuario
LEFT JOIN permisos p ON up.permisos_idPermisos = p.idPermisos
LEFT JOIN catalogo_permisos c ON p.catalogo_permisos_idcatalogo_permisos = c.idcatalogo_permisos
WHERE u.idusuario = 2







