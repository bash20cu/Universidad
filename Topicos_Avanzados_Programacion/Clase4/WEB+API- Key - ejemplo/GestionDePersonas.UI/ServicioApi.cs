using GestionDePersonas.Model;
using Newtonsoft.Json;

namespace GestionDePersonas.UI
{
    public class ServicioApi
    {
        private readonly IHttpClientFactory _httpClientFactory;
       
        public ServicioApi(IHttpClientFactory httpClientFactory)
        {
            _httpClientFactory = httpClientFactory;
        }

        public async Task<List<Persona>> ObtenerPersonasActivasAsync()
        {
            var client = _httpClientFactory.CreateClient("PersonasApi");
            var response = await client.GetAsync("api/ServicioDePersonas/ObtengaLaListaDeActivos");
            response.EnsureSuccessStatusCode();
            var result = await response.Content.ReadAsStringAsync();
            var lista = JsonConvert.DeserializeObject<List<Persona>>(result) ?? [];
            return lista;
        }

        internal async Task<List<Persona>> ObtenerPersonasInActivasAsync()
        {
            var client = _httpClientFactory.CreateClient("PersonasApi");
            var response = await client.GetAsync("api/ServicioDePersonas/ObtengaLaListaDeInActivos");
            response.EnsureSuccessStatusCode();
            var result = await response.Content.ReadAsStringAsync();
            var lista = JsonConvert.DeserializeObject<List<Persona>>(result) ?? [];
            return lista;
        }
    }
}