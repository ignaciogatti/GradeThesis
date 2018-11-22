package test;

import java.util.List;

import restricciones.RestriccionesCorreMumTiposPsico;

import modelo.InteractuanId;
import modelo.RedSocial;
import modelo.Usuario;
import configuracion.DBConfigurador;

public class PruebaCorrelacion {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DBConfigurador conf = new DBConfigurador("C:\\Users\\gatti\\Desktop\\Ignacio\\Ing. Sistemas\\Proyecto Beca\\perfiles.csv");
		
		//Lista de usuarios para trabajar; es compartida por todas las restricciones
		List<Usuario> usuarios = conf.getUsuarios();
		
		System.out.println( usuarios.get(0).getCompetencias());


		
	}

}
