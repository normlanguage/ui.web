$root = Split-Path $PSScriptRoot -Parent
$previousOptions = $env:JAVA_TOOL_OPTIONS
try {
    $developmentHome = Join-Path $root '.norm-home'
    $env:JAVA_TOOL_OPTIONS = "$previousOptions --enable-native-access=ALL-UNNAMED -Duser.home=`"$developmentHome`""
    $executable = if ($env:NORM_EXECUTABLE) { $env:NORM_EXECUTABLE } else { "norm" }
    & $executable @args
    $result = $LASTEXITCODE
} finally {
    $env:JAVA_TOOL_OPTIONS = $previousOptions
}
exit $result
