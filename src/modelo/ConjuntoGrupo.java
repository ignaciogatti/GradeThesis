package modelo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConjuntoGrupo implements Comparable{
	
	private List<Grupo> grupos = new ArrayList<Grupo>();;
	private double penalizacion=-1;
	
	
	public ConjuntoGrupo() {}
	
	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public void setPenalizacion(double penalizacion) {
		this.penalizacion = penalizacion;
	}
	
	public double getPenalizacion() {
//		double retorno = penalizacion/grupos.size();
//		retorno = 1/retorno;
//		return retorno;
		return penalizacion;
	}

	public void addGrupo(Grupo g) {
		grupos.add(g);
	}
	
	public String toString() {
		return grupos.toString()+ " " + String.valueOf(penalizacion);
	}
	
	public void calcPen(){
		double sum=0;
		for (Grupo grupo : grupos) {
			sum+=grupo.getPenalizacion();
		}
		penalizacion = sum/grupos.size();
	}

	public int compareTo(Object arg0) {
		if (penalizacion > ((ConjuntoGrupo)arg0).getPenalizacion())
			return 1;
		if(penalizacion < ((ConjuntoGrupo)arg0).getPenalizacion())
			return -1;
		return 0;
	}

	public boolean hayGrupos(){
		return !this.grupos.isEmpty();
	}
	
	public boolean hayGrupoVacio(){
		
		for( Grupo g : this.grupos){
			if(g.grupoVacio())
				return true;
		}
		return false;
	}
	
	public int getCantGrupos(){
		return this.grupos.size();
	}
	
	public void eliminarGrupo( Grupo g ){
		this.grupos.remove(g);
	}
	
	public boolean equals( Object o ){

		ConjuntoGrupo g = (ConjuntoGrupo) o;
		
		if( this.grupos.size() != g.grupos.size() )
			return false;
		
		boolean distinto = false;
		Iterator<Grupo> it = this.grupos.iterator();
		while( (it.hasNext()) && ( !distinto ) ){
			if( !g.grupos.contains( it.next() ) ){
				distinto = true;
			}
		}
		if( distinto )
				return false;
		return true;
	}

	public ConjuntoGrupo clone(){
		
		ArrayList<Grupo> grupos = new ArrayList<>();
		for(Grupo g : this.grupos){
			grupos.add(g.clone());
		}
		ConjuntoGrupo g = new ConjuntoGrupo();
		g.setGrupos(grupos);
		return g;
	}
}
