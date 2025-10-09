using Microsoft.AspNetCore.Mvc;
namespace GestionDePersonas.UI.Controllers
{
    public class GestionDePersonasActivasController(ServicioApi servicioApis) : Controller
    {
        private readonly ServicioApi _servicioApis = servicioApis;
        public async Task<IActionResult> Index()
        {
            List<Model.Persona> lista;
            try
            {         
                lista = await _servicioApis.ObtenerPersonasActivasAsync();
                ViewData["ProblemasAlConsultar"] = false;
                return View(lista);
            }
            catch 
            {
                lista = [];
                ViewData["ProblemasAlConsultar"] = true;
                return View(lista);
            }
        }      
    }


}
