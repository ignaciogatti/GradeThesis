
package test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.SolverFactory;

import restricciones.IRestricciones;
import restricciones.PaqueteRestricciones;
import restricciones.RestriccionesControlAsignacion;
import restricciones.RestriccionesCorreMumTiposPsico;
import restricciones.RestriccionesGenerales;
import restricciones.RestriccionesNGruposTemplate;
import restricciones.RestriccionesRedSocial;
import restricciones.RestriccionesRolBalanceado;
import restricciones.RestriccionesRoles;
import restricciones.RestriccionesTiposPsicologicos;
import restricciones.RestriccionesUsuariosDescartados;
import restricciones.RestriccionesUsuariosObligatorios;
import restricciones.formacionGrupos.RestriccionesPuesto;
import solvers.CHOCOSolver.CHOCOSolver;
import solvers.PseudoBoolean.otroPB.PBSolver;
import solvers.PseudoBoolean.otroPB.ProblemDefinition;
import solvers.ViejosPBSolver.OtroPBIterator;
import solvers.ViejosPBSolver.PBSolverIterator;
import solvers.ViejosPBSolver.PseudoBooleanSolver;
import utilidades.DecodificadorPB;
import utilidades.Utilidades;
import utilidades.filtros.FiltroAnd;
import utilidades.filtros.competencias.FiltroCompetencia;

import modelo.ConjuntoGrupo;
import modelo.Distancia;
import modelo.DistanciaAB;
import modelo.Grupo;
import modelo.InteractuanId;
import modelo.RedSocial;
import modelo.Usuario;

import configuracion.CSVConfigurador;
import configuracion.Configurador;
import configuracion.DBConfigurador;
import constantes.Constantes;
import constantes.VariablesSistema;
import db.DBInterface;
import db.MySQLDBInterface;

public class Prueba {

	
	public static int cantIntegrantes = 0;
	public static int cantGrupos = 0;
	/**
	 * @param args
	 * args[0]: cantidad de integrantes
	 * args[1]: cantidad de grupos
	 * args[2]: activa teoria de roles balanceados
	 * args[3]: activa teoria de correlacion
	 * args[4]: activa teoria de red social
	 * args[5]: activa listar usuarios por rol y perfil
	 * args[6]: tiempo maximo de ejecucion
	 */
	public static void main(String[] args) {
		
//		cantIntegrantes = Integer.parseInt(args[0]);
//		cantGrupos = Integer.parseInt(args[1]);
		
		cantIntegrantes = 4;
		cantGrupos = 4;
		
		DBConfigurador conf = new DBConfigurador("C:\\Users\\gatti\\Desktop\\Ignacio\\Ing. Sistemas\\Proyecto Beca\\perfiles.csv");
		
		//Lista de usuarios para trabajar; es compartida por todas las restricciones
		List<Usuario> usuarios = conf.getUsuarios();
		
		//Lista para que trabaje el CHOCO solver
		List<Usuario> lusuarios = conf.getUsuarios();
		List<InteractuanId> interacciones = conf.getInteracciones();
		RedSocial red = new RedSocial(usuarios, interacciones);
		
		System.out.println("Cantidad de integrantes : " + cantIntegrantes);
		System.out.println();
		System.out.println("Cantidad de grupos : " + cantGrupos);
		

		if ( args[ 5 ].equals("1") ){
			System.out.println("----------------------------Listas por roles----------------------------------");
			System.out.println();
			System.out.println("Cota para roles: " + Constantes.COTA_CUMPLE_ROL );
			System.out.println();
			RestriccionesRoles restriccionesRoles = 
					new RestriccionesRoles(cantIntegrantes, 0, usuarios);
			System.out.println(restriccionesRoles.getUsuarios());
			System.out.println();
			System.out.println("----------------------------Listas por perfiles psicologicos----------------------------------");
			System.out.println();
			System.out.println("Cota para perfiles: "+ Constantes.COTA_DICOTOMIA_MB );
			System.out.println();
			RestriccionesTiposPsicologicos restriccionesTiposPiscologicos = 
					new RestriccionesTiposPsicologicos(cantIntegrantes, 0, usuarios);
			System.out.println(restriccionesTiposPiscologicos.getUsuarios());
			System.out.println();
			System.out.println();

		}
	
		System.out.println();
		System.out.println("----------------------PseudoBoolean Solver------------------------");
		System.out.println();
		
		
		//defino a partir de que numero se pueden crear variables auxiliares
		VariablesSistema.setVariableAuxliar( lusuarios.size() * cantGrupos );
		
		PaqueteRestricciones paquetes = new PaqueteRestricciones("Paquetes de teoria");
		for( int i = 0; i < cantGrupos; i++){
			paquetes.agregarRestriccion(getPaqueteGrupo(i, lusuarios, red, 
					args[2].equals("1"), args[3].equals("1"), args[4].equals("1") ) );
		}
		
		RestriccionesControlAsignacion restriccionesControlAsignacion = 
				new RestriccionesControlAsignacion(cantGrupos, lusuarios);
		
		paquetes.agregarRestriccion(restriccionesControlAsignacion);
//		paquetes.agregarRestriccion( restriccionesUsuariosDescartados );
		IPBSolver solver = SolverFactory.newDefaultOptimizer();
/*
		System.out.println("------------Solver utilizado: DefaultNonNormalized----------------");
		
		PBSolverIterator pb = new PBSolverIterator(solver,3600, paquetes, 
				usuarios, cantIntegrantes, cantGrupos);
		
//		OtroPBIterator pb = new OtroPBIterator(solver,500, paquetes, usuarios, cantIntegrantes, cantGrupos);
		System.out.println(pb);
		TimeWatch watch = TimeWatch.start();
		long passedTimeInMs = watch.time();
		long passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
		long passedTimeInMinutes = watch.time(TimeUnit.MINUTES);
		System.out.println("Initial time (miliseconds): "+passedTimeInMs);
		System.out.println("Initial time (seconds): "+passedTimeInSeconds);
		System.out.println();
		
		Grupo grupo = pb.solve();
//		List<Grupo> grupos= pb.solveList();

//		ConjuntoGrupo grupos = pb.solveNGrupos( );

//		List<ConjuntoGrupo> nGrupos = pb.solveNGrupoList();
		
		passedTimeInMs = watch.time();
		passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
		passedTimeInMinutes = watch.time(TimeUnit.MINUTES);
		System.out.println("Current time (miliseconds): "+passedTimeInMs);
		System.out.println("Current time (seconds): "+passedTimeInSeconds);
		System.out.println("Current time (minutes): "+passedTimeInMinutes);
		System.out.println();
		
		
		
		System.out.println(grupo);
/*		
//		for(ConjuntoGrupo grupos : nGrupos){
			System.out.println();
			for( Grupo g : grupos.getGrupos() ){
				System.out.println(g);
			}
			System.out.println("Penalizacion: " + grupos.getPenalizacion());
			System.out.println();
//		}
/*		
		int sumatoria = 0;
		for (int i =1; i<grupo.size(); i++) 
			sumatoria += grupo.size()-i;
		double error=((double) pb.bestValue())/(grupo.size()*(Constantes.PENALIZACION_BALANCEO + Constantes.PENALIZACION_CORRELACION) + sumatoria);
		System.out.println("Costo de la solucion: " + error);
		System.out.println("Es optima: " + pb.isOptimal());
/*/	
		System.out.println("----------------------Otro PseudoBoolean Solver------------------------");

		int timeOut = Integer.valueOf(args[6]);
		solver.reset();
		solvers.PseudoBoolean.PBSolver anotherSolver = new solvers.PseudoBoolean.PBSolver
				( solver, timeOut, paquetes, usuarios, cantIntegrantes, cantGrupos);

		System.out.println(anotherSolver);

		
		TimeWatch watch = TimeWatch.start();
		long passedTimeInMs = watch.time();
		long passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
		long passedTimeInMinutes = watch.time(TimeUnit.MINUTES);
		System.out.println("Initial time (miliseconds): "+passedTimeInMs);
		System.out.println("Initial time (seconds): "+passedTimeInSeconds);
		System.out.println();
		
		ConjuntoGrupo anotherGrupos = anotherSolver.solve( );

		passedTimeInMs = watch.time();
		passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
		passedTimeInMinutes = watch.time(TimeUnit.MINUTES);
		System.out.println("Current time (miliseconds): "+passedTimeInMs);
		System.out.println("Current time (seconds): "+passedTimeInSeconds);
		System.out.println("Current time (minutes): "+passedTimeInMinutes);
		System.out.println();

		if (anotherGrupos != null){
			System.out.println();
			for( Grupo g : anotherGrupos.getGrupos() ){
				System.out.println(g);
			}
			System.out.println("Penalizacion: " + anotherGrupos.getPenalizacion());
			System.out.println();
			conf.guardarGrupos(anotherGrupos);
		}


			
/*		
		System.out.println("----------------------Otro PseudoBoolean Solver------------------------");
		System.out.println();
		
		ProblemDefinition pd = new ProblemDefinition(paquetes, usuarios.size());
//		System.out.println(pd);
		PBSolver anotherSolver = new PBSolver(solver, lusuarios);

		watch = TimeWatch.start();
		passedTimeInMs = watch.time();
		passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
		passedTimeInMinutes = watch.time(TimeUnit.MINUTES);
		System.out.println("Initial time (miliseconds): "+passedTimeInMs);
		System.out.println("Initial time (seconds): "+passedTimeInSeconds);
		System.out.println();
		
		List<Grupo> anotherGrupos = anotherSolver.solve(pd);

		passedTimeInMs = watch.time();
		passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
		passedTimeInMinutes = watch.time(TimeUnit.MINUTES);
		System.out.println("Current time (miliseconds): "+passedTimeInMs);
		System.out.println("Current time (seconds): "+passedTimeInSeconds);
		System.out.println("Current time (minutes): "+passedTimeInMinutes);
		System.out.println();

		
		if (anotherGrupos != null){
			for(Grupo g : anotherGrupos){
					
					System.out.println(g);
				}
				System.out.println();
				System.out.println("------------------------------------");
				System.out.println();
		}
		System.out.println();
//*/		System.out.println("-------------------------WPCS Solver------------------------------");
		CHOCOSolver chocoSolver = new CHOCOSolver(lusuarios, red);
//		CHOCOSolver chocoSolver = new CHOCOSolver(lusuarios);
		chocoSolver.setUsuarios(lusuarios);
	    chocoSolver.setModel(cantIntegrantes , cantGrupos);
	    
	    TimeWatch watch1 = TimeWatch.start();
		long passedTimeInMs1 = watch1.time();
		long passedTimeInSeconds1 = watch1.time(TimeUnit.SECONDS);
		long passedTimeInMinutes1 = watch1.time(TimeUnit.MINUTES);
		System.out.println("Initial time (miliseconds): "+passedTimeInMs1);
		System.out.println("Initial time (seconds): "+passedTimeInSeconds1);
		System.out.println();

		chocoSolver.solveGrupos();
		
		passedTimeInMs1 = watch1.time();
		passedTimeInSeconds1 = watch1.time(TimeUnit.SECONDS);
		passedTimeInMinutes1 = watch1.time(TimeUnit.MINUTES);
		System.out.println("Current time (miliseconds): "+passedTimeInMs1);
		System.out.println("Current time (seconds): "+passedTimeInSeconds1);
		System.out.println("Current time (minutes): "+passedTimeInMinutes1);
		System.out.println();
//*/

	}

	private static PaqueteRestricciones getPaqueteGrupo(int id, List<Usuario> usuarios, RedSocial red,
			boolean activarRolBalanceado, boolean activarCorrelacionMBMum, boolean activarRedSocial){
		
		PaqueteRestricciones paquete = new PaqueteRestricciones("Grupo " + id );
		//defino los paquetes
		PaqueteRestricciones rolesBalanceados = 
				new PaqueteRestricciones("Teoria Roles Balanceados");
		PaqueteRestricciones correlacionMBMum = 
				new PaqueteRestricciones("Teoria Correlacion MB Mum");
		PaqueteRestricciones redSocial = 
				new PaqueteRestricciones("Teoria Red Social");
		
		//defino las restricciones y las agrupo
		RestriccionesNGruposTemplate restriccionesGenerales = 
				new RestriccionesGenerales(cantIntegrantes, usuarios, id);
		rolesBalanceados.agregarRestriccion(restriccionesGenerales);
		correlacionMBMum.agregarRestriccion(restriccionesGenerales);
		redSocial.agregarRestriccion(restriccionesGenerales);
		RestriccionesNGruposTemplate restriccionesRoles = 
				new RestriccionesRoles(cantIntegrantes, id, usuarios);
		rolesBalanceados.agregarRestriccion(restriccionesRoles);
		correlacionMBMum.agregarRestriccion(restriccionesRoles);
		redSocial.agregarRestriccion(restriccionesRoles);
		RestriccionesNGruposTemplate restriccionesTiposPiscologicos = 
				new RestriccionesTiposPsicologicos(cantIntegrantes, id ,usuarios);
		rolesBalanceados.agregarRestriccion(restriccionesTiposPiscologicos);
		correlacionMBMum.agregarRestriccion(restriccionesTiposPiscologicos);
		redSocial.agregarRestriccion(restriccionesTiposPiscologicos);
		RestriccionesNGruposTemplate restriccionesRolBalanceado = 
				new RestriccionesRolBalanceado(usuarios, id);
		
/*		//prueba soft/hard
		restriccionesRoles.setSoft(true);
		restriccionesRoles.setCosto(5);

		restriccionesTiposPiscologicos.setSoft(true);
		restriccionesTiposPiscologicos.setCosto(5);
//*/
		//prueba para completar grupos
/*
		Grupo aComletar = new Grupo();
		aComletar.addIntegrante( lusuarios.get(0));
		aComletar.addIntegrante( lusuarios.get(3));
		List<Grupo> gruposACompletar = new ArrayList<>();
		gruposACompletar.add( aComletar );
		RestriccionesUsuariosObligatorios restriccionesUsuariosObligatorios = 
				new RestriccionesUsuariosObligatorios(lusuarios, gruposACompletar);
		
		RestriccionesUsuariosDescartados restriccionesUsuariosDescartados = 
				new RestriccionesUsuariosDescartados(lusuarios, gruposACompletar, cantGrupos);
*/		
		rolesBalanceados.agregarRestriccion(restriccionesRolBalanceado);
		RestriccionesNGruposTemplate restriccionesCorrelacion = 
				new RestriccionesCorreMumTiposPsico(usuarios, id);
		correlacionMBMum.agregarRestriccion(restriccionesCorrelacion);
		RestriccionesNGruposTemplate restriccionesRedSocial = 
				new RestriccionesRedSocial(red, usuarios, id);
		redSocial.agregarRestriccion(restriccionesRedSocial);
		
		//Eligo que paquetes agregar
		
		if( activarRolBalanceado )
			paquete.agregarRestriccion(rolesBalanceados);
		if( activarCorrelacionMBMum )
			paquete.agregarRestriccion(correlacionMBMum);
		if( activarRedSocial)
			paquete.agregarRestriccion(redSocial);
		
//		if( id == 0){
//			paquete.agregarRestriccion( definirPuestos( id, usuarios ) );
//		}

		return paquete;
	}
	
	private static PaqueteRestricciones definirPuestos( int id, List<Usuario> usuarios ){
		
		PaqueteRestricciones puestos = new PaqueteRestricciones("Puestos de trabajo ");
		FiltroCompetencia competenciaJava = new FiltroCompetencia(2, "java");
		FiltroCompetencia competenciaMySql = new FiltroCompetencia(1, "MySQL");
		FiltroCompetencia competenciaIngles = new FiltroCompetencia(1, "Ingles");
		
		FiltroAnd competenciasNoExcluyentes = new FiltroAnd(competenciaMySql, competenciaIngles);
//		competenciasNoExcluyentes= new FiltroAnd(competenciasNoExcluyentes, competenciaJava);
		RestriccionesPuesto puestoProgramador = 
				new RestriccionesPuesto(competenciaJava, competenciasNoExcluyentes, usuarios, 
						2, 5, id, "Programadores");
		puestos.agregarRestriccion(puestoProgramador);
		return puestos;
		
	}
}
