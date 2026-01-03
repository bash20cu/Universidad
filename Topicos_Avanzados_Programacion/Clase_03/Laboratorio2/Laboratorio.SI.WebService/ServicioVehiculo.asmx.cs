using Laboratorio.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace Laboratorio.SI.WebService
{
    /// <summary>
    /// Summary description for ServicioVehiculo
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class ServicioVehiculo : System.Web.Services.WebService
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
