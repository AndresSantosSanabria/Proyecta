# Script de verificación del sistema antes de ejecutar
# Uso: .\verificar-sistema.ps1

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Verificacion de Requisitos - API Gestion" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Verificar Java
Write-Host "Verificando Java..." -ForegroundColor White
$javaResult = java -version 2>&1
if ($javaResult) {
    Write-Host "✅ Java disponible" -ForegroundColor Green
    Write-Host "   $($javaResult[0])" -ForegroundColor Gray
} else {
    Write-Host "❌ Java NO disponible (REQUERIDO)" -ForegroundColor Red
}
Write-Host ""

# Verificar PostgreSQL
Write-Host "Verificando PostgreSQL (puerto 5432)..." -ForegroundColor White
$pgTest = Test-NetConnection -ComputerName localhost -Port 5432 -WarningAction SilentlyContinue
if ($pgTest.TcpTestSucceeded) {
    Write-Host "✅ PostgreSQL disponible en localhost:5432" -ForegroundColor Green
} else {
    Write-Host "⚠️ PostgreSQL NO responde en localhost:5432 (podrias iniciarlo)" -ForegroundColor Yellow
}
Write-Host ""

# Verificar JAR compilado
Write-Host "Verificando JAR compilado..." -ForegroundColor White
if (Test-Path "target/api-gestion-0.0.1-SNAPSHOT.jar") {
    Write-Host "✅ JAR compilado encontrado" -ForegroundColor Green
    $jarSize = (Get-Item "target/api-gestion-0.0.1-SNAPSHOT.jar").Length / 1MB
    Write-Host "   Tamaño: $([Math]::Round($jarSize, 2)) MB" -ForegroundColor Gray
} else {
    Write-Host "❌ JAR NO encontrado (REQUERIDO)" -ForegroundColor Red
}
Write-Host ""

# Verificar Maven
Write-Host "Verificando Maven..." -ForegroundColor White
if (Test-Path "apache-maven-3.9.14/bin/mvn.cmd") {
    Write-Host "✅ Maven disponible" -ForegroundColor Green
} else {
    Write-Host "⚠️ Maven no encontrado" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Para iniciar la aplicacion ejecuta:" -ForegroundColor Cyan
Write-Host "  .\run.ps1" -ForegroundColor Yellow
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""
