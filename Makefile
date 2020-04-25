JAVA_COMPILER=javac

JAR_ARCHIVER=jar

JAVA=java

module:
	@echo "-----Building 0%-----"
	rm build/class/* -r
	@$(JAVA_COMPILER) -sourcepath src src/ru/ColdChip/ChipDrive/Server.java -d build/class
	@echo "-----Building 50%-----"
	@cd build/class && $(JAR_ARCHIVER) -cvfm ../../build/server.jar "../../manifest/MANIFEST.MF" * > /dev/null
	@echo "-----Building 100%-----"
	@echo "-----DONE-----"
run:
	$(JAVA) -jar build/server.jar
runnohup:
	nohup java -jar build/server.jar >/dev/null 2>&1 &
