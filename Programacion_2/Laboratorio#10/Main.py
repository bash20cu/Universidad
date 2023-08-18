import Crypto as Cry

try:
    while (True):
        opUsuario = input("Digite que funcionalidad desea realizar\n\r1.Encriptar\n\r2.Desencriptar\n\r")
        if (not opUsuario.isdigit()):
            print('Usted no digito una opción numerica valida')
            continue
        if (opUsuario == '1'):
           textoCifrar = input("Que texto desea cifrar?: ")
           restulado = Cry.encriptarText(textoCifrar)
           print("La palabra cifrada es: "+restulado) 
           continue           
        if (opUsuario == '2'):
            textoCifrar = input("Que texto desea descifrar?: ")
            restulado = Cry.desencriptarText(textoCifrar)
            print("La palabra cifrada es: "+restulado) 
            continue
        break
except BaseException:
    print("Termino la ejecucion")
    
input()
