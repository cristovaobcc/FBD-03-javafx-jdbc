/**
 * 
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

/**
 * Classe de controlle da view SellerList.fxml.
 *
 */
public class SellerListController implements Initializable, DataChangeListener {
	
	private SellerService service;
	
	@FXML
	private TableView<Seller> tableViewSeller;
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
		
	private ObservableList<Seller> obsList; // os Deptos serão carregados em um ObservableList.
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller dept = new Seller();
		createDialogForm(dept,"/gui/SellerForm.fxml", parentStage);
	}
	
	public void setSellerService(SellerService departmentService) {
		this.service = departmentService;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {	
		
		initializeNodes();
	}



	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); // coloque, no parametro do construtor PropertyValueFactory, o nome igual ao do atributo da classe.
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		// Os comandos seguintes ajustam a tableView até os limites inferiores da janela. 
		Stage stage = (Stage) Main.getMainScene().getWindow(); // pega uma referência para janela principal. É do tipo superclasse de Stage.
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}
	
	/**
	 * Responsável por acessar serviços para carregar os dados 
	 * da tableView.
	 */
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons(); // Acrescenta um novo botão com o texto 'edit'
		initRemoveButtons(); // Acrescenta um novo botão com o texto 'remove'
		
	}
	
	
	/**
	 * Recebe um dept Seller, o caminho de uma view fxml e um Stage para criar uma 
	 * janela de diálogo.
	 * @param dept {@link Seller}
	 * @param absoluteName String
	 * @param parentStage {@link Stage}
	 */
	private void createDialogForm(Seller dept, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			// Injeta a dependência dept no controlador da tela de formulário:
			SellerFormController dfController = loader.getController();
			dfController.setSellerEntity(dept);
			dfController.setServices(new SellerService(), new DepartmentService());
			dfController.subscribeDataChangeListener(this);
			dfController.updateFormData();
			
			
			
			// Para carregar uma nova janela modal(que fica na frente e bloqueando a de baixo),
			// temos que carregar um novo stage:
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL); // método que diz se a janela criada será modal ou terá outro comportamento.
			dialogStage.showAndWait();
			
			
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
		
	}
	

	/**
	 * Cria um botão de edição em cada linha da tabela de Deptos. 
	 */
	private void initEditButtons() {
		
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>(){
			
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Seller dept, boolean empty) {
				super.updateItem(dept, empty);
				
				if (dept == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(
						event ->  
						createDialogForm(dept, "/gui/SellerForm.fxml", Utils.currentStage(event)));
						
			}
		});
	}
	
	
	/**
	 * Cria um botão de remoção em cada linha da tabela de Deptos na coluna de remoção. 
	 */
	private void initRemoveButtons() {
		
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller> (){
			
			private final Button button = new Button("remove");
			
			@Override
			protected void updateItem(Seller dept, boolean empty) {
				super.updateItem(dept, empty);
				
				if(dept == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> removeSeller(dept));
			}					
		});
		
	}

	/**
	 * Remove um departmento dept de uma linha da tela SellerList e do DB.
	 * @param dept {@link Seller}
	 */
	private void removeSeller(Seller dept) {
		Optional<ButtonType> resultOptional = 
				Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if (resultOptional.get() == ButtonType.OK) { // Verifica se usuário apertou no OK button
			
			if (service == null) {
				
				throw new IllegalStateException("Service was null");
			}
			try {
				
				service.remove(dept); 
				updateTableView();
				
			} catch (DbException e) { // uma exceção de integridade referencial pode ser lançada pelo SellerDaoJDBC
				
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
				
			}
		}
	}
}
