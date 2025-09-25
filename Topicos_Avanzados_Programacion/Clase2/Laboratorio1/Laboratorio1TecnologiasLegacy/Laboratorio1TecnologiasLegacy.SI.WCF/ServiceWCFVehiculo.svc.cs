//Servicio de vehiculos WCF que implementa operaciones CRUD utilizando la clase AdministradorDeVehiculos.
//return vehiculo;

using Laboratorio1TecnologiasLegacy.Model;
using System.Collections.Generic;

namespace Laboratorio1TecnologiasLegacy.SI.WCF
{
    public class ServiceWCFVehiculo : IServiceWCFVehiculo
    {
        // Instancia de la clase AdministradorDeVehiculos para manejar la lógica de negocio
        private readonly BusinessLogicBL.AdministradorDeVehiculos administrador;

        // Constructor que evita errores de referencia nula ;-)
        public ServiceWCFVehiculo()
        {
            administrador = new BusinessLogicBL.AdministradorDeVehiculos();
        }

        // Implementación de los métodos del servicio WCF utilizando la clase AdministradorDeVehiculos

        // Actualizar un vehiculo existente
        public void Actualizar(Vehiculo vehiculo)
        {
            administrador.Actualizar(vehiculo);
        }

        // Agregar un nuevo vehiculo
        public void Agregar(Vehiculo vehiculo)
        {
            administrador.Agregar(vehiculo);
        }

        // Eliminar un vehiculo por su Id
        public void Eliminar(int id)
        {
            administrador.Eliminar(id);
        }

        // Obtener un vehiculo por su Id
        public Vehiculo ObtenerPorId(int id)
        {
            return administrador.ObtenerPorId(id);
        }

        // Obtener todos los vehiculos
        public List<Vehiculo> ObtenerTodas()
        {
            return administrador.ObtenerTodos();
        }
    }
}
