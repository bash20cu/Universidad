using Microsoft.EntityFrameworkCore;

namespace Laboratorio.DA
{
    public class DBContexto : DbContext
    {
        public DBContexto(DbContextOptions<DBContexto> options) : base(options)
        {
        }

        public DbSet<Model.Vehiculo> Vehiculos { get; set; }


    }
}
