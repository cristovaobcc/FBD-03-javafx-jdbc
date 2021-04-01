/**
 * 
 */
package model.entities;

import java.io.Serializable;

/**
 * Classe que representa um departamento.
 *
 */
public class Department implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	
	public Department() { }
	
	/**
	 * @param id Integer
	 * @param name String
	 */
	public Department(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * O id é utilizado para geração de hashCode().
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * O id é utilizado como diferenciador do equals.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Department other = (Department) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return "Department {\n    id : " + id + "\n    name : " + name + "\n}";
	}
	
	

}
