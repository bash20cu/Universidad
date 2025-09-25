using Laboratorio1TecnologiasLegacy.Model;
using System;
using System.Collections.Generic;
using System.Web.Services;

namespace Laboratorio1TecnologiasLegacy.SI.WebService
{
    /// <summary>
    /// Summary description for WebService1
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]

    public class WebService1 : System.Web.Services.WebService
    {
        // Instancia de la clase administradora de vehiculos
        private readonly BusinessLogicBL.AdministradorDeVehiculos administrador = new BusinessLogicBL.AdministradorDeVehiculos();

        // Metodo para obtener todos los vehiculos
        [WebMethod]
        public List<Model.Vehiculo> Obtener()
        {
            return administrador.ObtenerTodos();
        }

        // Metodo para actualizar un vehiculo
        [WebMethod]
        public void Actualizar(Vehiculo vehiculo)
        {
            administrador.Actualizar(vehiculo);
        }

        // Metodo para agregar un vehiculo
        [WebMethod]
        public void Agregar(Vehiculo vehiculo)
        {
            administrador.Agregar(vehiculo);
        }

        // Metodo para eliminar un vehiculo por Id
        [WebMethod]
        public void Eliminar(int id)
        {
            throw new NotImplementedException();
        }

        // Metodo para obtener un vehiculo por Id
        [WebMethod]
        public Vehiculo ObtenerPorId(int id)
        {
            return administrador.ObtenerPorId(id);
        }
    }
}
