This example shows how to create a JADE agent whose main launches a JADE container and the remote GUI.

*Execute directly from console*
Run this:
- *mvn compile exec:java -Dexec.mainClass="myfirstjade.MyFirstJADEAgent"*
- Check that a GUI appears. Find your agent in the GUI by unfolding the folders.


*Execute it from a jar*
- Run "mvn package" in the console
- Run "java -cp target/ej1-0.0.1-SNAPSHOT-selfcontained.jar myfirstjade.MyFirstJADEAgent"
- Check that a GUI appears. Find your agent in the GUI by unfolding the folders.
2. 
