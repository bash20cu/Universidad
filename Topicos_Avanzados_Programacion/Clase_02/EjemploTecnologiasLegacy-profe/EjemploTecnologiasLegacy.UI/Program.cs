using System;

namespace EjemploTecnologiasLegacy.UI
{
    internal class Program
    {
        static void Main(string[] args)
        {
           //var persona = new Model.Persona
           // {
           //     Nombre = "Juan",
           //     Apellido = "Perez",
           //     Edad = 30
           // };
            var administrador = new SI.WebService.ServicioDePersonas.ServicioPersona();
            var personas = administrador.Obtener();
            foreach (var p in personas)
            {
                Console.WriteLine($"ID: {p.Id}, Nombre: {p.Nombre}, Apellido: {p.Apellido}, Estado: {p.Estado}");
            }
        }
    }
}
