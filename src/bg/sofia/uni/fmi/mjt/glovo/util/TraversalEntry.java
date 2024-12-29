package bg.sofia.uni.fmi.mjt.glovo.util;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public class TraversalEntry {
    private final Location location;
    private int distance;

    public TraversalEntry(Location location, int distance) {
        this.location = location;
        this.distance = distance;
    }

    public Location getLocation() {
        return location;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
