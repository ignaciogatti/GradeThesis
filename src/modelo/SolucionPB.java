package modelo;

public class SolucionPB {

	private int[] usuarios;
	private int costo;
	
	public SolucionPB() {

	}	
	
	public SolucionPB(int[] usuarios, int costo) {

		this.usuarios = new int[usuarios.length];
		for(int i = 0; i < usuarios.length; i++)
			this.usuarios[i] = usuarios[i];
		this.costo = costo;
	}



	public int getCosto() {
		return costo;
	}
	
	public int[] getUsuarios() {
		return usuarios;
	}
	
	public void setCosto(int costo) {
		this.costo = costo;
	}
	
	public void setUsuarios(int[] usuarios) {
		this.usuarios = usuarios;
	}

}
