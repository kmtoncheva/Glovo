import bg.sofia.uni.fmi.mjt.glovo.Glovo;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws NoAvailableDeliveryGuyException {

        char[][] mapLayout = {
            {'#', '#', '#', '.', 'C'},
            {'#', '.', 'B', '.', 'R'},
            {'.', '.', '#', '.', '.'},
            {'#', 'C', '.', '#', 'C'},
            {'#', '.', '.', 'A', '.'}
        };

        Glovo glovoApp = new Glovo(mapLayout);

        MapEntity client = new MapEntity(new Location(0, 4), MapEntityType.CLIENT);
        MapEntity client2 = new MapEntity(new Location(3, 4), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(1, 4), MapEntityType.RESTAURANT);

        try {
            // Cheapest delivery
            Delivery cheapestDelivery = glovoApp.getCheapestDelivery(client, restaurant, "Pizza");
            System.out.println("Cheapest Delivery:");
            printDeliveryDetails(cheapestDelivery);

            // Fastest delivery
            Delivery fastestDelivery = glovoApp.getFastestDelivery(client, restaurant, "Burger");
            System.out.println("\nFastest Delivery:");
            printDeliveryDetails(fastestDelivery);

            // Fastest delivery under price
            Delivery fastestUnderPrice = glovoApp.getFastestDeliveryUnderPrice(client, restaurant, "Sushi", 20.0);
            System.out.println("\nFastest Delivery Under Price:");
            printDeliveryDetails(fastestUnderPrice);

            // Cheapest delivery within time limit
            Delivery cheapestWithinTime = glovoApp.getCheapestDeliveryWithinTimeLimit(client2, restaurant, "Pasta", 100);
            System.out.println("\nCheapest Delivery Within Time Limit:");
            printDeliveryDetails(cheapestWithinTime);

        } catch (InvalidOrderException e) {
            System.err.println("Invalid order: " + e.getMessage());
        } catch (NoAvailableDeliveryGuyException e) {
            System.err.println("No delivery guys available: " + e.getMessage());
        }
    }

    private static void printDeliveryDetails(Delivery delivery) {
        if (delivery == null) {
            System.out.println("No delivery options available.");
            return;
        }
        System.out.println("Client Location: " + delivery.client());
        System.out.println("Restaurant Location: " + delivery.restaurant());
        System.out.println("Delivery Guy Location: " + delivery.deliveryGuy());
        System.out.println("Food Item: " + delivery.foodItem());
        System.out.println("Price: $" + delivery.price());
        System.out.println("Estimated Time: " + delivery.estimatedTime() + " minutes");
    }
}