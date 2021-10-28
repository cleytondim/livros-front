package br.com.kbase.livros.model;

import java.util.ArrayList;
import java.util.List;

import br.com.kbase.livros.util.CurrencyWriter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Livro implements Serializable{

	private String isbn;
	private String titulo;
	private List<Autor> autores = new ArrayList<>();
	private String editora;
	private String datalancamento;
	private float preco;
	private List<Critica> criticas = new ArrayList<>();
	
	public Livro() {}
	
	public Livro(String isbn, String titulo, List<Autor> autores, String editora, String datalancamento, float preco, List<Critica> criticas) {
		this.isbn=isbn;
		this.titulo=titulo;
		if(autores!=null)
		{
			this.autores.clear();
			this.autores.addAll(autores);
		}
		else
			this.autores=new ArrayList<>();
		this.editora = editora;
		this.datalancamento=datalancamento;
		this.preco=preco;
		if(criticas!=null)
		{
			this.criticas.clear();
			this.criticas.addAll(criticas);
		}
		else
			this.criticas=new ArrayList<>();
	}
	
	public String getPrecoReaisExtenso() {
		CurrencyWriter cw = CurrencyWriter.getInstance();
		return cw.write(new BigDecimal(this.preco));
	}
	
	public String getNomeAutores() {
		String nomes = "";
		if(autores==null||autores.isEmpty())
			return nomes;
		
		for (Autor autor : autores) {
			nomes=nomes+"  "+autor.getNome()+"  |";
		}
		nomes=nomes.substring(0, nomes.length()-1);
		return nomes;
	}
	
	public String getTextosCriticas() {
		String textos = "";
		if(criticas==null||criticas.isEmpty())
			return textos;
		
		for (Critica critica : criticas) {
			textos=textos+"  "+critica.getCritica()+"  |";
		}
		textos=textos.substring(0, textos.length()-1);
		return textos;
	}
	
	
}
