/**
 * 
 */
package gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
	
	
	/**
	 * Converte um valor string  para inteiro. Caso falhe,
	 * devolve null.
	 * @param str String
	 * @return Integer
	 */
	public static Integer tryParseToInteger(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
		
	}
	
	/**
	 * Método que preenche a célula de uma tableColumn com um formato de data.
	 * @param <T>
	 * @param tableColumn TableColumn<T, Date>
	 * @param format String
	 */
	public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
		tableColumn.setCellFactory(
				column -> {
					TableCell<T, Date> cell = new TableCell<T, Date>() {
						private SimpleDateFormat sdf = new SimpleDateFormat(format);
						
						@Override 
						protected void updateItem(Date item, boolean empty) {
							super.updateItem(item, empty);
							if(empty) {
								setText(null);
							} else {
								setText(sdf.format(item));
							}
						}
					};
					
					return cell;
				});
	}
	
	/**
	 * Método que preenche a célula de uma tableColumn com um número decimal de até decimalPlaces casas.
	 * @param <T>
	 * @param tableColumn TableColumn<T, Double> tableColumn
	 * @param decimalPlaces int
	 */
	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(
				column ->
					{
						TableCell<T, Double> cell = new TableCell<T,Double>(){
							
							@Override
							protected void updateItem(Double item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setText(null);
								} else {
									Locale.setDefault(Locale.US);
									setText(String.format("%." + decimalPlaces + "f", item));
								}
							}
						};
						
						return cell;
					}				
				);
	}
	
}
