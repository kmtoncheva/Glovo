package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import org.junit.jupiter.api.Test;

public class GlovoTest {
    @Test
    void testGetCheapestDeliveryWithClientOutsideTheBoundary() {
        char[][] layout = {
            {'#', '#', '#', '.', '#'},
            {'#', '.', 'B', 'R', '.'},
            {'.', '.', '#', '.', '#'},
            {'#', 'C', '.', 'A', '.'},
            {'#', '.', '#', '#', '#'}
        };

        MapEntity client = new MapEntity(new Location(1,3), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(3,1), MapEntityType.RESTAURANT);


    }
}
