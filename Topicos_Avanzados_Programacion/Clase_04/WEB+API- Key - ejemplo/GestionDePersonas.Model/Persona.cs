using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionDePersonas.Model
{
    [Table("Persona")]
    public class Persona
    {
        [Key]
        public int Id { get; set; }

        [Required(ErrorMessage = "El campo Identificación es requerido")]
        public string Identificacion { get; set; }
        [Required(ErrorMessage ="El campo Nombre es requerido")]
        public string Nombre { get; set;}

        [Required(ErrorMessage = "El campo Apellidos es requerido")]
        public string Apellidos { get; set; }

        [DataType(DataType.Date)]
        [Display(Name ="Fecha de Nacimiento")]
        [Required(ErrorMessage = "El campo Fecha de Nacimiento es requerido")]
        public DateTime FechaDeNacimiento { get; set; }
        public Estado Estado { get; set; }
    }
}
