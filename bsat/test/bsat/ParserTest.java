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
public class ParserTest {

    public ParserTest() {
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

    private void testParse(String in, String out) throws SyntaxErrorException {
        Parser p = new Parser();
        assertEquals(p.parse(in).toString(), out);
    }

    /**
     * Test of parse method, of class Parser.
     */
    @Test
    public void testParse1() throws Exception {
        // regression
        testParse("((x))", "x");
    }

    @Test
    public void testParse2() throws Exception {
        testParse("(!x&( y))|Zc", "!x & y | Zc");
    }

}