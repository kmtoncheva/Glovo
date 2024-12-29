package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

import static bg.sofia.uni.fmi.mjt.glovo.constant.MessageConstant.NO_DELIVERY_GUY_MSG;
import static bg.sofia.uni.fmi.mjt.glovo.constant.MessageConstant.OUTSIDE_BOUNDARIES_ERROR_MSG_PREFIX;
import static bg.sofia.uni.fmi.mjt.glovo.constant.MessageConstant.WRONG_POSITION_ERROR_MSG_SUFFIX;

public class Glovo implements GlovoApi {
    private final ControlCenter controlCenter;

    public Glovo(char[][] mapLayout) {
        controlCenter = new ControlCenter(mapLayout);
    }

    @Override
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {
        safeValidate(client, restaurant);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(restaurant.location(),
                                                                         client.location(),
                                                                         -1,
                                                                         -1,
                                                                         ShippingMethod.CHEAPEST);
        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException(NO_DELIVERY_GUY_MSG);
        }

        return new Delivery(client.location(),
                            restaurant.location(),
                            deliveryInfo.deliveryGuyLocation(),
                            foodItem,
                            deliveryInfo.price(),
                            deliveryInfo.estimatedTime());
    }

    @Override
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {
        safeValidate(client, restaurant);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(restaurant.location(),
                                                                         client.location(),
                                                                         -1,
                                                                         -1,
                                                                         ShippingMethod.FASTEST);
        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException(NO_DELIVERY_GUY_MSG);
        }

        return new Delivery(client.location(),
                            restaurant.location(),
                            deliveryInfo.deliveryGuyLocation(),
                            foodItem,
                            deliveryInfo.price(),
                            deliveryInfo.estimatedTime());
    }

    @Override
    public Delivery getFastestDeliveryUnderPrice(MapEntity client,
                                                 MapEntity restaurant,
                                                 String foodItem,
                                                 double maxPrice) throws NoAvailableDeliveryGuyException {
        safeValidate(client, restaurant);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(restaurant.location(),
                                                                         client.location(),
                                                                         maxPrice,
                                                                         -1,
                                                                         ShippingMethod.FASTEST);
        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException(NO_DELIVERY_GUY_MSG);
        }

        return new Delivery(client.location(),
                            restaurant.location(),
                            deliveryInfo.deliveryGuyLocation(),
                            foodItem,
                            deliveryInfo.price(),
                            deliveryInfo.estimatedTime());
    }

    @Override
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client,
                                                       MapEntity restaurant,
                                                       String foodItem,
                                                       int maxTime) throws NoAvailableDeliveryGuyException {
        safeValidate(client, restaurant);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(restaurant.location(),
                                                                         client.location(),
                                                                         -1,
                                                                         maxTime,
                                                                         ShippingMethod.CHEAPEST);

        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException(NO_DELIVERY_GUY_MSG);
        }

        return new Delivery(client.location(),
                            restaurant.location(),
                            deliveryInfo.deliveryGuyLocation(),
                            foodItem,
                            deliveryInfo.price(),
                            deliveryInfo.estimatedTime());
    }

    private void safeValidate(MapEntity client, MapEntity restaurant) {
        try {
            validateEntity(client);
            validateEntity(restaurant);
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderException(e.getMessage(), e);
        }
    }

    private void validateEntity(MapEntity mapEntity) {
        Location location = mapEntity.location();

        if (!controlCenter.validateLocation(location)) {
            throw new IllegalArgumentException(OUTSIDE_BOUNDARIES_ERROR_MSG_PREFIX + mapEntity.type().toString());
        }
        if (!controlCenter.validateEntity(location, mapEntity.type())) {
            throw new IllegalArgumentException(mapEntity.type().toString() + WRONG_POSITION_ERROR_MSG_SUFFIX);
        }
    }
}
