# Makefile to automate the building of the TypingTutorApp
# 23 August 2022
# Manelisi Ncube

.SUFFIXES: .java .class
BINDIR=bin/typingTutor
SRCDIR=src/typingTutor
JAVAC=/usr/bin/javac

$(BINDIR)%.class:$(SRCDIR)%.java
	$(JAVAC) -d $(BINDIR) -cp . $(SRCDIR)/*.java

#Classes for the VaccineArrayApp
CLASSES=WordDictionary.class Score.class FallingWord.class CatchWord.class WordMover.class HungryWordMover.class GamePanel.class ScoreUpdater.class TypingTutorApp.class
	       
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES) 

#Remove all class files
clean:
	rm $(BINDIR)/*.class

	
#Run option for the VaccineBSTAPp
run: $(CLASS_FILES)
	java -cp bin typingTutor.TypingTutorApp
