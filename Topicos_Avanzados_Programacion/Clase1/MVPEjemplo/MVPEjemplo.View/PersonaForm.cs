using System.ComponentModel;



namespace MVPEjemplo.View.WinForms;

public partial class PersonaForm : Form, IPersonaView
{
    public PersonaForm()
    {
        InitializeComponent();
        btnMostrar.Click+=(s,e) => GuardarClick?.Invoke(this, EventArgs.Empty);
    }

    [DesignerSerializationVisibility(DesignerSerializationVisibility.Hidden)]
    public string Nombre { get => txtNombre.Text; set => txtNombre.Text = value; }
    [DesignerSerializationVisibility(DesignerSerializationVisibility.Hidden)]
    public string Edad { get => txtEdad.Text; set => txtEdad.Text = value; }

    public event EventHandler GuardarClick;

    public void MostrarMensaje(string mensaje)
    {
        MessageBox.Show(mensaje);
    }

    
}
