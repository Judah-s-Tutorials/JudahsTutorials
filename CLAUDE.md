# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

This is **JudahsTutorials** — a collection of independent Java tutorial projects for educational purposes. Each subdirectory is a self-contained Maven project; there is no root/aggregator POM.

## Build & Test Commands

Run these commands from within the specific project directory (e.g., `cd "Cartesian Plane Part 6"`):

```bash
mvn clean install          # Compile, test, and install to local repo
mvn clean compile          # Compile only
mvn test                   # Run all tests
mvn -Dtest=ClassName test  # Run a single test class
mvn clean package          # Package as JAR
mvn assembly:assembly       # Create distributable archive (tar.gz/zip)
mvn javadoc:javadoc        # Generate JavaDoc
```

Or specify the POM explicitly from the workspace root:

```bash
mvn -f "Cartesian Plane Part 6/pom.xml" test
```

## Project Structure

All projects follow standard Maven layout:
- `src/main/java/` — production source code
- `src/test/java/` — JUnit test classes
- `src/main/resources/` / `src/test/resources/` — supporting files
- `target/` — build output (git-ignored)

### Key Project Groups

| Group | Projects | Topic |
|-------|----------|-------|
| **Cartesian Plane** | Parts 1–18 | Progressive 2D graphics/geometry visualization tutorial series |
| **HSJava** | CartesianPlane, GameOfLifeLib/App, Library | High School Java curriculum |
| **Sandboxes** | MockitoSandbox, Exp4jSandbox, JEPSandbox, TesseractSandbox | Framework/library exploration |
| **Apps** | Battleship, Weather, Penrose, Glossary | Complete application projects |

The **Cartesian Plane** series is the primary tutorial sequence — each part builds on the previous. The **HSJava/Library** project contains shared utilities used across HSJava subprojects.

## Tech Stack

- **Java 21** (primary; some projects target Java 17)
- **Maven** for build management
- **JUnit 5 (Jupiter)** for testing; versions range from 5.9.1 to 6.0.2
- **Mockito 5.9.0** (MockitoSandbox only)
- Compiler flags include `-Xlint:all -Xlint:serial` — expect warnings to be treated seriously

## Eclipse IDE

The `.metadata/` directory indicates this workspace is opened in Eclipse. Projects may have `.classpath` and `.project` files. Maven projects are typically imported as "Existing Maven Projects" in Eclipse.
