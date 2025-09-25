using System;

namespace EjemploTecnologiasLegacy.Model
{
  
    public class Persona
    {
        public int Id { get; set; }

        public string Identificacion { get; set; }
        public string Nombre { get; set;}

        public string Apellidos { get; set; }

        public DateTime FechaDeNacimiento { get; set; }
        public Estado Estado { get; set; }
    }
}
