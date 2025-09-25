using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace EjemploTecnologiasLegacy.SI.WCF
{
    public class ServicioPersona : IServicioPersona
    {
        private readonly BL.AdministradorDePersonas ElAdministrador = new BL.AdministradorDePersonas();

        public ServicioPersona()
        {
        }
        public void Active(int id)
        {
            ElAdministrador.Active(id);

        }

        public void Agregue(Model.Persona persona)
        {
            ElAdministrador.Agregue(persona);
        }

        public void DesActive(int id)
        {
            ElAdministrador.DesActive(id);
        }

        public void EditeLaPersona(Model.Persona persona)
        {
            ElAdministrador.EditeLaPersona(persona);
        }

        public List<Model.Persona> ObtengaLaLista()
        {
            return ElAdministrador.ObtengaLaLista();
        }

        public List<Model.Persona> ObtengaLaListaDeActivos()
        {
            return ElAdministrador.ObtengaLaListaDeActivos();
        }

        public List<Model.Persona> ObtengaLaListaDeInActivos()
        {
            return ElAdministrador.ObtengaLaListaDeInActivos();
        }

        public Model.Persona ObtengaLaPersona(int id)
        {
            return ElAdministrador.ObtengaLaPersona(id);
        }
    }
}
