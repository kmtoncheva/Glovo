package bg.sofia.uni.fmi.mjt.glovo.comparator;

import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;

import java.util.Comparator;

public class DeliveryByTimeComparator implements Comparator<DeliveryInfo> {

    @Override
    public int compare(DeliveryInfo first, DeliveryInfo second) {
        return Integer.compare(first.estimatedTime(), second.estimatedTime());
    }
}