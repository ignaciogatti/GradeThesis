package modelo;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class RedSocial {
	
	private UndirectedGraph<Usuario,DefaultEdge> stringGraph;
	private List<Distancia> distancias;
	private List<DistanciaAB> distanciasAB;
	private List<InteractuanId> interacciones;
	private List<Usuario> usuarios;
	
	public RedSocial(List<Usuario> usuarios, List<InteractuanId> interacciones) {
		// TODO Auto-generated constructor stub
		
		this.usuarios = usuarios;
		this.interacciones = interacciones;
		stringGraph = createStringGraph();
		this.distanciasAB = new ArrayList<>();
		this.distancias = new ArrayList<Distancia>();
		for (int i=0; i<usuarios.size(); i++)
			distancias.add(calcularDistancias(usuarios.get(i)));
	}
	
	
	private UndirectedGraph<Usuario,DefaultEdge> createStringGraph()  {
		UndirectedGraph<Usuario,DefaultEdge> g = new SimpleGraph<Usuario, DefaultEdge>(DefaultEdge.class);
		for (int i=0; i< interacciones.size(); i++) {
			Usuario u1 = getUsuario(interacciones.get(i).getIdUsr1());
			Usuario u2 = getUsuario(interacciones.get(i).getIdUsr2());
			if (!g.containsVertex(u1))
				g.addVertex(u1);
			if (!g.containsVertex(u2)){
				g.addVertex(u2);
			}
			g.addEdge(u1,u2);
		}
		for (int i=0; i<usuarios.size(); i++)
			if (!g.containsVertex(usuarios.get(i)))
				g.addVertex(usuarios.get(i));
		return g;
	}
	
	private Usuario getUsuario(int idUsr) {
		for (int i=0; i<usuarios.size(); i++)
			if (usuarios.get(i).getId() == idUsr)
				return usuarios.get(i);
		return null;
	}


	private Distancia calcularDistancias(Usuario usuario) {
		Distancia retorno = new Distancia(usuario.getId());
		for (int j=0; j< usuarios.size(); j++) {
			DijkstraShortestPath<Usuario,DefaultEdge> dijkstra = new DijkstraShortestPath<Usuario,DefaultEdge>(this.stringGraph,usuario,usuarios.get(j));
			int peso = (int) dijkstra.getPathLength();
			retorno.addDistancia(usuarios.get(j).getId(), peso);
			DistanciaAB d = new DistanciaAB(usuario, usuarios.get(j));
			if (!this.distanciasAB.contains(d)){
				d.setPeso(peso);
				this.distanciasAB.add(d);
			}
				
		}
		return retorno;
	}
	
	public Distancia buscarDistancia(Usuario u) {
		for (int i=0; i<distancias.size(); i++)
			if (distancias.get(i).getId() == u.getId())
				return distancias.get(i);
		return null;
	}
	

	
	public void mostrarRed(){
		System.out.println(this.stringGraph.toString());
	}

	public List<DistanciaAB> getDistancias(){
		return this.distanciasAB;
	}
	
}
