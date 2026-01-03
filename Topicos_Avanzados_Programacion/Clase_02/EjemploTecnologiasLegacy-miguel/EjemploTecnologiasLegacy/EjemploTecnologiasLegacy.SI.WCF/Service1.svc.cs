using EjemploTecnologiasLegacy.Model;
using System.Collections.Generic;


namespace EjemploTecnologiasLegacy.SI.WCF
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service1.svc or Service1.svc.cs at the Solution Explorer and start debugging.
    public class Service1 : IServicioPersona
    {

        private readonly BusinessLogicBL.AdministradorDePersonas administrador;

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
            administrador.Desactivar(id);
        }

        public void Eliminar(int id)
        {
            administrador.Eliminar(id);
        }

        public Persona ObtenerPorId(int id)
        {
            return administrador.ObtenerPorId(id);
        }

        public List<Persona> ObtenerTodas()
        {
            return administrador.ObtenerTodas();
        }
    }
}
