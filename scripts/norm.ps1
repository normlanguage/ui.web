$root = Split-Path $PSScriptRoot -Parent
$previousOptions = $env:JAVA_TOOL_OPTIONS
try {
    $developmentHome = Join-Path $root '.norm-home'
    $env:JAVA_TOOL_OPTIONS = "$previousOptions --enable-native-access=ALL-UNNAMED -Duser.home=`"$developmentHome`""
    $executable = if ($env:NORM_EXECUTABLE) {
        $env:NORM_EXECUTABLE
    } else {
        Join-Path (Split-Path $root -Parent) 'Norm/build/compiler/norm-runtime/bin/norm.bat'
    }
    if (-not (Test-Path -LiteralPath $executable -PathType Leaf)) {
        throw "Norm executable not found: $executable. Set NORM_EXECUTABLE to a built norm.bat."
    }
    & $executable @args
    $result = $LASTEXITCODE
} finally {
    $env:JAVA_TOOL_OPTIONS = $previousOptions
}
exit $result
