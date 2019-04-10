package myfirstjade;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Location;
import jade.core.PlatformID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MyFirstJADEAgent extends Agent {

	public MyFirstJADEAgent() {
		
	}
	
	public boolean migrate=false;
	
	@Override
	protected void setup() {		
		super.setup();
		JFrame jf=new JFrame();
		JButton jb=new JButton("migrate");
		jf.getContentPane().add(jb);
		jf.pack();
		jf.setVisible(true);
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){	
				MyFirstJADEAgent.this.addBehaviour(new OneShotBehaviour()	{
					@Override
					public void action(){
						ContainerID destination=new ContainerID();
						destination.setName("Container-1");
						doMove(destination);						
					}
				});
			}
		});
		TickerBehaviour tb=new TickerBehaviour(this,1000){
			@Override
			protected void onTick() {
				System.out.println("Hello!");
			}			
		};
		this.addBehaviour(tb);		

		JFrame dialog=new JFrame();

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
		p.setParameter("preload","a*");
		p.setParameter(Profile.LOCAL_PORT, "60000");		
		p.setParameter(Profile.MAIN_PORT, "60000");		
		p.setParameter(Profile.LOCAL_HOST, "147.96.80.91");		
		p.setParameter(Profile.MAIN_HOST, "147.96.80.91");		

		// Create a new non-main container, connecting to the default
		// main container (i.e. on this host, port 60000)
		final jade.wrapper.AgentContainer ac = rt.createMainContainer(p);
		
		launchGUI(ac);
		
		// if you want just to connect an existing container, run this
		//final jade.wrapper.AgentContainer ac = rt.createMainContainer(p);
		// Create a new agent
		final jade.wrapper.AgentController agc = 
				ac.createNewAgent("MyFirstJADEAgent",
						"myfirstjade.MyFirstJADEAgent", new Object[0]);	
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
