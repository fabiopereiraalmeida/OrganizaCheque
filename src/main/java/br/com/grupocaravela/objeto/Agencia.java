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
@Table(name="agencia")
public class Agencia implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String cod;
	private String nome;
	private String telefone;
	private String endereco;
	private String enderecoNumero;
	private String bairro;
	private String cidade;
	private String uf;
	private String cep;
	private Banco banco;
	private List<Conta> contas = new ArrayList<>();
	private List<Cheque> cheques = new ArrayList<>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "cod", nullable = false, length = 10)
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
			
	@Column(name = "nome", length = 30)
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Column(name = "telefone", length = 15)
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
	
	@Column(name = "bairro", length = 50)
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	@Column(name = "cidade", length = 50)
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
	
	@ManyToOne
	@JoinColumn(name = "banco_id", nullable = false)
	public Banco getBanco() {
		return banco;
	}
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	
	@OneToMany(mappedBy = "agencia", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Conta> getContas() {
		return contas;
	}
	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}
	
	@OneToMany(mappedBy = "agencia", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Cheque> getCheques() {
		return cheques;
	}
	public void setCheques(List<Cheque> cheques) {
		this.cheques = cheques;
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
		Agencia other = (Agencia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
			
	public String toString(){
        return "Cod. " + this.cod + " - " + banco.getNome();
    }
	
}
