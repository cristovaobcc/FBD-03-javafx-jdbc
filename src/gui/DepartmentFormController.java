/**
 * 
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
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
import model.entities.Department;
import model.services.DepartmentService;

/**
 * Classe de controle de eventos da Gui DepartmentForm.fxml.
 * Serve tanto para carregar dados de departamentos, como para inserir dados deles.
 *
 */
public class DepartmentFormController implements Initializable {
	
	private DepartmentService departmentService;
	
	private Department departmentEntity;
	
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
	 * Configura um Department.
	 * @param ds {@link DepartmentService}
	 */
	public void setDepartmentService(DepartmentService ds) {
		this.departmentService = ds;
	}
	
	/**
	 * Configura um Department.
	 * @param entity {@link Department}
	 */
	public void setDepartmentEntity(Department entity) {
		this.departmentEntity = entity;
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
			Utils.currentStage(event).close();
			
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	/**
	 * Obtém os dados da view DepartmentForm e devolve-os
	 * configurados num Department.
	 * @return {@link Department}
	 */
	private Department getFormData() {

		Department dept = new Department();
		dept.setId(Utils.tryParseToInteger(txtId.getText()));
		dept.setName(txtName.getText());
		
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
	 * DepartmentForm.fxml.
	 */
	public void updateFormData() {
		
		if (departmentEntity == null) {
			throw new IllegalStateException("DepartmentEntity is null");
		}
		txtId.setText(String.valueOf(departmentEntity.getId()));
		txtName.setText(departmentEntity.getName());
	}
}
