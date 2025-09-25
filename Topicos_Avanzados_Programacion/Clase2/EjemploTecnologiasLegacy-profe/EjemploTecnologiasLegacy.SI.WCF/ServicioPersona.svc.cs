using EjemploTecnologiasLegacy.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace EjemploTecnologiasLegacy.SI.WCF
{
   public class ServicioPersona : IServicioPersona
    {
        private readonly BL.AdministradorDePersonas administrador = new BL.AdministradorDePersonas();

        public void Activar(int id)
        {
          administrador.Activar(id);
        }

        public void Actualizar(Persona persona)
        {
            administrador.Actualizar(persona);
        }

        public void Agregar(Persona persona)
        {
            administrador.Agregar(persona);
        }

        public void Desactivar(int id)
        {
            throw new NotImplementedException();
        }

        public void Eliminar(int id)
        {
            throw new NotImplementedException();
        }

        public List<Persona> Obtener()
        {
        return  administrador.Obtener();
        }

        public Persona ObtenerPorId(int id)
        {
            return administrador.ObtenerPorId(id);
        }
    }
}
