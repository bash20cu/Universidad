using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Security;
using System.Security.Principal;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Windows.Media;
using System.Numerics;
using System.Security.Policy;

namespace Services_Control
{
    class Services
    {
        private MainWindow mainWindow;
        public Services(MainWindow main)
        {
            mainWindow = main;
            TestAdminRights();
            mainWindow.Texto.Text = "";
        }

        private void TestAdminRights()
        {
            WindowsIdentity identity = WindowsIdentity.GetCurrent();
            WindowsPrincipal principal = new WindowsPrincipal(identity);

            // Comprueba si el usuario actual tiene derechos administrativos
            bool isAdmin = principal.IsInRole(WindowsBuiltInRole.Administrator);
            mainWindow.Label1.Content = isAdmin;

        }

        public void ListarServicios()
        {
            ServiceController[] services = ServiceController.GetServices();
            mainWindow.Texto.Text = "";

            // Crear un StringBuilder para almacenar los resultados
            StringBuilder resultado = new StringBuilder();



            foreach (ServiceController service in services)
            {

                if (service.DisplayName.Contains("MySQL") || service.DisplayName.Contains("SQL Server"))
                {
                    resultado.AppendLine("Nombre del servicio: " + service.ServiceName);
                    resultado.AppendLine("Estado del servicio: " + service.Status);
                    resultado.AppendLine("--------------------------------------");
                }

                // Agregar la información del servicio al TextBox
                mainWindow.Texto.Text = resultado.ToString();
            }
        }

        public void ApagarServicios()
        {
            ServiceController[] services = ServiceController.GetServices();
            bool isMySQLSelected = mainWindow.mysql.IsChecked ?? false;
            bool isMSSQLSelected = mainWindow.mssql.IsChecked ?? false;
            mainWindow.Texto.Text = "";
            mainWindow.Texto.Text = "Apagando Servicios";
            foreach (ServiceController service in services)
            {
                string serviceName = service.DisplayName;

                if ((isMySQLSelected && serviceName.Contains("MySQL")) ||
                    (isMSSQLSelected && serviceName.Contains("SQL Server")))
                {
                    

                    try
                    {
                        if (service.Status == ServiceControllerStatus.Running)
                        {                            
                            service.Stop();
                            // Esperar hasta que se detenga o 30 segundos, lo que ocurra primero
                            service.WaitForStatus(ServiceControllerStatus.Stopped, TimeSpan.FromSeconds(30));
                            
                        }
                    }
                    catch (Exception ex)
                    {
                        // Manejo de errores si no se pudo detener el servicio
                        //Console.WriteLine("Error al detener el servicio: " + ex.Message);
                        mainWindow.Texto.Text = "";
                        mainWindow.Texto.Text = "Error al detener el servicio: " + ex.Message;

                    }
                }
            }        
        
        }
        public void EncenderServicios()
        {
            ServiceController[] services = ServiceController.GetServices();
            bool isMySQLSelected = mainWindow.mysql.IsChecked ?? false;
            bool isMSSQLSelected = mainWindow.mssql.IsChecked ?? false;


            mainWindow.Texto.Text = "";
            mainWindow.Texto.Text = "Encendiendo Servicios";

            foreach (ServiceController service in services)
            {
                string serviceName = service.DisplayName;

                if ((isMySQLSelected && serviceName.Contains("MySQL")) ||
                    (isMSSQLSelected && serviceName.Contains("SQL Server")))
                {
                    try
                    {
                        if (service.Status == ServiceControllerStatus.Stopped)
                        {                            
                            service.Start();
                            // Esperar hasta que se detenga o 30 segundos, lo que ocurra primero
                            service.WaitForStatus(ServiceControllerStatus.Running, TimeSpan.FromSeconds(30));
                            
                        }
                    }
                    catch (Exception ex)
                    {
                        // Manejo de errores si no se pudo detener el servicio
                        //Console.WriteLine("Error al detener el servicio: " + ex.Message);
                        mainWindow.Texto.Text = "";
                        mainWindow.Texto.Text = "Error al encender el servicio: " + ex.Message;

                    }
                }
            }
        }

    }
}
