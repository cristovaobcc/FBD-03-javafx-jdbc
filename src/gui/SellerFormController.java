package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
	
	private Seller departmentEntity;
	
	// Lista de objetos interessados em receber o evento de mudança de dados.
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
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
		this.departmentEntity = entity;
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
		
		if (departmentEntity == null) {
			throw new IllegalStateException("departmentEntity is null");
		}
		if (departmentService == null) {
			throw new IllegalStateException("departmentService is null");
		}
		
		try {
			departmentEntity = getFormData();
			departmentService.saveOrUpdate(departmentEntity);
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
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	
	/**
	 * Popula os dados de Departament setado neste controller, para o formulário
	 * SellerForm.fxml.
	 */
	public void updateFormData() {
		
		if (departmentEntity == null) {
			throw new IllegalStateException("SellerEntity is null");
		}
		txtId.setText(String.valueOf(departmentEntity.getId()));
		txtName.setText(departmentEntity.getName());
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
