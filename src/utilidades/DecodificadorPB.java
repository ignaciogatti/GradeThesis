package utilidades;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.sat4j.specs.IVec;

import restricciones.PaqueteRestricciones;

import modelo.ConjuntoGrupo;
import modelo.Grupo;
import modelo.Usuario;

public class DecodificadorPB {

	
	public static List<Usuario> decodificarPBSolucion(int[] sol, List<Usuario> usuarios){
		
		ArrayList<Usuario> resultado = new ArrayList<>(sol.length);
		for(int i = 0; i < sol.length; i++){
			if(sol[i] > 0){
				resultado.add(usuarios.get(i));
			}
		}
		
		return resultado;
	}
	
	 public static ConjuntoGrupo decodificarSolucion( IVec<Integer> sol, 
			 List<Usuario> listaUsuarios, int cantGrupos, int cantIntegrantes,
			 PaqueteRestricciones restricciones){

		 ConjuntoGrupo grupos = new ConjuntoGrupo();
		 ArrayList<Usuario> usuarios= new ArrayList<>();
			for (Iterator<Integer> it = sol.iterator(); it.hasNext();) {
				Integer index = it.next();
				if( ( index != null ) && 
						( index < ( listaUsuarios.size() * cantGrupos ) ) ){
					Usuario u = listaUsuarios.get( ( index % listaUsuarios.size() ) );
					usuarios.add(u);
				}
				
			}

		int index = 0;
		 for( int i = 0; i < cantGrupos; i++){
			 ArrayList<Usuario> grupo = new ArrayList<>();
			 for( int j = 0; j < cantIntegrantes; j++){
				 grupo.add(usuarios.get(index++));
			 }
			 
			 restricciones.reset();
			 grupos.addGrupo(new Grupo(grupo,
						Utilidades.calcularError(grupo, 
								BigInteger.valueOf( restricciones.getCosto( grupo, i ) ) ) ) );
		 }
		 grupos.calcPen();
		 return grupos;

	 }

}
