package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public record Delivery(Location client,
                       Location restaurant,
                       Location deliveryGuy,
                       String foodItem,
                       double price,
                       int estimatedTime) {
    @Override
    public String toString() {
        return "Delivery{" +
            "client= [" + client.x() + ", " + client.y() +
            "], restaurant= [" + restaurant.x() + ", " + restaurant.y() +
            ", deliveryGuy= [" + deliveryGuy.x() + ", " + deliveryGuy.y() +
            ", foodItem='" + foodItem + '\'' +
            ", price=" + price +
            ", estimatedTime=" + estimatedTime +
            '}';
    }
}
