namespace MVPEjemplo.View.WinForms;

partial class PersonaForm
{
    /// <summary>
    /// Required designer variable.
    /// </summary>
    private System.ComponentModel.IContainer components = null;

    /// <summary>
    /// Clean up any resources being used.
    /// </summary>
    /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
    protected override void Dispose(bool disposing)
    {
        if (disposing && (components != null))
        {
            components.Dispose();
        }
        base.Dispose(disposing);
    }

    #region Windows Form Designer generated code

    /// <summary>
    /// Required method for Designer support - do not modify
    /// the contents of this method with the code editor.
    /// </summary>
    private void InitializeComponent()
    {
        this.txtNombre = new System.Windows.Forms.TextBox();
        this.txtEdad = new System.Windows.Forms.TextBox();
        this.btnMostrar = new System.Windows.Forms.Button();
        this.label1 = new System.Windows.Forms.Label();
        this.label2 = new System.Windows.Forms.Label();
        this.SuspendLayout();
        // 
        // txtNombre
        // 
        this.txtNombre.Location = new System.Drawing.Point(97, 57);
        this.txtNombre.Name = "txtNombre";
        this.txtNombre.Size = new System.Drawing.Size(100, 20);
        this.txtNombre.TabIndex = 0;
        // 
        // txtEdad
        // 
        this.txtEdad.Location = new System.Drawing.Point(97, 109);
        this.txtEdad.Name = "txtEdad";
        this.txtEdad.Size = new System.Drawing.Size(100, 20);
        this.txtEdad.TabIndex = 1;
        // 
        // btnMostrar
        // 
        this.btnMostrar.Location = new System.Drawing.Point(122, 160);
        this.btnMostrar.Name = "btnMostrar";
        this.btnMostrar.Size = new System.Drawing.Size(75, 23);
        this.btnMostrar.TabIndex = 2;
        this.btnMostrar.Text = "Mostrar mensaje";
        this.btnMostrar.UseVisualStyleBackColor = true;
        // 
        // label1
        // 
        this.label1.AutoSize = true;
        this.label1.Location = new System.Drawing.Point(29, 64);
        this.label1.Name = "label1";
        this.label1.Size = new System.Drawing.Size(47, 13);
        this.label1.TabIndex = 3;
        this.label1.Text = "Nombre:";
        // 
        // label2
        // 
        this.label2.AutoSize = true;
        this.label2.Location = new System.Drawing.Point(29, 116);
        this.label2.Name = "label2";
        this.label2.Size = new System.Drawing.Size(35, 13);
        this.label2.TabIndex = 4;
        this.label2.Text = "Edad:";
        // 
        // PersonaForm
        // 
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.ClientSize = new System.Drawing.Size(269, 252);
        this.Controls.Add(this.label2);
        this.Controls.Add(this.label1);
        this.Controls.Add(this.btnMostrar);
        this.Controls.Add(this.txtEdad);
        this.Controls.Add(this.txtNombre);
        this.MaximumSize = new System.Drawing.Size(285, 291);
        this.MinimumSize = new System.Drawing.Size(285, 291);
        this.Name = "PersonaForm";
        this.Text = "MVP ejemplo";
        this.ResumeLayout(false);
        this.PerformLayout();

    }

    #endregion

    private System.Windows.Forms.TextBox txtNombre;
    private System.Windows.Forms.TextBox txtEdad;
    private System.Windows.Forms.Button btnMostrar;
    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.Label label2;
}