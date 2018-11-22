package solvers.PseudoBoolean.otroPB;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import modelo.Usuario;

import restricciones.IRestricciones;
import restricciones.PaqueteRestricciones;

/*
 * Esta clase se encarga de dar el formato correcto al problema para que pueda ser 
 * leído por el PBSolver
 */
public class ProblemDefinition {

	//DEFINIR UNA HASHMAP PARA MAPEAR VARIABLES A USUARIOS
	private HashMap<Usuario, Integer> variables;
	private PaqueteRestricciones cRestricciones;
	private String objetivo;
	private String restricciones;
	private int cantidadVariables = 1;
	private Integer cantRestricciones = 0;
	private Integer cantProductos = 0;
	private Integer tamProductos = 0;
	
	private FileWriter log;
	private BufferedWriter bw;
	private PrintWriter pw;


	public ProblemDefinition(PaqueteRestricciones cRestricciones, int cant) {
		// TODO Auto-generated constructor stub
		this.cRestricciones = cRestricciones;
		this.objetivo = "min: ";
		this.restricciones = "";
		this.cantidadVariables = cant;
		this.variables = new HashMap<>();
		
		
		try {
			log = new FileWriter("C:\\Users\\gatti\\Desktop\\Ignacio\\Ing. Sistemas\\Proyecto Beca\\Sat4j\\formacionGrupos.opb");
			bw = new BufferedWriter(log);
			pw = new PrintWriter(bw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void definirProblema(){
		
		this.cRestricciones.escribirRestricciones(this);
		pw.printf("* #variable= %d #constraint= %d #product= %d sizeproduct= %d\n ", this.cantidadVariables, this.cantRestricciones, this.cantProductos, this.tamProductos );
		pw.println(objetivo + ";");
		pw.println();
		pw.println(restricciones);
		this.close();
	}
		
	public void aumentarCantProd(){
		this.cantProductos++;
	}
	
	public void aumentarTamProd(int a){
		this.tamProductos += a;
	}
	
	
	private int getIndice(Usuario u){
		
		if(variables.containsKey(u)){
			return variables.get(u);
		}else{
			int indice = this.cantidadVariables++;
			variables.put(u, new Integer(indice));
			return indice;
		}

	}
	
	public void agregarRestriccionLinealIgualdad( Usuario[] usuarios, int cota){
		 
		for (int i = 0 ; i< usuarios.length; i++) {
			restricciones += "+1 x" + this.getIndice(usuarios[i]) + " ";
		}
		 restricciones += "= " + cota +" ;\n";
		 this.cantRestricciones++;
	}

	public void agregarRestriccionLinealMenorIgual( Usuario[] usuarios, int cota){
		 
		for (int i = 0 ; i< usuarios.length; i++) {
			restricciones += "+1 x" + this.getIndice(usuarios[i]) + " ";
		}
		 restricciones += "<= " + cota +" ;\n";
		 this.cantRestricciones++;
	}

	public void agregarRestriccionLinealMayorIgual( Usuario[] usuarios, int cota){
		 
		for (int i = 0 ; i< usuarios.length; i++) {
			restricciones += "+1 x" + this.getIndice(usuarios[i]) + " ";
		}
		 restricciones += ">= " + cota +" ;\n";
		 this.cantRestricciones++;
	}
	
	//Defino esta interfaz porque linealizo los literales agregando la variable aux
	public void agregarRestriccionAND(Usuario[] literales, Usuario aux){
		
		restricciones += "+1 x" + this.getIndice(aux) + " ";
		for(int i = 0; i < literales.length; i++){
			restricciones += "-1 x" + this.getIndice(literales[i]) + " ";
		}
		restricciones += ">= " +( (-1)* (literales.length - 1 ) ) + " ;\n";
		this.cantRestricciones++;

		restricciones += "-" + literales.length + " x" + this.getIndice(aux) + " ";
		for(int i = 0; i < literales.length; i++){
			restricciones += "+1 x" + this.getIndice(literales[i]) + " ";
		}
		restricciones += ">= 0 ;\n";
		this.cantRestricciones++;
		
	}
	
	public void agregarAObjetivo(Usuario u, int costo){
		this.objetivo += "+" + costo + " x" + this.getIndice(u) + " ";
	}

	public void agregarAObjetivoTerminoAND(Usuario[] u, int costo){
		
		this.objetivo += "+" + costo;
		for(int i = 0; i < u.length; i++)
			this.objetivo +=  " x" + this.getIndice(u[i]) + " ";
		this.cantProductos++;
		this.tamProductos += u.length;
	}
	
	public void agregarAObjetivo( String obj){
		this.objetivo += obj;
	}
	
	private void close(){

		pw.flush();
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		
		this.variables.clear();
		this.objetivo = "min: ";
		this.restricciones = "";
		this.cantProductos = 0;
		this.cantRestricciones = 0;
		this.tamProductos = 0;
		this.cantidadVariables = 1;
		String s = "";
		
		this.cRestricciones.reset();
		this.cRestricciones.escribirRestricciones(this);
		
		s += "* #variable= "+ this.cantidadVariables + " #constraint= "+ this.cantRestricciones +" #product= " + this.cantProductos + " sizeproduct= "+ this.tamProductos +" \n " ;
		s += objetivo + "; \n";
		s += restricciones;

		
		return s;
	}
		

}
