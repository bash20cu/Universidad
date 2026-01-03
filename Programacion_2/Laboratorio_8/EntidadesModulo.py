class Persona:
    def __init__(self,cedula,nombre,genero,pais,registro):
        self.cedula = cedula
        self.nombre = nombre
        self.pais = pais
        self.genero = genero
        self.registro = registro
        self.categoria = self.seleccionCategoria()
    
    def seleccionCategoria(self):
        match self.pais:
            case 'Costa Rica':
                return 'A'
            case 'Mexico':
                return 'B'
            case 'Panama':
                return 'C'

            
        