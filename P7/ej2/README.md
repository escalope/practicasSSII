This is a JADE mobility example where one agent prints messages to the output, migrates when requested to another container, and then continues printing messages in the other container.

Execute directly from sources
--------------------------
Run this:
- Open one console and run *mvn compile exec:java -Dexec.mainClass="myfirstjade.MyFirstJADEAgent"*
- Observe that "Hello World" messages are printed and that a GUI with the button "migrate" is shown
- Open another console and run *mvn exec:java -Dexec.mainClass="jade.Boot" -Dexec.args="-gui -port 60000 -fildir target/ -container"*
- Observe no message is printed in the new console
- Press the "migrate" button and check "Hellow world" messages stop appearing in one console and start showing in the other.


Execute it from a jar
--------------------------
- Run *mvn package* in the console
- Open one console and run *java -cp target/ej2-0.0.1-SNAPSHOT-selfcontained.jar myfirstjade.MyFirstJADEAgent*
- Observe that "Hello World" messages are printed and that a GUI with the button "migrate" is shown
- Open anoter console and run *java -cp target/ej2-0.0.1-SNAPSHOT-selfcontained.jar jade.Boot -gui -port 600 -fildir target/ -container*
- Observe no message is printed in the new console
- Press the "migrate" button and check "Hellow world" messages stop appearing in one console and start showing in the other.


2. 
