# Verificar si se tiene acceso administrativo
function Test-Administrator {
    $currentUser = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
    return $currentUser.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
}

# Comprobar si se tienen permisos de administrador
if (-not (Test-Administrator)) {
    Write-Host "Este script requiere acceso administrativo. Ejecuta el script como administrador."
    # Solicitar elevación de permisos
    Start-Process powershell.exe -Verb RunAs -ArgumentList ("$($MyInvocation.MyCommand.Path)")
    exit
}


# Desactivar la telemetría en Windows
# Nota: Esto puede requerir permisos de administrador

# Configuración de nivel de telemetría
$telemetryLevel = 0

# Establecer el nivel de telemetría en 0 (Desactivado)
Set-ItemProperty -Path "HKLM:\SOFTWARE\Policies\Microsoft\Windows\DataCollection" -Name "AllowTelemetry" -Value $telemetryLevel -Force

# Reiniciar el servicio de telemetría
Restart-Service -Name "DiagTrack"

# Detener y deshabilitar el servicio Connected User Experiences and Telemetry
Stop-Service -Name "DiagTrack" -Force
Set-Service -Name "DiagTrack" -StartupType Disabled

# Verificar que el servicio se haya desactivado
if ((Get-Service -Name "DiagTrack").Status -eq "Stopped") {
    Write-Host "Telemetría desactivada con éxito."
} else {
    Write-Host "Error al desactivar la telemetría."
}

# Reiniciar el sistema (opcional)
# Restart-Computer -Force
