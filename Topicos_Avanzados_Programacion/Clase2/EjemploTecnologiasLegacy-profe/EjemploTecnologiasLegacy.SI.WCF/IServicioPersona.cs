using System;
using System.Collections.Generic;
using System.ServiceModel;
namespace EjemploTecnologiasLegacy.SI.WCF
{
   [ServiceContract]
    public interface IServicioPersona
    {
        [OperationContract]
        List<Model.Persona> Obtener();
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
