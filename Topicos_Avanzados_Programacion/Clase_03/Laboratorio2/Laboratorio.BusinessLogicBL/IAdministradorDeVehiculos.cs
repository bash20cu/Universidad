using Laboratorio.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Laboratorio.BusinessLogicBL
{
    public interface IAdministradorDeVehiculos
    {
        Task AgregueAsync(Vehiculo vehiculo);
        Task<IEnumerable<Vehiculo>> ObtengaLaListaAsync();
        Task<Vehiculo> ObtengaELVehiculosync(int id);
        Task EditeElVehiculoAsync(Vehiculo vehiculo);
    }
}
