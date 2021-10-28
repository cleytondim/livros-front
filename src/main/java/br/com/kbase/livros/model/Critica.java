package br.com.kbase.livros.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Critica {

	private int id;
	
	private String critica;
	
	private Livro livro;
	
	public Critica() {}
	
	public Critica(String critica, Livro livro) {
		this.critica=critica;
		this.livro=livro;
	}
	
	public Critica(int id, String critica, Livro livro) {
		this.id=id;
		this.critica=critica;
		this.livro=livro;
	}
}
