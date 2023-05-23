package coffee.charlenes;

import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CoffeeOrderTest {

    @Test
    public void testFilesExistRelative() {
        File file;
        file = new File("src/test/resources/in-01-empty.txt");                assertTrue(file.exists());
        file = new File("src/test/resources/in-21-some.txt");                 assertTrue(file.exists());
        file = new File("src/test/resources/in-22-just-one.txt");             assertTrue(file.exists());
        file = new File("src/test/resources/in-23-one-each-empty-lines.txt"); assertTrue(file.exists());
        file = new File("src/test/resources/in-24-one-each-and-stamp.txt");   assertTrue(file.exists());
        file = new File("src/test/resources/in-25-one-of-type.txt");          assertTrue(file.exists());
        file = new File("src/test/resources/in-26-each-many.txt");            assertTrue(file.exists());
    }

    private void testFile(String fileName, BigDecimal assertedAmount) {
        CoffeeOrder order = new CoffeeOrder(fileName);
        order.loadOrder();
        assertEquals(order.getTotalAmount(), assertedAmount);
    }

    @Test
    public void testScenario01() {
        testFile("src/test/resources/in-01-empty.txt", new BigDecimal("0.00"));
    }

    @Test
    public void testScenario21() {
        testFile("src/test/resources/in-21-some.txt", new BigDecimal("78.80"));
    }

    @Test
    public void testScenario22() {
        testFile("src/test/resources/in-22-just-one.txt", new BigDecimal("2.50"));
    }

    @Test
    public void testScenario23() {
        testFile("src/test/resources/in-23-one-each-empty-lines.txt", new BigDecimal("19.45"));
    }

    @Test
    public void testScenario24() {
        testFile("src/test/resources/in-24-one-each-and-stamp.txt", new BigDecimal("21.95"));
    }

    @Test
    public void testScenario25() {
        testFile("src/test/resources/in-25-one-of-type.txt", new BigDecimal("9.00"));
    }

    @Test
    public void testScenario26() {
        testFile("src/test/resources/in-26-each-many.txt", new BigDecimal("295.55"));
    }

}
