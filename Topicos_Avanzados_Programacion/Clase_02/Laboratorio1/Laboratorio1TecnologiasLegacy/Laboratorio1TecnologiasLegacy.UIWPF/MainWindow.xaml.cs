using Laboratorio1TecnologiasLegacy.UIWPF.ServicioVehiculoWebService;
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

namespace Laboratorio1TecnologiasLegacy.UIWPF
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            CargarVehiculos();
        }
        private void CargarVehiculos()
        {
            // Instancia del cliente WCF generado por la referencia de servicio
            var cliente = new SI.WCF.ServiceWCFVehiculo();

            // Consulta los datos (ajusta el método si tu servicio tiene otro nombre)
            var vehiculosModel = cliente.ObtenerTodas();

            // Mapea los objetos del modelo a los del servicio web
            var vehiculos = vehiculosModel.Select(v => new Laboratorio1TecnologiasLegacy.UIWPF.ServicioVehiculoWebService.Vehiculo
            {
                Id = v.Id,
                Marca = (Laboratorio1TecnologiasLegacy.UIWPF.ServicioVehiculoWebService.Marca)
                    Enum.Parse(typeof(Laboratorio1TecnologiasLegacy.UIWPF.ServicioVehiculoWebService.Marca), v.Marca.ToString()), // Conversión de enum porque se genera diferente
                Anio = v.Anio,
                Modelo = v.Modelo,
                DobleTraccion = v.DobleTraccion
            }).ToList();

            // Muestra los datos en el DataGrid
            VehiculosDataGrid.ItemsSource = vehiculos;

        }

    }
}
