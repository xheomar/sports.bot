export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8
mvn clean install
mvn exec:java -Dexec.mainClass="test.myTelegramBot.SimpleBot"
