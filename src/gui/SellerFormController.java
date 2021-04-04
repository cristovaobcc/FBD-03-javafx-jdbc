package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

/**
 * Classe de controle de eventos da Gui SellerForm.fxml.
 * Serve tanto para carregar dados de departamentos, como para inserir dados deles.
 *
 */
public class SellerFormController implements Initializable {
			
	private DepartmentService deptService;
	
	private SellerService sellerService;
	
	private Seller sellerEntity;
	
	// Lista de objetos interessados em receber o evento de mudança de dados.
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private ComboBox<Department> comboBoxDepartment;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Label labelErrorBaseSalary;
	
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	
	private ObservableList<Department> obsList;
	
	/**
	 * Configura um SellerService e um DepartmentService.
	 * @param sellerService {@link SellerService}
	 * @param deptService {@link DepartmentService}
	 */
	public void setServices(SellerService sellerService, DepartmentService deptService) {
		this.sellerService = sellerService;
		this.deptService = deptService;	
	}
	
	/**
	 * Configura um Seller.
	 * @param entity {@link Seller}
	 */
	public void setSellerEntity(Seller entity) {
		this.sellerEntity = entity;
	}
	
	/**
	 * Registra um {@link DataChangeListener} que pode "ouvir" as mudanças de 
	 * uma instância desta classe.
	 * @param listener @DataChangeListener
	 */
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		
		if (sellerEntity == null) {
			throw new IllegalStateException("departmentEntity is null");
		}
		if (sellerService == null) {
			throw new IllegalStateException("departmentService is null");
		}
		
		try {
			sellerEntity = getFormData();
			sellerService.saveOrUpdate(sellerEntity);
			notifyDataChangeListeners(); 
			Utils.currentStage(event).close();
			
		} 
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	/**
	 * Executa o método DataChangeListener.onDataChanged() em cada listener inscrito na  
	 * instância desta classe.
	 */
	private void notifyDataChangeListeners() {
		
		for (DataChangeListener dataChangeListener : dataChangeListeners) {
			dataChangeListener.onDataChanged();
		}
		
	}

	/**
	 * Obtém os dados da view SellerForm e devolve-os
	 * configurados num Seller.
	 * @return {@link Seller}
	 */
	private Seller getFormData() {

		Seller dept = new Seller();
		
		ValidationException validationException = new ValidationException("Validation error");
		
		dept.setId(Utils.tryParseToInteger(txtId.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			validationException.addError("name", "Field can't be empty");
		}
		// TODO: tratar o restante dos erros para exibir ao usuário.
		dept.setName(txtName.getText());
		
		if (validationException.getErrors().size() > 0) {
			throw validationException;
		}
		
		return dept;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
		
	}
	
	
	/**
	 * Popula os dados de Seller setado neste controller, para o formulário
	 * SellerForm.fxml.
	 */
	public void updateFormData() {
		
		if (sellerEntity == null) {
			throw new IllegalStateException("SellerEntity is null");
		}
		txtId.setText(String.valueOf(sellerEntity.getId()));
		txtName.setText(sellerEntity.getName());
		txtEmail.setText(sellerEntity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f",sellerEntity.getBaseSalary()));
		
		if (sellerEntity.getBirthDate() != null) {
			// Configura a data conforme local onde roda o programa.
			dpBirthDate.setValue(LocalDate.ofInstant(sellerEntity.getBirthDate().toInstant(), ZoneId.systemDefault())  );
		}
		
		if (sellerEntity.getDepartment() == null) {
			
			comboBoxDepartment.getSelectionModel().selectFirst();
			
		}else {
			
			comboBoxDepartment.setValue(sellerEntity.getDepartment());
		}
		
		
	}
	
	/**
	 * Carrega os dados de Departmentos cadastrados p/ combobox.
	 */
	public void loadAssociatedObjects() {
		if (deptService == null) {
			throw new IllegalStateException("DepartmentService is null");
		}
		List<Department> list = deptService.findAll(); // carrega do DB
		obsList = FXCollections.observableArrayList(list); // Carrega na ObservableList
		comboBoxDepartment.setItems(obsList); // carrega na comboBox.
	}
	
		
	/**
	 * Preenche labels de erros de formulários com as mensagens de erros.
	 * @param errors {@link Map} < String, String >
	 */
	private void setErrorMessages(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) { // chave correspondente à instrução validationException.addError("name", "Field can't be empty");
			labelErrorName.setText(errors.get("name"));
		} 
	}
	
	/**
	 * Inicia o comboBoxDepartment. 
	 */
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = 
				lv -> new ListCell<Department>() {
					
					@Override
					protected void updateItem(Department dept, boolean empty) {
						super.updateItem(dept, empty);
						setText(empty ? "" : dept.getName());
					};
				};
				
				comboBoxDepartment.setCellFactory(factory);
				comboBoxDepartment.setButtonCell(factory.call(null));
	}
	
}
