/**
 * 
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
		loadView2("/gui/DepartmentList.fxml");
	}
	
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
	}
	
	
	private synchronized void loadView(String absoluteName) { 
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
	
	private synchronized void loadView2(String absoluteName) { 
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
			
			DepartmentListController controller = loader.getController();
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
