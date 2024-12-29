package bg.sofia.uni.fmi.mjt.glovo.util;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityMapper;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class GraphUtility {
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static final int START_DISTANCE = 0;

    /**
     *
     *
     * @param mapLayout        A 2D array representing the map, where each cell contains a {@link MapEntity}.
     * @param startLocation    The starting location for the BFS traversal.
     * @param endLocation      The end location (e.g., client's location) for route calculation.
     * @param rowsBoundary     The total number of rows in the map.
     * @param colsBoundary     The total number of columns in the map.
     * @param deliveryOptions  A set of delivery types to consider during traversal.
     * @param forbidden        The type of map entity that is considered invalid for traversal.
     * @return                 A map where each {@link DeliveryType} key maps to a list of {@link TraversalEntry}
     * objects representing valid routes for that delivery type.
     *
     */
    public static Map<DeliveryType, List<TraversalEntry>> bfs(MapEntity[][] mapLayout,
                                                              Location startLocation,
                                                              Location endLocation,
                                                              int rowsBoundary,
                                                              int colsBoundary,
                                                              Set<DeliveryType> deliveryOptions,
                                                              MapEntityType forbidden) {
        boolean[][] visited = new boolean[rowsBoundary][colsBoundary];
        Map<DeliveryType, List<TraversalEntry>> results = new EnumMap<>(DeliveryType.class);

        for (DeliveryType type : deliveryOptions) {
            results.put(type, new LinkedList<>());
        }

        Queue<TraversalEntry> queue = new LinkedList<>();
        queue.offer(new TraversalEntry(startLocation, START_DISTANCE));
        visited[startLocation.x()][startLocation.y()] = true;

        while (!queue.isEmpty()) {
            TraversalEntry currentNode = queue.poll();
            int x = currentNode.getLocation().x();
            int y = currentNode.getLocation().y();
            int distance = currentNode.getDistance();
            MapEntityType mapEntityType = mapLayout[x][y].type();

            if (deliveryOptions.contains(MapEntityMapper.entityToDelivery(mapEntityType))) {
                int distanceFromRestaurantToClient =
                    bfsFromTo(mapLayout, startLocation, endLocation, rowsBoundary, colsBoundary, forbidden);

                if (distanceFromRestaurantToClient != -1) {
                    currentNode.setDistance(currentNode.getDistance() + distanceFromRestaurantToClient);
                    results.get(MapEntityMapper.entityToDelivery(mapEntityType)).add(currentNode);
                }
            }
            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (validate(newX, newY, rowsBoundary, colsBoundary)
                    && !mapLayout[newX][newY].type().equals(forbidden)
                    && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    queue.offer(new TraversalEntry(mapLayout[newX][newY].location(), distance + 1));
                }
            }
        }

        return results;
    }

    public static boolean validate(int x, int y, int rowsBoundary, int colsBoundary) {
        return (x >= 0 && x < rowsBoundary && y >= 0 && y < colsBoundary);
    }

    private static int bfsFromTo(MapEntity[][] mapLayout,
                                 Location start,
                                 Location end,
                                 int rowsBoundary,
                                 int colsBoundary,
                                 MapEntityType forbidden) {
        int rows = mapLayout.length;
        int cols = mapLayout[0].length;
        Queue<Location> queue = new LinkedList<>();
        queue.add(start);

        boolean[][] visited = new boolean[rows][cols];
        visited[start.x()][start.y()] = true;
        int distance = START_DISTANCE;

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Location current = queue.poll();

                if (current.equals(end)) {
                    return distance;
                }
                for (int[] direction : DIRECTIONS) {
                    int newX = current.x() + direction[0];
                    int newY = current.y() + direction[1];

                    if (validate(newX, newY, rowsBoundary, colsBoundary)
                        && !mapLayout[newX][newY].type().equals(forbidden)
                        && !visited[newX][newY]) {
                        visited[newX][newY] = true;
                        queue.add(new Location(newX, newY));
                    }
                }
            }
            distance++;
        }

        return -1;
    }
}