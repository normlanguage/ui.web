$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$norm = Join-Path $PSScriptRoot 'norm.ps1'
$packages = Join-Path $root '.norm-home/.norm/cache/packages'
& (Join-Path $PSScriptRoot 'build.ps1')
& $norm package (Join-Path $root 'ui/web') --output $packages
if ($LASTEXITCODE -ne 0) { throw 'Backend artifact verification failed' }
& $norm check (Join-Path $root 'samples/demo')
if ($LASTEXITCODE -ne 0) { throw 'Consumer source check failed' }
