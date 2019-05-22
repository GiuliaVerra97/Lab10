package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Coautore;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	
	public List<Paper> trovaArticoli(Map<Integer, Paper> mappaArticoli) {

		final String sql = "SELECT * FROM paper ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			
			List<Paper> result=new ArrayList<Paper>();

			while (rs.next()) {
				
				
				if(mappaArticoli.get(rs.getInt("eprintid"))==null) {
				
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				mappaArticoli.put(rs.getInt("eprintid"), paper);
				result.add(paper);
				}else {
					result.add(mappaArticoli.get(rs.getInt("eprintid")));
				}
				
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	
	
	
	
	
	
	public List<Author> trovaAutori(Map<Integer, Author> mappaAutori) {

		final String sql = "SELECT * FROM author";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Author> result=new ArrayList<Author>();
			
			while (rs.next()) {

				if(mappaAutori.get(rs.getInt("id"))==null) {
					Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
					mappaAutori.put(autore.getId(), autore);
					result.add(autore);
				}else {
					result.add(mappaAutori.get(rs.getInt("id")));
				}
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			throw new RuntimeException("Errore Db");
		}
	}

	
	
	
	
	
	
	
	
	
	public List<Coautore> trovaCoautore(Map<Integer, Author> mappaAutori) {

		final String sql = "SELECT c1.authorid, c2.authorid " + 
				"FROM creator AS c1, creator AS c2 " + 
				"WHERE c1.eprintid=c2.eprintid AND c1.authorid>c2.authorid ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Coautore> result=new ArrayList<Coautore>();
			
			while (rs.next()) {
				
					Author partenza=mappaAutori.get(rs.getInt("c1.authorid"));
					Author destinazione=mappaAutori.get(rs.getInt("c2.authorid"));
					
					if(partenza==null || destinazione==null) {
						throw new RuntimeException("Problema coautore");
					}
					
					Coautore c=new Coautore(partenza, destinazione);
					result.add(c);
					
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			throw new RuntimeException("Errore Db");
		}
	}

	
	
	
	
	
	/**
	 * Metodo che trova il nome di almeno un articolo che i due autori hanno pubblicato insieme
	 * @param mappaAutori
	 * @param mappaArticoli
	 * @param id1 del primo autore
	 * @param id2 de secondo autore
	 * @return String
	 */
	public String trovaArticoliComuni(Map<Integer, Author> mappaAutori, Map<Integer, Paper> mappaArticoli, int id1, int id2) {

		final String sql = "SELECT c1.eprintid, c1.authorid, c2.authorid " + 
				"FROM creator AS c1, creator AS c2 " + 
				"WHERE c1.eprintid=c2.eprintid AND c1.authorid=? AND c2.authorid=? ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id1);
			st.setInt(2, id2);

			ResultSet rs = st.executeQuery();

			String s="";
			
			if (rs.next()) {
				
					Author partenza=mappaAutori.get(rs.getInt("c1.authorid"));
					Author destinazione=mappaAutori.get(rs.getInt("c2.authorid"));
					Paper articolo=mappaArticoli.get(rs.getInt("eprintid"));
					
					s=partenza.toString()+"-"+destinazione.toString()+"=>"+articolo.getTitle();
					
			}

			conn.close();
			return s;

		} catch (SQLException e) {
			throw new RuntimeException("Errore Db");
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}