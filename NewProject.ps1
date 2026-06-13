# Creates a new Maven project in the workspace using the standard
# normalized POM template (offline; no archetype needed).
#
# Usage:
#   .\NewProject.ps1 -ProjectName "Java Records"
#   .\NewProject.ps1 -ProjectName "Java Records" -ArtifactId JavaRecords -Package com.acmemail.judah.java_records
#
# Author: claude.ai

param(
    [Parameter(Mandatory=$true)]
    [string]$ProjectName,

    [string]$ArtifactId,

    [string]$Package
)

$ErrorActionPreference = 'Stop'

if ( [string]::IsNullOrWhiteSpace( $ArtifactId ) )
{
    $ArtifactId = $ProjectName -replace '[^A-Za-z0-9_.-]', ''
}
if ( [string]::IsNullOrWhiteSpace( $Package ) )
{
    $suffix  = $ProjectName.ToLower() -replace '[^a-z0-9]+', '_'
    $suffix  = $suffix.Trim( '_' )
    $Package = "com.acmemail.judah.$suffix"
}

$projectDir = Join-Path $PSScriptRoot $ProjectName
if ( Test-Path $projectDir )
{
    Write-Error "Directory already exists: $projectDir"
    exit 1
}

$pkgPath = $Package -replace '\.', '\'
$dirs = @(
    "src\main\java\$pkgPath",
    "src\test\java\$pkgPath",
    "src\main\resources",
    "src\test\resources"
)
foreach ( $dir in $dirs )
{
    New-Item -ItemType Directory -Force -Path (Join-Path $projectDir $dir) | Out-Null
}

$pomTemplate = @'
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.acmemail.judah</groupId>
    <artifactId>@ARTIFACT_ID@</artifactId>
    <packaging>jar</packaging>
    <version>01</version>
    <name>@ARTIFACT_ID@</name>
    <properties>
        <teacher>StraubJW</teacher>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>6.0.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
                <configuration>
                    <release>21</release>
                    <compilerArgs>
                        <arg>-Xlint:all</arg>
                        <arg>-Xlint:serial</arg>
                    </compilerArgs>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.7.1</version>
                <configuration>
                    <finalName>${teacher}-${project.version}</finalName>
                    <formats>
                        <format>tar.gz</format>
                        <format>zip</format>
                    </formats>
                    <descriptorRefs>
                        <descriptorRef>project</descriptorRef>
                    </descriptorRefs>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
'@

$pom = $pomTemplate -replace '@ARTIFACT_ID@', $ArtifactId
$utf8NoBom = New-Object System.Text.UTF8Encoding $false
[IO.File]::WriteAllText( (Join-Path $projectDir 'pom.xml'), $pom, $utf8NoBom )

$gitignore = "/target/`r`n/bin/`r`n"
[IO.File]::WriteAllText( (Join-Path $projectDir '.gitignore'), $gitignore, $utf8NoBom )

Write-Output "Created project: $projectDir"
Write-Output "  artifactId: $ArtifactId"
Write-Output "  package:    $Package"
Write-Output ""
Write-Output "Next steps:"
Write-Output "  - Import into Eclipse as an Existing Maven Project"
Write-Output "  - Build: mvn -f `"$ProjectName/pom.xml`" clean test"
