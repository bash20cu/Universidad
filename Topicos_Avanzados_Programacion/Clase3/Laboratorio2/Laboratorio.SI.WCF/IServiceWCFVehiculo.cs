using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace Laboratorio.SI.WCF
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IServiceWCFVehiculo" in both code and config file together.
    [ServiceContract]
    public interface IServiceWCFVehiculo
    {
        [OperationContract]
        List<Model.Vehiculo> ObtenerTodas();

        [OperationContract]
        Model.Vehiculo ObtenerPorId(int id);

        [OperationContract]
        void Agregar(Model.Vehiculo vehiculo);

        [OperationContract]
        void Actualizar(Model.Vehiculo vehiculo);

        [OperationContract]
        void Eliminar(int id);
    }
}
