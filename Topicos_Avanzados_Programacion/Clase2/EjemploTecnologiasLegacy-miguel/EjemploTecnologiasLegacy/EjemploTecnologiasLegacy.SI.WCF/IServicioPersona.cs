using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace EjemploTecnologiasLegacy.SI.WCF
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService1" in both code and config file together.
    [ServiceContract]
    public interface IServicioPersona
    {
        [OperationContract]
        List<Model.Persona> ObtenerTodas();

        [OperationContract]
        Model.Persona ObtenerPorId(int id);
        
        [OperationContract]
        void Agregar(Model.Persona persona);

        [OperationContract]
        void Actualizar(Model.Persona persona);

        [OperationContract]
        void Eliminar(int id);

        [OperationContract]
        void Activar(int id);

        [OperationContract]
        void Desactivar(int id);

    }

}
