/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bool.int32;

import java.util.ArrayList;
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
public class SCNFTest {

    public SCNFTest() {
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
    public void testOptimize() {
        List<SimpleConjunction> scnf = new ArrayList();
        scnf.add(new SimpleConjunction(new Variable("x", true, 29)));
        scnf.add(new SimpleConjunction(new Variable("x", true, 29)));
        scnf.add(new SimpleConjunction(new Variable("x", true, 19)));
        scnf.add(new SimpleConjunction(new Const(29)));
        SCNF instance = new SCNF(scnf);
        assertEquals(instance.optimize().toString(), "ffffffff & !x<<13 | 1d | ffffffff & !x<<1d");
    }

}