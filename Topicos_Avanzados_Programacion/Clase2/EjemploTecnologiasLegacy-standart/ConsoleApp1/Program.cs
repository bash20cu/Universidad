using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    internal class Program
    {
        static void Main(string[] args)
        {
            //// cosumir el agregar del wcf
            /*
            var elServicio = new wcf.ServicioPersonaWCF.ServicioPersonaClient();
            var laPersona = new wcf.ServicioPersonaWCF.Persona()
            {
               Apellidos = "111",
                Estado = wcf.ServicioPersonaWCF.Estado.Activo,
                Identificacion = "qqq",
                Nombre = "Maria",
               FechaDeNacimiento = DateTime.Now

            };
            elServicio.Agregue(laPersona);

            var lasPersonas = elServicio.ObtengaLaLista();

            Console.WriteLine(lasPersonas);
            */

            // cosumir el agregar del webservice
            var elServicio = new webservice.ServicioPersonaAsmx.ServicioPersona();
            var laPersona = new webservice.ServicioPersonaAsmx.Persona()
            {
                Apellidos = "111",
               Estado = webservice.ServicioPersonaAsmx.Estado.Activo,
                Identificacion = "qqq",
                Nombre = "Maria",
                FechaDeNacimiento = DateTime.Now

            };
           elServicio.Agregue(laPersona);

            var lasPersonas = elServicio.ObtengaLaLista();

            Console.WriteLine(lasPersonas);

        }
    }
}
