/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bsat;

import java.util.List;
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
public class VariableTest {

    public VariableTest() {
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

    /**
     * Test of getName method, of class Variable.
     */
    @Test
    public void testGetName() {
        Variable v = new Variable("x");
        assertEquals("x", v.getName());
    }

    /**
     * Test of isNegative method, of class Variable.
     */
    @Test
    public void testIsNegative() {
        Variable v = new Variable("x");
        assertEquals(false, v.isNegative());
    }

    /**
     * Test of toString method, of class Variable.
     */
    @Test
    public void testToString() {
        Variable v = new Variable("x");
        assertEquals("x", v.toString());
    }

    /**
     * Test of isComplex method, of class Variable.
     */
    @Test
    public void testIsComplex() {
        Variable v = new Variable("x");
        assertEquals(false, v.isComplex());
    }

    /**
     * Test of negate method, of class Variable.
     */
    @Test
    public void testNegate1() {
        Variable v = new Variable("x");
        assertEquals("!x", v.negate().toString());
    }

    @Test
    public void testNegate2() {
        Variable v = new Variable("x", true);
        assertEquals("x", v.negate().toString());
    }

    /**
     * Test of disjunctionalForm method, of class Variable.
     */
    @Test
    public void testDisjunctionalForm() {
        Variable v = new Variable("x");
        assertEquals("[[x]]", v.disjunctionalForm().toString());
    }

    /**
     * Test of equals method, of class Variable.
     */
    @Test
    public void testEquals1() {
        Variable v1 = new Variable("x");
        Variable v2 = new Variable("x");
        assertEquals(true, v1.equals(v2));
    }

    @Test
    public void testEquals2() {
        Variable v1 = new Variable("x");
        Variable v2 = new Variable("x", true);
        assertEquals(false, v1.equals(v2));
    }

    @Test
    public void testEquals3() {
        Variable v1 = new Variable("x");
        Variable v2 = new Variable("y");
        assertEquals(false, v1.equals(v2));
    }

    @Test
    public void testEquals4() {
        Variable v1 = new Variable("x");
        Variable v2 = new Variable("y", true);
        assertEquals(false, v1.equals(v2));
    }

    @Test
    public void testEquals5() {
        Variable v = new Variable("x");
        assertEquals(false, v.equals(""));
    }

}