$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$maven = Join-Path $root '.norm-home/.norm/cache/maven'
New-Item -ItemType Directory -Force $maven | Out-Null
foreach ($path in @('ui/web/resources/META-INF/licenses/ui.web', 'samples/demo/resources/META-INF/licenses/demo')) {
    $resources = Join-Path $root $path
    New-Item -ItemType Directory -Force $resources | Out-Null
    Copy-Item -LiteralPath (Join-Path $root 'LICENSE') -Destination (Join-Path $resources 'LICENSE') -Force
}
& (Join-Path $root 'gradlew.bat') -p $root :publish :demo:publish :consumerTest --console=plain
if ($LASTEXITCODE -ne 0) { throw 'Backend and consumer build failed' }
Copy-Item -Path (Join-Path $root 'build/repository/*') -Destination $maven -Recurse -Force
