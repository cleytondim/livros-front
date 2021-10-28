package br.com.kbase.livros.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatadorData {
	
	private static FormatadorData instance = null;
	
	public static FormatadorData getInstance() {
		if (instance == null) {
			instance = new FormatadorData();
		}

		return instance;
	}
	
	private FormatadorData() {
		
	}
	
	public String getFormatoYMD(final Date data) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dataFormatada = dateFormat.format(data);
        return dataFormatada;
	}
	
	public Date getDataFromString(final String dataString) throws ParseException {
		
		Date data = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
		data = formato.parse(dataString);
		return data;
		
	}


}
