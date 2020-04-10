JAVA_COMPILER=javac

JAR_ARCHIVER=jar

JAVA=java


module:
	$(JAVA_COMPILER) -sourcepath src src/ru/ColdChip/ChipDrive/Server.java -d build/class
	cd build/class && $(JAR_ARCHIVER) -cvfm ../../build/server.jar "../../manifest/MANIFEST.MF" * > /dev/null
	echo "-----DONE-----"
run:
	$(JAVA) -jar Build/server.jar
