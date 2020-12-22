package br.com.jopaulo.controle.equipamento.model;

public enum Situacao {

	EM_ABERTO("Em aberto"), 
	RESOLVIDO("Resolvido"), 
	NAO_RESOLVIDO("Não resolvido");

	private String nome;
	
//	private String valor;

	private Situacao(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

//	public String getValor() {
//		return valor = this.name();
//	}
//
//	public void setValor(String valor) {
//		this.valor = valor;
//	}
	
	@Override
	public String toString() {
		return this.name();
	}
}
