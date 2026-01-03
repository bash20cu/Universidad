using GestionDePersonas.Model;

namespace GestionDePersonas.BL
{
  public interface IAdministradorDePersonas
    {
        Task ActiveAsync(int id);
        Task DesActiveAsync(int id);
        Task AgregueAsync(Persona persona);
        Task<IEnumerable<Persona>> ObtengaLaListaAsync();
        Task<IEnumerable<Persona>> ObtengaLaListaDeActivosAsync();
        Task<IEnumerable<Persona>> ObtengaLaListaDeInActivosAsync();
        Task<Persona?> ObtengaLaPersonaAsync(int id);
        Task EditeLaPersonaAsync(Persona persona);

    }
}
