using Laboratorio.BusinessLogicBL;
using Laboratorio.Model;
using Microsoft.AspNetCore.Mvc;

namespace Laboratorio.SI.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ServicioDeVehiculosController : ControllerBase
    {
        private readonly IAdministradorDeVehiculos _admin;

        public ServicioDeVehiculosController(IAdministradorDeVehiculos admin)
        {
            _admin = admin;
        }

        [HttpGet("ObtengaLaLista")]
        public async Task<ActionResult<IEnumerable<Vehiculo>>> ObtengaLaLista()
        {
            var lista = await _admin.ObtengaLaListaAsync();
            return Ok(lista);
        }


        [HttpGet("ObtengaElVehiculo")]
        public async Task<ActionResult<Vehiculo>> ObtengaLaPersona(int id)
        {
            var vehiculo = await _admin.ObtengaELVehiculosync(id);
            if (vehiculo == null)
                return NotFound();
            return Ok(vehiculo);
        }

        [HttpPost("Agregue")]
        public async Task<IActionResult> Agregue([FromBody] Vehiculo vehiculo)
        {
            await _admin.AgregueAsync(vehiculo);
            return Ok();
        }

        [HttpPut("EditeElVehiculo")]
        public async Task<IActionResult> EditeLaPersona([FromBody] Vehiculo vehiculo)
        {
            await _admin.EditeElVehiculoAsync(vehiculo);
            return Ok();
        }
    }
}
