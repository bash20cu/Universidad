# security_manager.py
import sqlite3, os

# Clase especializada para el manejo de la base de datos
class SecurityManager:
    def __init__(self, db_path="security.db"):        
        self.dir_path = os.path.abspath(os.getcwd())
        self.db_path  = os.path.join(self.dir_path, "security.db")
        self.conn = sqlite3.connect(self.db_path)
        self.create_table()

    def create_table(self):
        conn = sqlite3.connect(self.db_path)
        cursor = conn.cursor()
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS protected_folders (
                id INTEGER PRIMARY KEY,
                path TEXT NOT NULL,
                password TEXT NOT NULL
            )
        ''')
        conn.commit()
        conn.close()

    def add_folder(self, path, password):
        conn = sqlite3.connect(self.db_path)
        cursor = conn.cursor()

        # Verificar si el path ya existe en la base de datos
        cursor.execute("SELECT * FROM protected_folders WHERE path=?", (path,))
        existing_entry = cursor.fetchone()

        if existing_entry:
            # Si el path ya existe, actualizar la contraseña
            cursor.execute("UPDATE protected_folders SET password=? WHERE path=?", (password, path))
            
        else:
            # Si el path no existe, agregar una nueva entrada
            cursor.execute("INSERT INTO protected_folders (path, password) VALUES (?, ?)", (path, password))
        conn.commit()
        conn.close()

    def check_password(self, path, password):
        conn = sqlite3.connect(self.db_path)
        cursor = conn.cursor()
        cursor.execute("SELECT id FROM protected_folders WHERE path=? AND password=?", (path, password))
        result = cursor.fetchone()
        conn.close()
        return result is not None
    
    def is_protected_folder(self, path):
        query = "SELECT * FROM protected_folders WHERE path = ?"
        result = self.conn.execute(query, (path,)).fetchone()
        return result is not None

    def verify_password(self, path, password):
        query = "SELECT password FROM protected_folders WHERE path = ?"
        stored_password = self.conn.execute(query, (path,)).fetchone()

        if stored_password and stored_password[0] == password:
            return True
        else:
            return False
            