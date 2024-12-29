package bg.sofia.uni.fmi.mjt.glovo.delivery;

public enum DeliveryType {
    CAR(5, 3),
    BIKE(3, 5);

    private final double price;
    private final int time;

    DeliveryType(double price, int time) {
        this.price = price;
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public int getTime() {
        return time;
    }
}
