/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.jlo.talendcomp.flatfileimport;

/**
 *
 * @author jan
 */
public final class ParserException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParserException(String message) {
        super(message);
    }
    
	public ParserException(String message, Throwable t) {
        super(message, t);
    }

	public ParserException(Throwable t) {
        super(t);
    }

}
