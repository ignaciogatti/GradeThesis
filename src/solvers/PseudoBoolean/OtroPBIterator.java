package solvers.PseudoBoolean;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import modelo.Grupo;
import modelo.SolucionPB;
import modelo.Usuario;

import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.PseudoIteratorDecorator;
import org.sat4j.pb.PseudoOptDecorator;
import org.sat4j.pb.reader.OPBReader2007;
import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.IVec;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

import restricciones.PaqueteRestricciones;

import constantes.Constantes;

import utilidades.Utilidades;

public class OtroPBIterator extends PseudoBooleanSolver{
	
	
	private ArrayList<SolucionPB> SolucionesSatisfacibles;
	
	public OtroPBIterator() {
		// TODO Auto-generated constructor stub
		super();
		this.SolucionesSatisfacibles = new ArrayList<>();
	}
	
	public OtroPBIterator(IPBSolver solver, int timeout, 
			PaqueteRestricciones restricciones, List<Usuario> usuarios, 
			int cantIntegrantes, int cantGrupos) {

		super();
		this.SolucionesSatisfacibles = new ArrayList<>();
		
		this.cantGrupos = cantGrupos;
		optimizer = new PseudoIteratorDecorator(solver); 
		optimizer.setTimeout(timeout);
		optimizer.setVerbose(true);
		helper = new DependencyHelper<Usuario, String>(optimizer, true);
		this.restricciones = restricciones;
		this.usuarios = usuarios;
		this.cantIntegrantes = cantIntegrantes;
		//No es lo mas elegante, pero por el momento...
		this.maxIdUsuario = this.usuarios.get(this.usuarios.size()-1).getId();

	}

	
	protected void solverEngine() {	

        helper.getSolver().setTimeout(3600); // 1 hour timeout

        try {
        	
            this.restricciones.definirRestricciones(helper);
            System.out.println("Cantidad de variables en el modelo : " + helper.getSolver().nVars());
            System.out.println(optimizer.nVars());
            int[] lastSolution = null;
            int quality = -1;
//            System.out.println(problem.isSatisfiable());
            if (helper.getSolver().isSatisfiable()) {
            	System.out.println("Calcule una solucion");
            	lastSolution = helper.getSolver().model();
            	quality = optimizer.calculateObjective().intValue();
            	this.SolucionesSatisfacibles.add(new SolucionPB(lastSolution, quality));

                System.out.println("Partial evaluation (initial): "+quality);

                boolean contradiction = false;
                //Obtengo las soluciones recorridas por el optimizador
            	while ( optimizer.admitABetterSolution() ) {
            		System.out.println();
            		lastSolution = helper.getSolver().model();
            		quality = optimizer.calculateObjective().intValue();
            		this.SolucionesSatisfacibles.add(new SolucionPB(lastSolution, quality));
            		List<Grupo> grupos = this.decodificarPBSolucion(SolucionesSatisfacibles);
            		
              	  	System.out.println(grupos);
//              	  	try {
              	  		optimizer.discardCurrentSolution();
//              	  	} catch (ContradictionException e) {
//              	  		System.out.println("    still solvable !");
//                	  	e.printStackTrace();
//              	  		contradiction = true;
//              	  	}
                }            	
            }
                    	  
            if (lastSolution != null) {
          	  System.out.println("Satisfiable!!  "+" with solution quality (min)= "+quality);          	  
//              System.out.println(reader.decode(problem.model()));
           } 
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
      	  	e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");      
            e.printStackTrace();
        }        
	}

	
	public List<Grupo> solveList() {     
		
		this.solverEngine();
        if (this.SolucionesSatisfacibles.isEmpty()) {
	        System.out.println("No se encontraron soluciones");
    		return null;
        }
		
        List<Grupo> grupos = this.decodificarPBSolucion(SolucionesSatisfacibles);
        Collections.sort(grupos);
        if(grupos.size() > Constantes.MAX_SOLUCIONES){
        	grupos = grupos.subList(0, Constantes.MAX_SOLUCIONES);
        }
        return grupos;
	}



	private List<Grupo> decodificarPBSolucion(ArrayList<SolucionPB> SolucionesSatisfacibles){
		
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
//			System.out.println(g);
			grupos.add(g);
		}
		
		return grupos;
	}


}
