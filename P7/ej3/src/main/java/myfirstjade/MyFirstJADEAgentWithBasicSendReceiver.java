package myfirstjade;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AlreadyBoundException;

import javax.swing.JButton;
import javax.swing.JFrame;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Location;
import jade.core.PlatformID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.ReceiverBehaviour.NotYetReady;
import jade.core.behaviours.ReceiverBehaviour.TimedOut;
import jade.core.behaviours.SenderBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MyFirstJADEAgentWithBasicSendReceiver extends Agent {

	public MyFirstJADEAgentWithBasicSendReceiver() {
		
	}
	
	public boolean migrate=false;
	
	@Override
	protected void setup() {		
		super.setup();
		
		SequentialBehaviour sb=new SequentialBehaviour();
		
		MessageTemplate.MatchExpression me = new MessageTemplate.MatchExpression() {
			public boolean match(ACLMessage mes) {
				// Be careful with reception of sync messages as conventional messages
				return mes.getContent()!=null && 
						mes.getContent().toString().equalsIgnoreCase("hello");
			};
		};

		final ReceiverBehaviour rb=new ReceiverBehaviour(this,-1,new MessageTemplate(me));
		OneShotBehaviour processMessage=new OneShotBehaviour() {
						
			@Override
			public void action() {
				ACLMessage messageReceived;
				try {
					messageReceived = rb.getMessage();
					ACLMessage answer=new ACLMessage(ACLMessage.INFORM);
					answer.addReceiver(messageReceived.getSender());
					answer.setSender(getAID());
					answer.setContent("hello "+messageReceived.getSender().getLocalName());		
					addBehaviour(new SenderBehaviour(MyFirstJADEAgentWithBasicSendReceiver.this, answer));
				} catch (TimedOut e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotYetReady e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		};
		
		sb.addSubBehaviour(rb);
		sb.addSubBehaviour(processMessage);
		
		this.addBehaviour(sb);		
	}



	public static void launchGUI(jade.wrapper.AgentContainer ac) throws StaleProxyException{
		final AgentController rmaController =
				ac.createNewAgent("rma-1", 
						"jade.tools.rma.rma",
						new Object[0]);	
		new Thread(){
			public void run(){
				try {

					rmaController.start();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}


	public  static void main(String args[]) throws StaleProxyException{
		// Get a hold on JADE runtime
		jade.core.Runtime rt = jade.core.Runtime.instance();
		// Exit the JVM when there are no more containers around
		rt.setCloseVM(true);
		// Create a default profile
		Profile p = new ProfileImpl();
		p.setParameter("preload","*");
		p.setParameter(Profile.MAIN_PORT, "60000");		

		// Create a new non-main container, connecting to the default
		// main container (i.e. on this host, port 60000)
		jade.wrapper.AgentContainer acl = null;

			acl=rt.createMainContainer(p);
		
		final jade.wrapper.AgentContainer ac=acl;
		
		launchGUI(ac);
		
		// if you want just to connect an existing container, run this
		//final jade.wrapper.AgentContainer ac = rt.createMainContainer(p);
		// Create a new agent
		final jade.wrapper.AgentController agc = 
				ac.createNewAgent("MyFirstJADEAgentWithBasicSendReceiver",
						"myfirstjade.MyFirstJADEAgentWithBasicSendReceiver", new Object[0]);	
		new Thread(){
			public void run(){
				try {
					System.out.println("Starting up MyFirstJADEAgent");
					agc.start();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}

}
