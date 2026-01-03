

namespace MVPEjemplo.View
{
    public interface IPersonaView
    {
        string Nombre { get; set; }
        string Edad { get; set; }
        void MostrarMensaje(string mensaje);

        event EventHandler GuardarClick;
    }
}
