package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.comparator.DeliveryByPriceComparator;
import bg.sofia.uni.fmi.mjt.glovo.comparator.DeliveryByTimeComparator;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityMapper;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidLayoutException;
import bg.sofia.uni.fmi.mjt.glovo.util.GraphUtility;
import bg.sofia.uni.fmi.mjt.glovo.util.TraversalEntry;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class ControlCenter implements ControlCenterApi {
    private MapEntity[][] mapLayout;
    private int rowsBoundary;
    private int colsBoundary;

    public ControlCenter(char[][] mapLayout) {
        setMapLayout(mapLayout);
    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation,
                                               Location clientLocation,
                                               double maxPrice,
                                               int maxTime,
                                               ShippingMethod shippingMethod) {
        return switch (shippingMethod) {
            case FASTEST -> getFastestDeliveryGuy(restaurantLocation, clientLocation, maxPrice);
            case CHEAPEST -> getCheapestDeliveryGuy(restaurantLocation, clientLocation, maxTime);
        };
    }

    @Override
    public MapEntity[][] getLayout() {
        return mapLayout;
    }

    public boolean validateLocation(Location location) {
        return GraphUtility.validate(location.x(), location.y(), rowsBoundary, colsBoundary);
    }

    public boolean validateEntity(Location location, MapEntityType mapEntityType) {
        return (mapLayout[location.x()][location.y()].type().equals(mapEntityType));
    }

    private void setMapLayout(char[][] mapLayout) {
        colsBoundary = mapLayout[0].length;
        rowsBoundary = mapLayout.length;

        this.mapLayout = new MapEntity[rowsBoundary][colsBoundary];

        for (int i = 0; i < rowsBoundary; i++) {
            for (int j = 0; j < colsBoundary; j++) {
                try {
                    MapEntity currentMapEntity =
                        new MapEntity(new Location(i, j), MapEntityMapper.find(mapLayout[i][j]));
                    this.mapLayout[i][j] = currentMapEntity;
                } catch (IllegalArgumentException e) {
                    throw new InvalidLayoutException(e.getMessage(), e);
                }
            }
        }
    }

    private DeliveryInfo getFastestDeliveryGuy(Location restaurantLocation,
                                               Location clientLocation,
                                               double maxPrice) {
        Comparator<DeliveryInfo> comparator = new DeliveryByTimeComparator();

        Queue<DeliveryInfo> allDeliveryOptions = getAllDeliveryOptions(restaurantLocation, clientLocation, comparator);

        if (maxPrice != -1) {
            while (allDeliveryOptions.peek() != null && allDeliveryOptions.peek().price() > maxPrice) {
                allDeliveryOptions.poll();
            }
        }

        return allDeliveryOptions.peek();
    }

    private DeliveryInfo getCheapestDeliveryGuy(Location restaurantLocation,
                                                Location clientLocation,
                                                int maxTime) {
        Comparator<DeliveryInfo> comparator = new DeliveryByPriceComparator();

        Queue<DeliveryInfo> allDeliveryOptions = getAllDeliveryOptions(restaurantLocation, clientLocation, comparator);

        if (maxTime != -1) {
            while (allDeliveryOptions.peek() != null && allDeliveryOptions.peek().estimatedTime() > maxTime) {
                allDeliveryOptions.poll();
            }
        }

        return allDeliveryOptions.peek();
    }

    private Queue<DeliveryInfo> getAllDeliveryOptions(Location restaurantLocation, Location clientLocation,
                                                      Comparator<DeliveryInfo> comparator) {
        Set<DeliveryType> deliveryOptions = EnumSet.allOf(DeliveryType.class);

        Map<DeliveryType, List<TraversalEntry>> deliverers = GraphUtility.bfs(mapLayout,
                                                                              restaurantLocation,
                                                                              clientLocation,
                                                                              rowsBoundary,
                                                                              colsBoundary,
                                                                              deliveryOptions,
                                                                              MapEntityType.WALL);

        Queue<DeliveryInfo> allDeliveryOptions = new PriorityQueue<>(comparator);

        for (Map.Entry<DeliveryType, List<TraversalEntry>> availableDeliverers : deliverers.entrySet()) {
            for (TraversalEntry calculatedDistance : availableDeliverers.getValue()) {
                double estimatesPrice = calculatedDistance.getDistance() * availableDeliverers.getKey().getPrice();
                int estimatedTime = calculatedDistance.getDistance() * availableDeliverers.getKey().getTime();

                DeliveryInfo deliveryInfo =
                    new DeliveryInfo(calculatedDistance.getLocation(), estimatesPrice, estimatedTime,
                        availableDeliverers.getKey());
                allDeliveryOptions.offer(deliveryInfo);
            }
        }

        return allDeliveryOptions;
    }
}