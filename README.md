# LFA: Recursive Descent Parser (MEL)

## Author

Name: David Pantaleão Vilaça

## Description

- Language: Java 1.8
- IDE: Vim
- O.S: Ubuntu 18.10 (Pop!OS distro)

## Files

- `Makefile` => Source make/build file.
- `build.sh` => Executable file to perform the compilation quickly and easily.
- `src/Main.java` => Main program. Read input string and call parser with correct parameters.
- `parser/Parser.java` => Abstract class with common methods and variables (parser).
- `parser/RDParserMEL.java` => Recursive descent parser class implementing MEL grammar.


## Build

- Makefile
  - To compile run: `$ make compile`
  - The previous command compiles and adds the compiled files in the "out" folder.
  - To execute run (after compile): `$ make execute`

- Shell script
  - if necessary make the file executable, run: `$ chmod +x build.sh`
  - To compile and execute run: `$ ./build.sh`



Note¹: After run the terminal waits input expression.

Note²: The shell script run Makefile.
