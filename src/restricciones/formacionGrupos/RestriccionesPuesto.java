package restricciones.formacionGrupos;

import java.util.ArrayList;
import java.util.List;

import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;

import restricciones.IRestricciones;
import solvers.PseudoBoolean.otroPB.ProblemDefinition;
import utilidades.filtros.Ifiltro;


public class RestriccionesPuesto implements IRestricciones{
	
	private Ifiltro competenciasExcluyentes;
	private Ifiltro competenciasNoExcluyentes;
	private CorrelacionPerfiles perfil;
	private CorrelacionRol rol;
	private List< Usuario > lusuarios;
	private int cantidadPuestos;
	private int costo;
	private int idGrupo;
	private boolean ejecutada = false;
	private String nombre;


	public RestriccionesPuesto( Ifiltro competenciasExcluyentes,
			Ifiltro competenciasNoExcluyentes, List<Usuario> listaTrabajadores, 
			int cota, int costo, int idGrupo, String nombre) {

		this.competenciasExcluyentes = competenciasExcluyentes;
		this.competenciasNoExcluyentes = competenciasNoExcluyentes;
		this.lusuarios = listaTrabajadores;
		this.costo = costo;
		this.cantidadPuestos = cota;
		this.idGrupo = idGrupo;
		this.nombre = nombre;
	}

		
	public RestriccionesPuesto(List<Usuario> listaTrabajadores, 
			int costo, int cota, int idGrupo, String nombre) {

		this.lusuarios = listaTrabajadores;
		this.costo = costo;
		this.cantidadPuestos = cota;
		this.idGrupo = idGrupo;
		this.nombre = nombre;
	}
	

	public void setCompetenciasExcluyentes( Ifiltro competenciasExcluyentes) {
		this.competenciasExcluyentes = competenciasExcluyentes;
	}

	public void setCompetenciasNoExcluyentes( Ifiltro competenciasNoExcluyentes) {
		this.competenciasNoExcluyentes = competenciasNoExcluyentes;
	}

	public void setPerfil(CorrelacionPerfiles perfil) {
		this.perfil = perfil;
	}

	public void setRol(CorrelacionRol rol) {
		this.rol = rol;
	}


	protected List< Usuario > getTrabajadoresCompetentes(){
		
		List< Usuario > trabajadores = new ArrayList<>();
		if(this.competenciasExcluyentes != null){
			for( Usuario u : this.lusuarios ){
				if( this.competenciasExcluyentes.cumple( u ) ){
					trabajadores.add( u );
				}
			}
			System.out.println(trabajadores);
		}else{
			trabajadores.addAll(lusuarios);
		}
			
		if( ( this.perfil != null ) && ( !this.perfil.isSoft() ) ){
			trabajadores = this.perfil.getUsuariosPorEstilo(trabajadores);
			
		}
		
		if( ( this.rol != null ) && (!this.rol.isSoft()) ){
			trabajadores =this.rol.getUsuariosPorRol(trabajadores);
		}
		return trabajadores;
	}

	@Override
	public void definirRestricciones(DependencyHelper<Usuario, String> helper)
			throws ContradictionException {
		
		
	}

	@Override
	public void definirRestriccionesParaNGrupos( DependencyHelper<Integer, String> helper )
			throws ContradictionException {

		Integer[] trabajadoresCompetentes = new Integer[ this.getTrabajadoresCompetentes().size() ];
		int i = 0;
		for( Usuario t : this.getTrabajadoresCompetentes() ){
			trabajadoresCompetentes[ i++ ] = (this.idGrupo * lusuarios.size()) + t.getId();
			if( ( this.competenciasNoExcluyentes != null )&& (!this.competenciasNoExcluyentes.cumple(t) ) ){
				helper.addToObjectiveFunction( (this.idGrupo * lusuarios.size()) + t.getId(), 
						this.costo );
			}
			if( ( this.rol != null ) && ( this.rol.isSoft() ) && ( !this.rol.cumpleRol( t ) ) ){
				helper.addToObjectiveFunction( (this.idGrupo * lusuarios.size()) + t.getId(),
						this.rol.getCosto() );
			}
			if( ( this.perfil != null ) && ( this.perfil.isSoft() ) && ( !this.perfil.cumplePerfil( t ) ) ){
				helper.addToObjectiveFunction( (this.idGrupo * lusuarios.size()) + t.getId(), 
						this.perfil.getCosto() );
			}
		}
		for (int j = 0; j < trabajadoresCompetentes.length; j++) {
			System.out.print( trabajadoresCompetentes[j] + " || ");
		}
		System.out.println();
		helper.atLeast("Restriccion para Puesto ", this.cantidadPuestos, trabajadoresCompetentes );

	}

	@Override
	public void escribirRestricciones(ProblemDefinition pd) {

	}

	@Override
	public boolean esPosibleFormarNgrupos() {

		return true;
	}

	@Override
	public boolean fueEjecutada() {
		
		return this.ejecutada;
	}

	@Override
	public void reset() {

		this.ejecutada = false;
	}

	@Override
	public int getCosto(List<Usuario> grupo, int id) {

		if( this.idGrupo == id ){
			int costo = 0;
			for( Usuario u : grupo){
				//Observar que el costo es el mismo para los usuarios que no tienen 1 o n competencias
				if( !this.competenciasNoExcluyentes.cumple( u ) ){
					costo += this.costo;
				}
			}
			return costo;
		}
		return 0;
	}
	
	public String toString(){
		
		return "Restriccion para el puesto " + nombre;
	}

}
