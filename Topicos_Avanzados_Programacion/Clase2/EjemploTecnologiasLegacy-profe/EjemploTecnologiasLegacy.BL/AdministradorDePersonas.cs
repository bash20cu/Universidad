using System;
using System.Collections.Generic;
using System.Linq;

namespace EjemploTecnologiasLegacy.BL
{

    public class AdministradorDePersonas
    {
        private static readonly List<Model.Persona> personas = new List<Model.Persona>();
        
        public AdministradorDePersonas()
        {
            // Inicializar con algunos datos de ejemplo
            if (!personas.Any())
            {
                personas.Add(new Model.Persona { Id = 1, Nombre = "Juan", Apellido = "Pérez", FechaNacimiento = new DateTime(1990, 1, 1), Estado = Model.Estado.Activo });
                personas.Add(new Model.Persona { Id = 2, Nombre = "María", Apellido = "Gómez", FechaNacimiento = new DateTime(1985, 5, 15), Estado = Model.Estado.Inactivo });
            }
        }

        public List<Model.Persona> Obtener()
        {
            return personas;
        }

        public Model.Persona ObtenerPorId(int id)
        {
            return personas.FirstOrDefault(p => p.Id == id);
        }

        public void Agregar(Model.Persona persona)
        {
            if (persona == null)
                throw new ArgumentNullException(nameof(persona));
            if (personas.Any(p => p.Id == persona.Id))
                throw new ArgumentException("Ya existe una persona con el mismo Id.");
            personas.Add(persona);
        }

        public void Actualizar(Model.Persona persona)
        {
            if (persona == null)
                throw new ArgumentNullException(nameof(persona));
            var existente = personas.FirstOrDefault(p => p.Id == persona.Id);
            if (existente == null)
                throw new ArgumentException("No se encontró la persona para actualizar.");
            existente.Nombre = persona.Nombre;
            existente.Apellido = persona.Apellido;
            existente.FechaNacimiento = persona.FechaNacimiento;
            existente.Estado = persona.Estado;
        }

        public void Eliminar(int id)
        {
            var persona = personas.FirstOrDefault(p => p.Id == id);
            if (persona == null)
                throw new ArgumentException("No se encontró la persona para eliminar.");
            personas.Remove(persona);
        }

        public void Activar(int id)
        {
            var persona = personas.FirstOrDefault(p => p.Id == id);
            if (persona == null)
                throw new ArgumentException("No se encontró la persona para activar.");
            persona.Estado = Model.Estado.Activo;
        }

        public void Desactivar(int id)
        {
            var persona = personas.FirstOrDefault(p => p.Id == id);
            if (persona == null)
                throw new ArgumentException("No se encontró la persona para desactivar.");
            persona.Estado = Model.Estado.Inactivo;
        }

    }
}
