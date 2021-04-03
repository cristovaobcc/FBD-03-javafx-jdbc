package gui.util;
/**
 * 
 */


import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

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
	
	
	/**
	 * Devolve um valor do tipo Optional<ButtonType>. Também exibe um alert do
	 * tipo Confirmation ao usuário. Isso serve para saber se o usuário
	 * apertou no 'sim' ou no 'não'.
	 * @param title String Titulo do Alert
	 * @param content String Conteúdo do Alert
	 * @return Optional< ButtonType >
	 */
	public static Optional<ButtonType> showConfirmation(String title, String content){
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		
		return alert.showAndWait();
		
	}

}
