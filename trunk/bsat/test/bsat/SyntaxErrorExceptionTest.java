/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bsat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sw
 */
public class SyntaxErrorExceptionTest {

    public SyntaxErrorExceptionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreate1() {
        SyntaxErrorException e = new SyntaxErrorException();
    }

    @Test
    public void testCreate2() {
        SyntaxErrorException e = new SyntaxErrorException("text");
    }

}