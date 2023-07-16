package it.polito.tdp.itunes.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.Adiacenza;
import it.polito.tdp.itunes.db.ItunesDAO;

import org.jgrapht.alg.connectivity.ConnectivityInspector;

public class Model {
	
	ItunesDAO dao;
	Map<Integer,Track> idMap;
	Graph<Track,DefaultWeightedEdge> grafo;
	List<Adiacenza> listaArchiController;
	
	//ricorsione
	List<Track> listaMigliore;
	
	
	public Model() {
		dao= new ItunesDAO();
		
		idMap=new HashMap<Integer,Track>();
	    dao.getAllTracks(idMap);
	}
	
	public void creaGrafo(Genre genere) {
		
		grafo = new SimpleWeightedGraph<Track,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		listaArchiController = dao.getArchiGrafo(genere, idMap);
		
		List<Track> listaVertici = dao.getVerticiGrafo(genere,idMap);
		Graphs.addAllVertices(grafo, listaVertici);
		
		List<Adiacenza> listaArchi = dao.getArchiGrafo(genere, idMap);
		for(Adiacenza a : listaArchi) {
			Graphs.addEdgeWithVertices(grafo, a.getT1(), a.getT2(), a.getPeso());
		}
	}
	
	public List<Track> creaLista(Track c , int m) {
		
		// calcolo_componente_connessa
		ConnectivityInspector<Track,DefaultWeightedEdge> inspector = new ConnectivityInspector<>(this.grafo);
		
		Set<Track> rimanenti = inspector.connectedSetOf(c);
		
		List<Track> parziale = new ArrayList<Track>(); 
		// aggiungo_c_alla_soluzione_parziale_perchè_deve_contenere_la_canzone_c
		parziale.add(c);
		
		ricorsione(parziale, m , rimanenti);
		listaMigliore = new ArrayList<>();
		
		return listaMigliore;
	}
	
	public void ricorsione(List<Track> parziale,int  m , Set<Track> rimanenti) {
		
		// Ogni volta che aggiungo un elemento(canzone) controllo che la mia soluzione sia migliore
		
		if(parziale.size() > listaMigliore.size()) {
			// Siamo di fronte ad una soluzione migliore, quindi sovrascrivo listaMigliore
			listaMigliore = new ArrayList<>(parziale);
			
			// In questo caso manca la return poichè il vincolo di uscita è dato dall'esaurimento 
			// del ciclo for sottostante al non verificarsi delle condizioni dell'IF ( in particolare 
			// la condizione del getBytes, che poteva anche essere messa precedentemente ma in modo diverso)
		}
		
		/* Ho un insieme di elementi in rimanenti, una per volta prendo gli elementi in rimanenti 
		 * e li metto in parziale facendo così partire la ricorsione, supponendo di aver messo quella canzone lì
		 * A questo punto la ricorsione andra avanti e ad un certo punto tornerò indietro 
		 * togliendo quell'elemento che ho appena messo: 
		 * la ricorsione continuerà di nuovo quel ramo in cui quel determinato elemento non c'è
		 */
		// algoritmo
		for(Track t : rimanenti) {
			if(! parziale.contains(t) && (sommaMemoria(parziale) + t.getBytes()) <= m) {
				parziale.add(t);
				ricorsione(parziale, m, rimanenti);
				parziale.remove(parziale.size()-1);
			
			}
		}
	}
	
	private int sommaMemoria (List<Track> canzoni) {
		int somma = 0;
		for(Track t : canzoni) {
			somma += t.getBytes();
		}
		return somma;
	}
	
	
	
	
	public List<Genre> getAllGenres(){
		return dao.getAllGenres();
	}

	public int getNumeroVertici() {
		if(grafo!=null) {
			return grafo.vertexSet().size();
		}
		else {
			return 0;	
		}
	}
	
	public int getNumeroArchi() {
		if(grafo!=null) {
			return grafo.edgeSet().size();
		}
		else {
			return 0;	
		}
	}

	public boolean grafoCreato() {
		if(grafo!=null) {
			return true;
		}
		else
		return false;
	}

	public List<Adiacenza> getArchi() {
		// TODO Auto-generated method stub
		return listaArchiController;
	}

	public Set<Track> getVertici() {
		// TODO Auto-generated method stub
		return grafo.vertexSet();
	}
	
	
}
