package modelo;

import modelo.formacionGrupos.CompetenciasTecnicas;
import constantes.Constantes;

public class Usuario {
	
	private int id;
	private String nombre;
	private String mail;
	private int idGrupo;
	private Rol rol;
	private Estilo estilo;
	private int penBalanceo;
	private int penCorrelacion;
	private CompetenciasTecnicas comptencias;


	public Usuario() {
		
		this.comptencias = new CompetenciasTecnicas();
	}
	
	
	public Usuario(int id) {
		
		this.id = id;
		this.comptencias = new CompetenciasTecnicas();
	}

	public Usuario(int id, String nombre, int idGrupo) {
		this.id = id;
		this.nombre = nombre;
		this.idGrupo = idGrupo;
		this.comptencias = new CompetenciasTecnicas();
	}

	public Usuario(int id, String nombre, String mail, int idGrupo) {
		this.id = id;
		this.nombre = nombre;
		this.mail = mail;
		this.idGrupo = idGrupo;
		this.comptencias = new CompetenciasTecnicas();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getIdGrupo() {
		return this.idGrupo;
	}

	public void setIdGrupo(int idGrupo) {
		this.idGrupo = idGrupo;
	}
	
	public Rol getRol() {
		return this.rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Estilo getEstilo() {
		return this.estilo;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo = estilo;
	}
/*	
	public static Usuario getUsuarioVacio() {
		Usuario retorno = new Usuario();
		retorno.setEstilo(Estilo.getEstiloVacio());
		retorno.setRol(Rol.getRolVacio());
		return retorno;
	}
*/	
	public String toString() {
		return "["+id+"]"+ this.nombre;
	}
	
	public boolean equals(Object o) {
		if (((Usuario)o).getId() == this.id)
			return true;
		return false;
		
	}

	public boolean[] getRoles() {
		boolean[] retorno = new boolean[8];
		for (int i= Constantes.LIDER ; i<= Constantes.FINALIZADOR; i++)
			retorno[i] = false;
		
		if (rol.getValLider() > Constantes.COTA_CUMPLE_ROL)
			retorno[Constantes.LIDER] = true;
		if (rol.getValModerador() > Constantes.COTA_CUMPLE_ROL)
			retorno[Constantes.MODERADOR] = true;
		if (rol.getValCreador() > Constantes.COTA_CUMPLE_ROL)
			retorno[Constantes.CREADOR] = true;
		if (rol.getValInnovador() > Constantes.COTA_CUMPLE_ROL)
			retorno[Constantes.INNOVADOR] = true;
		if (rol.getValManager() > Constantes.COTA_CUMPLE_ROL)
			retorno[Constantes.MANAGER] = true;
		if (rol.getValOrganizador() > Constantes.COTA_CUMPLE_ROL)
			retorno[Constantes.ORGANIZADOR] = true;
		if (rol.getValEvaluador() > Constantes.COTA_CUMPLE_ROL)
			retorno[Constantes.EVALUADOR] = true;
		if (rol.getValFinalizador() > Constantes.COTA_CUMPLE_ROL)
			retorno[Constantes.FINALIZADOR] = true;
		return retorno;
	}
	
	public int getPenBalanceo() {
		return penBalanceo;
	}

	public void setPenBalanceo(int penBalanceo) {
		this.penBalanceo = penBalanceo;
	}

	public int getPenCorrelacion() {
		return penCorrelacion;
	}

	public void setPenCorrelacion(int penCorrelacion) {
		this.penCorrelacion = penCorrelacion;
	}
	
	public Usuario clone(){
		Usuario u = new Usuario(this.id);
		return u;
	}

	public void agregarCompetencia( String nombre, Integer nivel ){
		
		this.comptencias.agregarCompetencia( nombre, nivel );
	}
	
	public CompetenciasTecnicas getCompetencias(){
		
		return this.comptencias;
	}



}
