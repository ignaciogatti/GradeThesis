package modelo;

import java.util.List;

public class DistanciaAB {
	
	private Usuario u1;
	private Usuario u2;
	private int peso;
	
	
	
	public DistanciaAB(Usuario u1, Usuario u2, int peso) {
		this.u1 = u1;
		this.u2 = u2;
		this.peso = peso;
	}
	
	
	
	public DistanciaAB(Usuario u1, Usuario u2) {
		this.u1 = u1;
		this.u2 = u2;
	}



	public Usuario getU1() {
		return u1;
	}
	public void setU1(Usuario u1) {
		this.u1 = u1;
	}
	public Usuario getU2() {
		return u2;
	}
	public void setU2(Usuario u2) {
		this.u2 = u2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}

	
	@Override
	public boolean equals(Object obj) {

		if (obj == null)
			return false;
		DistanciaAB other = (DistanciaAB) obj;
/*		if (u1 == null) {
			if (other.u1 != null)
				return false;
		} else if ( ( !u1.equals(other.u1) )  &&  ( !u2.equals(other.u1) ) )
			return false;
		if (u2 == null) {
			if (other.u2 != null)
				return false;
		} else if ( ( !u2.equals(other.u2) ) && ( !u1.equals(other.u2) ) )
			return false;
*/
		if ( ( u1.equals(u2) ) || ( other.u1.equals(other.u2) ) ){
			if( ( u1.equals(other.u1) ) && ( u2.equals(other.u2) ) ){
				System.out.println("Entre por esta rama");
				return true;
			}else
				return false;
		}
		
		if( ( ( u1.equals(other.u1) ) || ( u2.equals(other.u1) ) ) &&
			( ( u2.equals(other.u2) ) || ( u1.equals(other.u2) ) ) )
			return true;
		return false;
	}


	public boolean estaContenida( List<Usuario> usuarios){
		
		if( ( usuarios.contains( u1 ) ) && ( usuarios.contains( u2 ) ) )
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "Nodo [u1=" + u1 + ", u2=" + u2 + ", peso=" + peso + "]";
	}	

	
}
