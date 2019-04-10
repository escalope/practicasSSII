This is a JADE agent-to-agent interaction example. It shows a simple sender/receiver agent (MyFirstJADEAgentWithBasicSendReceiver) and another more fault tolerant using Rational Effect (RE) behaviors (MyFirstJADEAgentWithBasicSendReceiver)

Execute directly from sources
--------------------------
Basic send/receive behavior:
- Open one console and run *mvn compile exec:java -Dexec.mainClass="myfirstjade.MyFirstJADEAgentWithBasicSendReceiver"*
- Go to the GUI, open the menu option Tools->start Dummy Agent. a new GUI will appear where messages can be sent. 
- Focus in "Receivers:" field. Right click  and select "Add"
- Tick the checkbox next to "NAME" and write "MyFirstJADEAgentWithBasicSendReceiver". Press OK
- Focus in "Content:" field. Write "hello"
- Focus in "Communicative:" field. Select "request"
- Focus in "Protocol:" field. Select "fipa-request"
- In the Menu, go to Current Message -> Send
- New entries will appear on the right side of the GUI. 
- The INFORM message will be generated. Select that message.
- Go to the menu Queued message -> View
- Observe it was sent by the MyFirstJADEAgentWithBasicSendReceiver agent to the dummy agent (usually with id da0), and with the content "hello da0"
- Do not try to send the message again. The other agent will not answer. It answer only once.

Fault tollerant send/receive behavior:
- Open one console and run *mvn compile exec:java -Dexec.mainClass="myfirstjade.MyFirstJADEAgentWithRE"*
- Go to the GUI, open the menu option Tools->start Dummy Agent. a new GUI will appear where messages can be sent. 
- Focus in "Receivers:" field. Right click  and select "Add"
- Tick the checkbox next to "NAME" and write "MyFirstJADEAgentWithRE". Press OK
- In the Menu, go to Current Message -> Send
- No answer will be issued by the MyFirstJADEAgentWithRE agent
- Focus in "Protocol:" field. Select "fipa-request"
- In the Menu, go to Current Message -> Send
- This time a "not-understood" message is received
- Focus in "Content:" field. Write "hello"
- Focus in "Communicative:" field. Select "request"
- In the Menu, go to Current Message -> Send
- Observe that an AGREE and INFORM answer is delivered. Select the INFORM message.
- Go to the menu Queued message -> View
- Observe it was sent by the MyFirstJADEAgentWithRE agent to the dummy agent (usually with id da0), and with the content "hello da0"
- This time, the message can be sent many times and receive a proper answer


Execute it from a jar
--------------------------
To begin with, run the following:
- Run *mvn package* in the console

Then, for the basic send/receive behavior:
- Open one console and run *java -cp target/ej3-0.0.1-SNAPSHOT-selfcontained.jar myfirstjade.MyFirstJADEAgentWithBasicSendReceiver*
- Go to the GUI, open the menu option Tools->start Dummy Agent. a new GUI will appear where messages can be sent. 
- Focus in "Receivers:" field. Right click  and select "Add"
- Tick the checkbox next to "NAME" and write "MyFirstJADEAgentWithBasicSendReceiver". Press OK
- Focus in "Content:" field. Write "hello"
- Focus in "Communicative:" field. Select "request"
- Focus in "Protocol:" field. Select "fipa-request"
- In the Menu, go to Current Message -> Send
- New entries will appear on the right side of the GUI. 
- The INFORM message will be generated. Select that message.
- Go to the menu Queued message -> View
- Observe it was sent by the MyFirstJADEAgentWithBasicSendReceiver agent to the dummy agent (usually with id da0), and with the content "hello da0"
- Do not try to send the message again. The other agent will not answer. It answer only once.

Then, for the fault tollerant send/receive behavior:
- Open one console and run *java -cp target/ej3-0.0.1-SNAPSHOT-selfcontained.jar myfirstjade.MyFirstJADEAgentWithRE*
- Go to the GUI, open the menu option Tools->start Dummy Agent. a new GUI will appear where messages can be sent. 
- Focus in "Receivers:" field. Right click  and select "Add"
- Tick the checkbox next to "NAME" and write "MyFirstJADEAgentWithRE". Press OK
- In the Menu, go to Current Message -> Send
- No answer will be issued by the MyFirstJADEAgentWithRE agent
- Focus in "Protocol:" field. Select "fipa-request"
- In the Menu, go to Current Message -> Send
- This time a "not-understood" message is received
- Focus in "Content:" field. Write "hello"
- Focus in "Communicative:" field. Select "request"
- In the Menu, go to Current Message -> Send
- Observe that an AGREE and INFORM answer is delivered. Select the INFORM message.
- Go to the menu Queued message -> View
- Observe it was sent by the MyFirstJADEAgentWithRE agent to the dummy agent (usually with id da0), and with the content "hello da0"
- This time, the message can be sent many times and receive a proper answer




2. 
