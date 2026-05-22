# Script para iniciar la aplicacion API Gestion
# Uso: .\run.ps1

$projectPath = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectPath

Write-Host "================================" -ForegroundColor Cyan
Write-Host "Iniciando API Gestion" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

$mvnCmd = Join-Path $projectPath "apache-maven-3.9.14\bin\mvn.cmd"
if (-not (Test-Path $mvnCmd)) {
    throw "No se encontro el Maven incluido en el repo: $mvnCmd"
}

Write-Host "Verificando Java..." -ForegroundColor Green
java -version 2>&1 | Select-Object -First 1
Write-Host ""

Write-Host "Configurando variables de entorno..." -ForegroundColor Green
$env:KEYCLOAK_ISSUER_URI = "http://172.20.6.59:8080/realms/gob-cundinamarca-devqa"
$env:KEYCLOAK_JWKS_URI = "http://172.20.6.59:8080/realms/gob-cundinamarca-devqa/protocol/openid-connect/certs"
$env:GOB_RESOURCE_CLIENT_IDS = "proyecta-web"
$env:GOB_CORS_ORIGINS = "http://localhost:5173,http://localhost:3000"
Write-Host ""

Write-Host "Iniciando aplicacion con Maven local..." -ForegroundColor Green
Write-Host "API Documentation: http://localhost:8082/swagger-ui.html" -ForegroundColor Cyan
Write-Host ""

& $mvnCmd -f (Join-Path $projectPath "pom.xml") spring-boot:run

Write-Host ""
Write-Host "Aplicacion detenida" -ForegroundColor Red
