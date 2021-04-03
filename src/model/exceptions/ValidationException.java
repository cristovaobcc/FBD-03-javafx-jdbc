/**
 * 
 */
package model.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Exceção que trata validação de dados vindos de formulário.
 * Carrega as mensagens de erros do formulário, caso existam.
 * 
 */
public class ValidationException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> errors = new HashMap<>();
	
	/**
	 * Instancia uma {@link ValidationException} com a msg passada.
	 * @param msg String
	 */
	public ValidationException(String msg) {
		super(msg);
	}
	
	/**
	 * Obtém a Collection Map de errors do objeto.
	 * @return {@link Map} < String, String >
	 */
	public Map<String, String> getErrors(){
		return errors;
	}
	
	/**
	 * Adiciona a msg de erro correspondente ao campo fieldName.
	 * @param fieldname String
	 * @param errorMessage String
	 */
	public void addError(String fieldname, String errorMessage) {
		errors.put(fieldname, errorMessage);
	}
}
