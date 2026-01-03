using GestionDePersonas.BL;
using GestionDePersonas.Model;
using Moq;
using System.Threading.Tasks;
using Xunit;
namespace GestionDePersonas.BL.AdministradorDePersonas_Tests;
public class ObtengaLaPersonaAsync_Tests
{
    [Fact]
    [Trait("Funcionalidad", "ObtengaLaPersonaAsync")]
    [Trait("Escenario", "IdValido")]
    public async Task ObtengaLaPersonaAsync_IdValido_RepoRetornaPersona()
    {
        var repoMock = new Mock<IPersonaRepository>();
        var personaEsperada = new Persona { Id = 5 };
        repoMock.Setup(r => r.ObtenerPorIdAsync(5)).ReturnsAsync(personaEsperada);

        var admin = new AdministradorDePersonas(repoMock.Object);

        var resultado = await admin.ObtengaLaPersonaAsync(5);

        Assert.Equal(personaEsperada, resultado);
    }

    [Fact]
    [Trait("Funcionalidad", "ObtengaLaPersonaAsync")]
    [Trait("Escenario", "IdNoExiste")]
    public async Task ObtengaLaPersonaAsync_IdNoExiste_RepoRetornaNull()
    {
        var repoMock = new Mock<IPersonaRepository>();
        repoMock.Setup(r => r.ObtenerPorIdAsync(99)).ReturnsAsync((Persona?)null);

        var admin = new AdministradorDePersonas(repoMock.Object);

        var resultado = await admin.ObtengaLaPersonaAsync(99);

        Assert.Null(resultado);
    }
}