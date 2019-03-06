JAVAC = /usr/bin/javac

.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR):$(SRCDIR): $<

CLASSES =  Server.class\
ClientThread.class\
Client.class\
ClientReadThread.class\
ClientWriteThread.class\
MessagingProtocol.class\



CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

SRC_FILES=$(SRC:%.java=$(SRCDIR)/%.java)

default: $(CLASS_FILES)

par:
	java -cp $(BINDIR) sample_input.txt

clean:
	rm $(BINDIR)/*.class
