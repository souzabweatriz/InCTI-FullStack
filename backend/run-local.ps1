$ErrorActionPreference = "Stop"

$env:DB_HOST = "incti.postgres.database.azure.com"
$env:DB_PORT = "5432"
$env:DB_NAME = "postgres"
$env:DB_USER = "incti"
$env:DB_SSLMODE = "require"
$env:ETL_URL = "http://localhost:8000"
$env:ETL_KEY = "incti-etl-chave-local-2026"

$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)

$password = Read-Host "Digite a senha do PostgreSQL" -AsSecureString
$passwordPtr = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($password)
try {
    $env:DB_PASSWORD = [System.Runtime.InteropServices.Marshal]::PtrToStringBSTR($passwordPtr)
}
finally {
    [System.Runtime.InteropServices.Marshal]::ZeroFreeBSTR($passwordPtr)
}

try {
    & .\mvnw.cmd spring-boot:run
}
finally {
    Remove-Item Env:DB_PASSWORD -ErrorAction SilentlyContinue
    Remove-Item Env:JWT_SECRET -ErrorAction SilentlyContinue
    Remove-Item Env:ETL_URL -ErrorAction SilentlyContinue
    Remove-Item Env:ETL_KEY -ErrorAction SilentlyContinue
}
