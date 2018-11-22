package modelo.formacionGrupos;

import java.util.HashMap;

public class CompetenciasTecnicas {

	private HashMap<String, Integer> competencias;
	
	
	public CompetenciasTecnicas(){
		
		this.competencias = new HashMap<>();
	}
	
	public void agregarCompetencia( String nombre, Integer nivel ){
		
		this.competencias.put( nombre, nivel );
	}
	
	public Integer getValor( String nombre ){
		 
		if( this.competencias.containsKey( nombre ) ){
			return this.competencias.get( nombre );
		}
		return -1;
	}
	
	public String toString(){
		
		return this.competencias.toString();
	}
}
