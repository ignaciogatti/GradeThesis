package restricciones.formacionGrupos;

import java.util.ArrayList;
import java.util.List;

import modelo.Usuario;
import utilidades.filtros.Ifiltro;

public class CorrelacionRol {

	private  Ifiltro rol;
	private List< Usuario > lusuarios;
	private boolean soft;
	private int costo;
	
	
	public CorrelacionRol(Ifiltro rol, boolean soft, int costo) {
		
		this.rol = rol;
		this.soft = soft;
		this.costo = costo;
	}
	
	public CorrelacionRol(Ifiltro rol, List<Usuario> lusuarios, boolean soft, int costo) {

		this.rol = rol;
		this.lusuarios = lusuarios;
		this.soft = soft;
		this.costo = costo;
	}


	public boolean cumpleRol( Usuario u){
		
		if( rol.cumple( u ) ) return true;
		
		return false;
	}

	public List< Usuario > getUsuariosPorRol(){
		
		List< Usuario > resultado = new ArrayList<>();
		for( Usuario u : this.lusuarios ){
			if( rol.cumple(u)){
				resultado.add(u);
			}
		}
		return resultado;
	}

	public List< Usuario > getUsuariosPorRol( List< Usuario > lusuarios){
		
		this.lusuarios = lusuarios;
		List< Usuario > resultado = new ArrayList<>();
		for( Usuario u : this.lusuarios ){
			if( rol.cumple(u)){
				resultado.add(u);
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
