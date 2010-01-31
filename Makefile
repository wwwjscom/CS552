#
# Makefile for AA (The Actor Architecture)
#
# Date: 2004/03/04
# Auther: Myeong-Wuk Jang
#

all:
	javac aa/Start.java
#	javac aa/app/dm/DirectoryManager.java
#	javac aa/app/dummy/Dummy.java
#	javac app/quickstart/animal/*.java
#	javac app/quickstart/dm/*.java
#	javac app/quickstart/hello/*.java
#	javac app/quickstart/hello1/*.java
#	javac app/quickstart/hello2/*.java
#	javac app/quickstart/hello3/*.java
#	javac app/quickstart/hello4/*.java
#	javac app/quickstart/hello5/*.java
#	javac app/quickstart/hello6/*.java
#	javac app/quickstart/sum/*.java
	javac app/quickstart/pa1/*.java


clean:
	\rm *.html
	\rm */*.html
	\rm */*/*.html
	\rm */*.class
	\rm */*/*.class
	\rm */*/*/*.class

cleanJava:
	\rm *.html
	\rm */*.html
	\rm */*/*.html
	\rm */*/*/*.html
	\rm */*.java
	\rm */*/*.java
	\rm */*/*/*.java
