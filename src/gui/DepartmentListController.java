/**
 * 
 */
package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public void onBtNewAction() {
		System.out.println("onBtNewAction()");
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
}
