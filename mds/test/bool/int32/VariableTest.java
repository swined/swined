/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bool.int32;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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

    @Test
    public void testGetName() {
        Variable var = new Variable("x");
        assertEquals(var.toString(), "x");
    }

    @Test
    public void testIsNegative() {
        Variable var = new Variable("x", true, 0);
        assertEquals(var.isNegative(), true);
    }

    @Test
    public void testGetRotate() {
        Variable var = new Variable("x", false, 29);
        assertEquals(var.getRotate(), 29);
    }

    @Test
    public void testToSCNF() {
        Variable var = new Variable("x", true, 29);
        assertEquals(var.toSCNF().toString(), "ffffffff & !x<<1d");
    }

    @Test
    public void testRotate() {
        Variable var = new Variable("x");
        assertEquals(var.rotate(1).toString(), "x<<1");
    }

    @Test
    public void testOptimize() {
        Variable var = new Variable("x", true, 29);
        assertEquals(var, var.optimize());
    }

    @Test
    public void testInvert0() {
        Variable var = new Variable("x", true, 29);
        assertEquals(var.invert().toString(), "x<<1d");
    }

    @Test
    public void testInvert1() {
        Variable var = new Variable("x", false, 29);
        assertEquals(var.invert().toString(), "!x<<1d");
    }

    @Test
    public void testToString() {
        Variable var = new Variable("x", true, 29);
        assertEquals(var.toString(), "!x<<1d");
    }

    @Test
    public void testHashCode() {
        Variable x = new Variable("x");
        Variable y = new Variable("x", false, 0);
        assertEquals(x.hashCode(), y.hashCode());
    }

    @Test
    public void testEquals0() {
        Variable x = new Variable("x");
        Variable y = new Variable("x", false, 0);
        assertEquals(x, y);
    }

    @Test
    public void testEquals1() {
        Variable x = new Variable("x");
        Variable y = new Variable("y", false, 1);
        assertEquals(x.equals(y), false);
    }

    @Test
    public void testEquals2() {
        Variable x = new Variable("x");
        assertEquals(x.equals(new Integer(0)), false);
    }


}