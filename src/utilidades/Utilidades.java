package utilidades;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import constantes.Constantes;

import modelo.Estilo;
import modelo.Grupo;
import modelo.SolucionPB;
import modelo.Usuario;

public class Utilidades {

		
	public static List<Grupo> decodificarPBSolucion(ArrayList<SolucionPB> SolucionesSatisfacibles,
			List<Usuario> usuarios){
		
		ArrayList<Grupo> grupos = new ArrayList<>();
		for(SolucionPB sol : SolucionesSatisfacibles){
			ArrayList<Usuario> grupo = new ArrayList<>();
			for(int i = 0; ( ( i < sol.getUsuarios().length) && (i < usuarios.size()) ); i++){
				if(sol.getUsuarios()[i] > 0){
					grupo.add(usuarios.get(i));
				}
			}
			Grupo g = new Grupo(grupo, 
					Utilidades.calcularError(grupo, BigInteger.valueOf( sol.getCosto() ) ) );
			System.out.println(g);
			grupos.add(g);
		}
		
		return grupos;
	}
	
	public static double calcularError(List<Usuario> grupo, BigInteger l){
		
		int sumatoria = 0;
		for (int i =1; i<grupo.size(); i++) 
			sumatoria += grupo.size()-i;
		double error=(l.doubleValue())/(grupo.size()*(Constantes.PENALIZACION_BALANCEO + Constantes.PENALIZACION_CORRELACION) + sumatoria);
		return error;

	}

	public static Estilo defineEstiloStandard(Usuario u){
		
		Estilo tipo = new Estilo(getMyersChoice(u.getEstilo().getAttitude()),
								 getMyersChoice(u.getEstilo().getPerceiving()),
								 getMyersChoice(u.getEstilo().getJudging()),
								 getMyersChoice(u.getEstilo().getLifestyle()));
		return tipo;
	}



	private static int getMyersChoice(int valor){
		if (valor >=0)
			return 0;
		return 1;
	}
	
}
