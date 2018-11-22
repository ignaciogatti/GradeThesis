package modelo;

public class InteractuanId {
	
	private int idUsr1;
	private int idUsr2;
	private String topico;
	
	public InteractuanId() {
	}

	public InteractuanId(int idUsr1, int idUsr2, String topico) {
		this.idUsr1 = idUsr1;
		this.idUsr2 = idUsr2;
		this.topico = topico;
	}

	public int getIdUsr1() {
		return this.idUsr1;
	}

	public void setIdUsr1(int idUsr1) {
		this.idUsr1 = idUsr1;
	}

	public int getIdUsr2() {
		return this.idUsr2;
	}

	public void setIdUsr2(int idUsr2) {
		this.idUsr2 = idUsr2;
	}

	public String getTopico() {
		return this.topico;
	}

	public void setTopico(String topico) {
		this.topico = topico;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof InteractuanId))
			return false;
		InteractuanId castOther = (InteractuanId) other;

		return (this.getIdUsr1() == castOther.getIdUsr1())
				&& (this.getIdUsr2() == castOther.getIdUsr2())
				&& ((this.getTopico() == castOther.getTopico()) || (this
						.getTopico() != null && castOther.getTopico() != null && this
						.getTopico().equals(castOther.getTopico())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getIdUsr1();
		result = 37 * result + this.getIdUsr2();
		result = 37 * result
				+ (getTopico() == null ? 0 : this.getTopico().hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "[idUsr1=" + idUsr1 + ", idUsr2=" + idUsr2 + "]";
	}
	
	


}
