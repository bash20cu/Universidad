using MVPEjemplo.View;
using MVPEjemplo.View.Console;

class Program
{
    static void Main()
    {
        var vista = new PersonaConsola();
        var presentador = new PersonaPresenter(vista);
        vista.Iniciar();
    }
}
