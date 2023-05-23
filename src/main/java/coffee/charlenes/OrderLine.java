package coffee.charlenes;

/**
 * Bean to contain order-line: quantitiy and reference to the product. May contain "negative" product for the respective bonuses.
 *
 * @param quantity
 * @param product
 */
record OrderLine(
        int quantity,
        Product product
) {}
