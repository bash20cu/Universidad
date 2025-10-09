using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Laboratorio.Model
{
    public interface IVehiculoRepository
    {
        Task<Vehiculo> ObtenerPorIdAsync(int id);
        Task<IEnumerable<Vehiculo>> ObtenerAsync();
        Task AgregarAsync(Vehiculo vehiculo);
        Task ActualizarAsync(Vehiculo vehiculo);
        Task EliminarAsync(int id);
    }
}
