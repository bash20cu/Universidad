// OtherInformation: <TargetFramework>net48</TargetFramework>
//Aplicacion de Consola para visualizar la informacion de vehiculos

using System;

namespace Laboratorio1TecnologiasLegacy.UIConsole
{
    internal class Program
    {
        static void Main(string[] args)
        {
            var administrador = new SI.WebService.WebService1();
            var personas = administrador.Obtener();
            foreach (var p in personas)
            {
                Console.WriteLine($"ID: {p.Id}, Marca: {p.Marca}, Año: {p.Anio}, Modelo: {p.Modelo}, Doble Tracción: {p.DobleTraccion}");
            }
        }
    }
}
