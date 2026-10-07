$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$norm = Join-Path $PSScriptRoot 'norm.ps1'
$maven = Join-Path $root '.norm-home/.norm/cache/maven'
$packages = Join-Path $root '.norm-home/.norm/cache/packages'
New-Item -ItemType Directory -Force $maven, $packages | Out-Null

& (Join-Path $root 'gradlew.bat') -p $root publish --console=plain
if ($LASTEXITCODE -ne 0) { throw 'Vaadin backend and frontend build failed' }
Copy-Item -Path (Join-Path $root 'build/repository/*') -Destination $maven -Recurse -Force

$module = Join-Path $root 'ui/web/module.norm'
$source = Get-Content -LiteralPath $module -Raw
$unpinned = [regex]::Replace($source, ', resolution: sha256\("[a-f0-9]+"\)', '')
if ($source -ne $unpinned) { Set-Content -LiteralPath $module -Value $unpinned -NoNewline }
& $norm resolve (Join-Path $root 'ui/web')
if ($LASTEXITCODE -ne 0) { throw 'Vaadin module resolution failed' }
& $norm package (Join-Path $root 'ui/web') --output $packages
if ($LASTEXITCODE -ne 0) { throw 'Vaadin module packaging failed' }
& $norm check (Join-Path $root 'samples/demo')
if ($LASTEXITCODE -ne 0) { throw 'Web example source check failed' }
