<#
.SYNOPSIS
    Sets the classpath to target/classes and runs TextGameDemo via Maven.
#>

$ProjectRoot = $PSScriptRoot
Set-Location $ProjectRoot

# Workaround for TLS cert issue when Maven needs to download a new plugin.
$env:MAVEN_OPTS = "-Djavax.net.ssl.trustStoreType=Windows-ROOT"

$env:CLASSPATH = Join-Path $ProjectRoot "target\classes"

mvn compile
if ($LASTEXITCODE -ne 0) {
    Write-Error "mvn compile failed"
    exit $LASTEXITCODE
}

#    "-Dexec.mainClass=com.acmemail.judah.battleship.sandbox.TextGameDemo" `
#    "-Dexec.mainClass=com.acmemail.judah.battleship.sandbox.TextGridParentDemo" `
mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java `
    "-Dexec.mainClass=com.acmemail.judah.battleship.sandbox.TextGameDemo" `
    "-Dexec.classpathScope=compile"
exit $LASTEXITCODE
