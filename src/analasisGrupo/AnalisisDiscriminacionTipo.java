package analasisGrupo;

import java.util.List;

import modelo.Grupo;
import modelo.Usuario;

public class AnalisisDiscriminacionTipo {
	
	private List< Usuario[] > tiposPorTipo;
	private String nombre;
	
	public AnalisisDiscriminacionTipo(List< Usuario[] > listasPorTipo, String nombre) {
		
		this.tiposPorTipo = listasPorTipo;
		this.nombre = nombre;
		
	}
	
	public String analizarGrupo( Grupo g ){

		System.out.println( g );
		String analisis = "Analisis "+ this.nombre + " \n";
		int i = 0;
		for( Usuario[] perfil : this.tiposPorTipo ){
			analisis += "Tipo "+ i + " \n";
			i++;
			int cantUsuarios = 0;
			for( int j = 0; j < perfil.length; j++ ){
				if ( g.getIntegrantes().contains( perfil[ j ] ) ){
					analisis += perfil[j] + " \n";
					cantUsuarios++;
				}
			}
			analisis += "Total: " + cantUsuarios + " \n";
			}
		return analisis;
	}

}
