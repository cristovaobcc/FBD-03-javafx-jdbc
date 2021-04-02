/**
 * 
 */
package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe com métodos estáticos utilitários para Controllers de GUI JavaFx.
 *
 */
public class Utils {
	
	/**
	 * Devolve o stage a partir de objeto de evento passado como
	 * argumento.
	 * 
	 * @param event {@link ActionEvent}
	 * @return {@link Stage}
	 */
	public static Stage currentStage(ActionEvent event) {
		Node node = (Node) event.getSource();
		Scene scene = node.getScene();
		
		return (Stage) scene.getWindow();
		// Deixo abaixo outra forma de implementação:
		// return (Stage )((Node) event.getSource()).getScene().getWindow();
	}
	
	
}
