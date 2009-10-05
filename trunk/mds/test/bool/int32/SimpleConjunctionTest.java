/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bool.int32;

import java.util.HashSet;
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
public class SimpleConjunctionTest {

    public SimpleConjunctionTest() {
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
     * Test of toSCNF method, of class SimpleConjunction.
     */
    @Test
    public void testToSCNF() {
        SimpleConjunction instance = new SimpleConjunction(new Variable("x"));
        assertEquals(instance.toSCNF().toString(), "ffffffff & x");
    }

    /**
     * Test of getCoef method, of class SimpleConjunction.
     */
    @Test
    public void testGetCoef() {
        SimpleConjunction instance = new SimpleConjunction(new Const(29));
        assertEquals(instance.getCoef().getValue(), 29);
    }

    @Test
    public void testGetVars() {
        SimpleConjunction instance = new SimpleConjunction(new Const(29));
        assertEquals(instance.getVars().isEmpty(), true);
    }

    @Test
    public void testEqualVars() {
        System.out.println("equalVars");
        SimpleConjunction c = new SimpleConjunction(new Variable("x"));
        SimpleConjunction instance = new SimpleConjunction(new Variable("x"));
        assertEquals(c.equalVars(instance), true);
    }

    /**
     * Test of toString method, of class SimpleConjunction.
     */
    @Test
    public void testToString() {
        SimpleConjunction instance = new SimpleConjunction(new Const(29));
        assertEquals(instance.toString(), "1d");
    }

}