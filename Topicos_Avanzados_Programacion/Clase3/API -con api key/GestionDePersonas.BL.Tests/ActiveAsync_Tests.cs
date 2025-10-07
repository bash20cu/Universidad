using Moq;
using System.Threading.Tasks;
using Xunit;

namespace GestionDePersonas.BL.AdministradorDePersonas_Tests;
public class ActiveAsync_Tests
{
    [Fact]
    [Trait("Funcionalidad", "ActiveAsync")]
    [Trait("Escenario", "IdValido")]
    public async Task ActiveAsync_IdValido_LlamaActivarAsync()
    {
        var repoMock = new Mock<IPersonaRepository>();
        var admin = new AdministradorDePersonas(repoMock.Object);

        await admin.ActiveAsync(1);

        repoMock.Verify(r => r.ActivarAsync(1), Times.Once);
    }

    [Fact]
    [Trait("Funcionalidad", "ActiveAsync")]
    [Trait("Escenario", "RepoLanzaExcepcion")]
    public async Task ActiveAsync_RepoLanzaExcepcion_PropagaExcepcion()
    {
        var repoMock = new Mock<IPersonaRepository>();
        repoMock.Setup(r => r.ActivarAsync(It.IsAny<int>())).ThrowsAsync(new System.Exception("Error"));
        var admin = new AdministradorDePersonas(repoMock.Object);

        await Assert.ThrowsAsync<System.Exception>(() => admin.ActiveAsync(1));
    }
}