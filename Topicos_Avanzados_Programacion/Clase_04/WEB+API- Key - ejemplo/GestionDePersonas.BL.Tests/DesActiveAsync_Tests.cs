using GestionDePersonas.BL;
using Moq;
using System.Threading.Tasks;
using Xunit;
namespace GestionDePersonas.BL.AdministradorDePersonas_Tests;
public class DesActiveAsync_Tests
{
    [Fact]
    [Trait("Funcionalidad", "DesActiveAsync")]
    [Trait("Escenario", "IdValido")]
    public async Task DesActiveAsync_IdValido_LlamaDesActivarAsync()
    {
        var repoMock = new Mock<IPersonaRepository>();
        var admin = new AdministradorDePersonas(repoMock.Object);

        await admin.DesActiveAsync(2);

        repoMock.Verify(r => r.DesActivarAsync(2), Times.Once);
    }

    [Fact]
    [Trait("Funcionalidad", "DesActiveAsync")]
    [Trait("Escenario", "RepoLanzaExcepcion")]
    public async Task DesActiveAsync_RepoLanzaExcepcion_PropagaExcepcion()
    {
        var repoMock = new Mock<IPersonaRepository>();
        repoMock.Setup(r => r.DesActivarAsync(It.IsAny<int>())).ThrowsAsync(new System.Exception("Error"));
        var admin = new AdministradorDePersonas(repoMock.Object);

        await Assert.ThrowsAsync<System.Exception>(() => admin.DesActiveAsync(2));
    }
}