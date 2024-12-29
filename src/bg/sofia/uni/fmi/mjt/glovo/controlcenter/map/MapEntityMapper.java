package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;

import java.util.Map;

import static bg.sofia.uni.fmi.mjt.glovo.constant.MessageConstant.UNKNOWN_ENTITY_ERROR_MSG;

public class MapEntityMapper {
    private static final Map<Character, MapEntityType> MAPPER = Map.of(
        MapEntityType.ROAD.getSymbol(), MapEntityType.ROAD,
        MapEntityType.WALL.getSymbol(), MapEntityType.WALL,
        MapEntityType.RESTAURANT.getSymbol(), MapEntityType.RESTAURANT,
        MapEntityType.CLIENT.getSymbol(), MapEntityType.CLIENT,
        MapEntityType.DELIVERY_GUY_BIKE.getSymbol(), MapEntityType.DELIVERY_GUY_BIKE,
        MapEntityType.DELIVERY_GUY_CAR.getSymbol(), MapEntityType.DELIVERY_GUY_CAR
    );

    /**
     * @param ch The symbol to be mapped.
     * @return The Map Entity Type that stands for the provided symbol.
     * @throws IllegalArgumentException If the provided symbol is not valid.
     */
    public static MapEntityType find(char ch) {
        MapEntityType res = MAPPER.get(ch);

        if (res == null) {
            throw new IllegalArgumentException(UNKNOWN_ENTITY_ERROR_MSG + ch);
        }
        return res;
    }

    /**
     * @param mapEntityType The entity to be mapped.
     * @return The Delivery Type that stands for the provided entity.
     */
    public static DeliveryType entityToDelivery(MapEntityType mapEntityType) {
        return switch (mapEntityType) {
            case MapEntityType.DELIVERY_GUY_BIKE -> DeliveryType.BIKE;
            case MapEntityType.DELIVERY_GUY_CAR -> DeliveryType.CAR;
            default -> null;
        };
    }
}
