package exceptions;

import org.hibernate.HibernateException;

public class EmptyDBExeption extends HibernateException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmptyDBExeption(String message) {
		super("Something went wrong");
		// TODO Auto-generated constructor stub
	}

}
