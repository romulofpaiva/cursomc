package com.nelioalves.cursomc.domains.enums;

public enum EstadoPagamento {

	PENDENTE(1, "Pendente"),
	QUITADO(2, "Quitado"),
	CANCELADO(3, "Cancelado");
	
	private Integer cod;
	private String descricao;

	EstadoPagamento(Integer cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public static EstadoPagamento toEnum( Integer cod ) {
		for( EstadoPagamento estado : EstadoPagamento.values() ) {
			if( estado.getCod().equals( cod ) )
				return estado;
		}
		
		throw new IllegalArgumentException( "Código inválido: " + cod );
	}

	public Integer getCod() {
		return cod;
	}

	public void setCod(Integer cod) {
		this.cod = cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
