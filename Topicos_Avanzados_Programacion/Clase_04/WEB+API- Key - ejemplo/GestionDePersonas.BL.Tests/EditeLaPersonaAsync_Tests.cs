
using GestionDePersonas.Model;
using Moq;
using System.Threading.Tasks;
using Xunit;
namespace GestionDePersonas.BL.AdministradorDePersonas_Tests;
public class EditeLaPersonaAsync_Tests
{
    [Fact]
    [Trait("Funcionalidad", "EditeLaPersonaAsync")]
    [Trait("Escenario", "PersonaExiste")]
    public async Task EditeLaPersonaAsync_PersonaExiste_ModificaYActualiza()
    {
        var repoMock = new Mock<IPersonaRepository>();
        var personaOriginal = new Persona { Id = 10, Nombre = "Antiguo", Apellidos = "Apellido" };
        repoMock.Setup(r => r.ObtenerPorIdAsync(10)).ReturnsAsync(personaOriginal);

        var admin = new AdministradorDePersonas(repoMock.Object);
        var personaEditada = new Persona { Id = 10, Nombre = "Nuevo", Apellidos = "NuevoApellido" };

        await admin.EditeLaPersonaAsync(personaEditada);

        Assert.Equal("Nuevo", personaOriginal.Nombre);
        Assert.Equal("NuevoApellido", personaOriginal.Apellidos);
        repoMock.Verify(r => r.ActualizarAsync(personaOriginal), Times.Once);
    }

    [Fact]
    [Trait("Funcionalidad", "EditeLaPersonaAsync")]
    [Trait("Escenario", "PersonaNoExiste")]
    public async Task EditeLaPersonaAsync_PersonaNoExiste_NoActualiza()
    {
        var repoMock = new Mock<IPersonaRepository>();
        repoMock.Setup(r => r.ObtenerPorIdAsync(99)).ReturnsAsync((Persona?)null);

        var admin = new AdministradorDePersonas(repoMock.Object);
        var personaEditada = new Persona { Id = 99, Nombre = "Nuevo", Apellidos = "NuevoApellido" };

        await admin.EditeLaPersonaAsync(personaEditada);

        repoMock.Verify(r => r.ActualizarAsync(It.IsAny<Persona>()), Times.Never);
    }

    [Fact]
    [Trait("Funcionalidad", "EditeLaPersonaAsync")]
    [Trait("Escenario", "PersonaNula")]
    public async Task EditeLaPersonaAsync_PersonaNula_LanzaExcepcion()
    {
        var repoMock = new Mock<IPersonaRepository>();
        var admin = new AdministradorDePersonas(repoMock.Object);

        await Assert.ThrowsAsync<System.NullReferenceException>(() => admin.EditeLaPersonaAsync(null!));
    }
}
