using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EjemploTecnologiasLegacy.UIConsole
{
    internal class Program
    {
        static void Main(string[] args)
        {
            var administrador = new SI.WebService.ServicioPersona();
            var personas = administrador.Obtener();
            foreach (var p in personas)
            {
                Console.WriteLine($"ID: {p.Id}, Nombre: {p.Nombre}, Apellido: {p.Apellido}, Estado: {p.Estado}");
            }
        }
    }
}
