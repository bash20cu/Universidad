using MVPEjemplo.Model;

namespace MVPEjemplo.View
{
    public class PersonaPresenter
    {
        private readonly IPersonaView view;

        public PersonaPresenter(IPersonaView view)
        {
            this.view = view;
            this.view.GuardarClick += OnGuardarClick;
        }

        private void OnGuardarClick(object sender, EventArgs e)
        {
            if (int.TryParse(view.Edad, out int edad))
            {
                var persona = new Persona( view.Nombre, edad);
                view.MostrarMensaje($"Persona guardada: {persona.Nombre} ({persona.Edad} años)");
            }
            else
            {
                view.MostrarMensaje("Edad inválida.");
            }
        }
    }
}
