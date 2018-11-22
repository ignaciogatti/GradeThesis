package solvers.PseudoBoolean.otroPB;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.Grupo;
import modelo.SolucionPB;
import modelo.Usuario;

import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.PseudoIteratorDecorator;
import org.sat4j.pb.PseudoOptDecorator;
import org.sat4j.pb.SolverFactory;
import org.sat4j.pb.reader.OPBReader2007;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.TimeoutException;

import constantes.Constantes;

import utilidades.Utilidades;
/*
 * Este solver toma la definicion del problema desde un Reader (hay que definir el 
 * problema en un archivo de texto); e itera sobre todos los modelos que satisfacen las
 * restricciones. 
 */

public class PBSolver {

	private List<Usuario> usuarios;
	private ArrayList<SolucionPB> SolucionesSatisfacibles;
	private IPBSolver solver;

	public PBSolver(IPBSolver solver, List<Usuario> usuarios) {
		// Do something
		this.SolucionesSatisfacibles = new ArrayList<>();
		this.solver = solver;
		this.usuarios= usuarios;
	}
	
	protected void solve(java.io.Reader inputReader) {	

        solver.setTimeout(3600); // 1 hour timeout
        PseudoOptDecorator optimizer = new PseudoIteratorDecorator(solver);
//        PseudoOptDecorator optimizer = new PseudoOptDecorator(solver);
        OPBReader2007 reader = new OPBReader2007(optimizer /*solver*/);
        reader.setVerbosity(true);

        try {
            IProblem problem = reader.parseInstance(inputReader);

            int[] lastSolution = null;
            int quality = -1;
//            System.out.println(problem.isSatisfiable());
            if (problem.isSatisfiable()) {
            	System.out.println("Calcule una solucion");
            	lastSolution = problem.model();
            	quality = optimizer.calculateObjective().intValue();
            	this.SolucionesSatisfacibles.add(new SolucionPB(lastSolution, quality));

                System.out.println("Partial evaluation (initial): "+quality);

                boolean contradiction = false;
                
                //Obtengo las soluciones recorridas por el optimizador
            	while ( optimizer.admitABetterSolution() && !optimizer.isOptimal()) {
            		System.out.println();
            		lastSolution = problem.model();
            		quality = optimizer.calculateObjective().intValue();
            		this.SolucionesSatisfacibles.add(new SolucionPB(lastSolution, quality));
              	  	System.out.println("    partial evaluation: "+quality);
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
          } catch (ParseFormatException e) {        	  
            // TODO Auto-generated catch block
        	  e.printStackTrace();
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
      	  	e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");      
            e.printStackTrace();
        }        
	}

	
	public List<Grupo> solve(ProblemDefinition input) {     
		
		StringReader reader = new StringReader(input.toString());
        this.solve(reader);
        
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
