package gui.util;
/**
 * 
 */


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Classe com métodos para exibir Alerts do JavaFX.
 *
 */
public class Alerts {
	
	/**
	 * Exibe um Alert do tipo inserido e com os dados inseridos.
	 * @param title String
	 * @param header String
	 * @param content String
	 * @param type {@link AlertType} é uma Enumeration.
	 */
	public static void showAlert(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}

}
