/**
 * 
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

/**
 * Classe de controle de eventos da Gui DepartmentForm.fxml.
 * Serve tanto para carregar dados de departamentos, como para inserir dados deles.
 *
 */
public class DepartmentFormController implements Initializable {
	
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
	 * @param entity {@link Department}
	 */
	public void setDepartmentEntity(Department entity) {
		this.departmentEntity = entity;
	}
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
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
