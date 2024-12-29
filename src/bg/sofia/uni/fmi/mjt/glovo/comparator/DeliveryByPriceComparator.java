package bg.sofia.uni.fmi.mjt.glovo.comparator;

import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;

import java.util.Comparator;

public class DeliveryByPriceComparator implements Comparator<DeliveryInfo> {

    @Override
    public int compare(DeliveryInfo first, DeliveryInfo second) {
        return Double.compare(first.price(), second.price());
    }
}