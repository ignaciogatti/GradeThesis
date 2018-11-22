package formacionDeGrupos;

import java.util.ArrayList;
import java.util.List;

import modelo.Estilo;
import modelo.Usuario;
import utilidades.Utilidades;

public class CorrelacionPerfiles {
	
	private List< Usuario > lusuarios;
	private Estilo[] estilo_ideal;
	private String nombre;
	
	
	
	public CorrelacionPerfiles(List< Usuario > lusuarios, Estilo[] estilo_ideal, String nombre) {

		this.lusuarios = lusuarios;
		this.estilo_ideal = estilo_ideal;
		this.nombre = nombre;
	}

	
	public List< Usuario > getUsuariosPorEstilo(){
		
		List< Usuario > resultado = new ArrayList<>();
		for( Usuario u : this.lusuarios ){
			boolean ideal = false;
			for( int i = 0; i < this.estilo_ideal.length && !ideal; i++ ){
				if( Utilidades.defineEstiloStandard(u).equals(this.estilo_ideal[i]) ){
					resultado.add(u);
					ideal = true;
				}
			}
		}
		return resultado;
	}

	public String toString(){
		
		return this.nombre;
	}

}
