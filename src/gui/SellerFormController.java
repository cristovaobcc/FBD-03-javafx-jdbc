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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

/**
 * Classe de controle de eventos da Gui SellerForm.fxml.
 * Serve tanto para carregar dados de departamentos, como para inserir dados deles.
 *
 */
public class SellerFormController implements Initializable {
			
	private SellerService departmentService;
	
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
	
	/**
	 * Configura um Seller.
	 * @param ds {@link SellerService}
	 */
	public void setSellerService(SellerService ds) {
		this.departmentService = ds;
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
		if (departmentService == null) {
			throw new IllegalStateException("departmentService is null");
		}
		
		try {
			sellerEntity = getFormData();
			departmentService.saveOrUpdate(sellerEntity);
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
}
