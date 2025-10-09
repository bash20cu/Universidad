using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Laboratorio.BusinessLogicBL.Test
{
    public class AdministradorDeVehiculos_ObtenerTodos_Test
    {
        [Fact]
        public void ObtenerTodos_DeberiaRetornarVehiculosPorDefecto()
        {
            // Arrange
            var admin = new AdministradorDeVehiculos();

            // Act
            var vehiculos = admin.ObtenerTodos();

            // Assert
            Assert.NotNull(vehiculos);
            Assert.True(vehiculos.Count >= 3); // Por defecto hay 3
        }
    }
}
