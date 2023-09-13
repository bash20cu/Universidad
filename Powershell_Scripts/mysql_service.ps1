
# Función para mostrar un dibujo ASCII con tu nombre
function Show-MyNameAsciiArt {
    Write-Host @"
    ######     #     #####  #     #  #####  #     # 
    #     #   # #   #     # #     # #     # #     # 
    #     #  #   #  #       #     # #       #     # 
    ######  #     #  #####  ####### #       #     # 
    #     # #######       # #     # #       #     # 
    #     # #     # #     # #     # #     # #     # 
    ######  #     #  #####  #     #  #####   #####  
"@
    Write-Host "
    Script para controlar el servicio de MySQL
    "
}

# Verificar si se tiene acceso administrativo
function Test-Administrator {
    $currentUser = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
    return $currentUser.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
}

# Función para obtener el estado de un servicio específico
function Get-ServiceStatus {
    param (
        [string]$ServiceName
    )

    # Obtener el servicio por nombre
    $services = Get-Service | Where-Object { $_.DisplayName -like "*$ServiceName*" }

    if ($services.Count -gt 0) {
        # Crear una tabla para mostrar los resultados
        $table = @()
        foreach ($service in $services) {
            $table += New-Object PSObject -Property @{
                "Status" = $service.Status
                "Name" = $service.Name
                "DisplayName" = $service.DisplayName
            }
        }
        # Mostrar la tabla en formato legible
        $table | Format-Table -AutoSize
    }
    else {
        Write-Host "No se encontraron servicios con el nombre '$ServiceName' en este sistema."
    }
}



# Mostrar el dibujo ASCII con tu nombre
Show-MyNameAsciiArt

# Verificar si se tiene acceso administrativo
if (-not (Test-Administrator)) {
    Write-Host "
    Este script requiere acceso administrativo. Ejecuta el script como administrador.
    "
    # Solicitar elevación de permisos
    Start-Process powershell.exe -Verb RunAs -ArgumentList ("$($MyInvocation.MyCommand.Path)")
    exit
}




# Verificar si el servicio de MySQL existe
$services = Get-Service | Where-Object { $_.DisplayName -like "*MySQL*" }

if ($services -ne $null) {

    Get-ServiceStatus -ServiceName "*MySQL*"
   
    
    # Verificar si el servicio está en ejecución
    if ($services.Status -eq "Running") {
        Write-Host "`n Estado actual del servicio: En ejecucion"
    }
    else {
        Write-Host " `n Estado actual del servicio: Detenido"
    }
    
    # Mostrar menú de opciones
    Write-Host "Menu de opciones:"
    Write-Host "1. Apagar servicio"
    Write-Host "2. Encender servicio"
    Write-Host "3. Reiniciar servicio"
    
    $choice = Read-Host "`nSeleccione una opcion (1/2/3)"
    
    switch ($choice) {
        1 {
            # Apagar el servicio de MySQL
            Stop-Service -Name $services.Name -Force
            Write-Host "El servicio de MySQL se ha apagado."
        }
        2 {
            # Encender el servicio de MySQL
            Start-Service -Name $services.Name
            Write-Host "El servicio de MySQL se ha iniciado."
        }
        3 {
            # Reiniciar el servicio de MySQL
            Restart-Service -Name $services.Name
            Write-Host "El servicio de MySQL se ha reiniciado."
        }
        default {
            Write-Host "Opcion no válida. No se realizo ninguna accion."
        }
    }
}
else {
    Write-Host "El servicio de MySQL no existe en este sistema."
}

# Obtener todos los servicios de SQL Server
Get-ServiceStatus -ServiceName "*MySQL*"

# Agregar un mensaje de espera
Write-Host " `nPresiona Enter para cerrar el script..."
Read-Host