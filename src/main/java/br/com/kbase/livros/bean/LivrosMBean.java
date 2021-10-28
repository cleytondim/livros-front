package br.com.kbase.livros.bean;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.primefaces.PrimeFaces;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.kbase.livros.model.Autor;
import br.com.kbase.livros.model.Critica;
import br.com.kbase.livros.model.Livro;
import br.com.kbase.livros.util.CurrencyWriter;
import br.com.kbase.livros.util.FormatadorData;
import lombok.Getter;
import lombok.Setter;


@Named
@ViewScoped
@Getter
@Setter
public class LivrosMBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String URL = "http://localhost:8080/livros";
	
	private Livro livro = new Livro();
	private List<Livro> livros = new ArrayList<>();
	private Date datatemp = new Date();
	private List<Autor> autorestemp = new ArrayList<>();
	private Autor autortemp = new Autor();
	private List<Critica> criticastemp = new ArrayList<>();
	private Critica criticatemp = new Critica();
	CurrencyWriter cw = CurrencyWriter.getInstance();
	FormatadorData formatadorData = FormatadorData.getInstance();
	private String isbnSearch;
	private String autorSearch;
	
	@PostConstruct
	public void init() {
	    this.livros = atualizaLivros();
	    datatemp = new Date();
	}
	
    public LivrosMBean() {
		livro = new Livro();
		livros=atualizaLivros();
		datatemp = new Date();
		autorestemp = new ArrayList<>();
		criticastemp = new ArrayList<>();
	}
	
	public void openNew() {
        this.livro = new Livro();
        autorestemp = new ArrayList<>();
        criticastemp = new ArrayList<>();
        openNewAutores();
        openNewCriticas();
        PrimeFaces.current().ajax().update("form:manage-book-content");
    }
	
	public void openAtualiza() {
        autorestemp = new ArrayList<>();
        criticastemp = new ArrayList<>();
        openNewAutores();
        openNewCriticas();
        PrimeFaces.current().ajax().update("form:manage-book-content-edit");
    }
	
	public void openNewAutores() {
		autortemp = new Autor();
		autorestemp = new ArrayList<>();
		autorestemp.addAll(livro.getAutores());
		PrimeFaces.current().ajax().update("form:input-autor-dialog");
    }
	
	public void openNewCriticas() {
		criticatemp = new Critica();
		criticastemp = new ArrayList<>();
		criticastemp.addAll(livro.getCriticas());
		PrimeFaces.current().ajax().update("form:input-autor-dialog");
	}
	
	public void openBiblioteca() {
		livros=new ArrayList<>();
		livros=atualizaLivros();
		PrimeFaces.current().ajax().update("form:dt-livros");
	}
	
	
	public List<Livro> atualizaLivros() {
		List<Livro>livrosAtualizados = new ArrayList<>();
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		Response response = target.request().get();
		String json = response.readEntity(String.class);
		response.close();
		
		Type listType = new TypeToken<ArrayList<Livro>>(){}.getType();
		livrosAtualizados = new Gson().fromJson(json, listType);
		return livrosAtualizados;
	}
	
	public void buscarLivroPorIsbn() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL+"/"+isbnSearch);
		Response response = target.request().get();
		String json = response.readEntity(String.class);
		response.close();
		
		livro = new Gson().fromJson(json, Livro.class);
		livros=new ArrayList<>();
		livros.add(livro);
		
		if(response.getStatus()!=200)
			addMessage(FacesMessage.SEVERITY_ERROR, "Ocorrencia de Erro", "Um erro nao especificado ocorreu");
		else {
			addMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Livro Encontrado");
		}
		
		PrimeFaces.current().ajax().update("form:messages", "form", "form:dt-livros");
	
	}
	
	public void buscarLivroPorAutor() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL+"/autor/"+autorSearch);
		Response response = target.request().get();
		String json = response.readEntity(String.class);
		response.close();
		
		livros=new ArrayList<>();
		Type listType = new TypeToken<ArrayList<Livro>>(){}.getType();
		livros = new Gson().fromJson(json, listType);
		if(response.getStatus()!=200)
			 addMessage(FacesMessage.SEVERITY_ERROR, "Ocorrencia de Erro", "Um erro nao especificado ocorreu");
		else {
			addMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Listando livros");
		}
		
		PrimeFaces.current().ajax().update("form:messages", "form", "form:dt-livros");
	
	}
	
	public void cadastraLivro() {		
		
		
		livro.setDatalancamento(formatadorData.getFormatoYMD(datatemp));
		livro.setAutores(autorestemp);
		livro.setCriticas(criticastemp);
		Gson gson = new Gson();
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		Entity<String> dadospost = javax.ws.rs.client.Entity.json(gson.toJson(livro));
		Response response = target.request().post(dadospost);
		String json = response.readEntity(String.class);
		if(response.getStatus()!=201)
			 addMessage(FacesMessage.SEVERITY_ERROR, "Ocorrencia de Erro", json);
		else {
			addMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", json);
			livros.add(livro);
			PrimeFaces.current().executeScript("PF('manageBookDialog').hide()");
		}
		response.close();
		autorestemp=new ArrayList<>();
		criticastemp=new ArrayList<>();
		
		PrimeFaces.current().ajax().update("form:messages", "form:dt-livros");
        
		
	}
	
	
	public void atualizaLivro() {		
		
		
		livro.setDatalancamento(formatadorData.getFormatoYMD(datatemp));
		livro.setAutores(autorestemp);
		livro.setCriticas(criticastemp);
		Gson gson = new Gson();
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL+"/"+livro.getIsbn());
		Entity<String> dadosput = javax.ws.rs.client.Entity.json(gson.toJson(livro));
		Response response = target.request().put(dadosput);
		String json = response.readEntity(String.class);
		if(response.getStatus()!=200)
			if(response.getStatus()!=404)
			 addMessage(FacesMessage.SEVERITY_ERROR, "Ocorrencia de Erro", "Um erro nao especificado ocorreu");
			else
				addMessage(FacesMessage.SEVERITY_ERROR, "Ocorrencia de Erro", json);
		else {
			addMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", json);
			PrimeFaces.current().executeScript("PF('manageBookDialogEdit').hide()");
		}
		response.close();
		autorestemp=new ArrayList<>();
		criticastemp=new ArrayList<>();
		
		PrimeFaces.current().ajax().update("form:messages", "form:dt-livros");
        
		
	}
	
	
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
	
	
	public void removeLivro() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL+"/"+livro.getIsbn());
		Response response = target.request().delete();
		response.close();
		
		if(response.getStatus()!=204)
			if(response.getStatus()==404)
				addMessage(FacesMessage.SEVERITY_ERROR, "Ocorrencia de Erro", "Livro nao encontrado para exclusao");
			else
			 addMessage(FacesMessage.SEVERITY_ERROR, "Ocorrencia de Erro", "Um erro nao especificado ocorreu");
		else {
			addMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Livro removido com sucesso");
			livros.remove(livro);
		}
			
		
		PrimeFaces.current().ajax().update("form:messages", "form:dt-livros");
	}
	
	public void adicionaAutorTemporario(){
		autorestemp.add(autortemp);
		autortemp = new Autor();
		PrimeFaces.current().ajax().update("form:manage-book-content", "form:manage-book-content-edit");
	}
	
	
	public void removerAutorTemporario() {
		autorestemp.remove(autortemp);
		PrimeFaces.current().ajax().update("form:manage-book-content", "form:manage-book-content-edit");
	}
	
	
	public void adicionaCriticaTemporaria(){
		criticastemp.add(criticatemp);
		criticatemp = new Critica();
		PrimeFaces.current().ajax().update("form:manage-book-content", "form:manage-book-content-edit");
	}
	
	
	public void removerCriticaTemporaria() {
		criticastemp.remove(criticatemp);
		PrimeFaces.current().ajax().update("form:manage-book-content", "form:manage-book-content-edit");
	}
	
	
}
