/**
 * 
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

/**
 * Controlador da MainView
 *
 */
public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemSeller; 
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout; 
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	
	@FXML
	public void onMenuItemDepartmentAction() {
		// Passando uma função de inicialização do controller DepartmentListController
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	
	@FXML
	public void onMenuItemAboutAction() {
		// A função x -> {} não faz nada.
		loadView("/gui/About.fxml", x ->  {});
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Função genérica do tipo T que carrega uma view (absoluteName) e inicia um controlador com
	 * uma função lambda passada no parâmetro.
	 * @param <T>
	 * @param absoluteName String
	 * @param initializingAction Consumer< T >
	 */
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) { 
		// com o synchronized o try não é interrompido durante o multithread.
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load(); // Carrega os filhos da janela do parametro.
			
			Scene mainScene = Main.getMainScene();
			
			// Obtém os filhos da mainScene.
			VBox mainVBox = ((VBox) ((ScrollPane) mainScene.getRoot()).getContent());
			
			Node mainMenu = mainVBox.getChildren().get(0); // obtém o 1o. filho da mainScene.
			mainVBox.getChildren().clear(); // limpa todos os filhos da janela principal.
			mainVBox.getChildren().add(mainMenu); // insere os filhos da janela principal.
			mainVBox.getChildren().addAll(newVBox.getChildren()); // insere os filhos da janela que se está abrindo.
			
			
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	
}
