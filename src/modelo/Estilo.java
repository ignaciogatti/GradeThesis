package modelo;

public class Estilo {
	
	private int idUsr;
	
	private Integer attitude;
	private Integer perceiving;
	private Integer judging;
	private Integer lifestyle;

	public Estilo() {
	}

	public Estilo(Integer attitude, Integer perceiving,
			Integer judging, Integer lifestyle) {
		this.attitude = attitude;
		this.perceiving = perceiving;
		this.judging = judging;
		this.lifestyle = lifestyle;
	}

	public int getIdUsr() {
		return this.idUsr;
	}

	public void setIdUsr(int idUsr) {
		this.idUsr = idUsr;
	}

	public Integer getAttitude() {
		return this.attitude;
	}

	public void setAttitude(Integer attitude) {
		this.attitude = attitude;
	}

	public Integer getPerceiving() {
		return this.perceiving;
	}

	public void setPerceiving(Integer perceiving) {
		this.perceiving = perceiving;
	}

	public Integer getJudging() {
		return this.judging;
	}

	public void setJudging(Integer judging) {
		this.judging = judging;
	}

	public Integer getLifestyle() {
		return this.lifestyle;
	}

	public void setLifestyle(Integer lifestyle) {
		this.lifestyle = lifestyle;
	}


	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estilo other = (Estilo) obj;
		if (attitude == null) {
			if (other.attitude != null)
				return false;
		} else if (!attitude.equals(other.attitude))
			return false;
		if (judging == null) {
			if (other.judging != null)
				return false;
		} else if (!judging.equals(other.judging))
			return false;
		if (lifestyle == null) {
			if (other.lifestyle != null)
				return false;
		} else if (!lifestyle.equals(other.lifestyle))
			return false;
		if (perceiving == null) {
			if (other.perceiving != null)
				return false;
		} else if (!perceiving.equals(other.perceiving))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Estilo [attitude=" + attitude + ", perceiving=" + perceiving
				+ ", judging=" + judging + ", lifestyle=" + lifestyle + "]";
	}
	
	
/*	
	public static Estilo getEstiloVacio() {
		Estilo retorno = new Estilo();
		retorno.setAttitude(0);
		retorno.setJudging(0);
		retorno.setLifestyle(0);
		retorno.setPerceiving(0);
		return retorno;
	}
*/

}
