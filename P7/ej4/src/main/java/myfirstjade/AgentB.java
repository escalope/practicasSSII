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

public class AgentB extends Agent {

	public AgentB() {

	}


	/**
	 * Este método lo usa el framework JADE para inicializar el agente.
	 * El comportamiento esperado del agente consiste en:
	 * - esperar un mensaje del agente A
	 * - elaborar una respuesta y enviarla
	 * Cuando termine esta secuencia, el agente no hará nada
	 */
	@Override
	protected void setup() {		
		super.setup();

		// metemos los comportamientos en un método por si se
		// necesita repetir la secuencia en alguna otra circunstancia
		// o se quiere invocar este método desde un GUI para arrancar
		// el trabajo.
		createBehaviors();

	}



	private void createBehaviors() {
		// esto es un container donde vamos metiendo los comportamientos. Los comportamientos
		// que se agreguen serán ejecutados secuencialmente 
		final SequentialBehaviour sb=new SequentialBehaviour();

		// crea una plantilla que expresa como es el mensaje esperado como respuesta. Puede aludir al contenido o a cualesquiera
		// campos del mensaje
		MessageTemplate.MatchExpression me = new MessageTemplate.MatchExpression() {
			public boolean match(ACLMessage mes) {
				// Pide que el mensaje tenga contenido y que venga del agentea
				return mes.getContent()!=null && 
						mes.getSender().getLocalName().equals("agentea");
			};
		};

		// crea un comportamiento que usa la plantilla para identificar el mensaje esperado
		// y procesarlo de forma acorde. El comportamiento no termina hasta quese recibe este mensaje.
		final ReceiverBehaviour rb=new ReceiverBehaviour(this,-1,new MessageTemplate(me));

		// ahora otro comportamiento que tomará la información del mensaje y hará algo simple, como
		// mostrarlo en pantalla
		OneShotBehaviour processMessage=new OneShotBehaviour() {	
			@Override
			public void action() {
				ACLMessage messageReceived;
				try {
					messageReceived = rb.getMessage();
					System.out.println(getAID()+": Recibido mensaje:" +messageReceived.getContent());

					// Prepara el mensaje de respuesta. Se puede crear "a pelo", pero se
					// ahorra trabajo si dejamos que JADE cree el mensaje por defecto.
					ACLMessage respuesta = messageReceived.createReply();
					respuesta.setContent("Muy bien. Gracias por preguntar. Me llamo "+getAID());

					// dice al agente que prepare el comportamiento para enviarlo
					jade.core.behaviours.SenderBehaviour senderBehavior=new jade.core.behaviours.SenderBehaviour(this.myAgent,respuesta);
					// y desde un comportamiento, modificamos el container original para agregar el envío. 
					sb.addSubBehaviour(senderBehavior);
					// para terminar, creamos otra vez todos los comportamientos para seguir respondiendo más
					// mensajes. Si no lo hiciéramos, este terminaría.
					createBehaviors();

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
		// es un sequential behavior, por lo que se meterá en cada comportamiento
		// y, en el caso de la recepción del mensaje, no reanudará hasta que se
		// reciba.

		sb.addSubBehaviour(rb);

		sb.addSubBehaviour(processMessage);

		this.addBehaviour(sb);		

	}


}
