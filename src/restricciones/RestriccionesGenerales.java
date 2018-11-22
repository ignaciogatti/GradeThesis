package restricciones;

import java.math.BigInteger;
import java.util.List;

import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.pb.tools.WeightedObject;
import org.sat4j.specs.ContradictionException;

import constantes.VariablesSistema;

import solvers.PseudoBoolean.otroPB.ProblemDefinition;

/*
 * Restriccion cantidad de integrantes en el grupo
 * x1 + ... + xi + xn = Cantidad de integrantes
 * 
 * Si es soft se traduce de la siguiente manera:
 * -Cota(o un numero grande que haga cierta la desigualdad)*xaux + xi +...+ xj < Cant integrantes
 * -Cota(o un numero grande que haga cierta la desigualdad)*xaux + xi +...+ xj > Cant integrantes
 * Fcion objetivo : ...+ Costo * xaux
 *
*/


public class RestriccionesGenerales extends RestriccionesNGruposTemplate{
	
	private int cantIntegrantes;
	
	public RestriccionesGenerales(int cantIntegrantes, List<Usuario> usuarios, int idGrupo){
		
		super(usuarios, idGrupo);
		this.cantIntegrantes = cantIntegrantes;
		
	}

	@Override
	public void definirRestricciones(DependencyHelper<Usuario, String> helper) throws ContradictionException{

		this.ejecutada = true;
		Usuario[] usuarios = new Usuario[lusuarios.size()];
		lusuarios.toArray(usuarios);
		helper.atMost("Cantidad Integrantes", cantIntegrantes, usuarios);
		helper.atLeast("Cantidad Integrantes", cantIntegrantes, usuarios);
		

	}

	
	@Override
	public void escribirRestricciones(ProblemDefinition pd) {

		this.ejecutada = true;
		Usuario[] usuarios = new Usuario[lusuarios.size()];
		lusuarios.toArray(usuarios);
		pd.agregarRestriccionLinealIgualdad(usuarios, cantIntegrantes); 

	}

	@Override
	public boolean esPosibleFormarNgrupos() {

		if(  this.cantIntegrantes > this.lusuarios.size())
			return false;
		return true;
	}

	
	public String toString(){
		
		return "";
	}


	

	@Override
	public int getCosto(List<Usuario> grupo, int id) {
		
		if( this.idGrupo == id){
			this.ejecutada = true;
			if ( ( soft ) && ( grupo.size() != this.cantIntegrantes ) ) return this.costo;
			}
		return 0;
	}

	@Override
	protected void definirRestriccionSoft( DependencyHelper<Integer, String> helper )
			throws ContradictionException {
		
		@SuppressWarnings("unchecked") 
		WeightedObject<Integer>[] restriccionMenor = 
									new WeightedObject[ lusuarios.size()+ 1 ];
		@SuppressWarnings("unchecked")
		WeightedObject<Integer>[] restriccionMayor = 
				new WeightedObject[ lusuarios.size() + 1 ];
		Integer auxiliar = VariablesSistema.getVariableAuxiliar();
		restriccionMenor[0] = WeightedObject.newWO( auxiliar, (-1) * lusuarios.size() );
		restriccionMayor[0] = WeightedObject.newWO( auxiliar, lusuarios.size() );
		int i = 1;
		for( Usuario u : this.lusuarios ){
			restriccionMayor[i] = 
					WeightedObject.newWO( ( this.idGrupo * lusuarios.size()) + u.getId(), 1 );
			restriccionMenor[i] = 
					WeightedObject.newWO( (this.idGrupo * lusuarios.size()) + u.getId(), 1 );
			i++;
		}
		helper.atMost("Cantidad Integrantes", cantIntegrantes, restriccionMenor);
		helper.atLeast("Cantidad Integrantes", 
				BigInteger.valueOf( cantIntegrantes ),restriccionMayor);
		helper.addToObjectiveFunction(auxiliar, this.costo);

	}



	@Override
	protected void definirRestriccionHard( DependencyHelper<Integer, String> helper )
			throws ContradictionException {

		int index = 0;
		Integer[] usuarios = new Integer[ lusuarios.size() ];
		for(Usuario u : lusuarios){
			usuarios[index++] = ( this.idGrupo * lusuarios.size()) + u.getId();
		}

		helper.atMost("Cantidad Integrantes", cantIntegrantes, usuarios);
		helper.atLeast("Cantidad Integrantes", cantIntegrantes, usuarios);

	}
		 

}
