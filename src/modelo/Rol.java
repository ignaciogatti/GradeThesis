package modelo;

public class Rol {
	
	private int idUsr;
	private Integer valModerador;
	private Integer valCreador;
	private Integer valInnovador;
	private Integer valManager;
	private Integer valOrganizador;
	private Integer valEvaluador;
	private Integer valFinalizador;
	private Integer valLider;

	public Rol() {
	}

	public Rol(Integer valModerador, Integer valCreador,
			Integer valInnovador, Integer valManager, Integer valOrganizador,
			Integer valEvaluador, Integer valFinalizador, Integer valLider) {
		this.valModerador = valModerador;
		this.valCreador = valCreador;
		this.valInnovador = valInnovador;
		this.valManager = valManager;
		this.valOrganizador = valOrganizador;
		this.valEvaluador = valEvaluador;
		this.valFinalizador = valFinalizador;
		this.valLider = valLider;
	}

	public int getIdUsr() {
		return this.idUsr;
	}

	public void setIdUsr(int idUsr) {
		this.idUsr = idUsr;
	}

	public Integer getValModerador() {
		return this.valModerador;
	}

	public void setValModerador(Integer valModerador) {
		this.valModerador = valModerador;
	}

	public Integer getValCreador() {
		return this.valCreador;
	}

	public void setValCreador(Integer valCreador) {
		this.valCreador = valCreador;
	}

	public Integer getValInnovador() {
		return this.valInnovador;
	}

	public void setValInnovador(Integer valInnovador) {
		this.valInnovador = valInnovador;
	}

	public Integer getValManager() {
		return this.valManager;
	}

	public void setValManager(Integer valManager) {
		this.valManager = valManager;
	}

	public Integer getValOrganizador() {
		return this.valOrganizador;
	}

	public void setValOrganizador(Integer valOrganizador) {
		this.valOrganizador = valOrganizador;
	}

	public Integer getValEvaluador() {
		return this.valEvaluador;
	}

	public void setValEvaluador(Integer valEvaluador) {
		this.valEvaluador = valEvaluador;
	}

	public Integer getValFinalizador() {
		return this.valFinalizador;
	}

	public void setValFinalizador(Integer valFinalizador) {
		this.valFinalizador = valFinalizador;
	}

	public Integer getValLider() {
		return this.valLider;
	}

	public void setValLider(Integer valLider) {
		this.valLider = valLider;
	}
/*	
	public static Rol getRolVacio() {
		Rol retorno = new Rol(); 
		retorno.setValCreador(0);
		retorno.setValEvaluador(0);
		retorno.setValFinalizador(0);
		retorno.setValInnovador(0);
		retorno.setValLider(0);
		retorno.setValManager(0);
		retorno.setValModerador(0);
		retorno.setValOrganizador(0);
		return retorno;
	}
*/

}
