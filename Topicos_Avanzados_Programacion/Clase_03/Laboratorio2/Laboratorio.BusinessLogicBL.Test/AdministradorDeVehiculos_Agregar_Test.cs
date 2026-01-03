using Laboratorio.BusinessLogicBL;
using Laboratorio.Model;
using Xunit;

namespace Laboratorio.BusinessLogicBL.Test
{
    public class AdministradorDeVehiculos_Agregar_Test
    {
        [Fact]
        public void Agregar_DeberiaAgregarVehiculoNuevo()
        {
            // Arrange
            var admin = new AdministradorDeVehiculos();
            var nuevoVehiculo = new Vehiculo
            {
                Id = 100,
                Marca = Marca.Tesla,
                Modelo = "Model X",
                Anio = 2022,
                DobleTraccion = true
            };

            // Act
            admin.Agregar(nuevoVehiculo);
            var vehiculoAgregado = admin.ObtenerPorId(100);

            // Assert
            Assert.NotNull(vehiculoAgregado);
            Assert.Equal("Model X", vehiculoAgregado.Modelo);
        }
    }
}
