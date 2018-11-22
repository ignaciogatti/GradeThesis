package solvers.PseudoBoolean;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import modelo.ConjuntoGrupo;
import modelo.Grupo;
import modelo.Usuario;

import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.PseudoOptDecorator;
import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IVec;
import org.sat4j.specs.TimeoutException;

import constantes.Constantes;

import restricciones.PaqueteRestricciones;
import utilidades.DecodificadorPB;
import utilidades.Utilidades;

public class PBSolver {

	protected PaqueteRestricciones restricciones;
	protected PseudoOptDecorator optimizer; 
	protected DependencyHelper<Integer, String> helper;
	protected List<Usuario> usuarios;
	protected int cantIntegrantes;
	protected int cantGrupos = 1;
	
	public PBSolver() {

	}
	
	public PBSolver(IPBSolver solver, int timeout, 
			PaqueteRestricciones restricciones, List<Usuario> usuarios, 
			int cantIntegrantes, int cantGrupos) {

		this.cantGrupos = cantGrupos;
		optimizer = new PseudoOptDecorator(solver); 
		optimizer.setTimeout(timeout);
		optimizer.setVerbose(true);
		helper = new DependencyHelper<Integer, String>(optimizer, true);
		this.restricciones = restricciones;
		this.usuarios = usuarios;
		this.cantIntegrantes = cantIntegrantes;

	}

	public PBSolver(IPBSolver solver, int timeout, 
			PaqueteRestricciones restricciones, List<Usuario> usuarios, int cantIntegrantes) { 

		optimizer = new PseudoOptDecorator(solver); 
		optimizer.setTimeout(timeout);
		optimizer.setVerbose(true);
		helper = new DependencyHelper<Integer, String>(optimizer, true);
		this.restricciones = restricciones;
		this.usuarios = usuarios;
		this.cantIntegrantes = cantIntegrantes;
	}

	
	 public ConjuntoGrupo solve() { 

		 /* 
		  * 
		  * Aca va la logica del pseudo booleano
		  * 
		  */
		 try {

			this.restricciones.reset();
			this.restricciones.definirRestriccionesParaNGrupos(helper);
			ConjuntoGrupo g = null ;
			System.out.println("Cantidad de restricciones: "+helper.getNumberOfConstraints());
			System.out.println("Cantidad de variables: " + helper.getNumberOfVariables());
			System.out.println("Cantidad de variables en el modelo : " + 
									helper.getSolver().nVars());
			System.out.println("Fcion objetivo: " + helper.getObjectiveFunction());
			if (helper.hasASolution()) {
				 IVec<Integer> sol = helper.getSolution(); 
				 g = DecodificadorPB.decodificarSolucion( sol, this.usuarios, this.cantGrupos,
						 this.cantIntegrantes, this.restricciones);
//				 System.out.println(result);
			}else{
				System.out.println("Insatisfactible por: ");
				System.out.println(helper.why());
			}
		return g; 

		} catch (ContradictionException e) {

			e.printStackTrace();
			return null; 
		}
		 catch (TimeoutException e) { 
			System.out.println("No se encontro una solucion");
			return null; 
		}
		 //Hay un problema con las estructuras internas del solver
		 catch (NullPointerException e){
			 System.out.println("El sistema es insatisfactible");
			 return null;
		 }
	}


	 public List<ConjuntoGrupo> solveList(){
		 
		 ArrayList<ConjuntoGrupo> result = new ArrayList<ConjuntoGrupo>();
		 
		 try {
			this.restricciones.reset();
			this.restricciones.definirRestriccionesParaNGrupos(helper);

			System.out.println("Cantidad de restricciones: "+helper.getNumberOfConstraints());
			System.out.println("Cantidad de variables: " + helper.getNumberOfVariables());
			System.out.println("Cantidad de variables en el modelo : " + helper.getSolver().nVars());			
			
			int soluciones = 0;
            while ( (helper.hasASolution() ) && ( soluciones < Constantes.MAX_SOLUCIONES) ) {
				IVec<Integer> sol = helper.getSolution(); 
				result.add( DecodificadorPB.decodificarSolucion( sol, this.usuarios, 
						this.cantGrupos, this.cantIntegrantes, this.restricciones) );
				/*
				 * Agrego una restriccion para que descarte las soluciones ya obtenidas
				 */
         	  	helper.discard(sol);
         	  	soluciones++;

             }
		return result; 

		} catch (ContradictionException e) {

			System.out.println("Insatisfactible por: ");
			try {
				System.out.println(helper.why());
			} catch (TimeoutException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			return null; 
		}
		 catch (TimeoutException e) { 
			System.out.println("No se encontro una solucion");
			return null; 
		}
		//Hay un problema con las estructuras internas del solver
		 catch (NullPointerException e){
			 System.out.println("El sistema es insatisfactible");
			 return null;
		 }

	 }
	 
	 public String toString(){
		 
		 String rta = "";
		 rta += "Cantidad de integrantes : " + cantIntegrantes + "\n\n";
		 rta += "Cantidad de grupos : " + cantGrupos + "\n\n";
		 rta += "Total de usuarios : " + this.usuarios.size() + "\n\n";
		 rta += this.restricciones.toString();
		 rta += "\n"; 
		 return rta;
	 }

}
