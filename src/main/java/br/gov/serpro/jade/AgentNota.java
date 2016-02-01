package br.gov.serpro.jade;

import br.gov.serpro.jade.behaviour.LerMessageBehaviour;
import jade.lang.acl.ACLMessage;
import openjade.core.OpenAgent;
import openjade.core.annotation.ReceiveSimpleMessage;

public class AgentNota extends OpenAgent {

	
	private static final long serialVersionUID = 1L;

	@Override
	public void setup() {
		super.setup();
		super.addBehaviour(new LerMessageBehaviour(this));
	}
	
	/**
	 * Trata pedido do vendedor
	 * @param msg
	 * @throws InterruptedException
	 */
	@ReceiveSimpleMessage(conversationId = "compra")
	public void getReceiveCompra(ACLMessage msg) throws InterruptedException {
		log("verificando nota fiscal...");
		Thread.sleep(5000);	
		log("Nota fiscal Ok!");
		
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.setConversationId("retorno-nota");
		message.setSender(this.getAID());
		message.addReceiver(msg.getSender());
		message.setContent("yes");
		this.send(message);
	}
	
	public void log(String msg) {
		System.out.println("\n");
		System.out.println(msg);
		System.out.println("\n");
	}
}
