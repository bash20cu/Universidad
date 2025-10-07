using GestionDePersonas.Model;
using Moq;
using System.Collections.Generic;
using System.Threading.Tasks;
using Xunit;
namespace GestionDePersonas.BL.AdministradorDePersonas_Tests;
public class ObtengaLaListaDeInActivosAsync_Tests
{
    [Fact]
    [Trait("Funcionalidad", "ObtengaLaListaDeInActivosAsync")]
    [Trait("Escenario", "RepoRetornaLista")]
    public async Task ObtengaLaListaDeInActivosAsync_RepoRetornaLista_RetornaMismaLista()
    {
        var repoMock = new Mock<IPersonaRepository>();
        var listaEsperada = new List<Persona> { new Persona { Estado = Estado.InActivo } };
        repoMock.Setup(r => r.ObtenerInActivosAsync()).ReturnsAsync(listaEsperada);

        var admin = new AdministradorDePersonas(repoMock.Object);

        var resultado = await admin.ObtengaLaListaDeInActivosAsync();

        Assert.Equal(listaEsperada, resultado);
    }

    [Fact]
    [Trait("Funcionalidad", "ObtengaLaListaDeInActivosAsync")]
    [Trait("Escenario", "RepoRetornaNull")]
    public async Task ObtengaLaListaDeInActivosAsync_RepoRetornaNull_RetornaNull()
    {
        var repoMock = new Mock<IPersonaRepository>();
        repoMock.Setup(r => r.ObtenerInActivosAsync()).ReturnsAsync((IEnumerable<Persona>?)null);

        var admin = new AdministradorDePersonas(repoMock.Object);

        var resultado = await admin.ObtengaLaListaDeInActivosAsync();

        Assert.Null(resultado);
    }
}