$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$norm = Join-Path $PSScriptRoot 'norm.ps1'
& (Join-Path $PSScriptRoot 'build.ps1')
foreach ($path in @('ui/web', 'samples/demo')) {
    $module = Join-Path $root "$path/module.norm"
    $source = Get-Content -LiteralPath $module -Raw
    $unpinned = [regex]::Replace($source, ', resolution: sha256\("[a-f0-9]+"\)', '')
    Set-Content -LiteralPath $module -Value $unpinned -NoNewline
    & $norm resolve (Join-Path $root $path)
    if ($LASTEXITCODE -ne 0) { throw "Artifact update failed: $path" }
}
& (Join-Path $PSScriptRoot 'prepare.ps1')
