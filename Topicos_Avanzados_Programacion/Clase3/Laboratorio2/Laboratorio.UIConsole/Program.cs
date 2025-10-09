using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Laboratorio.UIConsole
{
    internal class Program
    {
        static void Main(string[] args)
        {
            var administrador = new WebService.WCF.ServiceWCFVehiculo();
            var vehiculos = administrador.ObtenerTodas();
            foreach (var p in vehiculos)
            {
                Console.WriteLine($"ID: {p.Id}, Marca: {p.Marca}, Año: {p.Anio}, Modelo: {p.Modelo}, Doble Tracción: {p.DobleTraccion}");
            }
        }
    }
}
