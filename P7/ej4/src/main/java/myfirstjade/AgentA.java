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

public class AgentA extends Agent {

	public AgentA() {

	}


	/**
	 * Este metodo lo usa el framework JADE para inicializar el agente.
	 * El comportamiento esperado del agente consiste en:
	 * - preparar un mensaje a enviar
	 * - esperar la respuesta
	 * - mostrar en la salida de comandos la respuesta
	 * Cuando termine esta secuencia, el agente no hara nada
	 */
	@Override
	protected void setup() {		
		super.setup();
		
		JFrame dialog=new JFrame();
		JButton arranque=new JButton("start");
		dialog.getContentPane().add(arranque);
		arranque.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// metemos los comportamientos en un mtodo por si se
				// necesita repetir la secuencia en alguna otra circunstancia
				// o se quiere invocar este mtodo desde un GUI para arrancar
				// el trabajo.
				createBehaviors();
				
			}
		});
		dialog.pack();
		dialog.setVisible(true);
		

	}



	private void createBehaviors() {
		// esto es un container donde vamos metiendo los comportamientos. Los comportamientos
		// que se agreguen sern ejecutados secuencialmente 
		SequentialBehaviour sb=new SequentialBehaviour();

		// Prepara el mensaje
		ACLMessage message=new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(new AID("agenteb",false));
		message.setContent("Hola?");

		// dice al agente que prepare el comportamiento para enviarlo
		jade.core.behaviours.SenderBehaviour senderBehavior=new jade.core.behaviours.SenderBehaviour(this,message);

		// crea una plantilla que expresa como es el mensaje esperado como respuesta. Puede aludir al contenido o a cualesquiera
		// campos del mensaje
		MessageTemplate.MatchExpression me = new MessageTemplate.MatchExpression() {
			public boolean match(ACLMessage mes) {
				// Pide que el mensaje tenga contenido y que venga del agenteb
				return mes.getContent()!=null && 
						mes.getSender().getLocalName().equals("agenteb");
			};
		};

		// crea un comportamiento que usa la plantilla para identificar el mensaje esperado
		// y procesarlo de forma acorde. El comportamiento no termina hasta quese recibe este mensaje.
		final ReceiverBehaviour rb=new ReceiverBehaviour(this,-1,new MessageTemplate(me));

		// ahora otro comportamiento que tomara la informacin del mensaje y har algo simple, como
		// mostrarlo en pantalla
		OneShotBehaviour processMessage=new OneShotBehaviour() {	
			@Override
			public void action() {
				ACLMessage messageReceived;
				try {
					messageReceived = rb.getMessage();
					System.out.println(getAID()+":Recibido mensaje:" +messageReceived.getContent());
				} catch (TimedOut e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotYetReady e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		};

		// ahora agregamos todo en el orden en que queremos que se ejecute. SB
		// es un sequential behavior, por lo que se meter en cada comportamiento
		// y, en el caso de la recepcin del mensaje, no reanudar hasta que se
		// reciba.

		sb.addSubBehaviour(senderBehavior);

		sb.addSubBehaviour(rb);

		sb.addSubBehaviour(processMessage);

		this.addBehaviour(sb);		

	}



}
