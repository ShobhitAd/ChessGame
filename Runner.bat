@echo off
echo "Start"

if %1==compile (
	echo "Compiling"
	javac *.java
)
if %1==jar (
	echo "Creating jar"
	jar cvfe Chess.jar clsMain *.class
)

if %1==run (
	echo "Running"
	java clsMain
)
if %1==clean (
	echo "Cleaning"
	del *.class
	del Chess.jar
)
echo "End"