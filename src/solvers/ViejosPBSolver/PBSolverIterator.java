package solvers.ViejosPBSolver;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.PBSolverDecorator;
import org.sat4j.pb.PseudoIteratorDecorator;
import org.sat4j.pb.PseudoOptDecorator;
import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.IVec;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

import constantes.Constantes;

import restricciones.IRestricciones;
import restricciones.PaqueteRestricciones;

import modelo.ConjuntoGrupo;
import modelo.Grupo;
import modelo.SolucionPB;
import modelo.Usuario;
import utilidades.Utilidades;

/*
 *Esta clase extiende al Solver que calcula de forma programática la solucion, para poder
 *calcular un conjunto de soluciones que satisfagan el problema. Sin embargo, la forma de 
 *hacerlo es costosa, porque en cada ciclo agrega una restricción al modelo para desechar la 
 *solucion anterior, volviendo a realizar todos los calculos.
 *Para más detalle ir a la clase PseudoBooleanSolver para ver su funcionamiento 
 */

public class PBSolverIterator extends PseudoBooleanSolver{

	private int profundidad = 0;
	
	public PBSolverIterator(IPBSolver solver, int timeout, 
			PaqueteRestricciones restricciones, List<Usuario> usuarios, 
			int cantIntegrantes, int cantGrupos) {
	
		super(solver, timeout, restricciones, usuarios, cantIntegrantes, cantGrupos);
	}
		
	 public List<Grupo> solveList() { 


		 Usuario[] usuarios = new Usuario[this.usuarios.size()];
		 this.usuarios.toArray(usuarios);
		 ArrayList<Grupo> result = new ArrayList<Grupo>();
		 
		 try {

			this.restricciones.definirRestricciones(helper);

			System.out.println("Cantidad de restricciones: "+helper.getNumberOfConstraints());
			System.out.println("Cantidad de variables: " + helper.getNumberOfVariables());
			System.out.println("Cantidad de variables en el modelo : " + helper.getSolver().nVars());			
			
			int soluciones = 0;
            while ( (helper.hasASolution() ) && ( soluciones < Constantes.MAX_SOLUCIONES) ) {
				IVec<Usuario> sol = helper.getSolution(); 
				result.add( this.decodificarGrupo(sol, helper) );
				/*
				 * Agrego una restriccion para que descarte las soluciones ya obtenidas
				 */
         	  	helper.discard(sol);
         	  	soluciones++;

             }
		return result; 

		} catch (ContradictionException e) {
			// TODO Auto-generated catch block
			System.out.println("Insatisfactible por: ");
			try {
				System.out.println(helper.why());
			} catch (TimeoutException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			return null; 
		}
		 catch (TimeoutException e) { 
			System.out.println("No se encontro una solucion");
			return null; 
			}
	}

	 public List<ConjuntoGrupo> solveNGrupoList() {
		 
		 ArrayList<ConjuntoGrupo> grupos = new ArrayList<>();
		 if( !this.restricciones.esPosibleFormarNgrupos() ){
			 System.out.printf("No es posible formar %d que cumpla todas las restricciones \n", this.cantGrupos);
			 return null;
		 }
		 
		 /*
		  * Obtengo los 10 grupos a partir de los cuales, para cada uno de ellos se calcula,
		  * un conjunto de grupos
		  */
		 
		 List<Grupo> gruposCabeceras = this.solveList();
		 
		 for( Grupo g : gruposCabeceras){
			 ConjuntoGrupo nGrupos = new ConjuntoGrupo();
			 nGrupos.addGrupo(g);
			 this.helper.reset();
			 this.restricciones.reset();
			 this.restriccionIntegrantePorGrupo(nGrupos, helper);
			 for(int i = 0; i < (this.cantGrupos - 1 ) ; i++){
				 
				 Grupo grupo = this.solve();
				 nGrupos.addGrupo(grupo);
				 /*
				  * Por cada grupo obtenido, agrega una nueva restriccion al modelo para que
				  * no vuelva a elegir a esos integrantes
				  */
				 this.helper.reset();
				 this.restricciones.reset();
				 this.restriccionIntegrantePorGrupo(nGrupos, helper);
				 }
			 
			 if ( (nGrupos.hayGrupos() ) && (!nGrupos.hayGrupoVacio()) ){
				 nGrupos.calcPen();
				 grupos.add(nGrupos);
			 }

		 }

		 return grupos;
		 
	 }
	
	 public ConjuntoGrupo solveNGrupos( ){
		 
		 ConjuntoGrupo grupos = this.solveNGrupos(new ConjuntoGrupo(), 
				 									new ArrayList<ConjuntoGrupo>());
		 if ( grupos != null )
			 grupos.calcPen();
		 return grupos;
	 }
	 
	 private ConjuntoGrupo solveNGrupos( ConjuntoGrupo grupos, 
			 								List<ConjuntoGrupo> visitados){
		 
		 //TODO: corregir restricciones para soportar las generadas por la Red Social
		 System.out.println("Profundidad : " + profundidad++);
		 this.restricciones.reset();
		 if( !this.restricciones.esPosibleFormarNgrupos() ){
			 System.out.printf("No es posible formar %d que cumpla todas las restricciones \n", this.cantGrupos);
			 return null;
		 }
		 
		 //Condicion de corte
		 if(grupos.getCantGrupos() == this.cantGrupos){
			 return grupos;
		 }else{
			 DependencyHelper<Usuario, String> ayudante = 
					 new DependencyHelper<>(optimizer, true);
			 try {
				ayudante.reset();
				this.restricciones.definirRestricciones(ayudante);
				this.restriccionIntegrantePorGrupo(grupos, ayudante);
				boolean haySolucion = false;
				while( ( grupos.getCantGrupos() < this.cantGrupos ) && 
						( ayudante.hasASolution() )){
					
					haySolucion = true;
					IVec<Usuario> sol = ayudante.getSolution();
					Grupo g = this.decodificarGrupo(sol, ayudante);
					grupos.addGrupo(g);
					System.out.println(grupos);
					System.out.println("Visitados: ");
					for(ConjuntoGrupo cg : visitados)
						System.out.println(cg);
					ConjuntoGrupo auxiliar = null;
					if( !visitados.contains( grupos ) ){
							auxiliar = this.solveNGrupos(grupos, visitados);
						if( (auxiliar == null) || 
								( grupos.getCantGrupos() < this.cantGrupos )){
							//agrego el esta solución como visitada
							visitados.add( grupos.clone() );
							grupos.eliminarGrupo(g);
						}
					}
					ayudante.reset();
					this.restricciones.reset();
					this.restricciones.definirRestricciones(ayudante);
					this.restriccionIntegrantePorGrupo(grupos, ayudante);
					ayudante.discard(sol);
				}
				profundidad--;
				if(!haySolucion){
//					System.out.println("Insatisfactible por: ");
//					System.out.println(ayudante.why());
					return null;
				}else{
					return grupos;
				}
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				profundidad--;
				return null;
			} catch (ContradictionException e) {
				// TODO Auto-generated catch block
				profundidad--;
				e.printStackTrace();
				return null;
			}
		 }
	 }

}