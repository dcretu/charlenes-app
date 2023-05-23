# charlenes-app

## Prepare to work
```bash
git clone https://github.com/dcretu/charlenes-app.git`
cd charlenes-app/
mvn package
```

## Run app against an order file
```bash
java -cp target/charlenes-app-1.0.jar coffee.charlenes.App ./src/test/resources/in-21-some.txt
```

## File format
Format:
`[quantity] | [product name]`

## File example

Includes all possible values for the product name.
```text
1 | Bacon Roll
1 | Coffee small
1 | Coffee medium
1 | Coffee large
1 | Freshly squeezed orange juice (0.25l)
1 | Extra milk
1 | Foamed milk
1 | Special roast coffee
1 | Stamps collected
```

## Assumptions
1. Maven is requied to compile the project along the git and java.
2. Order list is presented in a file having format described above
3. Customer stamp card
    * Stamp card contains information about number of beverages procured during earlier visits
    * That number is introduced into order as an item named "Stamps collected"
4. Bonuses are alocated "in the order of appearance" in the order list.
    * This means that when customer is entitled for a free beverage, bonus will be applied to the firstly appeared beverage in the list.
    * Thus the total amount might depend on the items order.

