import os
from SistemaArchivos import InfoArchivo

listaRutaArchivo = []

def obtener_nombre_archivo(ruta):
    nombre_archivo_con_extension = os.path.basename(ruta)
    nombre_archivo, extension = os.path.splitext(nombre_archivo_con_extension)
    return nombre_archivo

def recorrer_directorios(ruta):
    try:
        # Lista los contenidos del directorio
        contenidos = os.listdir(ruta)

        for contenido in contenidos:
            
            ruta_completa = os.path.join(ruta, contenido)

            if os.path.isdir(ruta_completa): # Si es un directorio, recurrir de manera recursiva
                recorrer_directorios(ruta_completa)
            else:
                # Si es un archivo, puedes realizar alguna acción con él
                nombreArchivo = obtener_nombre_archivo(ruta_completa)
                oFile = InfoArchivo(ruta_completa,nombreArchivo)
                listaRutaArchivo.append(oFile)
             
   
    except Exception as e:
        print(f"Error al acceder a: {ruta}. Error: {e}")


#def buscarArchivo(nombreArchivo):
    #return [objeto for objeto in listaRutaArchivo if objeto.NomArchivo == nombreArchivo]

'''
La funcion buscarArchivos, Hace uso de: 
1- Las expresiones lambda que se definen en una sola línea y se utilizan generalmente para funciones simples que se necesitan en un lugar específico,
    estan pensadas para que sean lo mas simples posibles de la siguiente forma: [lambda argumentos: expresión ] 
    Donde los argumentos serian ( objeto: objeto.NomArchivo == nombreArchivo, listaRutaArchivo)
2- La función filter devuelve un objeto "iterable" que contiene solo los elementos de la secuencia original
    que cumplen con la condición definida por la función de filtro , en este caso si el objeto.NomArchivo coincide con 
    nombreArchivo que pasamos como parametro, dentro de listaRutaArchivo
'''
def buscarArchivo(nombreArchivo):
    resultados = filter(lambda objeto: objeto.NomArchivo == nombreArchivo, listaRutaArchivo) 
    resultados_lista =  list(resultados) # Se convierte a una lista el objeto resultados con filtro .         
    return list(resultados_lista)

'''
La funcion buscarArchivo_docx_xlsx, Hace uso de: la logica de la funcion anterior , pero buscando archivos de extension
.docx y xlsx, haciendo uso de las rutas almacenadas dentro de la lista listaRutaArchivo
'''
def buscarArchivo_docx_xlsx():
    #Tenemos que comparar con la ruta, ya que esta es la que contiene la extension del archivo como tal.
    archivos_docx = filter(lambda objeto: objeto.Ruta.lower().endswith(('.docx')), listaRutaArchivo)
    archivos_encontrados_docx = list(archivos_docx)
    archivos_xlsx = filter(lambda objeto: objeto.Ruta.lower().endswith(('xlsx')), listaRutaArchivo)
    archivos_encontrados_xlsx = list(archivos_xlsx)
    
    if archivos_encontrados_docx:
        print("Archivos .docx encontrados:")
        for archivo in archivos_encontrados_docx:
            print(archivo.NomArchivo)
    else:
        print("No se encontraron archivos .docx.")

    if archivos_encontrados_xlsx:
        print("Archivos .xlsx encontrados:")
        for archivo in archivos_encontrados_xlsx:
            print(archivo.NomArchivo)
    else:
        print("No se encontraron archivos .xlsx.")


