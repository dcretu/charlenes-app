package coffee.charlenes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Integer.min;

/**
 * Coffee Order class.
 *
 * Contains input and computed data about order along the methods to parse the input and calculate the receipt.
 */
public class CoffeeOrder {

//    private static Logger logger = Logger.getLogger(CoffeeOrder.class.getName());

    // file name to load order list from
    private String orderFileName;

    // order lines
    private List<OrderLine> oLines;

    // total amount to pay, calculated
    private BigDecimal totalAmount;

    // catalog list
    private final List<Product> catalogList = Arrays.asList(
            // Beverages
            new Product('B', new BigDecimal("2.50"), "Coffee small"                          ),
            new Product('B', new BigDecimal("3.00"), "Coffee medium"                         ),
            new Product('B', new BigDecimal("3.50"), "Coffee large"                          ),
            new Product('B', new BigDecimal("3.95"), "Freshly squeezed orange juice (0.25l)" ),
            // Extra
            new Product('E', new BigDecimal("0.30"), "Extra milk"                            ),
            new Product('E', new BigDecimal("0.50"), "Foamed milk"                           ),
            new Product('E', new BigDecimal("0.90"), "Special roast coffee"                  ),
            // Snacks
            new Product('S', new BigDecimal("4.50"), "Bacon Roll"                            ),
            // stamps Collected
            new Product('C', new BigDecimal("0.00"), "Stamps collected"                      )
    );
    // look-up map
    private final Map<String, Product> catalogMap = catalogList.stream().collect(Collectors.toMap(Product::productName, item -> item));

    // constructor
    public CoffeeOrder(String fileName) {
        System.out.println("constructor for: " + fileName);
        orderFileName = fileName;
    }

    public void loadOrder() {
        this.readInput();
        this.doCalculate();
    }

    /**
     * Read and parse input file, pupulate orderLines List.
     */
    public void readInput() {
        System.out.println("readInput()");

        FileReader     fileReader     = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader     = new FileReader(orderFileName);
            bufferedReader = new BufferedReader(fileReader);
            oLines = new ArrayList<OrderLine>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if ("".equals(line)) continue;
                OrderLine oLine = getOrderLine(line);
                oLines.add(oLine);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The file appears not to be found. Please check the correctness of its path.", e);
        } catch (IOException e) {
            throw new RuntimeException("Unprocessed exception.", e);
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (fileReader     != null) fileReader.close();
            } catch (IOException e) {
                throw new RuntimeException("Unprocessed exception.", e);
            }
        }

        // printout input
        //System.out.println("oLines = " + oLines);
    }

    /**
     * Parse the line order.
     *
     * Expected format: format : [quantity] | [product-name]
     *
     * Example: 3 | Coffee small
     *
     * @param line
     * @return
     */
    public OrderLine getOrderLine(String line) {
        System.out.println("line = " + line);

        String qString        = null;

        try {
            String[] fields = line.split(" *\\| *");
            if (fields.length != 2) { throw new RuntimeException("Line couldn't be parsed. Should contain two fields separated by a bar: [quantity] | [product name]. The line:\n[%s]".formatted(line)); }
            String productName = fields[1];
            qString = fields[0];
            System.out.println("fields = " + productName + ": " + qString);

            int     q = Integer.parseInt(qString);
            Product p = catalogMap.get(productName);
            if (p == null) {
                throw new RuntimeException("The product name couldn't be found within catalog. Should be one from the list (as described in assignment pdf):\n" + catalogMap);
            }

            return new OrderLine(q, p);
        } catch (NumberFormatException e) {
            throw new RuntimeException("First fields, the quantity, should be an integer, which it is not: %s".formatted(qString));
        }
    }

    /**
     * Compute bonuses and total amount.
     */
    public void doCalculate() {
        System.out.println("doCalculate()");

        // plan:
        // 1. gather stats
        // 2. calculate bonus #1 and #2
        // 3. sum up while applying bonuses

        // 1. gather stats
        // number of Beverages, Snacks, Collected Snacks
        int nB=0, nS=0, nC=0;
        for (OrderLine l: oLines) {
            switch (l.product().foodType()) {
                case 'B': nB += l.quantity(); break;
                case 'S': nS += l.quantity(); break;
                case 'C': nC += l.quantity(); break;
            }
        }
        System.out.printf("B: %d, S: %d, C: %d\n", nB, nS, nC);

        //  2. calculate bonus #1 and #2
        // bonus #1: 5th beverage free
        // eligible number free beverages: take into account Collected stamps
        int eB = (nB + nC)/5;

        // bonus #2: 1 extra free, if beverage and a snack
        // eligible number of free extras: number of pairs {Snack + Beverage}
        int eE = min(nS, nB);
        System.out.printf("eB: %d, eE: %d\n", eB, eE);


        //  3. sum up while applying bonuses
        totalAmount = new BigDecimal("0.00");
        List<OrderLine> aggregatedList = new ArrayList<OrderLine>();
        for (OrderLine l : oLines) {
            aggregatedList.add(l);
            Product p = l.product();
            BigDecimal s = p.price().multiply(BigDecimal.valueOf(l.quantity()));
            totalAmount = totalAmount.add(s);
            // apply bonus
            int nToApply = 0;
            if       (p.foodType() == 'B' && eB > 0) {
                nToApply = min(eB, l.quantity());
                eB -= nToApply;
            } else if(p.foodType() == 'E' && eE > 0) {
                nToApply = min(eE, l.quantity());
                eE -= nToApply;
            }
            if (nToApply > 0) {
                // add bonus as "negative" product
                aggregatedList.add(new OrderLine(nToApply, new Product(p.foodType(), p.price().negate(), p.productName()+"**")));
                // update total amount
                s = p.price().multiply(BigDecimal.valueOf(nToApply));
                totalAmount = totalAmount.add(s);
            }
        }
        // replace original order lines with the one merged with corresponding bonuses
        oLines = aggregatedList;
        System.out.println("totalAmount = " + totalAmount);
    }

    /**
     * Print the receipt.
     */
    public void printReceipt() {
        System.out.println("printReceipt()");

        // "receipt width"
        final int WIDTH = 64;

        System.out.println("=".repeat(WIDTH));
        for (OrderLine l : oLines) {
            Product p = l.product();
            BigDecimal s = p.price().multiply(BigDecimal.valueOf(l.quantity()));
            System.out.printf("(%s)%-40s %6s x%3d = %6s\n", p.foodType(), p.productName(), p.price(), l.quantity(), s);
        }
        System.out.println("-".repeat(WIDTH));
        System.out.printf("%-55s : %6s\n", "Total", totalAmount);
        System.out.println("=".repeat(WIDTH));
    }

    /**
     * Print the catalog of products available.
     */
    public void printCatalog() {
        System.out.println("printCatalog()");
        System.out.println("Catalog = " + catalogList);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
