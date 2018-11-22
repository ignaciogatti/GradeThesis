package modelo;

public class Interactuan {

	private InteractuanId id;
	private Topico topico;
	private Integer peso;
	private Usuario usuarioByIdUsr1;
	private Usuario usuarioByIdUsr2;
	private String nombreUsr1;
	private String nombreUsr2;
	private String topicoAux;

	public Interactuan() {
	}

	public Interactuan(InteractuanId id, Topico topico, Integer peso, Usuario usuarioByIdUsr1, Usuario usuarioByIdUsr2, String nombreUsr1, String nombreUsr2,String topicoAux) {
		this.id = id;
		this.topico = topico;
		this.peso = peso;
		this.usuarioByIdUsr1 = usuarioByIdUsr1;
		this.usuarioByIdUsr2 = usuarioByIdUsr2;
		this.nombreUsr1 = nombreUsr1;
		this.nombreUsr2 = nombreUsr2;
		this.topicoAux = topicoAux;
	}

	public InteractuanId getId() {
		return this.id;
	}

	public void setId(InteractuanId id) {
		this.id = id;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public Topico getTopico() {
		return this.topico;
	}

	public void setTopico(Topico topico) {
		this.topico = topico;
	}

	public Usuario getUsuarioByIdUsr1() {
		return this.usuarioByIdUsr1;
	}

	public void setUsuarioByIdUsr1(Usuario usuarioByIdUsr1) {
		this.usuarioByIdUsr1 = usuarioByIdUsr1;
	}

	public Usuario getUsuarioByIdUsr2() {
		return this.usuarioByIdUsr2;
	}

	public void setUsuarioByIdUsr2(Usuario usuarioByIdUsr2) {
		this.usuarioByIdUsr2 = usuarioByIdUsr2;
	}
	
	
	public String getNombreUsr1() {
		return this.nombreUsr1;
	}

	public void setNombreUsr1(String nombreUsr1) {
		this.nombreUsr1 = nombreUsr1;
	}
	
	public String getNombreUsr2() {
		return this.nombreUsr2;
	}

	public void setNombreUsr2(String nombreUsr2) {
		this.nombreUsr2 = nombreUsr2;
	}
	
	public String getTopicoAux() {
		return this.topicoAux;
	}

	public void setTopicoAux(String topico) {
		this.topicoAux = topico;
	}
	
	public boolean equals (Object o) {
		return this.nombreUsr1.equals(((Interactuan) o).getNombreUsr1()) && this.nombreUsr2.equals(((Interactuan) o).getNombreUsr2()) &&
				this.topicoAux.equals(((Interactuan) o).getTopicoAux());
	}
	
	public String toString() {
		return id.getIdUsr1() + "-" + id.getIdUsr2() + "-" + id.getTopico() + '\n';
	}

}
