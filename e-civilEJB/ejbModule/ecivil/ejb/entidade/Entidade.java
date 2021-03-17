package ecivil.ejb.entidade;

import java.io.Serializable;

public interface Entidade extends Serializable {

	public Long getId();

	public boolean equals(Object obj);

	public int hashCode();

}