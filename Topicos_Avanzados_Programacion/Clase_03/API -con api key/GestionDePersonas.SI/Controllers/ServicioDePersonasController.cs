using GestionDePersonas.BL;
using GestionDePersonas.Model;
using Microsoft.AspNetCore.Mvc;

namespace GestionDePersonas.SI.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ServicioDePersonasController : ControllerBase
    {
        private readonly IAdministradorDePersonas _admin;

        public ServicioDePersonasController(IAdministradorDePersonas admin)
        {
            _admin = admin;
        }

        [HttpGet("ObtengaLaLista")]
        public async Task<ActionResult<IEnumerable<Persona>>> ObtengaLaLista()
        {
            var lista = await _admin.ObtengaLaListaAsync();
            return Ok(lista);
        }

        [HttpGet("ObtengaLaListaDeActivos")]
        public async Task<ActionResult<IEnumerable<Persona>>> ObtengaLaListaDeActivos()
        {
            var lista = await _admin.ObtengaLaListaDeActivosAsync();
            return Ok(lista);
        }

        [HttpGet("ObtengaLaListaDeInActivos")]
        public async Task<ActionResult<IEnumerable<Persona>>> ObtengaLaListaDeInActivos()
        {
            var lista = await _admin.ObtengaLaListaDeInActivosAsync();
            return Ok(lista);
        }

        [HttpGet("ObtengaLaPersona")]
        public async Task<ActionResult<Persona>> ObtengaLaPersona(int id)
        {
            var persona = await _admin.ObtengaLaPersonaAsync(id);
            if (persona == null)
                return NotFound();
            return Ok(persona);
        }

        [HttpPost("Agregue")]
        public async Task<IActionResult> Agregue([FromBody] Persona persona)
        {
            await _admin.AgregueAsync(persona);
            return Ok();
        }

        [HttpPut("EditeLaPersona")]
        public async Task<IActionResult> EditeLaPersona([FromBody] Persona persona)
        {
            await _admin.EditeLaPersonaAsync(persona);
            return Ok();
        }

        [HttpPut("Active")]
        public async Task<IActionResult> Active(int id)
        {
            await _admin.ActiveAsync(id);
            return Ok();
        }

        [HttpPut("DesActive")]
        public async Task<IActionResult> DesActive(int id)
        {
            await _admin.DesActiveAsync(id);
            return Ok();
        }
    }
}