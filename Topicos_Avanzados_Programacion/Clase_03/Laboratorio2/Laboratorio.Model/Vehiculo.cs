namespace Laboratorio.Model
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
