package br.com.grupocaravela.objeto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="historico")
public class Historico implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Date data;
	private String operacao;
	private String observacao;
	private Usuario usuario;
	private Cheque cheque;
	private Destinatario destinatario;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	
	@Column(name = "operacao", length = 150)
	public String getOperacao() {
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	
	@Column(name = "obsercacao", length = 220)
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@ManyToOne
	@JoinColumn(name = "cheque_id", nullable = false)
	public Cheque getCheque() {
		return cheque;
	}
	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}
	
	@ManyToOne
	@JoinColumn(name = "destinatario_id", nullable = false)
	public Destinatario getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(Destinatario destinatario) {
		this.destinatario = destinatario;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Historico other = (Historico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
