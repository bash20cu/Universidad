import Funciones as fn

fn.recorrer_directorios("C:\PythonTest")

listaArchivosEncontrados = fn.buscarArchivo("Main")
fn.buscarArchivo_docx_xlsx()


if len(listaArchivosEncontrados) > 0:
    etiqueta = "El archivo {0} se encuentra en la ruta {1}"
    for archivo in listaArchivosEncontrados:
        print(etiqueta.format(archivo.NomArchivo,archivo.Ruta))
