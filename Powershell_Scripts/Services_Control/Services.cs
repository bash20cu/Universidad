using System;
using System.Collections.Generic;
using System.Security.Principal;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;

namespace Services_Control
{
    class Services
    {
        //private MainWindow mainWindow;
        private List<string> listaServicios = new List<string>();
        private bool isAdmin { get; set; }
        public Services(TextBox Texto)
        {
            isAdmin = TestAdminRights(); // Valor inicial            
            listaServicios = ListarServicios(Texto);
        }

        public bool IsAdmin
        {
            get { return isAdmin; }
        }

        private bool TestAdminRights()
        {
            WindowsIdentity identity = WindowsIdentity.GetCurrent();
            WindowsPrincipal principal = new WindowsPrincipal(identity);
            // Comprueba si el usuario actual tiene derechos administrativos
            isAdmin = principal.IsInRole(WindowsBuiltInRole.Administrator);
            return isAdmin;
        }

        private List<string> ListarServicios(TextBox Texto)
        {
            ServiceController[] services = ServiceController.GetServices();
            bool serviciosNoEncontrados = true;

            // Crear un StringBuilder para almacenar los resultados
            StringBuilder resultado = new StringBuilder();
            resultado.AppendLine(string.Format("{0,-28} {1,-60}", "Status", "Name"));

            if (isAdmin)
            {
                foreach (ServiceController service in services)
                {
                    string serviceName = service.DisplayName;

                    if (serviceName.Contains("MySQL") || serviceName.Contains("SQL Server"))
                    {
                        serviciosNoEncontrados = false;
                        listaServicios.Add(service.ServiceName);
                        string nombreServicio = service.ServiceName;
                        string estadoServicio = service.Status.ToString();

                        // Formatear los datos en columnas
                        string fila = string.Format("{0,-10}\t{1,-60}", estadoServicio, nombreServicio);
                        resultado.AppendLine(fila);
                    }
                }
                if (serviciosNoEncontrados)
                {
                    Texto.Text = "";
                    Texto.Text = "Servicios No Encontrados";
                    return listaServicios;
                }
                Texto.Text = resultado.ToString();
            }
            return listaServicios;
        }

        public async Task ApagarServiciosAsync(TextBox Texto, ProgressBar progressBar, CheckBox mysql, CheckBox mssql)
        {
            Texto.Text = "";
            bool isMySQLSelected = mysql.IsChecked ?? false;
            bool isMSSQLSelected = mssql.IsChecked ?? false;
            int totalServicios = listaServicios.Count;
            int serviciosApagados = 0;
            progressBar.Value = 0;


            foreach (string serviceName in listaServicios)
            {
                try
                {
                    ServiceController service = new ServiceController(serviceName);

                    if ((isMSSQLSelected && service.Status == ServiceControllerStatus.Running) && service.DisplayName.Contains("SQL Server"))
                    {
                        await DetenerServicioAsync(service);
                    }

                    if ((isMySQLSelected && service.Status == ServiceControllerStatus.Running && service.DisplayName.Contains("MySQL")))
                    {
                        await DetenerServicioAsync(service);
                    }
                    // Actualiza la barra de progreso después de cada servicio apagado
                    serviciosApagados++;
                    await UpdateProgressBarAsync(progressBar, serviciosApagados, totalServicios);
                    // Muestra el servicio apagado en tiempo real
                    Texto.AppendText($"Servicio apagado: {serviceName}\n");
                }
                catch (Exception ex)
                {
                    // Manejo de errores si no se pudo detener el servicio
                    Texto.AppendText($"Error al detener el servicio {serviceName}: {ex.Message}\n");
                }
            }

            // Completado: Puedes mostrar un mensaje cuando se hayan apagado todos los servicios
            Texto.AppendText("Servicios apagados correctamente\n");
            listaServicios = ListarServicios(Texto);
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

        public async Task EncenderServiciosAsync(TextBox Texto, ProgressBar progressBar, CheckBox mysql, CheckBox mssql)
        {
            ServiceController[] services = ServiceController.GetServices();
            bool isMySQLSelected = mysql.IsChecked ?? false;
            bool isMSSQLSelected = mssql.IsChecked ?? false;
            int totalServicios = listaServicios.Count;
            int serviciosEncendidos = 0;
            progressBar.Value = 0;

            Texto.Text = "";

            foreach (string serviceName in listaServicios)
            {
                try
                {
                    ServiceController service = new ServiceController(serviceName);

                    if ((isMSSQLSelected && service.Status == ServiceControllerStatus.Stopped) && service.DisplayName.Contains("SQL Server"))
                    {
                        await IniciarServicioAsync(service);
                    }

                    if ((isMySQLSelected && service.Status == ServiceControllerStatus.Stopped && service.DisplayName.Contains("MySQL")))
                    {
                        await IniciarServicioAsync(service);
                    }
                    // Actualiza la barra de progreso después de cada servicio apagado
                    serviciosEncendidos++;
                    await UpdateProgressBarAsync(progressBar, serviciosEncendidos, totalServicios);
                    // Muestra el servicio encendido en tiempo real
                    Texto.AppendText($"Servicio encendido: {serviceName}\n");

                }
                catch (Exception ex)
                {
                    // Manejo de errores si no se pudo iniciar el servicio
                    Texto.Text = "";
                    Texto.Text = "Error al encender el servicio: " + ex.Message;
                }

            }
            Texto.Text = "Iniciados todos los servicios Servicios";
            listaServicios = ListarServicios(Texto);
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

        private async Task UpdateProgressBarAsync(ProgressBar progressBar, int currentValue, int totalValue)
        {
            // Calcula el progreso como un porcentaje
            int progressPercentage = (currentValue * 100) / totalValue;


            // Actualiza la barra de progreso
            progressBar.Value = progressPercentage;

            // Espera un breve período de tiempo para que la interfaz de usuario tenga tiempo de reflejar el cambio
            await Task.Delay(100);
        }

    }
}
