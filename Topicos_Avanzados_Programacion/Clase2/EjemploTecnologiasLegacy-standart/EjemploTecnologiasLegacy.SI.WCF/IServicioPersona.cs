using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace EjemploTecnologiasLegacy.SI.WCF
{
   [ServiceContract]
    public interface IServicioPersona
    {
        [OperationContract]
        List<Model.Persona> ObtengaLaLista();
       
        [OperationContract]
        List<Model.Persona> ObtengaLaListaDeActivos();
        [OperationContract]
        List<Model.Persona> ObtengaLaListaDeInActivos();
        [OperationContract]
        Model.Persona ObtengaLaPersona(int id);
        [OperationContract]
        void Agregue(Model.Persona persona);
       
        [OperationContract]
        void EditeLaPersona(Model.Persona persona);
      
        [OperationContract]
        void Active(int id);
     
        [OperationContract]
        void DesActive(int id);


    }

  
}
