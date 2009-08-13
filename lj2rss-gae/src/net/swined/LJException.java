/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.swined;

/**
 *
 * @author alex
 */
public class LJException extends Exception {

    /**
     * Creates a new instance of <code>LJException</code> without detail message.
     */
    public LJException() {
    }


    /**
     * Constructs an instance of <code>LJException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public LJException(String msg) {
        super(msg);
    }
}
