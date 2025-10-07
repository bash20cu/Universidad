    using GestionDePersonas.BL;
using GestionDePersonas.Model;
using Moq;
using System.Threading.Tasks;
using Xunit;
namespace GestionDePersonas.BL.AdministradorDePersonas_Tests;
public class AgregueAsync_Tests
{
    [Fact]
    [Trait("Funcionalidad", "AgregueAsync")]
    [Trait("Escenario", "PersonaValida")]
    public async Task AgregueAsync_PersonaValida_EstableceEstadoActivoYLlamaAgregarAsync()
    {
        var repoMock = new Mock<IPersonaRepository>();
        var admin = new AdministradorDePersonas(repoMock.Object);
        var persona = new Persona();

        await admin.AgregueAsync(persona);

        Assert.Equal(Estado.Activo, persona.Estado);
        repoMock.Verify(r => r.AgregarAsync(persona), Times.Once);
    }

    [Fact]
    [Trait("Funcionalidad", "AgregueAsync")]
    [Trait("Escenario", "PersonaNula")]
    public async Task AgregueAsync_PersonaNula_LanzaExcepcion()
    {
        var repoMock = new Mock<IPersonaRepository>();
        var admin = new AdministradorDePersonas(repoMock.Object);

        await Assert.ThrowsAsync<System.NullReferenceException>(() => admin.AgregueAsync(null!));
    }
}