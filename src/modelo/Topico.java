package modelo;

public class Topico {
	
	private String topico;

	public Topico() {
	}

	public Topico(String topico) {
		this.topico = topico;
	}

	public String getTopico() {
		return this.topico;
	}

	public void setTopico(String topico) {
		this.topico = topico;
	}
	
	public boolean equals(Object o) {
		return this.topico.equals(((Topico) o).getTopico());
	}


}
