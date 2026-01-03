-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema Restaurante
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Restaurante
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Restaurante` DEFAULT CHARACTER SET utf8 ;
USE `Restaurante` ;

-- -----------------------------------------------------
-- Table `Restaurante`.`Catalogo_Categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Catalogo_Categoria` (
  `idCatalogo_Categoria` INT NOT NULL,
  `Nombre_Categoria` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idCatalogo_Categoria`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Elemento_Menu`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Elemento_Menu` (
  `idElementos_Menu` INT NOT NULL,
  `Nombre_Plato` VARCHAR(45) NOT NULL,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Precio` DECIMAL(8,2) NOT NULL,
  `Catalogo_Categoria_idCatalogo_Categoria` INT NOT NULL,
  PRIMARY KEY (`idElementos_Menu`),
  INDEX `fk_Elementos_Menu_Catalogo_Categoria1_idx` (`Catalogo_Categoria_idCatalogo_Categoria` ASC) VISIBLE,
  CONSTRAINT `fk_Elementos_Menu_Catalogo_Categoria1`
    FOREIGN KEY (`Catalogo_Categoria_idCatalogo_Categoria`)
    REFERENCES `Restaurante`.`Catalogo_Categoria` (`idCatalogo_Categoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Menu`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Menu` (
  `idMenu` INT NOT NULL,
  `Fecha` DATETIME NOT NULL,
  `Elementos_Menu_idElementos_Menu` INT NOT NULL,
  PRIMARY KEY (`idMenu`),
  INDEX `fk_Menu_Elementos_Menu1_idx` (`Elementos_Menu_idElementos_Menu` ASC) VISIBLE,
  CONSTRAINT `fk_Menu_Elementos_Menu1`
    FOREIGN KEY (`Elementos_Menu_idElementos_Menu`)
    REFERENCES `Restaurante`.`Elemento_Menu` (`idElementos_Menu`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Sucursal_restaurante`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Sucursal_restaurante` (
  `idSucursal_restaurante` INT NOT NULL,
  `Numero_sucursal` INT NOT NULL,
  `Menu_idMenu` INT NOT NULL,
  PRIMARY KEY (`idSucursal_restaurante`),
  INDEX `fk_Sucursal_restaurante_Menu1_idx` (`Menu_idMenu` ASC) VISIBLE,
  CONSTRAINT `fk_Sucursal_restaurante_Menu1`
    FOREIGN KEY (`Menu_idMenu`)
    REFERENCES `Restaurante`.`Menu` (`idMenu`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Cliente` (
  `idCliente` INT NOT NULL,
  `Tipo` VARCHAR(45) NOT NULL,
  `Numero_Cliente` INT NOT NULL,
  `Sucursal_restaurante_idSucursal_restaurante` INT NOT NULL,
  PRIMARY KEY (`idCliente`),
  INDEX `fk_Cliente_Sucursal_restaurante1_idx` (`Sucursal_restaurante_idSucursal_restaurante` ASC) VISIBLE,
  CONSTRAINT `fk_Cliente_Sucursal_restaurante1`
    FOREIGN KEY (`Sucursal_restaurante_idSucursal_restaurante`)
    REFERENCES `Restaurante`.`Sucursal_restaurante` (`idSucursal_restaurante`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Catalogo_Tipo_Telefono`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Catalogo_Tipo_Telefono` (
  `IdTipo` INT NOT NULL,
  `Tipo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`IdTipo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Telefono`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Telefono` (
  `idTelefono` INT NOT NULL,
  `Numero_Telefono` INT NOT NULL,
  `Catalogo_Tipo_Telefono_IdTipo` INT NOT NULL,
  PRIMARY KEY (`idTelefono`),
  INDEX `fk_Telefono_Catalogo_Tipo_Telefono1_idx` (`Catalogo_Tipo_Telefono_IdTipo` ASC) VISIBLE,
  CONSTRAINT `fk_Telefono_Catalogo_Tipo_Telefono1`
    FOREIGN KEY (`Catalogo_Tipo_Telefono_IdTipo`)
    REFERENCES `Restaurante`.`Catalogo_Tipo_Telefono` (`IdTipo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Catalogo_Correo_Electronico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Catalogo_Correo_Electronico` (
  `idCatalogo_Correo_Electronico` INT NOT NULL,
  `Tipo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idCatalogo_Correo_Electronico`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Correo_Electronico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Correo_Electronico` (
  `idCorreo_Electronico` INT NOT NULL,
  `Direccion_Correo` VARCHAR(45) NOT NULL,
  `Catalogo_Correo_Electronico_idCatalogo_Correo_Electronico` INT NOT NULL,
  PRIMARY KEY (`idCorreo_Electronico`),
  INDEX `fk_Correo_Electronico_Catalogo_Correo_Electronico1_idx` (`Catalogo_Correo_Electronico_idCatalogo_Correo_Electronico` ASC) VISIBLE,
  CONSTRAINT `fk_Correo_Electronico_Catalogo_Correo_Electronico1`
    FOREIGN KEY (`Catalogo_Correo_Electronico_idCatalogo_Correo_Electronico`)
    REFERENCES `Restaurante`.`Catalogo_Correo_Electronico` (`idCatalogo_Correo_Electronico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Catalogo_Distrito`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Catalogo_Distrito` (
  `idCatalogo_Distrito` INT NOT NULL,
  `Nombre_Distrito` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idCatalogo_Distrito`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Catalogo_Canton`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Catalogo_Canton` (
  `idCatalogo_Canton` INT NOT NULL,
  `Nombre_Canton` VARCHAR(45) NOT NULL,
  `Codigo_Canton` VARCHAR(45) NOT NULL,
  `Catalogo_Distrito_idCatalogo_Distrito` INT NOT NULL,
  PRIMARY KEY (`idCatalogo_Canton`),
  INDEX `fk_Catalogo_Canton_Catalogo_Distrito1_idx` (`Catalogo_Distrito_idCatalogo_Distrito` ASC) VISIBLE,
  CONSTRAINT `fk_Catalogo_Canton_Catalogo_Distrito1`
    FOREIGN KEY (`Catalogo_Distrito_idCatalogo_Distrito`)
    REFERENCES `Restaurante`.`Catalogo_Distrito` (`idCatalogo_Distrito`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Catalogo_Provincia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Catalogo_Provincia` (
  `idCatalogo_Provincia` INT NOT NULL,
  `Nombre` VARCHAR(45) NOT NULL,
  `Codigo` INT(5) NOT NULL,
  `Catalogo_Provincia` VARCHAR(45) NOT NULL,
  `Catalogo_Canton_idCatalogo_Canton` INT NOT NULL,
  PRIMARY KEY (`idCatalogo_Provincia`),
  INDEX `fk_Catalogo_Provincia_Catalogo_Canton1_idx` (`Catalogo_Canton_idCatalogo_Canton` ASC) VISIBLE,
  CONSTRAINT `fk_Catalogo_Provincia_Catalogo_Canton1`
    FOREIGN KEY (`Catalogo_Canton_idCatalogo_Canton`)
    REFERENCES `Restaurante`.`Catalogo_Canton` (`idCatalogo_Canton`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Direccion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Direccion` (
  `idDireccion` INT NOT NULL,
  `Calle` VARCHAR(45) NOT NULL,
  `Numero_Casa` VARCHAR(45) NOT NULL,
  `Otras_Senas` VARCHAR(45) NOT NULL,
  `Catalogo_Provincia_idCatalogo_Provincia` INT NOT NULL,
  PRIMARY KEY (`idDireccion`),
  INDEX `fk_Direccion_Catalogo_Provincia1_idx` (`Catalogo_Provincia_idCatalogo_Provincia` ASC) VISIBLE,
  CONSTRAINT `fk_Direccion_Catalogo_Provincia1`
    FOREIGN KEY (`Catalogo_Provincia_idCatalogo_Provincia`)
    REFERENCES `Restaurante`.`Catalogo_Provincia` (`idCatalogo_Provincia`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Cocinero`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Cocinero` (
  `idCocinero` INT NOT NULL,
  `Fecha_Inicio` DATE NOT NULL,
  PRIMARY KEY (`idCocinero`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Salonero`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Salonero` (
  `idSalonero` INT NOT NULL,
  `Numero_Empleado` INT NOT NULL,
  `Fecha_Inicio` DATE NOT NULL,
  PRIMARY KEY (`idSalonero`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Administrador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Administrador` (
  `idAdministrador` INT NOT NULL,
  `Pivilegio_administrativo` BIT(1) NOT NULL,
  PRIMARY KEY (`idAdministrador`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Empleado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Empleado` (
  `idEmpleado` INT NOT NULL,
  `Empleado` BIT(1) NOT NULL,
  `Numero_Empleado` VARCHAR(45) NOT NULL,
  `Cocinero_idCocinero` INT NOT NULL,
  `Salonero_idSalonero` INT NOT NULL,
  `Administrador_idAdministrador` INT NOT NULL,
  `Sucursal_restaurante_idSucursal_restaurante` INT NOT NULL,
  PRIMARY KEY (`idEmpleado`),
  INDEX `fk_Empleado_Cocinero1_idx` (`Cocinero_idCocinero` ASC) VISIBLE,
  INDEX `fk_Empleado_Salonero1_idx` (`Salonero_idSalonero` ASC) VISIBLE,
  INDEX `fk_Empleado_Administrador1_idx` (`Administrador_idAdministrador` ASC) VISIBLE,
  INDEX `fk_Empleado_Sucursal_restaurante1_idx` (`Sucursal_restaurante_idSucursal_restaurante` ASC) VISIBLE,
  CONSTRAINT `fk_Empleado_Cocinero1`
    FOREIGN KEY (`Cocinero_idCocinero`)
    REFERENCES `Restaurante`.`Cocinero` (`idCocinero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Empleado_Salonero1`
    FOREIGN KEY (`Salonero_idSalonero`)
    REFERENCES `Restaurante`.`Salonero` (`idSalonero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Empleado_Administrador1`
    FOREIGN KEY (`Administrador_idAdministrador`)
    REFERENCES `Restaurante`.`Administrador` (`idAdministrador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Empleado_Sucursal_restaurante1`
    FOREIGN KEY (`Sucursal_restaurante_idSucursal_restaurante`)
    REFERENCES `Restaurante`.`Sucursal_restaurante` (`idSucursal_restaurante`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Restaurante`.`Persona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Restaurante`.`Persona` (
  `idPersona` INT NOT NULL,
  `Nombre` VARCHAR(45) NOT NULL,
  `Apellido1` VARCHAR(45) NOT NULL,
  `Apellido2` VARCHAR(45) NOT NULL,
  `Cliente_idCliente` INT NOT NULL,
  `Telefono_idTelefono` INT NOT NULL,
  `Correo_Electronico_idCorreo_Electronico` INT NOT NULL,
  `Direccion_idDireccion` INT NOT NULL,
  `Empleado_idEmpleado` INT NOT NULL,
  PRIMARY KEY (`idPersona`),
  INDEX `fk_Persona_Cliente1_idx` (`Cliente_idCliente` ASC) VISIBLE,
  INDEX `fk_Persona_Telefono1_idx` (`Telefono_idTelefono` ASC) VISIBLE,
  INDEX `fk_Persona_Correo_Electronico1_idx` (`Correo_Electronico_idCorreo_Electronico` ASC) VISIBLE,
  INDEX `fk_Persona_Direccion1_idx` (`Direccion_idDireccion` ASC) VISIBLE,
  INDEX `fk_Persona_Empleado1_idx` (`Empleado_idEmpleado` ASC) VISIBLE,
  CONSTRAINT `fk_Persona_Cliente1`
    FOREIGN KEY (`Cliente_idCliente`)
    REFERENCES `Restaurante`.`Cliente` (`idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Persona_Telefono1`
    FOREIGN KEY (`Telefono_idTelefono`)
    REFERENCES `Restaurante`.`Telefono` (`idTelefono`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Persona_Correo_Electronico1`
    FOREIGN KEY (`Correo_Electronico_idCorreo_Electronico`)
    REFERENCES `Restaurante`.`Correo_Electronico` (`idCorreo_Electronico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Persona_Direccion1`
    FOREIGN KEY (`Direccion_idDireccion`)
    REFERENCES `Restaurante`.`Direccion` (`idDireccion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Persona_Empleado1`
    FOREIGN KEY (`Empleado_idEmpleado`)
    REFERENCES `Restaurante`.`Empleado` (`idEmpleado`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
