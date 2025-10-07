using GestionDePersonas.Model;
namespace GestionDePersonas.BL
{
    public interface IPersonaRepository
    {
        Task<Persona?> ObtenerPorIdAsync(int id);
        Task<IEnumerable<Persona>> ObtenerAsync();
        Task AgregarAsync(Persona persona);
        Task ActualizarAsync(Persona persona);
        Task EliminarAsync(int id);
        Task ActivarAsync(int id);
        Task DesActivarAsync(int id);
        Task<IEnumerable<Persona>> ObtenerActivosAsync();
        Task<IEnumerable<Persona>> ObtenerInActivosAsync();
    }
}
