using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Laboratorio.BusinessLogicBL.Test
{
    public class AdministradorDeVehiculos_Actualizar_Test
    {
        [Fact]
        public void Actualizar_DeberiaActualizarVehiculoExistente()
        {
            // Arrange
            var admin = new AdministradorDeVehiculos();
            var vehiculo = admin.ObtenerTodos().First();
            var modeloOriginal = vehiculo.Modelo;
            vehiculo.Modelo = "Nuevo Modelo";

            // Act
            admin.Actualizar(vehiculo);
            var actualizado = admin.ObtenerPorId(vehiculo.Id);

            // Assert
            Assert.Equal("Nuevo Modelo", actualizado.Modelo);
            Assert.NotEqual(modeloOriginal, actualizado.Modelo);
        }
    }
}
