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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "banco")
public class Banco implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String cod;
	private String nome;
	private List<Agencia> agencias = new ArrayList<>();
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
	
	@Column(name = "nome", nullable = false, length = 60)
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@OneToMany(mappedBy = "banco", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Agencia> getAgencias() {
		return agencias;
	}
	public void setAgencias(List<Agencia> agencias) {
		this.agencias = agencias;
	}
	
	@OneToMany(mappedBy = "banco", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Conta> getContas() {
		return contas;
	}
	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}
	
	@OneToMany(mappedBy = "banco", cascade = CascadeType.ALL, orphanRemoval = true)
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
		Banco other = (Banco) obj;
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
