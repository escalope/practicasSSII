This is a JADE agent-to-agent interaction example with a GUI and sniffing capabilities embedded. It shows a basic combination of sender/receiver behaviors initiated by the user pressing a GUI button. There is the AgentA and AgentB. Both are started from the Main class.

Execute directly from sources
--------------------------
Basic send/receive behavior:
- Open one console and run *mvn compile exec:java -Dexec.mainClass="myfirstjade.Main"*
- Check the appearing GUIs.There should be three:one with a start button, another showing boxes which will trace messages interchanged among agents (sniffer), and a last one with the controls to handle the agent platform.
- Press the "start" button and observe the sniffer. Also watch the console and check the printed messages.


