package gui.util;
/**
 * 
 */


import javafx.scene.control.TextField;

/**
 * Classe com restrições de GUI.
 *
 */
public class Constraints {
	
	/**
	 * Configura o {@link TextField} com os dados passados, caso passe
	 * na verificação de ser um número inteiro.
	 * @param txt {@link TextField}
	 */
	public static void setTextFieldInteger(TextField txt) {
		// Caso não passem nos testes das funçòes lambdas os novos valores não são
		// configurados.
		txt.textProperty().addListener(
				(obs, oldValue, newValue)-> {
					if (newValue != null && !newValue.matches("\\d*")) {
						txt.setText(oldValue);
					}
				});
	}
	
	
	/**
	 * Verifica se o {@link TextField} txt é do tamanho máximo permitido.
	 * @param txt {@link TextField}
	 * @param max int
	 */
	public static void setTextFieldMaxLength(TextField txt, int max) {
		txt.textProperty().addListener(
				(obs, oldValue, newValue) -> {
					if (newValue != null && newValue.length() > max) {
						txt.setText(oldValue);
					}
				}
				);
	}
	
	
	/**
	 * Configura o {@link TextField} com os dados passados, caso passe
	 * na verificação de ser um número de ponto fluante. 
	 * @param txt {@link TextField}
	 */
	public static void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener(
				(obs, oldValue, newValue) -> {
					if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?")) {
						txt.setText(oldValue);
					}
				});
		
		
	}
	
}
