namespace MVPEjemplo.View.Console;
using System;

public class PersonaConsola : IPersonaView

{
    public string Nombre { get ; set ; }
    public string Edad { get; set; }

    public event EventHandler GuardarClick;

    public void Iniciar()
    {
        Console.WriteLine("Ingrese el nombre del cliente:");
        Nombre = Console.ReadLine();
        Console.WriteLine("Ingrese la edad del cliente:");
        Edad = Console.ReadLine();
        GuardarClick?.Invoke(this, EventArgs.Empty);
    }


    public void MostrarMensaje(string mensaje)
    {
        Console.WriteLine($"[Mensaje]: {mensaje}");

    }
}
