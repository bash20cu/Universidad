from sqlalchemy import create_engine, MetaData
import matplotlib.pyplot as plt
from prettytable import PrettyTable

def obtener_diccionario_datos(tabla):
    columnas = []
    for columna in tabla.columns:
        columnas.append({
            'nombre': columna.name,
            'tipo': str(columna.type),
            'longitud': getattr(columna.type, 'length', None),
            'permite_nulos': columna.nullable,
            'descripcion': '',  # Puedes agregar descripciones manualmente o mediante comentarios en tu esquema de base de datos
        })
    return columnas

def guardar_tabla_como_imagen(nombre_tabla, datos_tabla):
    # Crear una tabla con PrettyTable
    table = PrettyTable()
    table.field_names = ["Nombre", "Tipo", "Longitud", "Permite Nulos", "Descripción"]

    for row in datos_tabla:
        table.add_row([row['nombre'], row['tipo'], row['longitud'], row['permite_nulos'], row['descripcion']])
        
    # Agregar última fila con el nombre de la tabla
    table.add_row(['', '', '', 'Nombre de la tabla:', nombre_tabla])

    # Configurar el estilo de la tabla
    table.align = "l"

    # Crear una figura y ejes de Matplotlib
    fig, ax = plt.subplots(figsize=(10, 3))
    ax.axis('off')  # Ocultar ejes

    # Crear la tabla en la figura de Matplotlib
    table_output = ax.table(cellText=table._rows, colLabels=table.field_names, loc='center', cellLoc='center', colLoc='center')
    table_output.auto_set_font_size(False)
    table_output.set_fontsize(8)

    # Guardar la figura como imagen PNG
    plt.savefig(f'diccionario/{nombre_tabla}_datos.png', bbox_inches='tight', pad_inches=0.5)
    plt.close()  # Cerrar la figura para liberar memoria

# Reemplaza 'mysql' con el nombre de tu motor de base de datos
# Reemplaza 'usuario', 'contrasena', 'localhost', y 'nombre_base_datos' con tus credenciales y detalles de la base de datos
database_url = 'mysql://root:root@localhost/pcine'
engine = create_engine(database_url)

metadata = MetaData()
metadata.reflect(bind=engine)

# Iterar sobre las tablas
for nombre_tabla, tabla in metadata.tables.items():
    print(f"\nTabla: {nombre_tabla}")
    datos_tabla = obtener_diccionario_datos(tabla)
    guardar_tabla_como_imagen(nombre_tabla, datos_tabla)
    print(f"Imagen PNG para la tabla '{nombre_tabla}' creada en 'diccionario/{nombre_tabla}_datos.png'")


