using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using System.Text;

namespace GestionDePersonas.UI.Controllers
{
    public class GestionDePersonasController : Controller
    {

        private const string apiKey = "123456";

        // GET: GestionDePersonasController
        public async Task<IActionResult> Index(string nombre)
        {
            List<Model.Persona> lista;

            var httpClient = new HttpClient();
          
           
            httpClient.DefaultRequestHeaders.Add("X-API-KEY", apiKey); // Agregar la clave API al encabezado


            try
            {
                httpClient.BaseAddress = new Uri("https://localhost:7119/");
                var response = await httpClient.GetAsync("api/ServicioDePersonas/ObtengaLaLista");
                response.EnsureSuccessStatusCode();
                var result = await response.Content.ReadAsStringAsync();
                lista = JsonConvert.DeserializeObject<List<Model.Persona>>(result) ?? new List<Model.Persona>();
                ViewData["ProblemasAlConsultar"] = false;
            }
            catch (Exception ex)
            {
                lista = new List<Model.Persona>();
                ViewData["ProblemasAlConsultar"] = true;
            }




            if (nombre is null)
            {
                return View(lista);

            }

            
            else
            {
                List<Model.Persona> listaFiltrada;
                listaFiltrada = lista.Where(x => x.Nombre.Contains(nombre)).ToList();
                return View(listaFiltrada);
            }
        }


  

        // GET: GestionDePersonasController/Details/5
        public async Task<IActionResult> Details(int id)
        {

            Model.Persona persona;

            try
            {
                var httpClient = new HttpClient();
                httpClient.DefaultRequestHeaders.Add("X-API-KEY", apiKey); 

                httpClient.BaseAddress = new Uri("https://localhost:7119/");
                var response = await httpClient.GetAsync($"api/ServicioDePersonas/ObtengaLaPersona?id={id}");
                response.EnsureSuccessStatusCode();
                var result = await response.Content.ReadAsStringAsync();
                persona = JsonConvert.DeserializeObject<Model.Persona>(result);
                return View(persona);
            }
            catch (Exception ex)
            {

                return View(); ;
            }

            
        }

      


        // GET: GestionDePersonasController/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: GestionDePersonasController/Create
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create(Model.Persona persona)
        {
            try
            {

                var httpClient = new HttpClient();
                httpClient.DefaultRequestHeaders.Add("X-API-KEY", apiKey); // Agregar la clave API al encabezado

                httpClient.BaseAddress = new Uri("https://localhost:7119/");
                var json = JsonConvert.SerializeObject(persona);
                var content = new StringContent(json, Encoding.UTF8, "application/json");
                var response = httpClient.PostAsync("api/ServicioDePersonas/Agregue", content).Result;
                        
                ViewData["ProblemasAlInsertar"] = false;
                return RedirectToAction(nameof(Index));
            }
            catch
            {
                ViewData["ProblemasAlInsertar"] = true;
                return View();
            }
        }

        // GET: GestionDePersonasController/Edit/5
        public async Task<IActionResult> Edit(int id)
        {
            Model.Persona persona;

            try
            {
                var httpClient = new HttpClient();
                httpClient.DefaultRequestHeaders.Add("X-API-KEY", apiKey); // Agregar la clave API al encabezado

                httpClient.BaseAddress = new Uri("https://localhost:7119/");
                var response = await httpClient.GetAsync($"api/ServicioDePersonas/ObtengaLaPersona?id={id}");
                response.EnsureSuccessStatusCode();
                var result = await response.Content.ReadAsStringAsync();
                persona = JsonConvert.DeserializeObject<Model.Persona>(result);
                return View(persona);
            }
            catch (Exception ex)
            {

                return View(); ;
            }

        }

        // POST: GestionDePersonasController/Edit/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(Model.Persona persona)
        {
            try
            {
                var httpClient = new HttpClient();
                httpClient.DefaultRequestHeaders.Add("X-API-KEY", apiKey); // Agregar la clave API al encabezado

                httpClient.BaseAddress = new Uri("https://localhost:7119/");

                var json = JsonConvert.SerializeObject(persona);
                var content = new StringContent(json, Encoding.UTF8, "application/json");
                var response = httpClient.PutAsync("api/ServicioDePersonas/EditeLaPersona", content).Result;
                response.EnsureSuccessStatusCode();

                ViewData["ProblemasAlEditar"] = false;
               
                        
                    
                return RedirectToAction(nameof(Index));
            }
            catch
            {
                ViewData["ProblemasAlEditar"] = true;
                return View();
            }
        }

       
        public ActionResult Activar(int id)
        {
           var httpClient = new HttpClient();
           httpClient.DefaultRequestHeaders.Add("X-API-KEY", apiKey); // Agregar la clave API al encabezado


            try
            {
                httpClient.BaseAddress = new Uri("https://localhost:7119/");
                var response = httpClient.PutAsync($"api/ServicioDePersonas/Active?id={id}", null).Result;
                response.EnsureSuccessStatusCode();
                ViewData["ProblemasAlActivar"] = false;
                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                ViewData["ProblemasAlActivar"] = true;
                return RedirectToAction(nameof(Index));
            }

           
        }
        public ActionResult DesActivar(int id)
        {
            var httpClient = new HttpClient();
            httpClient.DefaultRequestHeaders.Add("X-API-KEY", apiKey); // Agregar la clave API al encabezado


            try
            {
                httpClient.BaseAddress = new Uri("https://localhost:7119/");
                var response = httpClient.PutAsync($"api/ServicioDePersonas/DesActive?id={id}", null).Result;
                response.EnsureSuccessStatusCode();
                ViewData["ProblemasAlDesActivar"] = false;
                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                ViewData["ProblemasAlDesActivar"] = true;
                return RedirectToAction(nameof(Index));
            }
        }




    }
}
