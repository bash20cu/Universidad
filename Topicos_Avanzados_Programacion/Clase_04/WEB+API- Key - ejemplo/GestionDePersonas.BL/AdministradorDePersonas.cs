using GestionDePersonas.Model;

namespace GestionDePersonas.BL
{
    public class AdministradorDePersonas : IAdministradorDePersonas
    {
        private readonly IPersonaRepository _personaRepository;

        public AdministradorDePersonas(IPersonaRepository personaRepository)
        {
            _personaRepository = personaRepository;
        }

        public async Task ActiveAsync(int id)
        {
            await _personaRepository.ActivarAsync(id);
        }

        public async Task DesActiveAsync(int id)
        {
            await _personaRepository.DesActivarAsync(id);
        }

        public async Task AgregueAsync(Persona persona)
        {
            persona.Estado = Estado.Activo;
            await _personaRepository.AgregarAsync(persona);
        }

        public async Task<IEnumerable<Persona>> ObtengaLaListaAsync()
        {
            return await _personaRepository.ObtenerAsync();
        }

        public async Task<IEnumerable<Persona>> ObtengaLaListaDeActivosAsync()
        {
            return await _personaRepository.ObtenerActivosAsync();
        }

        public async Task<IEnumerable<Persona>> ObtengaLaListaDeInActivosAsync()
        {
            return await _personaRepository.ObtenerInActivosAsync();
        }

        public async Task<Persona?> ObtengaLaPersonaAsync(int id)
        {
            return await _personaRepository.ObtenerPorIdAsync(id);
        }

        public async Task EditeLaPersonaAsync(Persona persona)
        {
            var laPersonaAModificar = await _personaRepository.ObtenerPorIdAsync(persona.Id);
            if (laPersonaAModificar != null)
            {
                laPersonaAModificar.Nombre = persona.Nombre;
                laPersonaAModificar.Apellidos = persona.Apellidos;
                await _personaRepository.ActualizarAsync(laPersonaAModificar);
            }
        }
    }
}