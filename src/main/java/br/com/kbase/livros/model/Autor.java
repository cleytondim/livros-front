package br.com.kbase.livros.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Autor {

	private int id;
	
	private String nome;
	
	private Livro livro;
	
	public Autor() {}
	
	public Autor(String nome, Livro livro) {
		this.nome=nome;
		this.livro=livro;
	}
	
	public Autor(int id, String nome, Livro livro) {
		this.id=id;
		this.nome=nome;
		this.livro=livro;
	}
	
}
