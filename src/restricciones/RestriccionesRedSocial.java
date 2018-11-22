package restricciones;

import java.math.BigInteger;
import java.util.List;

import modelo.DistanciaAB;
import modelo.RedSocial;
import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;

import constantes.Constantes;
import constantes.VariablesSistema;

import solvers.PseudoBoolean.otroPB.ProblemDefinition;
/*
 * Definicion de la restricción para que haya personas que se conozcan en el mismo grupo
 * Si es soft:
 * 			fcion Objetivo += Cij*xi*xj + ...+ Ctz*xt*xz; por cada par de personas que no se
 * 															conozcan
 * Si es hard:
 * 		Cij*xi*xj + ...+ Ctz*xt*xz <= Costo máximo
 */

public class RestriccionesRedSocial extends RestriccionesNGruposTemplate{

	protected List<DistanciaAB> distancias;
	
	public RestriccionesRedSocial(RedSocial red, List<Usuario> usuarios, int idGrupo) {

		super( usuarios, idGrupo);
		this.distancias = red.getDistancias();
		
		this.soft = true;
		this.costo = Constantes.PENALIZACION_REDSOCIAL;
		
	}


	@Override
	public void definirRestricciones(DependencyHelper<Usuario, String> helper)
			throws ContradictionException {

		this.ejecutada = true;
		for( DistanciaAB d: this.distancias ){

			if (d.getPeso() > 2){
				//Agrego el nuevo término a la funcion objetivo
				Usuario uAuxiliar = new Usuario( VariablesSistema.getVariableAuxiliar() );				
				helper.addToObjectiveFunction(uAuxiliar, BigInteger.valueOf(1));
				
				helper.and("Linearization", uAuxiliar, d.getU1(), d.getU2());
		
/*				//Agrego las restricciones para linealizar el término	
				@SuppressWarnings("unchecked") 
				WeightedObject<Usuario>[] aux1 = new WeightedObject[3];
				@SuppressWarnings("unchecked") 
				WeightedObject<Usuario>[] aux2 = new WeightedObject[3];
				aux1[ 0 ] = WeightedObject.newWO(uAuxiliar, (-2));
				aux1[ 1 ] = WeightedObject.newWO(d.getU1(), 1 );
				aux1[ 2 ] = WeightedObject.newWO(d.getU2(), 1 );
				helper.atLeast("Linearization", BigInteger.valueOf(0), aux1);
				aux2[ 0 ] = WeightedObject.newWO(uAuxiliar, 1 );
				aux2[ 1 ] = WeightedObject.newWO(d.getU1(), (-1) );
				aux2[ 2 ] = WeightedObject.newWO(d.getU2(), (-1) );
				helper.atLeast("Linearization", BigInteger.valueOf(-1), aux2);
//*/			}
		}
		
	}

	@Override
	public void escribirRestricciones(ProblemDefinition pd) {
		
		this.ejecutada = true;
		for( DistanciaAB d: this.distancias ){
			if (d.getPeso() > 2){
				Usuario[] literales = new Usuario[2];
				literales[0] = d.getU1();
				literales[1] = d.getU2();
				pd.agregarAObjetivoTerminoAND(literales, 1);
/*
				Usuario uAuxiliar = new Usuario(cantUsuarios++);
				pd.agregarAObjetivo(uAuxiliar, 1);
				pd.agregarRestriccionAND(literales, uAuxiliar);
*/
			}
		}

		
	}

	@Override
	public boolean esPosibleFormarNgrupos() {

		return true;
	}

	public String toString(){
		
		return "";
	}
	

	@Override
	public int getCosto(List<Usuario> grupo, int id) {

		if( this.idGrupo == id){
			this.ejecutada = true;
			if (soft){
				int costo = 0;
				for( DistanciaAB d: this.distancias ){
					if ( (d.estaContenida(grupo) ) && ( d.getPeso() > 2 ) ){
						costo += this.costo;
					}
				}
				return costo;
			}
		}
		return 0;
	}

	@Override
	protected void definirRestriccionSoft( DependencyHelper<Integer, String> helper )
			throws ContradictionException {
		
		for( DistanciaAB d: this.distancias ){
			if (d.getPeso() > 2){
			//Agrego el nuevo término a la funcion objetivo
				int uAuxiliar = VariablesSistema.getVariableAuxiliar();				
				helper.addToObjectiveFunction(uAuxiliar, 
						BigInteger.valueOf( this.costo ));
				helper.and("Linearization", uAuxiliar, d.getU1().getId() + (idGrupo * lusuarios.size()),
						(this.idGrupo * lusuarios.size()) +  d.getU2().getId() );
				}
			}

	}

	@Override
	protected void definirRestriccionHard( DependencyHelper<Integer, String> helper )
			throws ContradictionException {

		Integer[] restriccion = new Integer[ lusuarios.size() ];
		int i = 0;
		for( DistanciaAB d: this.distancias ){
			if (d.getPeso() > 2){
			//Agrego el nuevo término a la funcion objetivo
				int uAuxiliar = VariablesSistema.getVariableAuxiliar();				
				restriccion[ i++ ] = uAuxiliar;
				helper.and("Linearization", uAuxiliar, (idGrupo * lusuarios.size()) + d.getU1().getId(),
						(idGrupo * lusuarios.size()) + d.getU2().getId() );
				}
			}
		helper.atMost( "Restriccion Red Social", this.cota, restriccion );

	}

}
