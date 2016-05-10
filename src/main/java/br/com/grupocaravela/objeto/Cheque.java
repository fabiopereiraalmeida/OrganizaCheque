package br.com.grupocaravela.objeto;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "cheque")
public class Cheque implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String codCheque;
	private String numCheque;
	private String codVenda;
	private Double valor;
	private Date dataVencimento;
	private Date dataEntrada;
	private String observacao;
	private Boolean voltouUmaVez;
	private Boolean voltouDuasVezes;
	private Boolean comProblemas;
	private Boolean terceiros;
	private Boolean selecionado;
	//private Boolean ativo;
	private Banco banco;
	private Agencia agencia;
	private Conta conta;
	private Proprietario proprietario;
	private Destinatario destinatario;
	
	private Double juros;
	//private Double dias;
	private Double lucro;
	private Double valorPago;
	//private Destinatario destinatarioFilho;
	private List<Historico> historicos = new ArrayList<>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "cod_cheque", nullable = false, length = 50)
	public String getCodCheque() {
		return codCheque;
	}
	public void setCodCheque(String codCheque) {
		this.codCheque = codCheque;
	}
	
	@Column(name = "num_cheque", length = 10)	
	public String getNumCheque() {
		return numCheque;
	}
	public void setNumCheque(String numCheque) {
		this.numCheque = numCheque;
	}
	@Column(name = "cod_venda", length = 10)
	public String getCodVenda() {
		return codVenda;
	}
	public void setCodVenda(String codVenda) {
		this.codVenda = codVenda;
	}
		
	@Column(name = "valor", precision=11, scale=2, nullable = false)
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "vencimento", nullable = false)
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_entrada", nullable = false)
	public Date getDataEntrada() {
		return dataEntrada;
	}
	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}
	
	@Column(name = "observacao", length = 120)
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}	
	
	@Column(name = "voltou_uma_vez")
	public Boolean getVoltouUmaVez() {
		return voltouUmaVez;
	}
	public void setVoltouUmaVez(Boolean voltouUmaVez) {
		this.voltouUmaVez = voltouUmaVez;
	}
	
	@Column(name = "voltou_duas_vezes")
	public Boolean getVoltouDuasVezes() {
		return voltouDuasVezes;
	}
	public void setVoltouDuasVezes(Boolean voltouDuasVezes) {
		this.voltouDuasVezes = voltouDuasVezes;
	}
	
	@Column(name = "com_problemas")
	public Boolean getComProblemas() {
		return comProblemas;
	}
	public void setComProblemas(Boolean comProblemas) {
		this.comProblemas = comProblemas;
	}
		
	@Column(name = "terceiros")
	public Boolean getTerceiros() {
		return terceiros;
	}
	public void setTerceiros(Boolean terceiros) {
		this.terceiros = terceiros;
	}	
	
	@Column(name = "selecionado")
	public Boolean getSelecionado() {
		return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
/*		
	@Column(name = "ativo")
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
*/	
	@ManyToOne
	@JoinColumn(name = "banco_id", nullable = false)
	public Banco getBanco() {
		return banco;
	}
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	
	@ManyToOne
	@JoinColumn(name = "agencia_id", nullable = false)
	public Agencia getAgencia() {
		return agencia;
	}
	public void setAgencia(Agencia agencia) {
		this.agencia = agencia;
	}
	
	@ManyToOne
	@JoinColumn(name = "conta_id", nullable = false)
	public Conta getConta() {
		return conta;
	}
	public void setConta(Conta conta) {
		this.conta = conta;
	}
	
	@ManyToOne
	@JoinColumn(name = "proprietario_id", nullable = false)
	public Proprietario getProprietario() {
		return proprietario;
	}
	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}
		
	@ManyToOne
	@JoinColumn(name = "destinatario_id", nullable = false)
	public Destinatario getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(Destinatario destinatario) {
		this.destinatario = destinatario;
	}
	
	/*
	@ManyToOne
	@JoinColumn(name = "destinatario_filho_id")	
	public Destinatario getDestinatarioFilho() {
		return destinatarioFilho;
	}
	public void setDestinatarioFilho(Destinatario destinatarioFilho) {
		this.destinatarioFilho = destinatarioFilho;
	}
	*/
	
	@Column(name = "juros", precision=11, scale=2)
	public Double getJuros() {
		return juros;
	}
	public void setJuros(Double juros) {
		this.juros = juros;
	}
	/*
	@Column(name = "dias", precision=11, scale=2)
	public Double getDias() {
		return dias;
	}
	public void setDias(Double dias) {
		this.dias = dias;
	}
	*/
	@Column(name = "lucro", precision=11, scale=2)
	public Double getLucro() {
		return lucro;
	}
	public void setLucro(Double lucro) {
		this.lucro = lucro;
	}
	
	@Column(name = "valor_pago", precision=11, scale=2)
	public Double getValorPago() {
		return valorPago;
	}
	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}
	
	
	@OneToMany(mappedBy = "cheque", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Historico> getHistoricos() {
		return historicos;
	}
	
	public void setHistoricos(List<Historico> historicos) {
		this.historicos = historicos;
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
		Cheque other = (Cheque) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
