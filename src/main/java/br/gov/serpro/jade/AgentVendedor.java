package br.gov.serpro.jade;

import javax.swing.JOptionPane;

import br.gov.serpro.jade.behaviour.LerMessageBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.lang.acl.ACLMessage;
import openjade.core.annotation.ReceiveSimpleMessage;

public class AgentVendedor extends Agent {

	private static final long serialVersionUID = 1L;

	public void setup() {
		super.addBehaviour(new LerMessageBehaviour(this));
	}

	private boolean cartaoOk = false;
	private boolean estoqueOK = false;
	private boolean notaOk = false;

	/**
	 * Trata pedido de movimentação 
	 * @param msg
	 */
	@ReceiveSimpleMessage(conversationId = "go")	
	public void getReceiveGo(ACLMessage msg) {
		log(msg.toString());
		log("Indo para (" + msg.getContent() + ")");
		doMove(new ContainerID(msg.getContent(), null));
	}

	/**
	 * Trata pedido de compra
	 * @param msg
	 */
	@ReceiveSimpleMessage(conversationId = "compra")
	public void getReceiveCompra(ACLMessage msg) {		
		log(msg.toString());
		int selectedOption = JOptionPane.showConfirmDialog(null, "Posso iniciar compra?", "Confirmação", JOptionPane.YES_NO_OPTION);
		if (selectedOption == JOptionPane.YES_OPTION) {
			log("Compra iniciada...");

			// Enviando mensagem para todos os agentes que atendem compra
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.setConversationId("compra");
			message.setSender(this.getAID());
			message.addReceiver(new AID("agenteCartao", false));
			message.addReceiver(new AID("agenteEstoque", false));
			message.addReceiver(new AID("agenteNota", false));
			this.send(message);
		}
	}
	
	/**
	 * Recebe confirmação do Cartão
	 * @param msg
	 */
	@ReceiveSimpleMessage(conversationId = "retorno-cartao")
	public void getReceiveRetornoCartao(ACLMessage msg) {
		log(msg.toString());
		this.cartaoOk = msg.getContent().equals("yes");
		verificaConclusao();	
	}
	
	/**
	 * Recebe confirmação da Nota Fiscal
	 * @param msg
	 */
	@ReceiveSimpleMessage(conversationId = "retorno-nota")
	public void getReceiveRetornoNota(ACLMessage msg) {
		log(msg.toString());
		this.notaOk = msg.getContent().equals("yes");
		verificaConclusao();	
	}
	
	/**
	 * Recebe confirmação do Estoque
	 * @param msg
	 */
	@ReceiveSimpleMessage(conversationId = "retorno-estoque")
	public void getReceiveRetornoEstoque(ACLMessage msg) {
		log(msg.toString());
		this.estoqueOK = msg.getContent().equals("yes");
		verificaConclusao();	
	}
	

	/**
	 * Verificar conclusão do processo de compra
	 */
	private void verificaConclusao() {
		if (this.cartaoOk && this.estoqueOK && this.notaOk){
			JOptionPane.showMessageDialog(null, "Compra Concluída com Sucesso \n Vou para o Container CT-038, Bye!");
			doMove(new ContainerID("CT-038", null));
		}		
	}

	public void log(String msg) {
		System.out.println("\n");
		System.out.println(msg);
		System.out.println("\n");
	}

}
