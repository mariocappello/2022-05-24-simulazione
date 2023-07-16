/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.Collections;
import java.util.*;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.db.Adiacenza;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="btnMassimo"
    private Button btnMassimo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCanzone"
    private ComboBox<Track> cmbCanzone; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void btnCreaLista(ActionEvent event) {
    	
    	Track c = cmbCanzone.getValue();
    	if(c==null) {
    		txtResult.appendText("selezionare un valore");
    		return;
    	}
    	
    	int memoria;
    	try{
    		memoria = Integer.parseInt(txtMemoria.getText().toString());
    	}
    	catch(NumberFormatException e) {
    		txtResult.appendText("inserire un vlore numerico valido");
    		return;
    	}
    	
    	txtResult.appendText("Lista canzoni migliori: "+"\n");
    	for(Track t : model.creaLista(c, memoria)) {
    		txtResult.appendText(t.toString()+"\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	if(cmbGenere.getValue()==null) {
    		txtResult.setText("inserire un valore!");
    		return;
    	}
    	
    	Genre genere = cmbGenere.getValue();
    	model.creaGrafo(genere);
    	
    	if(model.grafoCreato()==true) {
    		txtResult.appendText("grafo creato!"+"\n");
    		txtResult.appendText("Archi: " + model.getNumeroArchi()+"\n");
    		txtResult.appendText("Vertici: "+ model.getNumeroVertici()+"\n");
    		cmbCanzone.getItems().addAll(model.getVertici());
    		
    	}
    	else {
    		txtResult.setText("Devi creare il grafo!");
    	}

    }
    

    @FXML
    void doDeltaMassimo(ActionEvent event) {
    	
    	if(!(model.grafoCreato())) {
    		txtResult.appendText("Devi prima creare il grafo!"); 
    		return;
    	}
    	
    	int max=0;
    	List<Adiacenza> listaPerOrdinamentoFinale = new ArrayList<Adiacenza>();
    	
    	List<Adiacenza> listaPerOrdinamento = model.getArchi();
    	for(Adiacenza a : listaPerOrdinamento) {
    		if(a.getPeso()>max) {
    			listaPerOrdinamentoFinale.clear();
    			max=a.getPeso();
    			listaPerOrdinamentoFinale.add(a);
    		}
    		if(a.getPeso()==max) {
    			
    			listaPerOrdinamentoFinale.add(a);
    		}
    		
    	}
    	for(Adiacenza adiacenza : listaPerOrdinamentoFinale) {
    		txtResult.setText(adiacenza.getT1()+" "+adiacenza.getT2()+" "+adiacenza.getPeso()+"\n");
    	}
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMassimo != null : "fx:id=\"btnMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCanzone != null : "fx:id=\"cmbCanzone\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbGenere.getItems().addAll(model.getAllGenres());
    }

}
