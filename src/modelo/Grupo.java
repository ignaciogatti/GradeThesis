package modelo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Grupo implements Comparable<Grupo>{

	//Atributos DB
	private int id;
	private Integer tamanio = 0;
	private Integer cantUsuarios;
	
	private List<Usuario> integrantes = new ArrayList<Usuario>();
	private double penalizacion;
	
	public Grupo() {}

	public Grupo(int id) {
		this.id = id;
	}
	
	public Grupo(List<Usuario> l, double c){
		integrantes = l;
		tamanio=l.size();
		penalizacion=c;
	}

	public Grupo(int id, Integer tamanio, Integer cantUsuarios) {
		this.id = id;
		this.tamanio = tamanio;
		this.cantUsuarios = cantUsuarios;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getTamanio() {
		return this.tamanio;
	}

	public void setTamanio(Integer tamanio) {
		this.tamanio = tamanio;
	}

	public Integer getCantUsuarios() {
		return this.cantUsuarios;
	}

	public void setCantUsuarios(Integer cantUsuarios) {
		this.cantUsuarios = cantUsuarios;
	}
	
	public double getPenalizacion(){
		return penalizacion;
	}
	
	
	public void setPenalizacion(double penalizacion) {
		this.penalizacion = penalizacion;
	}
	
	public List<Usuario> getIntegrantes() {
		return integrantes;
	}

	public void setIntegrantes(List<Usuario> integrantes) {
		this.integrantes = integrantes;
	}
	
	public void addIntegrante(Usuario u) {
		integrantes.add(u);
	}

	public String toString() {
		return  integrantes.toString() + " " + String.valueOf(penalizacion);
	}

	@Override
	public int compareTo(Grupo o) {
		// TODO Auto-generated method stub
		if (penalizacion > o.getPenalizacion())
			return 1;//si es mayor
		if(penalizacion < o.getPenalizacion())
			return -1;//si es menor
		return 0;

	}
	
	public boolean grupoVacio(){
		
		return this.integrantes.isEmpty();
	}

	public boolean equals( Object o ){
		
		Grupo g = (Grupo) o;
		if( this.integrantes.size() != g.integrantes.size() )
			return false;
		
		boolean distinto = false;
		Iterator<Usuario> it = this.integrantes.iterator();
		while( (it.hasNext()) && ( !distinto ) ){
			if( !g.integrantes.contains( it.next() ) ){
				distinto = true;
			}
		}
		if( distinto )
				return false;
		return true;
	}
	
	public Grupo clone(){
		ArrayList<Usuario> usuarios = new ArrayList<>();
		for(Usuario u : this.integrantes){
			usuarios.add(u.clone());
		}
		Grupo g  = new Grupo(usuarios, this.penalizacion);
		return g;
	}
}
