package myfirstjade;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main {
	/**
	 *  Lanza la interfaz de gestión de los agentes. No importa si hay varias abiertas.
	 * @param ac
	 * @throws StaleProxyException
	 */
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
		p.setParameter("preload","agenta;agentb");
		p.setParameter(Profile.MAIN_PORT, "60000");		

		// Create a new non-main container, connecting to the default
		// main container (i.e. on this host, port 60000)
		jade.wrapper.AgentContainer acl = null;


		acl=rt.createMainContainer(p);

		launchGUI(acl);


		final jade.wrapper.AgentContainer ac=acl;	

		// if you want just to connect an existing container, run this
		//final jade.wrapper.AgentContainer ac = rt.createMainContainer(p);
		// Create a new agent
		final jade.wrapper.AgentController agenta = 
				ac.createNewAgent("agentea",
						"myfirstjade.AgentA", new Object[0]);	
		new Thread(){
			public void run(){
				try {
					System.out.println("Starting up AgentA");
					agenta.start();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}.start();
		
		final jade.wrapper.AgentController agentb = 
				ac.createNewAgent("agenteb",
						"myfirstjade.AgentB", new Object[0]);	
		new Thread(){
			public void run(){
				try {
					System.out.println("Starting up AgentB");
					agentb.start();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}.start();
		
		final jade.wrapper.AgentController sniffer = 
				ac.createNewAgent("sniffer",
						"jade.tools.sniffer.Sniffer", new Object[0]);	
		new Thread(){
			public void run(){
				try {
					System.out.println("Starting up AgentB");
					sniffer.start();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}


}
