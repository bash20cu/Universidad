using Laboratorio.Model;

namespace Laboratorio.BusinessLogicBL.Test
{
    public class AdministradorDeVehiculos_Eliminar_Test
    {
        [Fact]
        public void Eliminar_DeberiaEliminarVehiculoExistente()
        {
            // Arrange
            var admin = new AdministradorDeVehiculos();
            var vehiculo = new Vehiculo
            {
                Id = 200,
                Marca = Marca.BYD,
                Modelo = "Tang",
                Anio = 2023,
                DobleTraccion = false
            };
            admin.Agregar(vehiculo);

            // Act
            admin.Eliminar(200);

            // Assert
            Assert.Throws<ArgumentNullException>(() => admin.ObtenerPorId(200));
        }
    }
}
