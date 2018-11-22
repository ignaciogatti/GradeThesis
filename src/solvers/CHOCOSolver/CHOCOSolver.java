package solvers.CHOCOSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import constantes.Constantes;

import modelo.ConjuntoGrupo;
import modelo.Distancia;
import modelo.Estilo;
import modelo.Grupo;
import modelo.RedSocial;
import modelo.Usuario;

import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.variables.integer.IntegerConstantVariable;
import choco.kernel.model.variables.integer.IntegerVariable;

public class CHOCOSolver {

	static final int E=0,S=0,T=0,J=0,I=1,N=1,F=1,P=1;
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private IntegerVariable[][] solucion;
	private IntegerVariable[][][] sol;
	private int tamanioGrupo;
	private int cantGrupos;
	private CPModel model;	
	private CPSolver solver;
	private List<Distancia> distancias;
	private RedSocial red;
	
	
	private int pRedSocial = 1;
	private int pBalanceo = 2;
	private int pCorr = 3;
	
	public CHOCOSolver(List<Usuario> usrs, RedSocial red) {
		usuarios = usrs;
		distancias = new ArrayList<Distancia>();
		this.red = red;
	}
	
	public CHOCOSolver(List<Usuario> usrs) {
		usuarios = usrs;
		distancias = new ArrayList<Distancia>();
		this.red = null;
	}
	
	
	public void setModel(int tamGrupo, int[] lusr){
		model = new CPModel();
		tamanioGrupo = tamGrupo;	
		solucion = new IntegerVariable[13][tamanioGrupo];
		//Declararion de la tabla
		List<int[]> tuples = getTable();
		for (int i=0; i<tamanioGrupo; i++) {
			solucion[0][i] = Choco.makeIntVar("var_id",getIds(tuples));
			for (int j=1;j<13; j++) 
				solucion[j][i] = Choco.makeIntVar("var_" + i+j,0,1);
		}
		
		for (int i=0; i<tamanioGrupo; i++) {
			//Constraint para que  solucion este dentro del dominio de usuarios
			model.addConstraint(Choco.regular(new IntegerVariable[]{solucion[0][i],solucion[1][i],solucion[2][i],
														            solucion[3][i],solucion[4][i],solucion[5][i],
														            solucion[6][i],solucion[7][i],solucion[8][i],
														            solucion[9][i],solucion[10][i],solucion[11][i],
														            solucion[12][i]},tuples));
			
			//Constraint para que no haya permutaciones de una solucion y no haya dos ids iguales
			if (i < tamanioGrupo-1)
				model.addConstraint(Choco.lt(solucion[0][i],solucion[0][i+1]));
		}
		
		//*
		//Constraint para que un rol no sea llevado a cabo por mas de la mitad de los integrantes del grupo
		for (int i=5; i<13; i++)
			model.addConstraint(Choco.and(Choco.geq(tamanioGrupo/2,Choco.sum(solucion[i])),Choco.leq(1,Choco.sum(solucion[i]))));
		//*/	
			

		//Constraint para balancear perfiles de Myers Brigss
		for (int i=1; i<5; i++)
			model.addConstraint(Choco.and(Choco.geq(tamanioGrupo/2+2,Choco.sum(solucion[i])),Choco.leq(tamanioGrupo/2-2,Choco.sum(solucion[i]) )));
		//
		
		
		//Constraint para cuando el usuario selecciona personas para pertenecer al grupo
		if(lusr!=null)
			for (int i=0; i<lusr.length;i++) 
				model.addConstraint(Choco.occurrence(lusr[i], new IntegerConstantVariable(1), solucion[0]));
	}
	
	@SuppressWarnings("unchecked")
	public List<Grupo> solve(){
		solver = new CPSolver();
		
		//Se le entrega el modelo al solver
		solver.read(model);	
		
		//Se llama a resolver el probelma
		solver.solve();
		
		List<Grupo> soluciones = new ArrayList<Grupo>();
		if (solver.isFeasible()) {
			int cantSol = 0;
			do {
				List<Usuario> integrantes = new ArrayList<Usuario>();
				for (int i = 0; i < tamanioGrupo; i++) 
					integrantes.add(getUsuario(solver.getVar(solucion[0][i]).getVal()));
				Grupo grupo = new Grupo(integrantes,softConstraintSolve(integrantes));
				if (soluciones.size() < 10) { 
					soluciones.add(grupo);
					Collections.sort(soluciones);
				}
				else
					if (grupo.getPenalizacion() < penalizacionMayor(soluciones)) {
						soluciones.remove(soluciones.size()-1);
						soluciones.add(grupo);
						Collections.sort(soluciones);
					}
				cantSol++;
			} 
			while (solver.nextSolution() && cantSol <50000);			
		}
		return soluciones;
	}
	
	private double penalizacionMayor(List<Grupo> soluciones) {
		return soluciones.get(soluciones.size()-1).getPenalizacion();
	}

	public int getMyersChoice(int valor){
		if (valor >=0)
			return 0;
		return 1;
	}
	
	public int cumpleRol(int valor) {
		if (valor > Constantes.COTA_CUMPLE_ROL)
			return 1;
		return 0;
	}
	
	private int rolesBalanceados(Usuario usr){
		if (!(balanceado(usr.getRol().getValCreador()) &&
			balanceado(usr.getRol().getValEvaluador()) &&
			balanceado(usr.getRol().getValFinalizador()) &&
			balanceado(usr.getRol().getValInnovador()) &&
			balanceado(usr.getRol().getValLider()) &&
			balanceado(usr.getRol().getValManager()) &&
			balanceado(usr.getRol().getValModerador()) &&
			balanceado(usr.getRol().getValOrganizador())))
			return 1;
		return 0;
	}
	
	private boolean balanceado(int val){
		int linf = 20;
		int lsup = 36; // limites para saber si es balanceado
		if (val <= lsup && val>=linf)
			return true;
		return false;
	}
	
	private int correlMByMumma(Usuario usr){
		if (usr.getRol().getValModerador()> Constantes.COTA_CUMPLE_ROL )
			if(esEstilo(usr.getEstilo(), E, S, F, J) || esEstilo(usr.getEstilo(), E, N, F, J))
				return 0;
		if (usr.getRol().getValInnovador()> Constantes.COTA_CUMPLE_ROL )
			if (esEstilo(usr.getEstilo(), E, N, T, P) || esEstilo(usr.getEstilo(), E, N, F, P))
				return 0;
		if (usr.getRol().getValCreador()> Constantes.COTA_CUMPLE_ROL )
			if (esEstilo(usr.getEstilo(), I, N, T, J) || esEstilo(usr.getEstilo(), I, N, F, J))
				return 0;
		if (usr.getRol().getValManager() > Constantes.COTA_CUMPLE_ROL )
			if (esEstilo(usr.getEstilo(), E, S, T, P) || esEstilo(usr.getEstilo(), E, S, F, P) ||
				esEstilo(usr.getEstilo(), E, S, T, J) || esEstilo(usr.getEstilo(), E, N, T, J))
				return 0;
		if (usr.getRol().getValEvaluador() > Constantes.COTA_CUMPLE_ROL )
			if (esEstilo(usr.getEstilo(), I, S, T, J) || esEstilo(usr.getEstilo(), I, S, F, J))
				return 0;
		if (usr.getRol().getValOrganizador() > Constantes.COTA_CUMPLE_ROL )
			if (esEstilo(usr.getEstilo(), I, S, T, J) || esEstilo(usr.getEstilo(), I, S, F, J) ||
				esEstilo(usr.getEstilo(), E, S, T, J) || esEstilo(usr.getEstilo(), E, N, T, J))
				return 0;
		if (usr.getRol().getValLider() > Constantes.COTA_CUMPLE_ROL || 
				usr.getRol().getValFinalizador() > Constantes.COTA_CUMPLE_ROL )
			if (esEstilo(usr.getEstilo(), E, S, T, J) || esEstilo(usr.getEstilo(), E, N, T, J))
				return 0;
		return 1;
	}
	
	private boolean esEstilo(Estilo e, int a, int p, int j, int l){
		if(getMyersChoice(e.getAttitude()) == a && getMyersChoice(e.getPerceiving()) == p &&
		   getMyersChoice(e.getJudging()) == j && getMyersChoice(e.getLifestyle()) == l)
			return true;	
		return false;
	}
	
	private double softConstraintSolve(List<Usuario> lusr)	{
		//Seccion donde se definen las penalizaciones de cada soft constrain
		int contRM = 0;
		int contCorr = 0;
		int contRedSocial = 0;
		for (int i=0; i<lusr.size(); i++) {
			contRM += rolesBalanceados(lusr.get(i));
			contCorr+=correlMByMumma(lusr.get(i));
//*	
  		Distancia distancia = red.buscarDistancia(lusr.get(i));
			if (i < lusr.size()-1)
				for (int j=i+1; j<lusr.size(); j++)
					if (distancia.getDistancia(lusr.get(j).getId()) > 2)
						contRedSocial += 1;

//*/
		}
		double error = contRM*pBalanceo+contCorr*pCorr+contRedSocial*pRedSocial;
		int sumatoria = 0;
		for (int i =1; i<lusr.size(); i++) 
			sumatoria += lusr.size()-i;
		return error/(lusr.size()*(pBalanceo+pCorr) + sumatoria);
	}
	

/*	@SuppressWarnings("unchecked")
	private UndirectedGraph<Usuario,DefaultEdge> createStringGraph(List<Usuario> usuarios)  {
		Session session = getSession();
		List<Interactuan> interacciones = new ArrayList<Interactuan>(session.createQuery("from Interactuan").list());
		UndirectedGraph<Usuario,DefaultEdge> g = new SimpleGraph<Usuario, DefaultEdge>(DefaultEdge.class);
		for (int i=0; i< interacciones.size(); i++) {
			Usuario u1 = getUsuario(interacciones.get(i).getId().getIdUsr1());
			Usuario u2 = getUsuario(interacciones.get(i).getId().getIdUsr2());
			if (!g.containsVertex(u1))
				g.addVertex(u1);
			if (!g.containsVertex(u2))
				g.addVertex(u2);
			g.addEdge(u1,u2);
		}
		for (int i=0; i<usuarios.size(); i++)
			if (!g.containsVertex(usuarios.get(i)))
				g.addVertex(usuarios.get(i));
		return g;
	}
*/	
	private Usuario getUsuario(int idUsr1) {
		for (int i=0; i<usuarios.size(); i++)
			if (usuarios.get(i).getId() == idUsr1)
				return usuarios.get(i);
		return null;
	}
/*
	private Session getSession() {
		Session retorno;
		retorno = HibernateUtil.getSessionFactory().getCurrentSession();
		retorno.beginTransaction();
		return retorno;
	}
*/
	public void setModel(int tamG, int cantG) {
		model = new CPModel();
		tamanioGrupo = tamG;
		List<int[]> tuples = getTable();
		cantGrupos = cantG;
		sol = new IntegerVariable[13][cantGrupos][tamanioGrupo];
		for (int k=0; k<cantGrupos; k++)
			for (int i=0; i<tamanioGrupo; i++) {
				sol[0][k][i] = Choco.makeIntVar("idUsuario"+i+"-grupo"+k,getIds(tuples));
				for (int j=1;j<13; j++) 
					sol[j][k][i] = Choco.makeIntVar("var_" + i+j+k,0,1);
			}
		
		//Constraint para que  solucion este dentro del dominio de usuarios
		for (int k=0; k<cantGrupos; k++){
			for (int i=0; i<tamanioGrupo; i++) {
				model.addConstraint(Choco.regular(new IntegerVariable[]{sol[0][k][i],sol[1][k][i],
																	    sol[2][k][i],sol[3][k][i],
																	    sol[4][k][i],sol[5][k][i],
																	    sol[6][k][i],sol[7][k][i],
																	    sol[8][k][i],sol[9][k][i],
																	    sol[10][k][i],sol[11][k][i],
																	    sol[12][k][i]},tuples));
				//Constraint para que no haya permutaciones de una solucion y no haya dos ids iguales
				if (i < tamanioGrupo-1)
					model.addConstraint(Choco.lt(sol[0][k][i],sol[0][k][i+1]));
			}
			//Constraint para que no haya permutaciones de una solucion 
			if (k < cantGrupos-1)
				model.addConstraint(Choco.lt(sol[0][k][0],sol[0][k+1][0]));
		}
		
		//*
		//Constraint para balancear perfiles de Myers Brigss y se cumplan todos los roles dentro de un grupo
		for (int k=0; k<cantGrupos; k++)
			for (int i=1; i<13; i++)
				if (i<5)
					model.addConstraint(Choco.and(Choco.geq(tamanioGrupo/2,Choco.sum(sol[i][k])),Choco.leq(tamanioGrupo/2,Choco.sum(sol[i][k]) )));
				else
					model.addConstraint(Choco.and(Choco.geq(tamanioGrupo/2,Choco.sum(sol[i][k])),Choco.leq(1,Choco.sum(sol[i][k]))));		
		//*/
		
		
		//Constraints para que cada usuario se asigne solo a un grupo
		IntegerVariable[] ids = new IntegerVariable[cantGrupos*tamanioGrupo];
		for (int i=0; i<cantGrupos; i++)
			for (int j=0; j<tamanioGrupo; j++)
				ids[i*tamanioGrupo+j] = sol[0][i][j];
		model.addConstraint(Choco.allDifferent(ids));
	}
	
	@SuppressWarnings("unchecked")
	public List<ConjuntoGrupo> solveGrupos(){
		solver = new CPSolver();
		
		//Se le entrega el modelo al solver
		solver.read(model);
		
		//Se llama a resolver el probelma
		solver.solve();
		
		List<ConjuntoGrupo> soluciones = new ArrayList<ConjuntoGrupo>();
		if (solver.isFeasible()) {
			int cantSol = 0;
			do {
				ConjuntoGrupo grupos = new ConjuntoGrupo();
				for (int i = 0; i < cantGrupos; i++) {
					List<Usuario> integrantes = new ArrayList<Usuario>();
					for (int j=0;j<tamanioGrupo; j++ ) 
						integrantes.add(getUsuario(solver.getVar(sol[0][i][j]).getVal()));
					Grupo grupo = new Grupo(integrantes, softConstraintSolve(integrantes));
					grupos.addGrupo(grupo);
				}
				grupos.calcPen();
				if (soluciones.size() < 10) { 
					soluciones.add(grupos);
					Collections.sort(soluciones);
				}
				else
					if (grupos.getPenalizacion() < maxPenalizacion(soluciones)) {
						soluciones.remove(soluciones.size()-1);
						soluciones.add(grupos);
						Collections.sort(soluciones);
					}
				cantSol++;
			} 
			while (solver.nextSolution() && cantSol < 5000);			
		}
		System.out.println("Cantidad de soluciones: " + soluciones.size());
		for (int i=0; i<soluciones.size(); i++)
			System.out.println(soluciones.get(i));
		System.out.println("Se termino de calcular");
		return soluciones;
	}
	
	private double maxPenalizacion(List<ConjuntoGrupo> soluciones) {
		return soluciones.get(soluciones.size()-1).getPenalizacion();
	}

	public List<int[]> getTable() {
		List<int[]> tuples = new LinkedList<int[]>();
		for (int i=0; i<usuarios.size(); i++) 
			if (cumpleRol(usuarios.get(i).getRol().getValLider()) == 1 ||
			 	cumpleRol(usuarios.get(i).getRol().getValModerador())== 1 ||
			 	cumpleRol(usuarios.get(i).getRol().getValCreador())==1 ||
			 	cumpleRol(usuarios.get(i).getRol().getValInnovador())==1 ||
			 	cumpleRol(usuarios.get(i).getRol().getValManager())==1 ||
			 	cumpleRol(usuarios.get(i).getRol().getValOrganizador())==1 ||
			 	cumpleRol(usuarios.get(i).getRol().getValEvaluador())==1 ||
			 	cumpleRol(usuarios.get(i).getRol().getValFinalizador())==1)
				tuples.add(new int[]{usuarios.get(i).getId(),
									 getMyersChoice(usuarios.get(i).getEstilo().getAttitude()),
									 getMyersChoice(usuarios.get(i).getEstilo().getPerceiving()),
									 getMyersChoice(usuarios.get(i).getEstilo().getJudging()),
									 getMyersChoice(usuarios.get(i).getEstilo().getLifestyle()),
									 cumpleRol(usuarios.get(i).getRol().getValLider()),
									 cumpleRol(usuarios.get(i).getRol().getValModerador()),
									 cumpleRol(usuarios.get(i).getRol().getValCreador()),
									 cumpleRol(usuarios.get(i).getRol().getValInnovador()),
									 cumpleRol(usuarios.get(i).getRol().getValManager()),
									 cumpleRol(usuarios.get(i).getRol().getValOrganizador()),
									 cumpleRol(usuarios.get(i).getRol().getValEvaluador()),
									 cumpleRol(usuarios.get(i).getRol().getValFinalizador()),
									});
		return tuples;
	}
	
	public int[] getIds(List<int[]> tuples) {
		int[] retorno = new int[tuples.size()];
		for (int i=0; i<tuples.size(); i++)
			retorno[i] = tuples.get(i)[0];
		return retorno;
	}

	public void setUsuarios(List<Usuario> u) {
		usuarios = u;
	}
	
/*	
	public static void main(String[] args) {
		UsrServiceImpl impl = new UsrServiceImpl();
		impl.generarDistancias();
		
		
		int tamGru = Integer.parseInt(args[0]);
		int cantGru = Integer.parseInt(args[1]); 
		
		List<ConjuntoGrupos> grupos = impl.generarGrupos(tamGru, cantGru);
		for (ConjuntoGrupos conjuntoGrupos : grupos) {
			for (Grupo g :conjuntoGrupos.getGrupos()){
				System.out.println(g);
			}
		}
	}
*/
}
