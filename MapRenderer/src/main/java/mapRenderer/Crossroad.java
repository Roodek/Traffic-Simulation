package mapRenderer;

import mapRenderer.utils.Coord;

public class Crossroad {
    private Street firstStreet;
    private Street secondStreet;
    private Coord coord;

    public Street getFirstStreet() {
        return firstStreet;
    }

    public void setFirstStreet(Street firstStreet) {
        this.firstStreet = firstStreet;
    }

    public Street getSecondStreet() {
        return secondStreet;
    }

    public void setSecondStreet(Street secondStreet) {
        this.secondStreet = secondStreet;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }
}
