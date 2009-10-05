package bool.int32;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AndTest {

    public AndTest() {
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
    public void testToSCNF() {
        And and = new And(new Variable("x"), new Variable("y"));
        assertEquals(and.toSCNF().toString(), "ffffffff & y & x");
    }

    @Test
    public void testRotate() {
        And and = new And(new Variable("x"), new Variable("y"));
        assertEquals(and.rotate(1).toString(), "x<<1 & y<<1");
    }

    @Test
    public void testInvert() {
        And and = new And(new Variable("x"), new Variable("y"));
        assertEquals(and.invert().toString(), "(!x | !y)");
    }

    @Test
    public void testOptimize0() {
        And and = new And(new Variable("x"), new Variable("y"));
        assertEquals(and.optimize().toString(), "x & y");
    }

    @Test
    public void testOptimize1() {
        And and = new And(new Const(0), new Const(1));
        assertEquals(and.optimize().toString(), "0");
    }

    @Test
    public void testToString() {
        And and = new And(new Variable("x"), new Variable("y"));
        assertEquals(and.toString(), "x & y");
    }

}