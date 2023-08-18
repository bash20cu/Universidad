
charToChange = ['a','e','i','o','u'] #list
revToChange = ['h','p','Ñ','6','$']
mixText = "a-h,e-p,i-Ñ,o-6,u-$" #mixText.split(',') ->['a-h','e-p','i-Ñ','o-6','u-$']


def encriptarText(pTexto): #Costa Rica ->C6sth RÑch
    nuevoTexto = ''
    for c in pTexto:
        #Determinar si el caracter se debe remplazar
        if (c in charToChange):
            for l in mixText.split(','): #[a-h , e-p , i-Ñ, o-6, u-$]
                v = l.split('-')[0] #[o,6]
                if (c == v):
                    nuevoTexto += l.split('-')[1]
        else:
            nuevoTexto += c #C6
    
    return nuevoTexto    


def desencriptarText(pTextoEncriptado):# C6sth RÑch -> Costa Rica
    nuevoTexto = ''
    for c in pTextoEncriptado:
        found = False
        for l in mixText.split(','):
            v = l.split('-')[1]  # Reemplazo inverso: [reemplazo-original]
            if c == v:
                nuevoTexto += l.split('-')[0]
                found = True
                break
        if not found:
            nuevoTexto += c

    return nuevoTexto     






