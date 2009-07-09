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
public class AndOperatorTest {

    public AndOperatorTest() {
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
     * Test of getArg1 method, of class AndOperator.
     */
    @Test
    public void testGetArg1() {
        Variable v1 = new Variable("v1");
        Variable v2 = new Variable("v2");
        AndOperator op = new AndOperator(v1, v2);
        assertEquals(v1, op.getArg1());
    }

    /**
     * Test of getArg2 method, of class AndOperator.
     */
    @Test
    public void testGetArg2() {
        Variable v1 = new Variable("v1");
        Variable v2 = new Variable("v2");
        AndOperator op = new AndOperator(v1, v2);
        assertEquals(v2, op.getArg2());
    }

    /**
     * Test of toString method, of class AndOperator.
     */
    @Test
    public void testToString1() throws Exception {
        assertEquals("v1 & v2", Parser.parse("v1 & v2").toString());
    }

    @Test
    public void testToString2() throws Exception {
        assertEquals("(a | !b) & (!a | b)", Parser.parse("(a | !b) & (!a | b)").toString());
    }

    /**
     * Test of isComplex method, of class AndOperator.
     */
    @Test
    public void testIsComplex() throws Exception {
        assertEquals(true, Parser.parse("v1 & v2").isComplex());
    }

    /**
     * Test of negate method, of class AndOperator.
     */
    @Test
    public void testNegate() throws Exception {
        assertEquals("!v1 | !v2", Parser.parse("v1 & v2").negate().toString());
    }

    /**
     * Test of disjunctionalForm method, of class AndOperator.
     */
    @Test
    public void testDisjunctionalForm() throws Exception {
        assertEquals("[[v1, v2]]", Parser.parse("v1 & v2").disjunctionalForm().toString());
    }

}