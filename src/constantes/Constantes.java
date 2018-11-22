package constantes;

import java.util.ArrayList;
import java.util.List;

import modelo.Estilo;
import modelo.Usuario;
import utilidades.filtros.Ifiltro;
import utilidades.filtros.roles.FiltroCreador;
import utilidades.filtros.roles.FiltroEvaluador;
import utilidades.filtros.roles.FiltroFinalizador;
import utilidades.filtros.roles.FiltroInnovador;
import utilidades.filtros.roles.FiltroLider;
import utilidades.filtros.roles.FiltroManager;
import utilidades.filtros.roles.FiltroModerador;
import utilidades.filtros.roles.FiltroOrganizador;
import utilidades.filtros.tipoPsicologicos.FiltroActitud;
import utilidades.filtros.tipoPsicologicos.FiltroDecision;
import utilidades.filtros.tipoPsicologicos.FiltroEmocional;
import utilidades.filtros.tipoPsicologicos.FiltroEstiloVida;
import utilidades.filtros.tipoPsicologicos.FiltroIntroversion;
import utilidades.filtros.tipoPsicologicos.FiltroIntuitivo;
import utilidades.filtros.tipoPsicologicos.FiltroPercepcion;
import utilidades.filtros.tipoPsicologicos.FiltroPerceptivo;

public class Constantes {

	public static final String DB_DRIVER_NAME = "com.mysql.jdbc.Driver";
	public static final String DB_USER = "root";
	public static final String DB_PASSWORD = "";
	public static final String DB_NAME = "dbtesis";
	public static final String DB_PORT = "3306";
	public static final String DATA_BASE_URL = "jdbc:mysql://localhost:" + DB_PORT + "/" + DB_NAME;

	
	public static final int COTA_CUMPLE_ROL = 25;
	public static final int COTA_DICOTOMIA_MB = 0;
	public static final int COTAINF_BALANCEADO = 20;
	public static final int COTASUP_BALANCEADO = 36;
	public static final int PENALIZACION_REDSOCIAL = 1;
	public static final int PENALIZACION_BALANCEO = 2;
	public static final int PENALIZACION_CORRELACION = 3;
	public static final int MAX_SOLUCIONES = 10;

	public static final int LIDER = 0; 
	public static final int MODERADOR = 1; 
	public static final int CREADOR = 2; 
	public static final int INNOVADOR= 3; 
	public static final int MANAGER = 4; 
	public static final int ORGANIZADOR = 5;
	public static final int EVALUADOR = 6;
	public static final int FINALIZADOR= 7;


	/*
	 *Estilo psicologico ideal para cada rol 
	 * 	E=0,S=0,T=0,J=0,I=1,N=1,F=1,P=1;
	 * 
	 * Estilo (actitud, percepcion, decision, estilo de vida) 
	 */
	private static Estilo[] TIPO_MODERADOR ={new Estilo(0,1,0,0), new Estilo(0,1,1,0)};
	private static Estilo[] TIPO_INNOVADOR = {new Estilo(0,0,1,1), new Estilo(0,1,1,1)};
	private static Estilo[] TIPO_CREADOR = {new Estilo(1,0,1,0), new Estilo(1,1,1,0)};
	private static Estilo[] TIPO_MANAGER= {new Estilo(0,1,0,1), new Estilo(0,0,0,1), new Estilo(0,0,0,0), new Estilo(0,0,1,0)};
	private static Estilo[] TIPO_EVALUADOR = {new Estilo(1,0,0,0), new Estilo(1,1,0,0)};
	private static Estilo[] TIPO_ORGANIZADOR= {new Estilo(1,0,0,0), new Estilo(1,1,0,0), new Estilo(0,0,0,0), new Estilo(0,0,1,0)};
	private static Estilo[] TIPO_LIDER = {new Estilo(0,0,0,0), new Estilo(0,0,1,0)};
	private static Estilo[] TIPO_FINALIZADOR = {new Estilo(0,0,0,0), new Estilo(0,0,1,0)};
	

	public static Estilo[][] estilo_ideal ={TIPO_LIDER, TIPO_MODERADOR, TIPO_CREADOR, TIPO_INNOVADOR,
											TIPO_MANAGER, TIPO_ORGANIZADOR, TIPO_EVALUADOR, TIPO_FINALIZADOR};
	


	
}


