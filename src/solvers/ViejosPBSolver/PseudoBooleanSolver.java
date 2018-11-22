package solvers.ViejosPBSolver;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import modelo.ConjuntoGrupo;
import modelo.Grupo;
import modelo.SolucionPB;
import modelo.Usuario;

import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.OptToPBSATAdapter;
import org.sat4j.pb.PseudoOptDecorator;
import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.pb.tools.WeightedObject;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IVec;
import org.sat4j.specs.TimeoutException;

import choco.cp.solver.variables.delta.iterators.BitSetIterator;

import restricciones.IRestricciones;
import restricciones.PaqueteRestricciones;

import utilidades.Utilidades;
import constantes.Constantes;

/*
 * Esta clase obtiene la solución optima al problema de forma programática; con lo cual
 * obtiene una sola solución, ya que no permite recolectar las parciales. 
 * Para eso, se vale de la clase DependecyHelper, proporcionada por el framework.
 */

public class PseudoBooleanSolver {

	protected PaqueteRestricciones restricciones;
	protected PseudoOptDecorator optimizer; 
	protected DependencyHelper<Usuario, String> helper;
	protected List<Usuario> usuarios;
	protected int cantIntegrantes;
	protected int cantGrupos = 1;
	protected int maxIdUsuario;
	
	public PseudoBooleanSolver() {
		// TODO Auto-generated constructor stub
	}
	
	public PseudoBooleanSolver(IPBSolver solver, int timeout, 
			PaqueteRestricciones restricciones, List<Usuario> usuarios, 
			int cantIntegrantes, int cantGrupos) {

		this.cantGrupos = cantGrupos;
		optimizer = new PseudoOptDecorator(solver); 
		optimizer.setTimeout(timeout);
		optimizer.setVerbose(true);
		helper = new DependencyHelper<Usuario, String>(optimizer, true);
		this.restricciones = restricciones;
		this.usuarios = usuarios;
		this.cantIntegrantes = cantIntegrantes;
		//No es lo mas elegante, pero por el momento...
		this.maxIdUsuario = this.usuarios.get(this.usuarios.size()-1).getId() + 1;

	}

	public PseudoBooleanSolver(IPBSolver solver, int timeout, 
			PaqueteRestricciones restricciones, List<Usuario> usuarios, int cantIntegrantes) { 

		optimizer = new PseudoOptDecorator(solver); 
		optimizer.setTimeout(timeout);
		optimizer.setVerbose(true);
		helper = new DependencyHelper<Usuario, String>(optimizer, true);
		this.restricciones = restricciones;
		this.usuarios = usuarios;
		this.cantIntegrantes = cantIntegrantes;
		//No es lo mas elegante, pero por el momento...
		this.maxIdUsuario = this.usuarios.get(this.usuarios.size()-1).getId();
	}

	
	 public Grupo solve() { 

		 /* 
		  * 
		  * Aca va la logica del pseudo booleano
		  * 
		  */

		 Usuario[] usuarios = new Usuario[this.usuarios.size()];
		 this.usuarios.toArray(usuarios);
		 List<Usuario> result = new ArrayList<Usuario>();
		 try {

			this.restricciones.definirRestricciones(helper);
			Grupo g = null ;
			System.out.println("Cantidad de restricciones: "+helper.getNumberOfConstraints());
			System.out.println("Cantidad de variables: " + helper.getNumberOfVariables());
			System.out.println("Cantidad de variables en el modelo : " + helper.getSolver().nVars());
			
			if (helper.hasASolution()) {
				 IVec<Usuario> sol = helper.getSolution(); 
				 g = this.decodificarGrupo(sol, helper);
//				 System.out.println(result);
			}else{
				System.out.println("Insatisfactible por: ");
				System.out.println(helper.why());
			}
		return g; 

		} catch (ContradictionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null; 
		}
		 catch (TimeoutException e) { 
			System.out.println("No se encontro una solucion");
			return null; 
			}
	}
	 
	 public ConjuntoGrupo solveNGrupos(){
		 
		 //TODO: corregir restricciones para soportar las generadas por la Red Social
		 
		 ConjuntoGrupo grupos = new ConjuntoGrupo();
		 if( !this.restricciones.esPosibleFormarNgrupos() ){
			 System.out.printf("No es posible formar %d que cumpla todas las restricciones \n", this.cantGrupos);
			 return null;
		 }
		 for(int i = 0; i < this.cantGrupos ; i++){
			 
			 try{
				 Grupo grupo = this.solve();
				 grupos.addGrupo(grupo);
			 }catch (NullPointerException e){
				 System.out.printf("No es posible formar %d que cumpla todas las restricciones \n", this.cantGrupos);
				 return null;
			 }
			 
			 /*
			  * Por cada grupo obtenido, agrega una nueva restriccion al modelo para que
			  * no vuelva a elegir a esos integrantes
			  */
			 
			 this.restricciones.reset();
			 this.helper.reset();
			 this.restriccionIntegrantePorGrupo(grupos, this.helper);
			 }
		 System.out.println(grupos.hayGrupos());
		 if (grupos.hayGrupos()){
			 grupos.calcPen();
		 }
		 return grupos;
	 }

	 
	 protected void restriccionIntegrantePorGrupo(ConjuntoGrupo grupos, 
			 DependencyHelper<Usuario, String> helper){
		 
		 for(Grupo grupo : grupos.getGrupos()){
			 Usuario[] usuarios = new Usuario[grupo.getIntegrantes().size()];
			 int i = 0;
			 for(Usuario u : grupo.getIntegrantes()){
				 usuarios[i++] = u;
				 String n = "Usuario " + u.getId() +" usado ";
				 try {
					helper.setFalse(u, n);
				} catch (ContradictionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
//			 try {
//				helper.atMost("Integrantes por grupo", 0, usuarios);
//			} catch (ContradictionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
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
	 
	 public long bestValue() {
		 return helper.getSolutionCost().longValue(); 
	}
	 
	 public boolean isOptimal() {
		 return optimizer.isOptimal();
	}
	 
	 public void printStats() {
		 System.out.println(optimizer.toString(""));
		 optimizer.printStat(new PrintWriter(System.out, true), "");
	}
	 
	 protected Grupo decodificarGrupo( IVec<Usuario> sol, DependencyHelper<Usuario, String> helper ){

		 ArrayList<Usuario> grupo = new ArrayList<>();
			for (Iterator<Usuario> it = sol.iterator(); it.hasNext();) {
				Usuario u = it.next();
				if(u != null)
					grupo.add(u);
			}
			
			
			 for(Iterator<Usuario> it = grupo.iterator(); it.hasNext();){
				 if(it.next().getId() >= (this.maxIdUsuario))
					 it.remove();
			 }

			return (new Grupo(grupo,
					Utilidades.calcularError(grupo, helper.getSolutionCost())));

	 }

	 
}

