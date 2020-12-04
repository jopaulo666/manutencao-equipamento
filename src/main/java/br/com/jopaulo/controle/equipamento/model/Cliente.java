package br.com.jopaulo.controle.equipamento.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "O nome do cliente é obrigatório")
	private String nome;

	private String endereco;

	@NotEmpty(message = "O telefone do cliente é obrigatório")
	private String telefone;

	@NotEmpty(message = "O e-mail do cliente é obrigatório")
	private String email;

	@NotEmpty(message = "O equipamento do cliente é obrigatório")
	private String equipamento;

	@NotEmpty(message = "O problema do cliente é obrigatório")
	private String problema;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date entrada;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date saida;
	
	private String situacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(String equipamento) {
		this.equipamento = equipamento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getProblema() {
		return problema;
	}

	public void setProblema(String problema) {
		this.problema = problema;
	}

	public Date getEntrada() {
		return entrada;
	}

	public void setEntrada(Date entrada) {
		this.entrada = entrada;
	}

	public Date getSaida() {
		return saida;
	}

	public void setSaida(Date saida) {
		this.saida = saida;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

}
