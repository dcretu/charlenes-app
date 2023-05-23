package coffee.charlenes;

import java.math.BigDecimal;

/**
 * Bean for the product properties.
 *
 * @param foodType [S]nack, [B]everage, [E]xtra, [C]ollected stamps
 * @param price 0.00
 * @param productName product name as described in assignment pdf, coffee extended to "Coffee small/medium/large"
 */
record Product(
        // [S]nack, [B]everage, [E]xtra, [C]ollected stamps
        char foodType,
        BigDecimal price,
        String productName
) {}
