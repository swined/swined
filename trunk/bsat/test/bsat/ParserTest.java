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

    private void testParseOutput(String in, String out) throws Exception {
        Parser p = new Parser();
        assertEquals(p.parse(in).toString(), out);
    }

    private void testParseException(String in, String ex) throws Exception {
        Parser p = new Parser();
        try {
            p.parse(in);
        } catch (SyntaxErrorException e) {
            assertEquals(e.getMessage(), ex);
            return;
        }
        fail("exception expected");
    }

    /**
     * Test of parse method, of class Parser.
     */
    @Test
    public void testParse1() throws Exception {
        // regression
        testParseOutput("((x))", "x");
    }

    @Test
    public void testParse2() throws Exception {
        testParseOutput("(!x&( y))|Zc", "!x & y | Zc");
    }

    @Test
    public void testParse3() throws Exception {
        testParseException("", "trying to parse empty token set");
    }

    @Test
    public void testParse4() throws Exception {
        testParseException("(", "closing bracket expected");
    }

    @Test
    public void testParse5() throws Exception {
        testParseException(")", "unexpected closing bracket");
    }

    @Test
    public void testParse6() throws Exception {
        testParseException("|", "operator not expected");
    }

    @Test
    public void testParse7() throws Exception {
        testParseException("&", "operator not expected");
    }

    @Test
    public void testParse8() throws Exception {
        testParseException("~", "unexpected character: ~");
    }

    @Test
    public void testParse9() throws Exception {
        testParseException("x!", "unexpected token");
    }

}