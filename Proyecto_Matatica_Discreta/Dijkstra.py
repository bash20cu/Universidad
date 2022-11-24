import cv2

def dijkstra(graph,src,dest,visited=[],distances={},predecessors={}):
    # Esto es en caso de no encontrar el destino en el grafo
    if src not in graph:
        raise TypeError('El nodo de origen no está en el grafo')
    if dest not in graph:
        raise TypeError('El nodo de destino no está en el grafo')
    # condiciones finales   
    if src == dest:
        # Construimos el camino mas corto
        path=[]
        pred=dest
        while pred != None:
            path.append(pred)
            pred=predecessors.get(pred,None)
            newPath = path[::-1]
            textPatch = ' -> '.join(newPath)
        print('El camino mas corto es: '+str(newPath)+" Tiene un costo de="+str(distances[dest]))
###################################################################################################################################################################################        
        #Aqui vamos a crear el programa para pintar en el grafico el recorrido mas corto
        # Cargamos la imagen
        image = cv2.imread('imagen.jpg')
        cv2.namedWindow("Algoritmo de Dijkstra", cv2.WINDOW_NORMAL)
        
        #Este es el diccionario de coordenadas
        mappedImage = {
        'Q':{ 'x': 237, 'y': 21},
        'U':{ 'x': 618, 'y': 68},
        'Y':{ 'x': 339, 'y': 85},
        'JJ':{ 'x': 502, 'y': 85},
        'I':{ 'x': 673, 'y': 50},
        'O':{ 'x': 853, 'y': 95},
        'F':{ 'x': 762, 'y': 120},
        'A':{ 'x': 425, 'y': 139},
        'S':{ 'x': 529, 'y': 138},
        'P':{ 'x': 292, 'y': 176},
        'K':{ 'x': 574, 'y': 210},
        'OZ':{ 'x': 826, 'y': 185},
        'J':{ 'x': 503, 'y': 238},
        'H':{ 'x': 420, 'y': 249},
        'G':{ 'x': 248, 'y': 238},
        'L':{ 'x': 635, 'y': 256},
        'D':{ 'x': 530, 'y': 267},
        'Z':{ 'x': 706, 'y': 254},
        'X':{ 'x': 774, 'y': 247},
        'C':{ 'x': 233, 'y': 319},
        'V':{ 'x': 268, 'y': 310},
        'B':{ 'x': 519, 'y': 311},
        'N':{ 'x': 637, 'y': 319},
        'M':{ 'x': 834, 'y': 311},
        'MM':{ 'x': 304, 'y': 355},
        'NN':{ 'x': 431, 'y': 357},
        'BB':{ 'x': 611, 'y': 373},
        'CC':{ 'x': 754, 'y': 363},
        'SS':{ 'x': 331, 'y': 419},
        'DD':{ 'x': 482, 'y': 445},
        'FF':{ 'x': 590, 'y': 470},
        'GG':{ 'x': 671, 'y': 426},
        'HH':{ 'x': 859, 'y': 425},
        'LL':{ 'x': 41, 'y': 525},
        'QE':{ 'x': 123, 'y': 552},
        'RT':{ 'x': 285, 'y': 553},
        'YU':{ 'x': 403, 'y': 535},
        'IO':{ 'x': 491, 'y': 543},
        'PA':{ 'x': 743, 'y': 554},
        'SD':{ 'x': 208, 'y': 607},
        'FG':{ 'x': 313, 'y': 607},
        'KL':{ 'x': 546, 'y': 590},
        'ZX':{ 'x': 79, 'y': 644},
        'HJ':{ 'x': 447, 'y': 644},
        'FD':{ 'x': 609, 'y': 652},
        'NM':{ 'x': 355, 'y': 679},
        'TR':{ 'x': 33, 'y': 708},
        'CV':{ 'x': 206, 'y': 714},
        'BN':{ 'x': 287, 'y': 706},
        'LK':{ 'x': 422, 'y': 723},
        'JH':{ 'x': 492, 'y': 722},
        'GF':{ 'x': 556, 'y': 717},
        'EW':{ 'x': 16, 'y': 785},
        'YT':{ 'x': 152, 'y': 779},
        'IU':{ 'x': 306, 'y': 776},
        'OI':{ 'x': 421, 'y': 791},
        'SA':{ 'x': 425, 'y': 533},
        'ON':{ 'x': 618, 'y': 778},
        'TM':{ 'x': 217, 'y': 824},
        'EC':{ 'x': 285, 'y': 832},
        'PO':{ 'x': 537, 'y': 831},
        'UB':{ 'x': 116, 'y': 887},
        'TN':{ 'x': 267, 'y': 912},
        'TB':{ 'x': 373, 'y': 939},
        'RV':{ 'x': 457, 'y': 897},
        'RC':{ 'x': 627, 'y': 896},
    }
        #Aqui vamos a crear el for loop para pintar el camino mas corto
        for i in range(len(newPath)-1):
            cv2.arrowedLine(image, (mappedImage[newPath[i]]['x'], mappedImage[newPath[i]]['y']), (mappedImage[newPath[i+1]]['x'], mappedImage[newPath[i+1]]['y']), (164, 45, 45), 2 )
        
        # Mostramos la imagen
        cv2.putText(img=image, text=textPatch, org=(0, 980), fontFace=cv2.FONT_HERSHEY_TRIPLEX, fontScale=0.5, color=(164, 45, 45),thickness=1)
        cv2.imshow("Algoritmo de Dijkstra", image)

        cv2.resizeWindow("Algoritmo de Dijkstra", 900, 1000)
    
        cv2.waitKey(0)
        cv2.destroyAllWindows()

    else :     
        # Si es el nodo inicial, inicializamos el costo
        if not visited: 
            distances[src]=0
        # visitamos los vecinos
        for neighbor in graph[src] :
            if neighbor not in visited:
                new_distance = distances[src] + graph[src][neighbor]
                if new_distance < distances.get(neighbor,float('inf')):
                    distances[neighbor] = new_distance
                    predecessors[neighbor] = src
        # Marcamos como visitado
        visited.append(src)
        # Ahora que los vecinos han sido visitados, recursividad        
        # Seleccionamos el nodo no visitado con el costo mas bajo
        # corremos Dijskstra con src='x'
        unvisited={}
        for k in graph:
            if k not in visited:
                unvisited[k] = distances.get(k,float('inf'))        
        x=min(unvisited, key=unvisited.get)
        dijkstra(graph,x,dest,visited,distances,predecessors)
        

if __name__ == "__main__":    
    #66 different nodes
    graph = {
        'Q':{'H':9, 'Y':5, 'U':6},
        'U':{'Q':6, 'S':11, 'J':5, 'K':13},
        'Y':{'Q':5, 'J':2, 'A':8},
        'JJ':{"A":9, 'S':4},
        'I':{'K':10, 'O':9},
        'O':{'I':9, 'F':6, 'L':5, 'HH':6},
        'F':{'O':6, 'B':4, 'OZ':6},
        'A':{'Y':8, 'JJ':9, 'S':12, 'J':4},
        'S':{'A':12, 'JJ':4, 'U':11, 'GG':15},
        'P':{'NN':1, 'V':7, 'G':7},
        'K':{'U':13, 'I':10},
        'OZ':{'F':6, 'L':8, 'M':4},
        'J':{'Y':2, 'U':5, 'A':4, 'H':6, 'NN':0},
        'H':{'Q':9, 'J':6, 'SS':5},
        'G':{'P':7, 'C':5, 'V':19},
        'L':{'O':5, 'OZ':8},
        'D':{'NN':6},
        'Z':{'N':6, 'FF':6},
        'X':{'CC':6, 'M':0},
        'C':{'G':5, 'SD':5},
        'V':{'P':7, 'G':19, 'MM':15},
        'B':{'F':4, 'N':7, 'BB':3},
        'N':{'B':7, 'Z':6, 'CC':6},
        'M':{'OZ':4, 'X':0, 'CC':6},
        'MM':{'V':15, 'FG':8},
        'NN':{'P':1, 'J':0, 'D':6, 'FF':6, 'DD':7, 'YU':6},
        'BB':{'B':3, 'GG':6},
        'CC':{'N':6, 'X':6, 'M':6},
        'SS':{'H':5},
        'DD':{'NN':7},
        'FF':{'NN':6, 'Z':6},
        'GG':{'BB':6, 'S':15},
        'HH':{'O':6, 'KL':6},
        'LL':{'QE':5, 'YU':8},
        'QE':{'LL':5, 'SD':7, 'TM':4},
        'RT':{'SD':5, 'FG':5},
        'YU':{'NN':6, 'LL':8, 'FG':6, 'BN':3, 'NM':7},
        'IO':{'NM':7, 'PA':5},
        'PA':{'IO':5, 'LK':9, 'KL':8, 'RC':8},
        'SD':{'C':5, 'QE':7, 'RT':5, 'FG':6, 'BN':4, 'ZX':2},
        'FG':{'MM':8, 'YU':6, 'SD':6, 'RT':5, 'RV':4},
        'KL':{'HH':6, 'PA':8, 'IU':5, 'FD':4},
        'ZX':{'SD':2, 'TR':6, 'YT':3},
        'HJ':{'TM':8},
        'FD':{'KL':4, 'LK':3, 'SA':3},
        'NM':{'YU':7, 'IO':7},
        'TR':{'ZX':6, 'YT':5, 'EW':7},
        'CV':{'BN':1, 'YT':5, 'UB':2},
        'BN':{'YU':3, 'SD':4, 'CV':1, 'TM':2},
        'LK':{'PA':9, 'FD':3},
        'JH':{'OI':2, 'TB':2},
        'GF':{'PO':11, 'SA':12},
        'EW':{'TR':7},
        'YT':{'ZX':3, 'TR':5, 'CV':5, 'ON':4},
        'IU':{'KL':5, 'EC':5, 'OI':5},
        'OI':{'JH':2, 'IU':5, 'PO':10},
        'SA':{'FD':3, 'GF':12, 'PO':4},
        'ON':{'YT':4},
        'TM':{'QE':4, 'BN':2, 'HJ':8, 'TB':9, 'TN':8},
        'EC':{'IU':5, 'RV':7},
        'PO':{'GF':11, 'OI':10, 'SA':4},
        'UB':{'CV':2},
        'TN':{'TM':8},
        'TB':{'JH':2, 'TM':9},
        'RV':{'FG':4, 'EC':7},
        'RC':{'PA':8},
    }
    
    In = input("Ingrese el nodo de origen: ")
    Dst = input("Ingrese el nodo de destino: ")

    source = In.upper()
    destination = Dst.upper()

    dijkstra(graph, source, destination)
        
    