using EjemploTecnologiasLegacy.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace EjemploTecnologiasLegacy.SI.WebService
{
    /// <summary>
    /// Descripción breve de ServicioPersona
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // Para permitir que se llame a este servicio web desde un script, usando ASP.NET AJAX, quite la marca de comentario de la línea siguiente. 
    // [System.Web.Script.Services.ScriptService]
    public class ServicioPersona : System.Web.Services.WebService
    {
        private readonly BL.AdministradorDePersonas administrador = new BL.AdministradorDePersonas();

        [WebMethod]
        public List<Model.Persona> Obtener()
        {
            return administrador.Obtener();
        }
        [WebMethod]
        public void Activar(int id)
        {
            administrador.Activar(id);
        }
        [WebMethod]
        public void Actualizar(Persona persona)
        {
            administrador.Actualizar(persona);
        }
        [WebMethod]
        public void Agregar(Persona persona)
        {
            administrador.Agregar(persona);
        }
        [WebMethod]
        public void Desactivar(int id)
        {
            throw new NotImplementedException();
        }
        [WebMethod]
        public void Eliminar(int id)
        {
            throw new NotImplementedException();
        }
        [WebMethod]
        public Persona ObtenerPorId(int id)
        {
            return administrador.ObtenerPorId(id);
        }

    }
}
