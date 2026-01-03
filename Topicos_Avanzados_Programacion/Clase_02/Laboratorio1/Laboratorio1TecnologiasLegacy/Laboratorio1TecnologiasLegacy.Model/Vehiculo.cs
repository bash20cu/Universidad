// OtherInformation: <TargetFramework>net48</TargetFramework>

//•	Marca: Campo enumerado con las siguientes marcas
//      o 1 = Tesla
//      o 2 = Toyota
//      o 3 = BYD
//•	Año: Campo entero
//•	Modelo: Campo tipo string
//•	Doble Tracción: Valor booleano(true o false)

namespace Laboratorio1TecnologiasLegacy.Model
{
    public class Vehiculo
    {
        public int Id { get; set; }
        public Marca Marca { get; set; }
        public int Anio { get; set; }
        public string Modelo { get; set; }
        public bool DobleTraccion { get; set; }
    }
}
