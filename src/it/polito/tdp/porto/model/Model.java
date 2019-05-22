package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	
	
	Graph<Author, DefaultEdge> grafo;
	Map<Integer, Author> mappaAutori;
	Map<Integer, Paper> mappaArticoli;
	List<Coautore> coautori;
	
	
	public Model() {
		grafo=new SimpleGraph<>(DefaultEdge.class);
		mappaArticoli=new HashMap<Integer, Paper>();
		mappaAutori=new HashMap<Integer, Author>();
		coautori=new ArrayList<Coautore>();
	
	}
	
	
	
	
	
	public void creaGrafo() {
		
		grafo=new SimpleGraph<>(DefaultEdge.class);
		mappaArticoli=new HashMap<Integer, Paper>();
		mappaAutori=new HashMap<Integer, Author>();
		coautori=new ArrayList<Coautore>();
		
		
		PortoDAO dao=new PortoDAO();
		dao.trovaAutori(mappaAutori);
		Graphs.addAllVertices(grafo, mappaAutori.values());
		
		dao.trovaArticoli(mappaArticoli);
		
		coautori=dao.trovaCoautore(mappaAutori);
		
		for(Coautore c: coautori) {
			
			Author source=mappaAutori.get(c.getA1().getId());
			Author destinazione=mappaAutori.get(c.getA2().getId());
			
			Graphs.addEdgeWithVertices(grafo, source, destinazione);
		}
		
		
		
	}

	
	
	
	/**
	 * Metodo che trova i coautori dell'attore passato come parametro, ovvero i vertici vicini a lui (collegati direttamente)
	 * @param autore
	 * @return lista di {@link Author}
	 */
	public List<Author> trovaVicini(Author autore) {
		return Graphs.neighborListOf(this.grafo, autore);
	}
	
	
	
	
	
	/**
	 * Metodo che permette di ricavare tutti gli autori che si devono "attraversare" per raggiungere l'autore di destinazione
	 * @param autore1
	 * @param autore2
	 * @return lista di {@link Author}
	 */
	
	public List<Author> passaggiPerArrivare(Author autore1, Author autore2){
				
		DijkstraShortestPath<Author, DefaultEdge> dj=new DijkstraShortestPath<Author, DefaultEdge>(grafo);
		GraphPath<Author, DefaultEdge> path=dj.getPath(autore1, autore2);
		path=dj.getPath(autore1, autore2);
		
		if(path!=null) {
			return path.getVertexList();
		}else {
			return new ArrayList<Author>();
		}
		
	}
	
	
	/**
	 * Metodo che associa ad ogni coppia di coautori un titolo di un articolo che hanno pubblicato insieme
	 * @param lista di autori
	 * @return stringa
	 */
	
	public String trovaArticoliComuni(List<Author> lista) {
		
		PortoDAO dao=new PortoDAO();
		String s="";
		
		if(lista.isEmpty()) {
			s="Non è possibile arrivare al secondo autore";
			return s;
		}
		
		
		for(int i=0; i<lista.size();i++) {
			int a1=lista.get(i).getId();
	
			if((i+1)!=lista.size()) {
			
			int a2=lista.get(i+1).getId();
			s=s+dao.trovaArticoliComuni(mappaAutori, mappaArticoli, a1, a2)+"\n";

			}

		}
		
		return s;
	}




	//per riempire i menu a tendina
	public List<Author> getAutori() {
		PortoDAO dao=new PortoDAO();
		List<Author> lista=dao.trovaAutori(mappaAutori);
		Collections.sort(lista);
		return lista;
	}
	
	
	

	//metodi GET e SET
	public Graph<Author, DefaultEdge> getGrafo() {
		return grafo;
	}

	public Map<Integer, Author> getMappaAutori() {
		return mappaAutori;
	}

	public Map<Integer, Paper> getMappaArticoli() {
		return mappaArticoli;
	}
	
	
	

}
