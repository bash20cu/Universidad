using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.ServiceProcess;

namespace Service_Control_Console
{
    internal class Service_Control
    {

       public void hello() 
        {
            Console.WriteLine("Hola");
        }


        public void ListarServicios()
        {
            ServiceController[] services = ServiceController.GetServices();

            foreach (ServiceController service in services)
            {
                Console.WriteLine("Nombre del servicio: " + service.ServiceName);
                Console.WriteLine("Estado del servicio: " + service.Status);
                Console.WriteLine("--------------------------------------");
            }
        }

        public void IniciarServicio(string serviceName)
        {
            using (ServiceController myService = new ServiceController(serviceName))
            {
                if (myService.Status == ServiceControllerStatus.Stopped)
                {
                    myService.Start();
                    myService.WaitForStatus(ServiceControllerStatus.Running);
                    Console.WriteLine("El servicio " + serviceName + " ha sido iniciado.");
                }
            }
        }

        public static void DetenerServicio(string serviceName)
        {
            using (ServiceController myService = new ServiceController(serviceName))
            {
                if (myService.Status == ServiceControllerStatus.Running)
                {
                    myService.Stop();
                    myService.WaitForStatus(ServiceControllerStatus.Stopped);
                    Console.WriteLine("El servicio " + serviceName + " ha sido detenido.");
                }
            }
        }


    }
}
