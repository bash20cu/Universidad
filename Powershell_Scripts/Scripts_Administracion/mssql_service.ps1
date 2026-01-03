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
    Script para controlar el servicio de SQL Server
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
        Write-Host "`nNo se encontraron servicios con el nombre '$ServiceName' en este sistema."
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

# Verificar si el servicio de SQL Server existe
$services = Get-Service | Where-Object { $_.DisplayName -like "*SQL Server*" }


if ($services -ne $null) {

    # Obtener todos los servicios de SQL Server
    Get-ServiceStatus -ServiceName "*SQL Server*"

    
    # Verificar si el servicio está en ejecución
    if ($mssqlService.Status -eq "Running") {
        Write-Host "`nEstado actual del servicio: En ejecucion"
    }
    else {
        Write-Host "`nEstado actual del servicio: Detenido"
    }
    
    # Mostrar menú de opciones
    Write-Host "Menu de opciones:"
    Write-Host "1. Apagar servicio"
    Write-Host "2. Encender servicio"
    Write-Host "3. Reiniciar servicio"
    
    $choice = Read-Host "`nSeleccione una opcion (1/2/3)"
    
    
    switch ($choice) {
        1 {
            # Detener todos los servicios de SQL Server y sus dependencias

            foreach ($service in $services)
            {
                Write-Host "Deteniendo $($service.DisplayName)..."
                Stop-Service -Name $service.Name -Force
            }

            # Esperar hasta que todos los servicios se detengan completamente
            while ($services | Where-Object { $_.Status -eq "Stopping" }) 
            {
                Start-Sleep -Seconds 1
            }

            Write-Host "Todos los servicios de SQL Server han sido detenidos."

        }
        2 {
            # Iniciar todos los servicios de SQL Server nuevamente
            foreach ($service in $services) 
            {
                Write-Host "Iniciando $($service.DisplayName)..."
                Start-Service -Name $service.Name
            }

            # Esperar hasta que todos los servicios se inicien completamente
            while ($services | Where-Object { $_.Status -eq "Starting" }) 
            {
                Start-Sleep -Seconds 1
            }
            Write-Host "El servicio de SQL Server se ha iniciado."
        }
        3 {
            # Reiniciar todos los servicios de SQL Server y sus dependencias
            $services = Get-Service | Where-Object { $_.DisplayName -like "*SQL Server*" }

            foreach ($service in $services)
            {
                Write-Host "Deteniendo $($service.DisplayName)..."
                Stop-Service -Name $service.Name -Force
            }

            # Esperar hasta que todos los servicios se detengan completamente
            while ($services | Where-Object { $_.Status -eq "Stopping" }) 
            {
                Start-Sleep -Seconds 1
            }

            # Iniciar todos los servicios de SQL Server nuevamente
            foreach ($service in $services) 
            {
                Write-Host "Iniciando $($service.DisplayName)..."
                Start-Service -Name $service.Name
            }

            # Esperar hasta que todos los servicios se inicien completamente
            while ($services | Where-Object { $_.Status -eq "Starting" }) 
            {
                Start-Sleep -Seconds 1
            }

            Write-Host "Todos los servicios de SQL Server han sido detenidos y reiniciados."
        }
        default {
            Write-Host "Opcion no valida. No se realizo ninguna accion."
        }
    }
}
else {
    Write-Host "El servicio de SQL Server no existe en este sistema."
}

# Obtener todos los servicios de SQL Server
Get-ServiceStatus -ServiceName "*SQL Server*"

# Agregar un mensaje de espera
Write-Host "`nPresiona Enter para cerrar el script..."
Read-Host