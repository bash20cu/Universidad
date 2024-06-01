-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema pCine
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `pCine` ;

-- -----------------------------------------------------
-- Schema pCine
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pCine` DEFAULT CHARACTER SET utf8 ;
USE `pCine` ;

-- -----------------------------------------------------
-- Table `pCine`.`IdiomaCatalogo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`IdiomaCatalogo` (
  `idcatalogo_tipo_pelicula` INT NOT NULL AUTO_INCREMENT,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Activo` BIT NOT NULL,
  PRIMARY KEY (`idcatalogo_tipo_pelicula`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`CategoriaCatalogo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`CategoriaCatalogo` (
  `idcatalogo_categoria` INT NOT NULL AUTO_INCREMENT,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Activo` BIT NOT NULL,
  PRIMARY KEY (`idcatalogo_categoria`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`pelicula`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`pelicula` (
  `idpelicula` INT NOT NULL,
  `Titulo` VARCHAR(45) NOT NULL,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Fecha` DATETIME NOT NULL,
  `Activo` BIT NOT NULL,
  `IdiomaCatalogo` INT NOT NULL,
  `CategoriaCatalogo` INT NOT NULL,
  `duracion` DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (`idpelicula`),
  INDEX `fk_pelicula_IdiomaCatalogo_idx` (`IdiomaCatalogo` ASC) VISIBLE,
  INDEX `fk_pelicula_CategoriaCatalogo1_idx` (`CategoriaCatalogo` ASC) VISIBLE,
  CONSTRAINT `fk_pelicula_IdiomaCatalogo`
    FOREIGN KEY (`IdiomaCatalogo`)
    REFERENCES `pCine`.`IdiomaCatalogo` (`idcatalogo_tipo_pelicula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pelicula_CategoriaCatalogo1`
    FOREIGN KEY (`CategoriaCatalogo`)
    REFERENCES `pCine`.`CategoriaCatalogo` (`idcatalogo_categoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`HorarioPelicula`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`HorarioPelicula` (
  `idHorario` INT NOT NULL,
  `Dia` DATE NOT NULL,
  `Hora` TIME NOT NULL,
  `pelicula_idpelicula` INT NOT NULL,
  PRIMARY KEY (`idHorario`),
  INDEX `fk_Horario_pelicula1_idx` (`pelicula_idpelicula` ASC) VISIBLE,
  CONSTRAINT `fk_Horario_pelicula1`
    FOREIGN KEY (`pelicula_idpelicula`)
    REFERENCES `pCine`.`pelicula` (`idpelicula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`CatalogoGenero`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`CatalogoGenero` (
  `idCatalogoGenero` INT NOT NULL AUTO_INCREMENT,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Activo` BIT NOT NULL,
  `Fecha` DATETIME NOT NULL,
  PRIMARY KEY (`idCatalogoGenero`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`Persona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`Persona` (
  `idPersona` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  `Apellido1` VARCHAR(45) NOT NULL,
  `Apellido2` VARCHAR(45) NOT NULL,
  `Fecha_Nacimiento` DATETIME NOT NULL,
  `Fecha_Introducido` DATETIME NOT NULL,
  `CatalogoGenero` INT NOT NULL,
  PRIMARY KEY (`idPersona`),
  INDEX `fk_Persona_CatalogoGenero1_idx` (`CatalogoGenero` ASC) VISIBLE,
  CONSTRAINT `fk_Persona_CatalogoGenero1`
    FOREIGN KEY (`CatalogoGenero`)
    REFERENCES `pCine`.`CatalogoGenero` (`idCatalogoGenero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`CatalogoTelefono`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`CatalogoTelefono` (
  `idCatalogoTelefono` INT NOT NULL AUTO_INCREMENT,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Activo` BIT NOT NULL,
  `Fecha` DATETIME NOT NULL,
  PRIMARY KEY (`idCatalogoTelefono`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`Telefono`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`Telefono` (
  `Numero_Telefono` INT NOT NULL,
  `Activo` BIT NOT NULL,
  `CatalogoTelefono` INT NOT NULL,
  `Persona_idPersona` INT NOT NULL,
  `FechaIngreso` DATETIME NOT NULL,
  PRIMARY KEY (`Numero_Telefono`),
  INDEX `fk_Telefono_CatalogoTelefono1_idx` (`CatalogoTelefono` ASC) VISIBLE,
  INDEX `fk_Telefono_Persona1_idx` (`Persona_idPersona` ASC) VISIBLE,
  CONSTRAINT `fk_Telefono_CatalogoTelefono1`
    FOREIGN KEY (`CatalogoTelefono`)
    REFERENCES `pCine`.`CatalogoTelefono` (`idCatalogoTelefono`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Telefono_Persona1`
    FOREIGN KEY (`Persona_idPersona`)
    REFERENCES `pCine`.`Persona` (`idPersona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`CatalogoCorreoElectronico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`CatalogoCorreoElectronico` (
  `idCatalogoCorreoElectronico` INT NOT NULL AUTO_INCREMENT,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Activo` BIT NOT NULL,
  `Fecha` DATETIME NOT NULL,
  PRIMARY KEY (`idCatalogoCorreoElectronico`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`CorreoElectronico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`CorreoElectronico` (
  `CorreoElectronico` VARCHAR(45) NOT NULL,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Activo` BIT NOT NULL,
  `Fecha` DATETIME NOT NULL,
  `CatalogoCorreoElectronico` INT NOT NULL,
  `Persona_idPersona` INT NOT NULL,
  PRIMARY KEY (`CorreoElectronico`),
  INDEX `fk_CorreoElectronico_CatalogoCorreoElectronico1_idx` (`CatalogoCorreoElectronico` ASC) VISIBLE,
  INDEX `fk_CorreoElectronico_Persona1_idx` (`Persona_idPersona` ASC) VISIBLE,
  CONSTRAINT `fk_CorreoElectronico_CatalogoCorreoElectronico1`
    FOREIGN KEY (`CatalogoCorreoElectronico`)
    REFERENCES `pCine`.`CatalogoCorreoElectronico` (`idCatalogoCorreoElectronico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CorreoElectronico_Persona1`
    FOREIGN KEY (`Persona_idPersona`)
    REFERENCES `pCine`.`Persona` (`idPersona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`Colaborador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`Colaborador` (
  `idColaborador` INT NOT NULL AUTO_INCREMENT,
  `Colaborador_idColaborador` INT NOT NULL,
  `Persona_idPersona` INT NOT NULL,
  PRIMARY KEY (`idColaborador`),
  INDEX `fk_Colaborador_Colaborador1_idx` (`Colaborador_idColaborador` ASC) VISIBLE,
  INDEX `fk_Colaborador_Persona1_idx` (`Persona_idPersona` ASC) VISIBLE,
  CONSTRAINT `fk_Colaborador_Colaborador1`
    FOREIGN KEY (`Colaborador_idColaborador`)
    REFERENCES `pCine`.`Colaborador` (`idColaborador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Colaborador_Persona1`
    FOREIGN KEY (`Persona_idPersona`)
    REFERENCES `pCine`.`Persona` (`idPersona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`Horarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`Horarios` (
  `idHorario` INT NOT NULL AUTO_INCREMENT,
  `Dia` DATE NOT NULL,
  `HoraInicio` TIME NOT NULL,
  `HoraFinal` TIME NOT NULL,
  PRIMARY KEY (`idHorario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`HorariosColaborador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`HorariosColaborador` (
  `Horarios` INT NOT NULL,
  `Colaborador` INT NOT NULL,
  PRIMARY KEY (`Horarios`, `Colaborador`),
  INDEX `fk_HorariosColaborador_Colaborador1_idx` (`Colaborador` ASC) VISIBLE,
  CONSTRAINT `fk_HorariosColaborador_Horarios1`
    FOREIGN KEY (`Horarios`)
    REFERENCES `pCine`.`Horarios` (`idHorario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_HorariosColaborador_Colaborador1`
    FOREIGN KEY (`Colaborador`)
    REFERENCES `pCine`.`Colaborador` (`idColaborador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`Sala`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`Sala` (
  `idSala` INT NOT NULL AUTO_INCREMENT,
  `Ubicacion` VARCHAR(45) NOT NULL,
  `Capacidad` INT NOT NULL,
  `Cantidad_Asientos` INT NOT NULL,
  `Horarios` INT NOT NULL,
  PRIMARY KEY (`idSala`),
  INDEX `fk_Sala_Horarios1_idx` (`Horarios` ASC) VISIBLE,
  CONSTRAINT `fk_Sala_Horarios1`
    FOREIGN KEY (`Horarios`)
    REFERENCES `pCine`.`Horarios` (`idHorario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`CatalogoAsientos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`CatalogoAsientos` (
  `idCatalogoAsientos` INT NOT NULL AUTO_INCREMENT,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Activo` BIT NOT NULL,
  `Fecha` DATETIME NOT NULL,
  PRIMARY KEY (`idCatalogoAsientos`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`SalaPelicula`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`SalaPelicula` (
  `idSalaPelicula` INT NOT NULL AUTO_INCREMENT,
  `Fecha` DATETIME NOT NULL,
  `HoraProyeccion` TIME NOT NULL,
  `Sala_idSala` INT NOT NULL,
  `pelicula_idpelicula` INT NOT NULL,
  INDEX `fk_SalaPelicula_Sala1_idx` (`Sala_idSala` ASC) VISIBLE,
  INDEX `fk_SalaPelicula_pelicula1_idx` (`pelicula_idpelicula` ASC) VISIBLE,
  PRIMARY KEY (`idSalaPelicula`),
  CONSTRAINT `fk_SalaPelicula_Sala1`
    FOREIGN KEY (`Sala_idSala`)
    REFERENCES `pCine`.`Sala` (`idSala`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SalaPelicula_pelicula1`
    FOREIGN KEY (`pelicula_idpelicula`)
    REFERENCES `pCine`.`pelicula` (`idpelicula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`CatalogoDescuento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`CatalogoDescuento` (
  `idCatalogoDescuento` INT NOT NULL AUTO_INCREMENT,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Activo` BIT NOT NULL,
  PRIMARY KEY (`idCatalogoDescuento`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`Descuento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`Descuento` (
  `idDescuento` INT NOT NULL AUTO_INCREMENT,
  `Procentaje` DECIMAL(5,2) NOT NULL,
  `Activo` BIT NOT NULL,
  `CatalogoDescuento` INT NOT NULL,
  `Fecha_Inicio` DATETIME NOT NULL,
  `Fecha_Final` DATETIME NOT NULL,
  PRIMARY KEY (`idDescuento`),
  INDEX `fk_Descuento_CatalogoDescuento1_idx` (`CatalogoDescuento` ASC) VISIBLE,
  CONSTRAINT `fk_Descuento_CatalogoDescuento1`
    FOREIGN KEY (`CatalogoDescuento`)
    REFERENCES `pCine`.`CatalogoDescuento` (`idCatalogoDescuento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`AsientoSala`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`AsientoSala` (
  `idAsiento` INT NOT NULL,
  `Sala_idSala` INT NOT NULL,
  `Precio_Base` INT NOT NULL,
  `Activo` BIT NOT NULL,
  `CatalogoAsientos` INT NOT NULL,
  `Descripcion` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idAsiento`),
  INDEX `fk_Asiento_Sala1_idx` (`Sala_idSala` ASC) VISIBLE,
  INDEX `fk_Asiento_CatalogoAsientos1_idx` (`CatalogoAsientos` ASC) VISIBLE,
  CONSTRAINT `fk_Asiento_Sala1`
    FOREIGN KEY (`Sala_idSala`)
    REFERENCES `pCine`.`Sala` (`idSala`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Asiento_CatalogoAsientos1`
    FOREIGN KEY (`CatalogoAsientos`)
    REFERENCES `pCine`.`CatalogoAsientos` (`idCatalogoAsientos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`Precio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`Precio` (
  `idPrecios` INT NOT NULL AUTO_INCREMENT,
  `Sala_idSala` INT NOT NULL,
  `pelicula_idpelicula` INT NOT NULL,
  `Descuento_idDescuento` INT NOT NULL,
  `Precio_Final` INT NOT NULL,
  PRIMARY KEY (`idPrecios`),
  INDEX `fk_Precios_Sala1_idx` (`Sala_idSala` ASC) VISIBLE,
  INDEX `fk_Precio_pelicula1_idx` (`pelicula_idpelicula` ASC) VISIBLE,
  INDEX `fk_Precio_Descuento1_idx` (`Descuento_idDescuento` ASC) VISIBLE,
  CONSTRAINT `fk_Precios_Sala1`
    FOREIGN KEY (`Sala_idSala`)
    REFERENCES `pCine`.`Sala` (`idSala`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Precio_pelicula1`
    FOREIGN KEY (`pelicula_idpelicula`)
    REFERENCES `pCine`.`pelicula` (`idpelicula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Precio_Descuento1`
    FOREIGN KEY (`Descuento_idDescuento`)
    REFERENCES `pCine`.`Descuento` (`idDescuento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`Cliente` (
  `idCliente` INT NOT NULL AUTO_INCREMENT,
  `Persona_idPersona` INT NOT NULL,
  `Frecuente` BIT NOT NULL,
  `Fecha_Ingreso` DATETIME NOT NULL,
  PRIMARY KEY (`idCliente`),
  INDEX `fk_Cleinte_Persona1_idx` (`Persona_idPersona` ASC) VISIBLE,
  CONSTRAINT `fk_Cleinte_Persona1`
    FOREIGN KEY (`Persona_idPersona`)
    REFERENCES `pCine`.`Persona` (`idPersona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`Compras`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`Compras` (
  `idCompras` INT NOT NULL AUTO_INCREMENT,
  `Fecha` DATETIME NOT NULL,
  `Monto` INT NOT NULL,
  `Cliente_idCleinte` INT NOT NULL,
  `Descuento_idDescuento` INT NOT NULL,
  PRIMARY KEY (`idCompras`),
  INDEX `fk_Compras_Cliente1_idx` (`Cliente_idCleinte` ASC) VISIBLE,
  INDEX `fk_Compras_Descuento1_idx` (`Descuento_idDescuento` ASC) VISIBLE,
  CONSTRAINT `fk_Compras_Cliente1`
    FOREIGN KEY (`Cliente_idCleinte`)
    REFERENCES `pCine`.`Cliente` (`idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Compras_Descuento1`
    FOREIGN KEY (`Descuento_idDescuento`)
    REFERENCES `pCine`.`Descuento` (`idDescuento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`Factura`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`Factura` (
  `idFactura` INT NOT NULL AUTO_INCREMENT,
  `Fecha` DATETIME NOT NULL,
  `Anulado` BIT NOT NULL,
  `Total` DECIMAL(15,2) NOT NULL,
  `Cliente_idCleinte` INT NOT NULL,
  `Colaborador_idColaborador` INT NOT NULL,
  PRIMARY KEY (`idFactura`),
  INDEX `fk_Factura_Cliente1_idx` (`Cliente_idCleinte` ASC) VISIBLE,
  INDEX `fk_Factura_Colaborador1_idx` (`Colaborador_idColaborador` ASC) VISIBLE,
  CONSTRAINT `fk_Factura_Cliente1`
    FOREIGN KEY (`Cliente_idCleinte`)
    REFERENCES `pCine`.`Cliente` (`idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Factura_Colaborador1`
    FOREIGN KEY (`Colaborador_idColaborador`)
    REFERENCES `pCine`.`Colaborador` (`idColaborador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`CatalogoTipoPago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`CatalogoTipoPago` (
  `idCatalogoTipoPago` INT NOT NULL AUTO_INCREMENT,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Activo` BIT NOT NULL,
  PRIMARY KEY (`idCatalogoTipoPago`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`FacturaMetodoPago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`FacturaMetodoPago` (
  `Monto` DECIMAL(15,2) NOT NULL,
  `Factura_idFactura` INT NOT NULL,
  `CatalogoTipoPago` INT NOT NULL,
  PRIMARY KEY (`Factura_idFactura`),
  INDEX `fk_FacturaMetodoPago_CatalogoTipoPago1_idx` (`CatalogoTipoPago` ASC) VISIBLE,
  CONSTRAINT `fk_FacturaMetodoPago_Factura1`
    FOREIGN KEY (`Factura_idFactura`)
    REFERENCES `pCine`.`Factura` (`idFactura`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FacturaMetodoPago_CatalogoTipoPago1`
    FOREIGN KEY (`CatalogoTipoPago`)
    REFERENCES `pCine`.`CatalogoTipoPago` (`idCatalogoTipoPago`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pCine`.`FacturaDetalle`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pCine`.`FacturaDetalle` (
  `idFacturaDetalle` INT NOT NULL AUTO_INCREMENT,
  `Precio` DECIMAL(15,2) NOT NULL,
  `Subtotal` DECIMAL(15,2) NOT NULL,
  `pelicula_idpelicula` INT NOT NULL,
  `Factura_idFactura` INT NOT NULL,
  PRIMARY KEY (`idFacturaDetalle`, `Factura_idFactura`, `pelicula_idpelicula`),
  INDEX `fk_FacturaDetalle_pelicula1_idx` (`pelicula_idpelicula` ASC) VISIBLE,
  INDEX `fk_FacturaDetalle_Factura1_idx` (`Factura_idFactura` ASC) VISIBLE,
  CONSTRAINT `fk_FacturaDetalle_pelicula1`
    FOREIGN KEY (`pelicula_idpelicula`)
    REFERENCES `pCine`.`pelicula` (`idpelicula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FacturaDetalle_Factura1`
    FOREIGN KEY (`Factura_idFactura`)
    REFERENCES `pCine`.`Factura` (`idFactura`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
