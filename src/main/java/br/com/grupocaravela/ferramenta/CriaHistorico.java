package br.com.grupocaravela.ferramenta;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.objeto.Cheque;
import br.com.grupocaravela.objeto.Historico;
import br.com.grupocaravela.objeto.Usuario;

public class CriaHistorico {
	
	private EntityManager manager;
	private EntityTransaction trx;
	
	private Date data;
	private String operacao;
	private Usuario usuario;
	private Cheque cheque;
	private Historico historico;
		
	public Boolean criar(String opearacao, Usuario u, Cheque c){
		
		Boolean retorno = false;		
		
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
		
		historico = new Historico();
		
		historico.setCheque(c);
		historico.setUsuario(u);
		historico.setData(dataAtual());
		historico.setOperacao(opearacao);
		
		try {
			trx.begin();
			manager.persist(historico);
			trx.commit();
			
			retorno = true;
		} catch (Exception e) {
			retorno = false;
		}
		
		return retorno;
	}
	
	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;

	}

}
