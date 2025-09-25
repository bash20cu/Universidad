using System.Collections.Generic;
using System.ServiceModel;

namespace Laboratorio1TecnologiasLegacy.SI.WCF
{
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
