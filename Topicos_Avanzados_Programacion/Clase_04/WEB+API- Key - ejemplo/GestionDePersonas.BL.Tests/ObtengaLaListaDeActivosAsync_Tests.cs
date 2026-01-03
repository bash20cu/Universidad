using GestionDePersonas.BL;
using GestionDePersonas.Model;
using Moq;
using System.Collections.Generic;
using System.Threading.Tasks;
using Xunit;
namespace GestionDePersonas.BL.AdministradorDePersonas_Tests;
public class ObtengaLaListaDeActivosAsync_Tests
{
    [Fact]
    [Trait("Funcionalidad", "ObtengaLaListaDeActivosAsync")]
    [Trait("Escenario", "RepoRetornaLista")]
    public async Task ObtengaLaListaDeActivosAsync_RepoRetornaLista_RetornaMismaLista()
    {
        var repoMock = new Mock<IPersonaRepository>();
        var listaEsperada = new List<Persona> { new Persona { Estado = Estado.Activo } };
        repoMock.Setup(r => r.ObtenerActivosAsync()).ReturnsAsync(listaEsperada);

        var admin = new AdministradorDePersonas(repoMock.Object);

        var resultado = await admin.ObtengaLaListaDeActivosAsync();

        Assert.Equal(listaEsperada, resultado);
    }

    [Fact]
    [Trait("Funcionalidad", "ObtengaLaListaDeActivosAsync")]
    [Trait("Escenario", "RepoRetornaNull")]
    public async Task ObtengaLaListaDeActivosAsync_RepoRetornaNull_RetornaNull()
    {
        var repoMock = new Mock<IPersonaRepository>();
        repoMock.Setup(r => r.ObtenerActivosAsync()).ReturnsAsync((IEnumerable<Persona>?)null);

        var admin = new AdministradorDePersonas(repoMock.Object);

        var resultado = await admin.ObtengaLaListaDeActivosAsync();

        Assert.Null(resultado);
    }
}