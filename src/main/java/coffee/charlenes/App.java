package coffee.charlenes;

import java.io.IOException;


public class App {
    //todo: logger

    public static void main( String[] args ) throws IOException {
        System.out.println( "start me up" );

        // todo: check args not empty
        System.out.printf("args.length = %d\n", args.length);

        CoffeeOrder order = new CoffeeOrder(args[0]);
        order.loadOrder();
        order.printReceipt();
//        order.printCatalog();
    }

}
