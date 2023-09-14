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
using System.Windows.Controls;
using System.Threading;
using System.Windows.Threading;
using System.Windows;
using System.Runtime.CompilerServices;

namespace Services_Control
{
    class Services
    {
        private MainWindow mainWindow;
        private List<string> listaServicios = new List<string>();
        public Services(MainWindow main)
        {
            mainWindow = main;            
            if (!TestAdminRights())
            {
                MessageBox.Show("Esta aplicacion necesita provilegios administrativos", "Advertencia", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            // mainWindow.Texto.Text = "";
            listaServicios = ListarServicios();
        }

        private bool TestAdminRights()
        {
            WindowsIdentity identity = WindowsIdentity.GetCurrent();
            WindowsPrincipal principal = new WindowsPrincipal(identity);
            // Comprueba si el usuario actual tiene derechos administrativos
            bool isAdmin = principal.IsInRole(WindowsBuiltInRole.Administrator);  
            if (!isAdmin) 
            {
                mainWindow.btnEncenderServicios.IsEnabled = false;
                mainWindow.btnShutdownServices.IsEnabled = false;   
            }
            return isAdmin;
        }

        private List<string> ListarServicios()
        {
            ServiceController[] services = ServiceController.GetServices();
            //List<string> listaServicios = new List<string>();

            // Crear un StringBuilder para almacenar los resultados
            StringBuilder resultado = new StringBuilder();
            resultado.AppendLine(string.Format("{0,-28} {1,-60}", "Status", "Name"));

            if (!TestAdminRights()) 
            {
                mainWindow.Texto.Text = "No tiene privilegios administratvos para acceder a los servicios, por favor ejecute la aplicacion como Administrador";
            } 
            else
            {
                foreach (ServiceController service in services)
                {
                    string serviceName = service.DisplayName;

                    if (serviceName.Contains("MySQL") || serviceName.Contains("SQL Server"))
                    {
                        listaServicios.Add(service.ServiceName);
                        string nombreServicio = service.ServiceName;
                        string estadoServicio = service.Status.ToString();

                        // Formatear los datos en columnas
                        string fila = string.Format("{0,-10}\t{1,-60}", estadoServicio, nombreServicio);
                        resultado.AppendLine(fila);
                    }
                }

                // Mostrar los resultados en formato de tabla
                mainWindow.Texto.Text = "";
                mainWindow.Texto.Text = resultado.ToString();
            }
            return listaServicios;

        }

        public async Task ApagarServiciosAsync()
        {
            mainWindow.Texto.Text = "";
            bool isMySQLSelected = mainWindow.mysql.IsChecked ?? false;
            bool isMSSQLSelected = mainWindow.mssql.IsChecked ?? false;
            int totalServicios = listaServicios.Count;
            int serviciosApagados = 0;


            foreach (string serviceName in listaServicios)
            {
                try
                {
                    ServiceController service = new ServiceController(serviceName);

                    if ((isMSSQLSelected && service.Status == ServiceControllerStatus.Running) && service.DisplayName.Contains("SQL Server"))
                    {
                        await DetenerServicioAsync(service);
                        // Actualiza la barra de progreso después de cada servicio apagado
                        serviciosApagados++;
                        int porcentajeCompleto = (serviciosApagados * 100) / totalServicios;
                        // Muestra el servicio apagado en tiempo real
                        mainWindow.Texto.AppendText($"Servicio apagado: {serviceName}\n");
                    }

                    if ((isMySQLSelected && service.Status == ServiceControllerStatus.Running && service.DisplayName.Contains("MySQL")))
                    {
                        await DetenerServicioAsync(service);
                        // Actualiza la barra de progreso después de cada servicio apagado
                        serviciosApagados++;
                        int porcentajeCompleto = (serviciosApagados * 100) / totalServicios;
                        // Muestra el servicio apagado en tiempo real
                        mainWindow.Texto.AppendText($"Servicio apagado: {serviceName}\n");
                    }

                }
                catch (Exception ex)
                {
                    // Manejo de errores si no se pudo detener el servicio
                    mainWindow.Texto.AppendText($"Error al detener el servicio {serviceName}: {ex.Message}\n");
                }
            }

            // Completado: Puedes mostrar un mensaje cuando se hayan apagado todos los servicios
            mainWindow.Texto.AppendText("Servicios apagados correctamente\n");
            listaServicios = ListarServicios();
        }

        private async Task DetenerServicioAsync(ServiceController service)
        {
            await Task.Run(() =>
            {
                service.Stop();
                // Esperar hasta que se detenga o 30 segundos, lo que ocurra primero
                service.WaitForStatus(ServiceControllerStatus.Stopped, TimeSpan.FromSeconds(30));
            });
        }

        public async Task EncenderServiciosAsync()
        {
            ServiceController[] services = ServiceController.GetServices();
            bool isMySQLSelected = mainWindow.mysql.IsChecked ?? false;
            bool isMSSQLSelected = mainWindow.mssql.IsChecked ?? false;

            mainWindow.Texto.Text = "";
            mainWindow.Texto.Text = "Encendiendo Servicios";

            foreach (ServiceController service in services)
            {
                try
                {
                    string serviceName = service.DisplayName;

                    if ((isMSSQLSelected && service.Status == ServiceControllerStatus.Stopped) && service.DisplayName.Contains("SQL Server"))
                    {
                        await IniciarServicioAsync(service);
                        // Actualiza la barra de progreso después de cada servicio apagado
                        //serviciosApagados++;
                        //int porcentajeCompleto = (serviciosApagados * 100) / totalServicios;
                        // Muestra el servicio apagado en tiempo real
                        mainWindow.Texto.AppendText($"Servicio encendido: {serviceName}\n");
                    }

                    if ((isMySQLSelected && service.Status == ServiceControllerStatus.Stopped && service.DisplayName.Contains("MySQL")))
                    {
                        await IniciarServicioAsync(service);
                        // Actualiza la barra de progreso después de cada servicio apagado
                        //serviciosApagados++;
                        //int porcentajeCompleto = (serviciosApagados * 100) / totalServicios;
                        // Muestra el servicio apagado en tiempo real
                        mainWindow.Texto.AppendText($"Servicio encendido: {serviceName}\n");
                    }
                }
                catch (Exception ex)
                {
                    // Manejo de errores si no se pudo iniciar el servicio
                        mainWindow.Texto.Text = "";
                        mainWindow.Texto.Text = "Error al encender el servicio: " + ex.Message;                   
                }

            }
            mainWindow.Texto.Text = "Iniciados todos los servicios Servicios";
            listaServicios = ListarServicios();
        }
        private async Task IniciarServicioAsync(ServiceController service)
        {
            await Task.Run(() =>
            {
                service.Start();
                // Esperar hasta que se inicie o 30 segundos, lo que ocurra primero
                service.WaitForStatus(ServiceControllerStatus.Running, TimeSpan.FromSeconds(30));
            });
        }

    }
}
