using System.Collections.Generic;
using System.Linq;

namespace EjemploTecnologiasLegacy.BL
{
    public class AdministradorDePersonas
    {
        private static readonly List<Model.Persona> listaDePersonas = new List<Model.Persona>();

        public AdministradorDePersonas()
        {
        }

        public void Active(int id)
        {
            var persona = listaDePersonas.FirstOrDefault(p => p.Id == id);
            if (persona != null)
            {
                persona.Estado = Model.Estado.Activo;
            }
        }

        public void DesActive(int id)
        {
            var persona = listaDePersonas.FirstOrDefault(p => p.Id == id);
            if (persona != null)
            {
                persona.Estado = Model.Estado.InActivo;
            }
        }

        public void Agregue(Model.Persona persona)
        {
            if (persona != null && !listaDePersonas.Any(p => p.Id == persona.Id))
            {
                listaDePersonas.Add(persona);
            }
        }

        public List<Model.Persona> ObtengaLaLista()
        {
            return listaDePersonas.ToList();
        }

        public List<Model.Persona> ObtengaLaListaDeActivos()
        {
            return listaDePersonas.Where(p => p.Estado == Model.Estado.Activo).ToList();
        }

        public List<Model.Persona> ObtengaLaListaDeInActivos()
        {
            return listaDePersonas.Where(p => p.Estado == Model.Estado.InActivo).ToList();
        }

        public Model.Persona ObtengaLaPersona(int id)
        {
            return listaDePersonas.FirstOrDefault(p => p.Id == id);
        }

        public void EditeLaPersona(Model.Persona persona)
        {
            if (persona == null) return;
            var existente = listaDePersonas.FirstOrDefault(p => p.Id == persona.Id);
            if (existente != null)
            {
                existente.Identificacion = persona.Identificacion;
                existente.Nombre = persona.Nombre;
                existente.Apellidos = persona.Apellidos;
                existente.FechaDeNacimiento = persona.FechaDeNacimiento;
                existente.Estado = persona.Estado;
            }
        }
    }
}
