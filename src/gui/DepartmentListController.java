/**
 * 
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

/**
 * Classe de controlle da view DepartmentList.fxml.
 *
 */
public class DepartmentListController implements Initializable {
	
	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnName;
	@FXML
	private Button btNew;
	
	
	private ObservableList<Department> obsList; // os Deptos serão carregados em um ObservableList.
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department dept = new Department();
		createDialogForm(dept,"/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService departmentService) {
		this.service = departmentService;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {	
		
		initializeNodes();
	}



	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		// Os comandos seguintes ajustam a tableView até os limites inferiores da janela. 
		Stage stage = (Stage) Main.getMainScene().getWindow(); // pega uma referência para janela principal. É do tipo superclasse de Stage.
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	/**
	 * Responsável por acessar serviços para carregar os dados 
	 * da tableView.
	 */
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
		
	}
	
	
	/**
	 * Recebe um dept Department, o caminho de uma view fxml e um Stage para criar uma 
	 * janela de diálogo.
	 * @param dept {@link Department}
	 * @param absoluteName String
	 * @param parentStage {@link Stage}
	 */
	private void createDialogForm(Department dept, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			// Injeta a dependência dept no controlador da tela de formulário:
			DepartmentFormController dfController = loader.getController();
			dfController.setDepartmentEntity(dept);
			dfController.updateFormData();
			
			// Para carregar uma nova janela modal(que fica na frente e bloqueando a de baixo),
			// temos que carregar um novo stage:
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL); // método que diz se a janela criada será modal ou terá outro comportamento.
			dialogStage.showAndWait();
			
			
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
