package br.com.grupocaravela.objeto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="destinatario")
public class Destinatario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String observacao;
	private String telefone;
	private String endereco;
	private String enderecoNumero;
	private String bairro;
	private String cidade;
	private String uf;
	private String cep;
	private Boolean local;
	private Boolean filho;
	private Destinatario destinatario;
	private List<Cheque> cheques = new ArrayList<>();
	private List<Historico> historico = new ArrayList<>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "nome", nullable = false, length = 60)
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Column(name = "observacao", length = 80)
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	@Column(name = "telefone", length = 20)
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	@Column(name = "endereco", length = 80)
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	@Column(name = "endereco_numero", length = 6)
	public String getEnderecoNumero() {
		return enderecoNumero;
	}
	public void setEnderecoNumero(String enderecoNumero) {
		this.enderecoNumero = enderecoNumero;
	}
	
	@Column(name = "bairro", length = 60)
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	@Column(name = "cidade", length = 60)
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	@Column(name = "uf", length = 2)
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	
	@Column(name = "cep", length = 15)
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
			
	public Boolean getLocal() {
		return local;
	}
	public void setLocal(Boolean local) {
		this.local = local;
	}
	
	public Boolean getFilho() {
		return filho;
	}
	public void setFilho(Boolean filho) {
		this.filho = filho;
	}
	
	@ManyToOne
	@JoinColumn(name = "destinatario_id")
	public Destinatario getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(Destinatario destinatario) {
		this.destinatario = destinatario;
	}
	
	@OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL, orphanRemoval = true)	
	public List<Cheque> getCheques() {
		return cheques;
	}
	public void setCheques(List<Cheque> cheques) {
		this.cheques = cheques;
	}
		
	@OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL, orphanRemoval = true)	
	public List<Historico> getHistorico() {
		return historico;
	}
	public void setHistorico(List<Historico> historico) {
		this.historico = historico;
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
		Destinatario other = (Destinatario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String toString(){
        return this.nome;
    }

}
