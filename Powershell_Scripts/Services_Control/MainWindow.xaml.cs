using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

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
            Services services = new Services(this);
            Graficos graficos = new Graficos(this);
            
        }

        private void btnShutdownServices_Click(object sender, RoutedEventArgs e)
        {
            // Inicializa la barra de progreso
            ProgressBar.Value = 0;
            ProgressBar.Maximum = 100; // Puedes ajustar este valor según tu preferencia
            Services services = new Services(this);
            services.ApagarServiciosAsync();
            
        }

        private void btnEncenderServicios_Click(object sender, RoutedEventArgs e)
        {
            ProgressBar.Value = 0;
            ProgressBar.Maximum = 100;
            Services services = new Services(this);
            services.EncenderServiciosAsync();
        }
    }
}
