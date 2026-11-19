# Sobe o projeto inteiro: Python (8000), backend (8080) e frontend (5173).
# Uso, na pasta InCTI:  .\iniciar.ps1
# Cada serviço abre na sua própria janela; feche a janela para parar o serviço.

$raiz = $PSScriptRoot

function Porta-Ocupada($porta) {
    [bool](Get-NetTCPConnection -State Listen -LocalPort $porta -ErrorAction SilentlyContinue)
}

function Iniciar($nome, $pasta, $comando, $porta) {
    if (Porta-Ocupada $porta) {
        Write-Host "[$nome] porta $porta já está em uso - pulando (já está rodando?)" -ForegroundColor Yellow
        return
    }
    Write-Host "[$nome] iniciando na porta $porta..." -ForegroundColor Cyan
    Start-Process powershell -WorkingDirectory (Join-Path $raiz $pasta) -ArgumentList @(
        "-NoExit",
        "-Command",
        "`$Host.UI.RawUI.WindowTitle = 'InCTI - $nome'; $comando"
    )
}

if (-not (Porta-Ocupada 5432)) {
    Write-Host "PostgreSQL não está rodando na porta 5432. Inicie o serviço do Postgres antes." -ForegroundColor Red
    exit 1
}

Iniciar "Python"   "analytics-python" "python -m uvicorn api:app --reload --port 8000" 8000
Iniciar "Backend"  "backend"          ".\mvnw.cmd spring-boot:run"                     8080
Iniciar "Frontend" "frontend"         "npm run dev"                                    5173

Write-Host ""
Write-Host "Aguarde o backend terminar de subir (~15s) e abra http://localhost:5173" -ForegroundColor Green
