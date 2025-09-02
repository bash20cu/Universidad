use pcine;

select * from pcine.cliente;

SELECT
    Per.Nombre  Cliente,
    Cmp.Fecha  Fecha,
    COUNT(CL.idCliente) CantidadCompras,
    SUM(Cmp.Monto) TotalFacturado
FROM pcine.compras  Cmp
JOIN pcine.cliente CL ON Cmp.Cliente_idCleinte = CL.idCliente
JOIN pcine.persona Per ON CL.Persona_idPersona = Per.idPersona
GROUP BY Per.Nombre, Cmp.Fecha;


SELECT  Per.Nombre Cliente, Cmp.Fecha Fecha, (
        SELECT
            Det.Factura_idFactura numero_factura,
            Det.Precio Precio,
            Det.Asiento_idAsiento Asiento,
			Det.Descuento_idDescuento Descuento
        FROM pcine.facturadetalle Det
        WHERE Det.idFacturaDetalle = Det.Factura_idFactura
        FOR XML PATH('detalle'), TYPE
    ) AS detalle_factura
FROM pcine.compras Cmp
JOIN pcine.cliente CL ON Cmp.Cliente_idCleinte = CL.idCliente
JOIN pcine.persona Per ON CL.Persona_idPersona = Per.idPersona
GROUP BY Per.Nombre, Cmp.Fecha
FOR XML PATH('Compra'), ROOT('Compras');


SELECT Per.Nombre cliente,F.Fecha fecha, F.Total total_facturado,
    (SELECT FD.idFacturaDetalle detalle_id, FD.Precio precio, FD.Subtotal subtotal, FD.Asiento_idAsiento asiento, CD.Descripcion descuento, D.Procentaje porcentaje_descuento
        FROM pCine.FacturaDetalle FD
        LEFT JOIN pCine.CatalogoDescuento CD ON FD.Descuento_idDescuento = CD.idCatalogoDescuento
		LEFT JOIN pcine.descuento D ON FD.Descuento_idDescuento = D.idDescuento 
        WHERE FD.Factura_idFactura IN (1, 2, 3) --los 3 clientes
        FOR XML PATH('detalle'), TYPE ) AS detalles_factura
FROM pCine.Factura F
JOIN pCine.Cliente C ON F.Cliente_idCleinte = C.idCliente
JOIN pCine.Persona Per ON C.Persona_idPersona = Per.idPersona
WHERE F.idFactura IN (1, 2, 3) -- los 3 clientes
FOR XML PATH('factura'), ROOT('facturas');
