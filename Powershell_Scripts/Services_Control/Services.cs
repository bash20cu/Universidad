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

namespace Services_Control
{   
    class Services
    {
        private MainWindow mainWindow;
        public Services(MainWindow main) 
        {
            mainWindow = main;
            TestAdminRights();

            ListarServicios();
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

            bool isMySQLSelected = mainWindow.mysql.IsChecked ?? false;
            bool isMSSQLSelected = mainWindow.mssql.IsChecked ?? false;

            foreach (ServiceController service in services)
            {
                if ((isMySQLSelected  && service.DisplayName.Contains("MySQL")) ||
                    (isMSSQLSelected  && service.DisplayName.Contains("SQL Server")))
                {
                    resultado.AppendLine("Nombre del servicio: " + service.ServiceName);
                    resultado.AppendLine("Estado del servicio: " + service.Status);
                    resultado.AppendLine("--------------------------------------");
                }

                // Agregar la información del servicio al TextBox
                mainWindow.Texto.Text = resultado.ToString();
            }
        }

    }
}
