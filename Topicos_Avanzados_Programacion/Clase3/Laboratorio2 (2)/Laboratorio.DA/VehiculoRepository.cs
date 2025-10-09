using Laboratorio.Model;
using Laboratorio.BusinessLogicBL;
using Microsoft.EntityFrameworkCore; 


namespace Laboratorio.DA
{
    public class VehiculoRepository : IVehiculoRepository
    {
        private readonly DBContexto _context;

        public VehiculoRepository(DBContexto context)
        {
            _context = context;
        }

        public async Task<Vehiculo?> ObtenerPorIdAsync(int id)
        {
            return await _context.Vehiculos.FirstOrDefaultAsync(p => p.Id == id);
        }

        public async Task<IEnumerable<Vehiculo>> ObtenerAsync()
        {
            return await _context.Vehiculos.ToListAsync();
        }

        public async Task AgregarAsync(Vehiculo vehiculo)
        {
            await _context.Vehiculos.AddAsync(vehiculo);
            await _context.SaveChangesAsync();
        }

        public async Task ActualizarAsync(Vehiculo vehiculo)
        {
            _context.Vehiculos.Update(vehiculo);
            await _context.SaveChangesAsync();
        }

        public async Task EliminarAsync(int id)
        {
            var vehiculo = await ObtenerPorIdAsync(id);
            if (vehiculo != null)
            {
                _context.Vehiculos.Remove(vehiculo);
                await _context.SaveChangesAsync();
            }
        }

    }
}
