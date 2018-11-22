package modelo;

import java.util.HashMap;

public class Distancia {

	private HashMap<Integer,Integer> distancias;
	private int id;
	
	public Distancia(int identificador) {
		id = identificador;
		distancias = new HashMap<Integer,Integer>();
	}
	
	public void addDistancia(int id, int distancia) {
		distancias.put(id, distancia);
	}
	
	public int getId() {
		return id;
	}
	
	public int getDistancia(int id) {
		return distancias.get(id);
	}

	@Override
	public String toString() {
		return " id=" + id + "[distancias=" + distancias +  "]";
	}
	
	

}
