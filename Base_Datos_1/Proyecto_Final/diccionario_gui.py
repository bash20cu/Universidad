import tkinter as tk
from tkinter import filedialog, scrolledtext, messagebox
from sqlalchemy import create_engine, MetaData
from prettytable import PrettyTable
import matplotlib.pyplot as plt

def obtener_diccionario_datos(tabla):
    columnas = []
    for columna in tabla.columns:
        columnas.append({
            'nombre': columna.name,
            'tipo': str(columna.type),
            'longitud': getattr(columna.type, 'length', None),
            'permite_nulos': columna.nullable,
            'descripcion': '',
        })
    return columnas

def guardar_tabla_como_imagen(nombre_tabla, datos_tabla, directorio, label_progreso):
    table = PrettyTable()   
    table.field_names = ["Nombre", "Tipo", "Longitud", "Permite Nulos", "Descripción"]

    for row in datos_tabla:
        table.add_row([row['nombre'], row['tipo'], row['longitud'], row['permite_nulos'], row['descripcion']])
    
    # Agregar última fila con el nombre de la tabla
    table.add_row(['', '', '', 'Nombre de la tabla:', nombre_tabla])

    table.align = "l"

    fig, ax = plt.subplots(figsize=(10, 1))
    ax.axis('off')

    table_output = ax.table(cellText=table._rows, colLabels=table.field_names, loc='center', cellLoc='center', colLoc='center')
    table_output.auto_set_font_size(False)
    table_output.set_fontsize(8)

    plt.savefig(f'{directorio}/{nombre_tabla}_datos.png', bbox_inches='tight', pad_inches=0.5)
    plt.close()

    mensaje = f"Tabla: {nombre_tabla}\nImagen PNG para la tabla '{nombre_tabla}' creada en '{directorio}/{nombre_tabla}_datos.png'"
    label_progreso.config(state=tk.NORMAL)
    label_progreso.insert(tk.END, mensaje + "\n\n")
    label_progreso.see(tk.END)
    label_progreso.config(state=tk.DISABLED)
    label_progreso.update()

def obtener_datos_base_de_datos():
    nombre_base_datos = entry_nombre_base.get()
    usuario = entry_usuario.get()
    contrasena = entry_contrasena.get()

    return nombre_base_datos, usuario, contrasena

def seleccionar_directorio():
    directorio = filedialog.askdirectory()
    entry_directorio.delete(0, tk.END)
    entry_directorio.insert(0, directorio)

def generar_imagenes():
    directorio = entry_directorio.get()
    nombre_base_datos, usuario, contrasena = obtener_datos_base_de_datos()

    if not nombre_base_datos or not usuario or not contrasena or not directorio:
        messagebox.showwarning("Advertencia", "Por favor, complete todos los campos.")
        return

    try:
        database_url = f"mysql://{usuario}:{contrasena}@localhost/{nombre_base_datos}"
        engine = create_engine(database_url)

        metadata = MetaData()
        metadata.reflect(bind=engine)

        for nombre_tabla, tabla in metadata.tables.items():
            guardar_tabla_como_imagen(nombre_tabla, obtener_diccionario_datos(tabla), directorio, label_progreso)

        messagebox.showinfo("Éxito", "Imágenes generadas exitosamente.")
    except Exception as e:
        messagebox.showerror("Error", f"Ocurrió un error: {str(e)}")

# Crear la ventana principal
ventana = tk.Tk()
ventana.title("Generador de Imágenes de Datos")

# Etiqueta y campo de texto para el directorio
label_directorio = tk.Label(ventana, text="Directorio:")
label_directorio.grid(row=0, column=0, sticky=tk.E)
entry_directorio = tk.Entry(ventana, width=40)
entry_directorio.grid(row=0, column=1, padx=5, pady=5, sticky=tk.W)
btn_seleccionar_directorio = tk.Button(ventana, text="Seleccionar", command=seleccionar_directorio)
btn_seleccionar_directorio.grid(row=0, column=2, pady=10, sticky=tk.E)

# Etiquetas y campos de texto para la base de datos
label_nombre_base = tk.Label(ventana, text="Base de Datos:")
label_nombre_base.grid(row=1, column=0, sticky=tk.E)
entry_nombre_base = tk.Entry(ventana, width=20)
entry_nombre_base.grid(row=1, column=1, padx=10, pady=10, sticky=tk.W)

label_usuario = tk.Label(ventana, text="Usuario:")
label_usuario.grid(row=2, column=0, sticky=tk.E)
entry_usuario = tk.Entry(ventana, width=20)
entry_usuario.grid(row=2, column=1, padx=10, pady=10, sticky=tk.W)

label_contrasena = tk.Label(ventana, text="Contraseña:")
label_contrasena.grid(row=3, column=0, sticky=tk.E)
entry_contrasena = tk.Entry(ventana, width=20, show="*")
entry_contrasena.grid(row=3, column=1, padx=10, pady=10, sticky=tk.W)

# Etiqueta de progreso
label_progreso = scrolledtext.ScrolledText(ventana, width=60, height=10, state=tk.DISABLED)
label_progreso.grid(row=5, column=0, padx=10, pady=10, columnspan=2)

# Botón para generar imágenes
btn_generar_imagenes = tk.Button(ventana, text="Generar Imágenes", command=generar_imagenes)
btn_generar_imagenes.grid(row=4, column=1, pady=20)

# Evitar que la ventana se pueda redimensionar
ventana.resizable(False, False)

# Iniciar el bucle de eventos de la ventana
ventana.mainloop()
