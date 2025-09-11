namespace MVPEjemplo.View.WinForms;

static class Program
{
    /// <summary>
    ///  The main entry point for the application.
    /// </summary>
    [STAThread]
    static void Main()
    {
        var laVista = new PersonaForm(); // Implementa IClienteView
        var elPresentador = new PersonaPresenter(laVista);
        Application.Run(laVista);

    }
}