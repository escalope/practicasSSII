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
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MyFirstJADEAgentWithRE extends Agent {

	public MyFirstJADEAgentWithRE() {

	}

	public boolean migrate=false;

	@Override
	protected void setup() {		
		super.setup();				
		jade.core.behaviours.Behaviour sare=createResponder();		
		this.addBehaviour(sare);				
	}
	private jade.core.behaviours.Behaviour createResponder() {
		MessageTemplate.MatchExpression me = new MessageTemplate.MatchExpression() {
			public boolean match(ACLMessage mes) {
				// Be careful with reception of sync messages as conventional messages
				return mes.getProtocol()!=null &&
						mes.getProtocol().equalsIgnoreCase(FIPANames.InteractionProtocol.FIPA_REQUEST);
			};
		};
		SequentialBehaviour sb=new SequentialBehaviour();	
		sb.setBehaviourName("Answering");
		SimpleAchieveREResponder sre= new SimpleAchieveREResponder(this,new MessageTemplate(me)){
			@Override
			protected ACLMessage prepareResponse(ACLMessage request)
					throws NotUnderstoodException, RefuseException {
				ACLMessage reply=request.createReply();
				if (request.getContent()==null)
					throw new NotUnderstoodException(request);
				if (!request.getContent().equalsIgnoreCase("hello")){
					throw new NotUnderstoodException(request);
				}
				reply.setPerformative(ACLMessage.AGREE);
				return reply;
			}
			@Override
			protected ACLMessage prepareResultNotification(ACLMessage request,
					ACLMessage response) throws FailureException {

				ACLMessage answer = request.createReply();
				answer.setContent("hello "+request.getSender().getLocalName());
				answer.setPerformative(ACLMessage.INFORM);
				return answer;
			}			
		};
		sre.setBehaviourName("processing hello");
		sb.addSubBehaviour(sre);
		OneShotBehaviour oneShotBehaviour = new OneShotBehaviour() {			
			@Override
			public void action() {
				MyFirstJADEAgentWithRE.this.addBehaviour(createResponder());				
			}
		};
		oneShotBehaviour.setBehaviourName("recreating");
		sb.addSubBehaviour(oneShotBehaviour);		
		return sb;
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


	public  static void main(String args[]) throws Exception{
		// Get a hold on JADE runtime
		jade.core.Runtime rt = jade.core.Runtime.instance();
		// Exit the JVM when there are no more containers around
		rt.setCloseVM(true);
		// Create a default profile
		Profile p = new ProfileImpl();
		p.setParameter("preload","a*");
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
				ac.createNewAgent("MyFirstJADEAgentWithRE",
						"myfirstjade.MyFirstJADEAgentWithRE", new Object[0]);	
		new Thread(){
			public void run(){
				try {
					System.out.println("Starting up MyFirstJADEAgentWithRE");
					agc.start();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}

}
