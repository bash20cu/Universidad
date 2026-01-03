using GestionDePersonas.Model;
using GestionDePersonas.BL;
using Microsoft.EntityFrameworkCore;

namespace GestionDePersonas.DA
{
    public class PersonaRepository : IPersonaRepository
    {
        private readonly DBContexto _context;

        public PersonaRepository(DBContexto context)
        {
            _context = context;
        }

        public async Task<Persona?> ObtenerPorIdAsync(int id)
        {
            return await _context.Personas.FirstOrDefaultAsync(p => p.Id == id);
        }

        public async Task<IEnumerable<Persona>> ObtenerAsync()
        {
            return await _context.Personas.ToListAsync();
        }

        public async Task AgregarAsync(Persona persona)
        {
            await _context.Personas.AddAsync(persona);
            await _context.SaveChangesAsync();
        }

        public async Task ActualizarAsync(Persona persona)
        {
            _context.Personas.Update(persona);
            await _context.SaveChangesAsync();
        }

        public async Task EliminarAsync(int id)
        {
            var persona = await ObtenerPorIdAsync(id);
            if (persona != null)
            {
                _context.Personas.Remove(persona);
                await _context.SaveChangesAsync();
            }
        }

        public async Task ActivarAsync(int id)
        {
            var persona = await ObtenerPorIdAsync(id);
            if (persona != null)
            {
                persona.Estado = Estado.Activo;
                _context.Personas.Update(persona);
                await _context.SaveChangesAsync();
            }
        }

        public async Task DesActivarAsync(int id)
        {
            var persona = await ObtenerPorIdAsync(id);
            if (persona != null)
            {
                persona.Estado = Estado.InActivo;
                _context.Personas.Update(persona);
                await _context.SaveChangesAsync();
            }
        }

        public async Task<IEnumerable<Persona>> ObtenerActivosAsync()
        {
            return await _context.Personas.Where(p => p.Estado == Estado.Activo).ToListAsync();
        }

        public async Task<IEnumerable<Persona>> ObtenerInActivosAsync()
        {
            return await _context.Personas.Where(p => p.Estado == Estado.InActivo).ToListAsync();
        }
    }
}