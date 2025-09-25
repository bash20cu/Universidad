using System;
using System.Collections.Generic;
using System.Linq;

namespace EjemploTecnologiasLegacy.BusinessLogicBL
{
    /// <summary>
    ///   <br />
    /// </summary>
    public class AdministradorDePersonas
    {
        // Simulando una base de datos en memoria
        private static readonly List<Model.Persona> personas = new List<Model.Persona>();

        public AdministradorDePersonas()
        {
            //Inicializar con datos de ejemplo

            if (!personas.Any())
            //Sino existen personas, crear algunas
            {
                personas.Add(new Model.Persona { Id = 1, Nombre = "Miguel", Apellido = "Pérez", FechaNacimiento = new DateTime(1990, 1, 1), Estado = Model.Estado.activo });
                personas.Add(new Model.Persona { Id = 2, Nombre = "María", Apellido = "Gómez", FechaNacimiento = new DateTime(1985, 5, 15), Estado = Model.Estado.activo });
                personas.Add(new Model.Persona { Id = 3, Nombre = "Alejandro", Apellido = "López", FechaNacimiento = new DateTime(2000, 12, 30), Estado = Model.Estado.activo });
            }
        }

        //Metodo para comporbar Persona Null
        private void ComprobarNulo(Model.Persona persona)
        {
            if (persona == null)
            {
                throw new ArgumentNullException(nameof(persona), "La persona no puede ser nula.");
            }
        }

        // Método para obtener las personas.
        public List<Model.Persona> ObtenerTodas()
        {
            return personas;
        }

        // Metodo para obtener persona por Id
        public Model.Persona ObtenerPorId(int id)
        {
            var persona = personas.FirstOrDefault(p => p.Id == id);
            ComprobarNulo(persona);
            return persona;

        }

        //Metodo para agregar personas
        public void Agregar(Model.Persona persona)
        {
            ComprobarNulo(persona);

            if (personas.Any(personas => persona.Id == persona.Id))
            {
                throw new ArgumentException("Ya existe una persona con el mismo Id.");
            }
            personas.Add(persona);
        }

        //Metodo para actualizar las personas
        public void Actualizar(Model.Persona persona)
        {
            //Si persona es nulo, lanzamos excepcion
            ComprobarNulo(persona);

            //Comprobar si la persona existe
            var Existente = personas.FirstOrDefault(p => p.Id == persona.Id);

            if (Existente == null)
            {
                throw new ArgumentException("No existe una persona con el Id proporcionado.");
            }

            //Actualizar los datos
            Existente.Nombre = persona.Nombre;
            Existente.Apellido = persona.Apellido;
            Existente.FechaNacimiento = persona.FechaNacimiento;
            Existente.Estado = persona.Estado;
        }

        //Metodo para eliminar personas
        public void Eliminar(int id)
        {
            var persona = personas.FirstOrDefault(p => p.Id == id);

            //Comporbamos Nulo
            ComprobarNulo(persona);

            personas.Remove(persona);
        }

        //Metodo para Activar personas
        public void Activar(int id)
        {
            var persona = personas.FirstOrDefault(p => p.Id == id);
            ComprobarNulo(persona);
            persona.Estado = Model.Estado.activo;

        }

        //Metodo para Desactivar personas
        public void Desactivar(int id)
        {
            var persona = personas.FirstOrDefault(p => p.Id == id);
            ComprobarNulo(persona);
            persona.Estado = Model.Estado.inactivo;

        }

    }
}
