package restricciones.formacionGrupos;

import java.util.ArrayList;
import java.util.List;

import modelo.Estilo;
import modelo.Usuario;

import utilidades.Utilidades;

public class CorrelacionPerfiles{
	
	private List< Usuario > lusuarios;
	private Estilo[] estilo_ideal;
	private boolean soft;
	private int costo;
	
	public CorrelacionPerfiles( Estilo[] estilo_ideal, boolean soft, int costo ){
		
		this.estilo_ideal = estilo_ideal;
		this.soft = soft;
		this.costo = costo;
	}
	
	public CorrelacionPerfiles( List< Usuario > lusuarios, Estilo[] estilo_ideal,
			boolean soft, int costo ) {

		this.lusuarios = lusuarios;
		this.estilo_ideal = estilo_ideal;
		this.soft = soft;
		this.costo = costo;
	}

	public boolean cumplePerfil( Usuario u){
		
		for( int i = 0; i < this.estilo_ideal.length ; i++ ){
			if( Utilidades.defineEstiloStandard(u).equals(this.estilo_ideal[i]) ){
				return true;
			}
		}
		return false;
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

	
	public List< Usuario > getUsuariosPorEstilo( List< Usuario > lusuarios){
		
		this.lusuarios = lusuarios;
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

	public boolean isSoft() {
		return soft;
	}

	public int getCosto() {
		return costo;
	}

	public void setCosto(int costo) {
		this.costo = costo;
	}

	public void setSoft(boolean soft) {
		this.soft = soft;
	}

	
}
