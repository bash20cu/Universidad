# MSSQL con Docker + SSMS (punto intermedio) — Guía completa

Este documento contiene un **paso a paso** para:

- Levantar **SQL Server (MSSQL)** dentro de Docker en Windows 11 (WSL2).
- Mantener **SSMS** instalado en Windows para gestionar las bases de datos desde tu PC.
- Tener una **estructura de GitHub** con configuraciones y plantillas para varios entornos (dev/test/prod) sin ensuciar tu sistema.

---

## Resumen rápido

1. Habilitar WSL2 en Windows 11.
2. Instalar Docker Desktop y habilitar integración con WSL2 (tu distribución, p.ej. Ubuntu).
3. Descargar e instalar SSMS en Windows (solo interfaz de gestión).
4. Clonar/crear repo en GitHub con la estructura que propongo (ver más abajo).
5. Levantar la imagen oficial de MSSQL en Docker con `docker compose` (cada entorno con su `.env`).
6. Ejecutar scripts de inicialización (init.sql) mediante `docker exec` o montando la carpeta `initdb`.

---

## Prerrequisitos

- Windows 11 con virtualización habilitada (en BIOS/UEFI).
- Cuenta de administrador en la máquina para instalar software.
- Recomendado: 8 GB RAM (mínimo 4 GB) para un uso cómodo con Docker y MSSQL.
- Git instalado.

---

## Paso 1 — WSL2 (rápido)

Abrir PowerShell como Administrador y ejecutar:

```powershell
wsl --install
wsl --set-default-version 2
```

Esto instalará WSL y te pedirá elegir o instalar una distro (recomiendo **Ubuntu**). Si ya tienes WSL 1, conviértela:

```powershell
wsl --set-version <DistroName> 2
```

---

## Paso 2 — Instalar Docker Desktop

1. Descarga Docker Desktop (instalador oficial) y ejecútalo.
2. Durante la instalación, deja seleccionado usar **WSL 2**.
3. Una vez instalado, abre Docker Desktop → Settings → Resources → WSL Integration → habilita la integración con tu distro (ej. Ubuntu).

Nota: Docker Desktop pedirá reiniciar la sesión/PC.

---

## Paso 3 — Instalar SSMS (cliente)

Descarga **SQL Server Management Studio (SSMS)** desde Microsoft e instálalo en Windows. SSMS solo es la interfaz gráfica para conectarte a SQL Server.

---

## Paso 4 — Estructura de repositorio (sugerida)

```
mssql-docker-ssms/            # repo raíz
├─ .gitignore
├─ README.md
├─ docker-compose.yml        # compose "base" con variables
├─ docker-compose.override.yml # overrides si quieres
├─ .env.example
├─ envs/
│  ├─ .env.dev
│  ├─ .env.test
│  └─ .env.prod
├─ initdb/
│  └─ init.sql               # scripts SQL para crear DB/usuarios
└─ scripts/
   └─ run-init.sh            # (opcional) helper para ejecutar init
```

**Importante:** nunca comites `.env` reales con contraseñas. Mantén `.env.example` y usa GitHub Secrets si vas a integrar CI.

---

## Paso 5 — `docker-compose.yml` (plantilla)

> Este `docker-compose.yml` está pensado para usarse junto a un archivo `.env` por entorno.

```yaml
version: '3.8'
services:
  mssql:
    image: mcr.microsoft.com/mssql/server:2022-latest
    container_name: mssql_${ENV:-dev}
    restart: unless-stopped
    environment:
      - ACCEPT_EULA=Y
      - SA_PASSWORD=${SA_PASSWORD}
      - MSSQL_PID=${MSSQL_PID:-Developer}
    ports:
      - "${HOST_PORT:-1433}:1433"
    volumes:
      - mssql_data_${ENV:-dev}:/var/opt/mssql
      - ./initdb:/initdb:ro
    healthcheck:
      test: ["CMD-SHELL", "/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P $${SA_PASSWORD} -Q \"SELECT 1\" || exit 1"]
      interval: 10s
      timeout: 5s
      retries: 10

volumes:
  mssql_data_dev:
    driver: local
```

**Notas:**
- `SA_PASSWORD` se toma del archivo `.env` que indiques cuando levantes la composición.
- Se monta `./initdb` para tener los scripts a mano. El contenedor oficial no ejecuta automáticamente `initdb` como Postgres — ver siguientes pasos para ejecutar scripts.

---

## Paso 6 — Archivos `.env` (ejemplo)

**envs/.env.dev** (no la subas al repo):

```
ENV=dev
HOST_PORT=1433
SA_PASSWORD=DevStrongPass!23
MSSQL_PID=Developer
```

**envs/.env.test** (ejemplo cambiando puerto y contraseña):

```
ENV=test
HOST_PORT=1434
SA_PASSWORD=TestStrongPass!23
MSSQL_PID=Developer
```

Crea un `.env.example` con las llaves (sin valores) para subir al repo.

---

## Paso 7 — Levantar el servicio

Desde la raíz del repo, por ejemplo para dev:

```bash
# usando docker compose v2 (recomendado)
docker compose --env-file envs/.env.dev up -d
```

Verificar contenedor:

```bash
docker ps
docker compose --env-file envs/.env.dev logs -f
```

---

## Paso 8 — Ejecutar scripts de inicialización (init.sql)

El patrón simple que recomiendo es **montar** `./initdb` en el contenedor y, una vez el servidor esté arriba, ejecutar los scripts con `sqlcmd` usando `docker exec`.

Ejemplo `initdb/init.sql`:

```sql
CREATE DATABASE universidaddb;
GO
USE universidaddb;
CREATE LOGIN u_dev WITH PASSWORD = 'DevUser!23';
CREATE USER u_dev FOR LOGIN u_dev;
ALTER ROLE db_owner ADD MEMBER u_dev;
GO
```

Ejecutar el script desde tu host (Windows PowerShell o WSL):

```bash
# asume que el contenedor se llama mssql_dev o mssql_dev según tu docker-compose
# reemplaza <container_name> por el nombre real
docker exec -it <container_name> /opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P "<SA_PASSWORD>" -i /initdb/init.sql
```

Sugerencia: copia la contraseña desde tu `.env.dev` o usa un pequeño script `scripts/run-init.sh` que lee `.env` y ejecuta `docker exec`.

---

## Paso 9 — Conectar con SSMS

- Abre SSMS en Windows.
- Server name: `127.0.0.1,1433` (o `localhost,1433` — si usaste otro puerto, cámbialo).
- Authentication: SQL Server Authentication.
- Login: `sa`.
- Password: la que tengas en tu `.env`.

Deberías poder ver la base `universidaddb` creada por `init.sql`.

---

## Multi-entornos (dev/test/prod)

Opciones para manejar entornos:

1. **Un solo `docker-compose.yml` + `.env` por entorno** (recomendado). Usas `--env-file envs/.env.test` para levantar test.
2. **`docker-compose.yml` + `docker-compose.override.yml`** donde override contiene cambios para dev (puertos, mounts, etc.).
3. **Carpetas por entorno** con `docker-compose.yml` específicas (más verborreico pero explícito).

Ejemplo para ejecutar test (puerto 1434):

```bash
docker compose --env-file envs/.env.test up -d
```

---

## Buenas prácticas / tips

- **No** subir `.env` con contraseñas; subir `.env.example` en su lugar.
- Usa contraseñas fuertes en `SA_PASSWORD` (mín. 8 caracteres con mayúsculas, minúsculas, números, símbolos).
- Para integración CI/CD, almacena contraseñas en GitHub Secrets y no levantes contenedores con SA en producción — usa servicios gestionados.
- Para múltiples instancias locales, cambia `HOST_PORT` y el `volume` name para evitar colisiones.
- Si quieres automatizar la ejecución de `init.sql`, añade un script en `scripts/run-init.sh` que haga `docker exec` tras comprobar que la DB está lista.

---

## Comandos útiles de limpieza

```bash
# bajar y remover contenedores (no borra volúmenes a menos que pidas)
docker compose --env-file envs/.env.dev down

# bajar y remover con volúmenes (cuidado: borrará datos)
docker compose --env-file envs/.env.dev down -v

# eliminar imágenes (libera espacio)
docker image prune -a

# limpieza general (cuidado con lo que borras)
docker system prune --volumes
```

---

## Ejemplo de `.gitignore`

```
# envs reales
envs/.env.*
.env*

# Docker volumes
mssql_data_*

# IDE
.vscode/
```

---

## README de ejemplo (breve) — qué poner en el repo

- Qué hace el repo.
- Cómo configurar `.env` usando `.env.example`.
- Comandos para levantar el entorno: `docker compose --env-file envs/.env.dev up -d`.
- Cómo ejecutar `init.sql`.
- Cómo conectarse con SSMS.

---

## ¿Siguiente paso opcional?

Puedo:

- Generar el repo listo para subir a GitHub (estructura + archivos de ejemplo) y entregarte un zip.
- Crear un `Dockerfile` extendido si quieres correr scripts de inicialización automáticamente.
- Preparar un pequeño workflow de GitHub Actions que levante una instancia para pruebas (nota: levantar SQL Server en runners puede aumentar tiempo y complejidad).

Dime cuál de esas opciones prefieres y lo preparo.

---

**Fin del documento.**

Si quieres, puedo ahora crear el repo inicial y devolverte un zip con todo lo anterior listo para subir a GitHub.

