package parserArchivoGrupos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import modelo.ConjuntoGrupo;
import modelo.Grupo;
import modelo.Usuario;

import com.csvreader.CsvReader;

public class ParserArchgrupos {

	private CsvReader reader;
	private ConjuntoGrupo grupos;
	int cantgrupos = 0;
	int cantIngegrantes = 0;
	
	public ParserArchgrupos( String path ) {

		this.grupos = new ConjuntoGrupo();
		try {
			Charset charset = Charset.forName("UTF-8");
			reader = new CsvReader(path,';',charset);
			parsear();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public ConjuntoGrupo getGrupos(){
		
		return this.grupos;
	}
	
	public int getCantGrupos(){
		
		return this.cantgrupos;
	}
	
	public int getCantIntegrantes(){
		
		return this.cantIngegrantes;
	}
	
	private void parsear() throws IOException {

	    this.cantgrupos = 0;
		while (reader.readRecord()) {
			String[] record = reader.getValues();
			Grupo g = this.getGrupo( record );
			g.setId( this.cantgrupos++ );
			this.grupos.addGrupo( g );
		}
		this.cantIngegrantes = this.grupos.getGrupos().get(0).getIntegrantes().size();

	}

	private Grupo getGrupo( String[] records ){
		
		Grupo g = new Grupo();
		for( int i = 0; i < (records.length -1) ; i++ ){
			g.addIntegrante( this.getUsuario( records[i] ));
		}
		g.setPenalizacion( Double.parseDouble( records[ records.length -1 ] ) );
		return g;
	}

	
	private Usuario getUsuario( String record ){
		
		Usuario u = new Usuario();
		int del = record.indexOf("]");
		u.setId( Integer.parseInt( record.substring(1, del ) ) );
		u.setNombre( record.substring( del + 1) );
		return u;
	}
}
