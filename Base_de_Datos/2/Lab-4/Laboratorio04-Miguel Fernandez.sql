USE BDII;

SELECT dbo.CalcularDeducciones(5000000,3,0,0)

CREATE OR ALTER FUNCTION CalcularDeducciones (
    @salario DECIMAL(12, 4),
    @tipoDeduccion INT, 
    @cantidadHijos INT = 0, 
    @tieneConyuge BIT = 0  
)
RETURNS DECIMAL(12, 4)
AS
BEGIN
    DECLARE @deduccionCCSS DECIMAL(12, 4)
    DECLARE @creditoHijos DECIMAL(12, 4) = 1730.00 * @cantidadHijos 
    DECLARE @creditoConyuge DECIMAL(12, 4) = 2620.00 e
    DECLARE @tasaCCSS DECIMAL(12, 4) = 0.1057 
    DECLARE @tasaImpuesto DECIMAL(12, 4)  
    -- Rebajo de la CCSS
    SET @deduccionCCSS = @salario * @tasaCCSS    
    -- Rebajo del impuesto al salario
    SET @tasaImpuesto =
        CASE 
            WHEN @salario <= 929000 THEN 0
            WHEN @salario <= 1363000 THEN ((@salario - 929000) * 0.10) + ((@salario - 929000) * 0.05)
            WHEN @salario <= 2392000 THEN ((1363000 - 929000) * 0.10) + ((@salario - 1363000) * 0.15)
            WHEN @salario <= 4783000 THEN ((1363000 - 929000) * 0.10) + ((2392000 - 1363000) * 0.15) + ((@salario - 2392000) * 0.20)
            ELSE ((1363000 - 929000) * 0.10) + ((2392000 - 1363000) * 0.15) + ((4783000 - 2392000) * 0.20) + ((@salario - 4783000) * 0.25)
        END
    -- Cr�ditos fiscales
    SET @tasaImpuesto = @tasaImpuesto - @creditoHijos
    IF @tieneConyuge = 1 SET @tasaImpuesto = @tasaImpuesto - @creditoConyuge
    RETURN 
        CASE
            WHEN @tipoDeduccion = 1 THEN @deduccionCCSS
            WHEN @tipoDeduccion = 2 THEN @tasaImpuesto
            ELSE @deduccionCCSS + @tasaImpuesto
        END
END;
