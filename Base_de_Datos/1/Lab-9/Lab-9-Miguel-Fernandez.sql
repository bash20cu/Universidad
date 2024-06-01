/*
Escribir una instrucci�n DQL que este dentro de un Stored Procedure que genere un XML para el cliente con el customerID 1060.
El XML debe cumplir con lo siguiente: 
a.	Nombre del nodo ra�z: formulario_ 
b.	Del cliente se debe desplegar el customerid como id_cliente, 
	el customername como el nombre_cliente y se debe desplegar dentro de una etiqueta llamada 
	facturas, cada una de las facturas que ese cliente ha comprado, mostrando los datos
	de la fecha de la factura y el total facturado (usando la ecuaci�n quantity * unitprice) 
c.	Se debe mostrar  como elementos y no como atributos. 
*/

USE [WideWorldImporters];
--DQL
SELECT
    C.CustomerID  id_cliente, C.CustomerName nombre_cliente,
    (   SELECT SI.InvoiceID id_factura, SI.InvoiceDate fecha_factura, SUM(SO.Quantity * SO.UnitPrice) total_facturado
        FROM [Sales].[Invoices] SI
        JOIN [Sales].[OrderLines] SO ON SI.InvoiceID = SO.OrderID
        WHERE C.CustomerID = SI.CustomerID
        GROUP BY SI.InvoiceID, SI.InvoiceDate
        FOR XML PATH('factura'), TYPE
    )detalle_facturas
FROM [Sales].[Customers] C
WHERE  C.CustomerID = 1060
FOR XML PATH('cliente'), ROOT('formulario_');

--SP

CREATE or ALTER PROCEDURE dbo.lab_9_obtener_cliente
	@cliente int
AS
SELECT
    C.CustomerID  id_cliente, C.CustomerName nombre_cliente,
    (   SELECT SI.InvoiceID id_factura, SI.InvoiceDate fecha_factura, SUM(SO.Quantity * SO.UnitPrice) total_facturado
        FROM [Sales].[Invoices] SI
        JOIN [Sales].[OrderLines] SO ON SI.InvoiceID = SO.OrderID
        WHERE C.CustomerID = SI.CustomerID
        GROUP BY SI.InvoiceID, SI.InvoiceDate
        FOR XML PATH('factura'), TYPE
    )detalle_facturas
FROM [Sales].[Customers] C
WHERE C.CustomerID = @cliente
FOR XML PATH('cliente'), ROOT('formulario_');

EXEC dbo.lab_9_obtener_cliente @cliente = 1060
