using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;

namespace Services_Control
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            ProgressBar.Value = 0;
            Services services = new Services(Texto);

            if (!services.IsAdmin)
            {
                MessageBox.Show("Esta aplicacion necesita provilegios administrativos", "Advertencia", MessageBoxButton.OK, MessageBoxImage.Warning);
                btnEncenderServicios.IsEnabled = false;
                btnShutdownServices.IsEnabled = false;
                Texto.Text = "No tiene privilegios administratvos para acceder a los servicios, por favor ejecute la aplicacion como Administrador";
            }
            if (Texto.Text == "Servicios No Encontrados")
            {
                mssql.IsEnabled = false;
                mysql.IsEnabled = false;
                Texto.IsEnabled = false;
            }


            //Graficos graficos = new Graficos(this);


        }

        private void btnShutdownServices_Click(object sender, RoutedEventArgs e)
        {     
            // Inicializa la barra de progreso
            ProgressBar.Value = 0;
            ProgressBar.Maximum = 100; // Puedes ajustar este valor según tu preferencia

            Services services = new Services(Texto);
            services.ApagarServiciosAsync(Texto, ProgressBar, mysql, mssql);

        }

        private void btnEncenderServicios_Click(object sender, RoutedEventArgs e)
        {
            ProgressBar.Value = 0;
            ProgressBar.Maximum = 100;
            Services services = new Services(Texto);
            services.EncenderServiciosAsync(Texto, ProgressBar, mysql, mssql);
        }
    }
}
