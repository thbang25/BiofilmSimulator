#thabang sambo
#makefile compilation BIOFILM
#BIOFILM


# Makefile for BioFilmSimulator

JAVAC = javac
JAVA = java
JFLAGS = -g
CLASSES = BioFilmSimulator.class BlueBacteria.class RedBacteria.class Bacteria.class Nutrient.class
MAIN = BioFilmSimulator

all: compile

compile: $(CLASSES)

BioFilmSimulator.class: BioFilmSimulator.java BlueBacteria.java RedBacteria.java Bacteria.java Nutrient.java
	$(JAVAC) $(JFLAGS) BioFilmSimulator.java BlueBacteria.java RedBacteria.java Bacteria.java Nutrient.java

run: compile
	$(JAVA) $(MAIN)

clean:
	rm -f *.class

.PHONY: all compile run clean
