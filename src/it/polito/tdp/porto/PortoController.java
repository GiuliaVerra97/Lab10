package it.polito.tdp.porto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;
    

    @FXML
    private Button btnArticoli;


	private Model model;

    @FXML
    void handleCoautori(ActionEvent event) {
    	
    	txtResult.clear();
    	btnArticoli.setDisable(false);
    	
    	if(boxPrimo.getValue()==null) {
    		txtResult.setText("Manca l'autore");
    		return;
    	}
    	
    	Author autore=boxPrimo.getValue();
    	model.creaGrafo();
    	List<Author> lista=model.trovaVicini(autore);
    	txtResult.appendText("L'autore "+autore+" ha avuto come coautori: \n");
    	
    	for(Author a: lista) {
    		txtResult.appendText(a+"\n");
    	}
    	
    	
    	boxSecondo.getItems().addAll(model.getAutori());
    	
    	

    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	
    	
    	txtResult.clear();
    	
    	if(boxPrimo.getValue()==null || boxSecondo.getValue()==null) {
    		txtResult.setText("Manca il primo o il secondo autore");
    		return;
    	}
    	
    	if(boxPrimo.getValue().equals(boxSecondo.getValue())) {
    		txtResult.setText("Selezionare due autori diversi");
    	}
    	
    	List<Author> lista=model.passaggiPerArrivare(boxPrimo.getValue(), boxSecondo.getValue());
    	txtResult.appendText(model.trovaArticoliComuni(lista));
    	
    	btnArticoli.setDisable(true);
    	lista.clear();

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model model) {
		this.model=model;
		boxPrimo.getItems().addAll(model.getAutori());
	}
}
