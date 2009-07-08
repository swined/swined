/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bsat;

/**
 *
 * @author sw
 */
public class SyntaxErrorException extends Exception {

    /**
     * Creates a new instance of <code>SyntaxErrorException</code> without detail message.
     */
    public SyntaxErrorException() {
    }


    /**
     * Constructs an instance of <code>SyntaxErrorException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SyntaxErrorException(String msg) {
        super(msg);
    }
}
