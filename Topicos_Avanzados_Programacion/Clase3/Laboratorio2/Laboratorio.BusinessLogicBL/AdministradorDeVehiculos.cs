using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;


namespace Laboratorio.BusinessLogicBL
{
    public class AdministradorDeVehiculos : IAdministradorDeVehiculos
    {
        // Simulando una base de datos <Lista> en memoria
        private static readonly List<Model.Vehiculo> vehiculos = new List<Model.Vehiculo>();

        // Constructor
        public AdministradorDeVehiculos()
        {
            //Inicializar la lista de vehiculos con datos por defecto
            InicializarVehiculosPorDefecto();
        }

        //Sino existen vehiculos en la lista, se agregan 3 vehiculos por defecto
        //  para tener datos iniciales con los cuales trabajar.

        //Separacion de responsabilidades: este metodo solo se encarga de inicializar los datos
        private static void InicializarVehiculosPorDefecto()
        {
            if (!vehiculos.Any())
            {
                vehiculos.Add(new Model.Vehiculo { Id = 1, Marca = Model.Marca.Tesla, Anio = 2020, Modelo = "Model S", DobleTraccion = true });
                vehiculos.Add(new Model.Vehiculo { Id = 2, Marca = Model.Marca.Toyota, Anio = 2018, Modelo = "Corolla", DobleTraccion = false });
                vehiculos.Add(new Model.Vehiculo { Id = 3, Marca = Model.Marca.BYD, Anio = 2021, Modelo = "Tang", DobleTraccion = true });
            }
        }

        //Metodo para comprobar que los vehiculos no sean  Null
        private void ComprobarNulo(Model.Vehiculo vehiculo)
        {
            //Si vehiculo es nulo, lanzamos excepcion
            if (vehiculo == null)
            {
                throw new ArgumentNullException(nameof(vehiculo), "El vehiculo no puede ser nula.");
            }
        }

        // Método para obtener la lista de vehiculos.
        public List<Model.Vehiculo> ObtenerTodos()
        {
            //Retornar la lista de vehiculos
            return vehiculos;
        }

        // Metodo para obtener vehiculo por Id
        public Model.Vehiculo ObtenerPorId(int id)
        {
            //Buscar vehiculo por Id
            var vehiculo = vehiculos.FirstOrDefault(p => p.Id == id);
            //Comprobar si es nulo
            ComprobarNulo(vehiculo);
            //Retornar vehiculo
            return vehiculo;
        }

        //Metodo para agregar vehiculo
        public void Agregar(Model.Vehiculo vehiculo)
        {
            ComprobarNulo(vehiculo);
            //Comprobar si ya existe una vehiculo con el mismo Id
            //Sino existe, lanzamos excepcion
            if (vehiculos.Any(vehiculos => vehiculos.Id == vehiculo.Id))
            {
                throw new ArgumentException("Ya existe una vehiculo con el mismo Id.");
            }
            //Agregar vehiculo a la lista
            vehiculos.Add(vehiculo);
        }

        //Metodo para actualizar los vehiculos
        public void Actualizar(Model.Vehiculo vehiculo)
        {
            //Si vehiculo es nulo, lanzamos excepcion
            ComprobarNulo(vehiculo);

            //Comprobar si la vehiculo existe
            var Existente = vehiculos.FirstOrDefault(p => p.Id == vehiculo.Id);
            //Si no existe, lanzamos excepcion
            if (Existente == null)
            {
                throw new ArgumentException("No existe una vehiculo con el Id proporcionado.");
            }

            //Actualizar los datos
            Existente.Modelo = vehiculo.Modelo;
            Existente.DobleTraccion = vehiculo.DobleTraccion;
            Existente.Anio = vehiculo.Anio;
            Existente.Marca = vehiculo.Marca;
        }

        //Metodo para eliminar vehiculos
        public void Eliminar(int id)
        {
            var vehiculo = vehiculos.FirstOrDefault(p => p.Id == id);

            //Comporbamos Nulo
            ComprobarNulo(vehiculo);

            //Eliminar vehiculo
            vehiculos.Remove(vehiculo);
        }

        // Implementación de métodos asíncronos de la interfaz

        public async Task AgregueAsync(Model.Vehiculo vehiculo)
        {
            await Task.Run(() => Agregar(vehiculo));
        }

        public async Task<IEnumerable<Model.Vehiculo>> ObtengaLaListaAsync()
        {
            return await Task.Run(() => ObtenerTodos().AsEnumerable());
        }

        public async Task<Model.Vehiculo> ObtengaELVehiculosync(int id)
        {
            return await Task.Run(() => ObtenerPorId(id));
        }

        public async Task EditeElVehiculoAsync(Model.Vehiculo vehiculo)
        {
            await Task.Run(() => Actualizar(vehiculo));
        }
    }
}

