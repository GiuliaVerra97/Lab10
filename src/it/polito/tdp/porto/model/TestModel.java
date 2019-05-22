package it.polito.tdp.porto.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		model.creaGrafo();
		System.out.println("Grafo con "+model.getGrafo().vertexSet().size()+" e "+model.getGrafo().edgeSet().size()+" archi");
		System.out.println(model.getGrafo().edgeSet());
		Author autore=model.getMappaAutori().get(572);
		System.out.println("\n\nI coautori di "+autore+" sono "+model.trovaVicini(autore));
		Author destinazione=model.getMappaAutori().get(537);
		List<Author> lista=model.passaggiPerArrivare(autore, destinazione);
		System.out.println("\n\nLa lista da "+autore+" a "+destinazione+"\n"+lista);
		System.out.print("\n\nGli articoli comuni per arrivare a destinazione sono \n"+model.trovaArticoliComuni(lista));
		
	}

}
