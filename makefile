# Used for common java files

# Name for jar file
JAR = Chess.jar 

# name for main executable class
EXE = clsMain

#names of java files to be compiled
OBJECTS = clsGame.java clsMain.java ChessGame.java ChessRules.java ClientServerSocket.java MessageBox.java
 
compile:
	javac $(OBJECTS)

run:
	java $(EXE)

jar:
	jar cvfe $(JAR) $(EXE) *.class

clean:
	rm *.class


