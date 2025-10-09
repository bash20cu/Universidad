using GestionDePersonas.Model;
using Moq;
using System.Collections.Generic;
using System.Threading.Tasks;
using Xunit;
namespace GestionDePersonas.BL.AdministradorDePersonas_Tests;
public class ObtengaLaListaAsync_Tests
{
    [Fact]
    [Trait("Funcionalidad", "ObtengaLaListaAsync")]
    [Trait("Escenario", "RepoRetornaLista")]
    public async Task ObtengaLaListaAsync_RepoRetornaLista_RetornaMismaLista()
    {
        var repoMock = new Mock<IPersonaRepository>();
        var listaEsperada = new List<Persona> { new Persona { Id = 1 }, new Persona { Id = 2 } };
        repoMock.Setup(r => r.ObtenerAsync()).ReturnsAsync(listaEsperada);

        var admin = new AdministradorDePersonas(repoMock.Object);

        var resultado = await admin.ObtengaLaListaAsync();

        Assert.Equal(listaEsperada, resultado);
    }

    [Fact]
    [Trait("Funcionalidad", "ObtengaLaListaAsync")]
    [Trait("Escenario", "RepoRetornaNull")]
    public async Task ObtengaLaListaAsync_RepoRetornaNull_RetornaNull()
    {
        var repoMock = new Mock<IPersonaRepository>();
        repoMock.Setup(r => r.ObtenerAsync()).ReturnsAsync((IEnumerable<Persona>?)null);

        var admin = new AdministradorDePersonas(repoMock.Object);

        var resultado = await admin.ObtengaLaListaAsync();

        Assert.Null(resultado);
    }
}