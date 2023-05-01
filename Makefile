# Makefile to automate the building of the TypingTutorApp
# 23 August 2022
# Manelisi Ncube

.SUFFIXES: .java .class
BINDIR=bin/typingTutor/
SRCDIR=src/typingTutor/

$(BINDIR)%.class:$(SRCDIR)%.java
	javac -d bin/ -cp . $(SRCDIR)*.java


CLASSES=WordDictionary.class Score.class FallingWord.class CatchWord.class WordMover.class HungryWordMover.class GamePanel.class ScoreUpdater.class TypingTutorApp.class
	       
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)%.class)

default: $(CLASS_FILES) 

# Getting arguments from console 
args = `arg="$(filter-out $@,$(MAKECMDGOALS))" && echo $${arg:-${1}}`

#Remove all class files
clean:
	rm $(BINDIR)*.class

	
#Run TypingTutorApp
run: $(CLASS_FILES)
	@java -cp bin typingTutor.TypingTutorApp $(call args)
